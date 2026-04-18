package com.tripsphere.data.remote.repository

import com.tripsphere.data.remote.api.NominatimApiService
import com.tripsphere.data.remote.api.WikiGeoPlace
import com.tripsphere.data.remote.api.WikimediaApiService
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.domain.model.Place
import com.tripsphere.domain.model.PlaceSuggestion
import com.tripsphere.domain.repository.PlacesRepository
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
            val response = wikiApi.searchDestinations(query = query, limit = 50)
            val pages = response.query?.pages?.values ?: emptyList()

            pages
                .filter { it.thumbnail != null && !it.extract.isNullOrBlank() }
                .sortedByDescending { it.coordinates.firstOrNull()?.lat ?: 0.0 } // keeps variety
                .map { page ->
                    val coord = page.coordinates.firstOrNull()
                    val title = page.title
                    // Split "Place, Country" titles for cleaner display
                    val parts = title.split(",").map { it.trim() }
                    Destination(
                        id        = page.pageId.toInt(),
                        name      = parts.firstOrNull() ?: title,
                        country   = parts.drop(1).joinToString(", ").ifBlank { "" },
                        description = page.extract?.trim() ?: "",
                        imageUrl  = page.thumbnail?.source ?: "",
                        category  = category.takeIf { it != DestinationCategory.ALL }
                            ?: inferDestinationCategory(title),
                        bestTimeToVisit = "Year-round",
                        budgetEstimate  = "\$\$",
                        rating          = 4.5f,
                        topAttractions  = emptyList(),
                        latitude  = coord?.lat ?: 0.0,
                        longitude = coord?.lon ?: 0.0
                    )
                }
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
