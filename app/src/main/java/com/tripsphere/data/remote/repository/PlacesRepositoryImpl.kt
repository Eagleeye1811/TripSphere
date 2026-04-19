package com.tripsphere.data.remote.repository

import com.tripsphere.data.remote.api.NominatimApiService
import com.tripsphere.data.remote.api.WikiGeoPlace
import com.tripsphere.data.remote.api.WikimediaApiService
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.domain.model.Place
import com.tripsphere.domain.model.PlaceSuggestion
import com.tripsphere.domain.repository.PlacesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(
    private val wikiApi: WikimediaApiService,
    private val nominatimApi: NominatimApiService
) : PlacesRepository {

    /**
     * Find tourist places near a coordinate using Wikipedia GeoSearch,
     * then enrich each result with a photo and description.
     */
    override suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): Result<List<Place>> = runCatching {
        // 1. GeoSearch — get nearby Wikipedia articles (tourist places)
        val geoResponse = wikiApi.geoSearch(
            gsCoord = "$latitude|$longitude",
            gsRadius = radiusMeters.coerceAtMost(10000),
            gsLimit = 15
        )
        val geoPlaces = geoResponse.query?.geoSearch ?: emptyList()
        if (geoPlaces.isEmpty()) return@runCatching emptyList()

        // 2. Batch-fetch details (photo + description) for all found pages
        val pageIds = geoPlaces.joinToString("|") { it.pageId.toString() }
        val detailsResponse = wikiApi.getPageDetails(pageIds = pageIds)
        val detailsMap = detailsResponse.query?.pages ?: emptyMap()

        // 3. Merge geo results with their details
        geoPlaces.map { geo ->
            val detail = detailsMap[geo.pageId.toString()]
            Place(
                id = geo.pageId.toString(),
                name = geo.title,
                category = inferCategory(geo.title),
                categoryIconUrl = "",
                address = "",
                distanceMeters = geo.distanceMeters.toInt(),
                rating = null,
                photoUrl = detail?.thumbnail?.source,
                isOpenNow = null,
                latitude = geo.lat,
                longitude = geo.lon,
                description = detail?.extract?.trim() ?: "",
                wikipediaPageId = geo.pageId
            )
        }
    }

    override suspend fun autocomplete(
        query: String,
        latitude: Double?,
        longitude: Double?
    ): Result<List<PlaceSuggestion>> = runCatching {
        val results = nominatimApi.search(query = query, limit = 6)
        results.map { r ->
            PlaceSuggestion(
                placeId = r.placeId.toString(),
                primaryText = r.name?.takeIf { it.isNotBlank() }
                    ?: r.displayName.split(",").firstOrNull()?.trim()
                    ?: r.displayName,
                secondaryText = r.address?.locality?.takeIf { it.isNotBlank() }
                    ?: r.displayName.split(",").drop(1).take(2).joinToString(",").trim(),
                category = r.type.replaceFirstChar { it.uppercase() }
            )
        }
    }

    override suspend fun getPlacePhotos(placeId: String): Result<List<String>> = runCatching {
        val response = wikiApi.getPageDetails(pageIds = placeId)
        response.query?.pages?.values
            ?.mapNotNull { it.thumbnail?.source }
            ?: emptyList()
    }

    /**
     * Search Wikipedia for tourist destinations by category and map results to
     * the app's [Destination] domain model.
     */
    override suspend fun fetchTouristPlaces(category: DestinationCategory): Result<List<Destination>> =
        runCatching {
            val query = categorySearchQuery(category)

            // Fetch two pages of 50 in parallel and merge, giving up to 100 results.
            val (resp1, resp2) = coroutineScope {
                val a = async { wikiApi.searchDestinations(query = query, limit = 50, offset = 0) }
                val b = async { wikiApi.searchDestinations(query = query, limit = 50, offset = 50) }
                a.await() to b.await()
            }
            val pages = buildMap {
                resp1.query?.pages?.let { putAll(it) }
                resp2.query?.pages?.let { putAll(it) } // deduplicates by page ID key
            }.values

            val mapped = pages
                .filter { it.thumbnail != null && !it.extract.isNullOrBlank() }
                .sortedByDescending { it.coordinates.firstOrNull()?.lat ?: 0.0 }
                .map { page ->
                    val coord = page.coordinates.firstOrNull()
                    val title = page.title
                    val parts = title.split(",").map { it.trim() }
                    Destination(
                        id        = page.pageId.toInt(),
                        name      = parts.firstOrNull() ?: title,
                        country   = parts.drop(1).joinToString(", ").ifBlank { "" },
                        description = page.extract?.trim() ?: "",
                        imageUrl  = page.thumbnail?.source ?: "",
                        category  = category.takeIf { it != DestinationCategory.ALL }
                            ?: inferDestinationCategory(title),
                        bestTimeToVisit = bestTimeForCategory(category),
                        budgetEstimate  = estimateBudget(page.pageId, category),
                        rating          = deriveRating(page.pageId),
                        topAttractions  = emptyList(),
                        latitude  = coord?.lat ?: 0.0,
                        longitude = coord?.lon ?: 0.0
                    )
                }

            // Geocode destinations that Wikipedia didn't supply coordinates for.
            // All requests run in parallel so there is no sequential delay.
            coroutineScope {
                mapped.map { dest ->
                    async {
                        if (dest.latitude == 0.0 && dest.longitude == 0.0) {
                            val geocodeQuery = if (dest.country.isNotBlank())
                                "${dest.name}, ${dest.country}" else dest.name
                            val hit = runCatching {
                                nominatimApi.search(query = geocodeQuery, limit = 1).firstOrNull()
                            }.getOrNull()
                            if (hit != null) {
                                val lat = hit.lat.toDoubleOrNull() ?: 0.0
                                val lon = hit.lon.toDoubleOrNull() ?: 0.0
                                dest.copy(latitude = lat, longitude = lon)
                            } else dest
                        } else dest
                    }
                }.awaitAll()
            }
        }

    override suspend fun fetchDestinationPhotos(pageTitle: String): Result<List<String>> =
        runCatching {
            val response = wikiApi.getPageImages(titles = pageTitle)
            val excludeKeywords = listOf(
                "icon", "flag", "logo", "coat", "seal", "map", "symbol",
                "button", "arrow", "diagram", "locator", "location", "portal",
                "commons-logo", "wikidata", "wikisource", "wikipedia"
            )
            response.query?.pages?.values
                ?.filter { page ->
                    val t = page.title.lowercase()
                    (t.endsWith(".jpg") || t.endsWith(".jpeg") || t.endsWith(".png")) &&
                        excludeKeywords.none { t.contains(it) }
                }
                ?.mapNotNull { page ->
                    page.imageInfo.firstOrNull()?.url?.takeIf { it.isNotBlank() }
                }
                ?.take(8)
                ?: emptyList()
        }

    /**
     * Derives a per-day budget range from the Wikipedia page ID so that every
     * destination gets a different but stable price, within a realistic band
     * for its category.
     *
     * Formula: use two different remainders of the page ID to get independent
     * low and high values, then round to the nearest $5 for a natural look.
     */
    private fun estimateBudget(pageId: Long, category: DestinationCategory): String {
        val (minFloor, minCeil, spread) = when (category) {
            DestinationCategory.BEACH     -> Triple(30,  150, 80)
            DestinationCategory.MOUNTAIN  -> Triple(50,  200, 100)
            DestinationCategory.CITY      -> Triple(40,  220, 120)
            DestinationCategory.ADVENTURE -> Triple(60,  250, 130)
            DestinationCategory.ALL       -> Triple(35,  180, 90)
        }
        val seed = pageId.toInt().let { if (it < 0) -it else it }
        // Low end: minFloor..minCeil, rounded to nearest $5
        val low  = ((minFloor + (seed % (minCeil - minFloor))) / 5) * 5
        // High end: low + (20..spread), rounded to nearest $5
        val high = ((low + 20 + ((seed / 100) % (spread - 20))) / 5) * 5
        return "\$$low–\$$high/day"
    }

    /**
     * Derives a varied star rating (4.0–5.0) from the page ID so destinations
     * don't all show the same 4.5.
     */
    private fun deriveRating(pageId: Long): Float {
        val seed = pageId.toInt().let { if (it < 0) -it else it }
        // Ratings in steps of 0.1 from 4.0 to 5.0 → 11 possible values
        val steps = seed % 11
        return (40 + steps) / 10f
    }

    private fun bestTimeForCategory(category: DestinationCategory) = when (category) {
        DestinationCategory.BEACH     -> "Nov – Apr"
        DestinationCategory.MOUNTAIN  -> "Jun – Sep"
        DestinationCategory.CITY      -> "Year-round"
        DestinationCategory.ADVENTURE -> "Mar – Oct"
        DestinationCategory.ALL       -> "Year-round"
    }

    private fun categorySearchQuery(category: DestinationCategory) = when (category) {
        DestinationCategory.ALL       -> "famous tourist destination travel"
        DestinationCategory.BEACH     -> "beach resort tropical tourism"
        DestinationCategory.MOUNTAIN  -> "mountain resort alpine tourism hiking"
        DestinationCategory.CITY      -> "historic city cultural tourism world heritage"
        DestinationCategory.ADVENTURE -> "adventure ecotourism natural wonder"
    }

    private fun inferDestinationCategory(title: String): DestinationCategory {
        val lower = title.lowercase()
        return when {
            "beach" in lower || "island" in lower || "coast" in lower || "bay" in lower
                -> DestinationCategory.BEACH
            "mountain" in lower || "alpine" in lower || "peak" in lower || "glacier" in lower
                -> DestinationCategory.MOUNTAIN
            "adventure" in lower || "safari" in lower || "trek" in lower
                -> DestinationCategory.ADVENTURE
            else -> DestinationCategory.CITY
        }
    }

    /** Infer a human-readable category from the Wikipedia article title. */
    private fun inferCategory(title: String): String {
        val lower = title.lowercase()
        return when {
            "museum" in lower || "gallery" in lower  -> "Museum"
            "castle" in lower || "fort" in lower || "palace" in lower -> "Historic Site"
            "church" in lower || "cathedral" in lower || "temple" in lower || "mosque" in lower -> "Religious Site"
            "beach" in lower || "bay" in lower || "cove" in lower -> "Beach"
            "park" in lower || "garden" in lower || "forest" in lower -> "Nature"
            "island" in lower                        -> "Island"
            "volcano" in lower || "mountain" in lower || "peak" in lower -> "Natural Landmark"
            "lake" in lower || "river" in lower || "waterfall" in lower -> "Natural Landmark"
            "ruins" in lower || "ancient" in lower || "archaeological" in lower -> "Archaeological Site"
            "hotel" in lower || "resort" in lower    -> "Hotel"
            "airport" in lower                       -> "Transport"
            "port" in lower || "harbor" in lower || "harbour" in lower -> "Port"
            "market" in lower || "bazaar" in lower   -> "Market"
            else                                     -> "Tourist Attraction"
        }
    }
}
