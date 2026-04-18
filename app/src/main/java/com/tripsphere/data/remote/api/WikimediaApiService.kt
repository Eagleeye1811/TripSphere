package com.tripsphere.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Wikipedia / Wikimedia API — completely free, no key needed.
 * Used for:
 *  1. GeoSearch  → find tourist places near a coordinate
 *  2. Page detail → get photo + description for a place
 */
interface WikimediaApiService {

    /** Find Wikipedia articles (tourist places) near a lat/lon coordinate. */
    @GET("w/api.php")
    suspend fun geoSearch(
        @Query("action")    action: String = "query",
        @Query("list")      list: String = "geosearch",
        @Query("gscoord")   gsCoord: String,
        @Query("gsradius")  gsRadius: Int = 10000,
        @Query("gslimit")   gsLimit: Int = 15,
        @Query("format")    format: String = "json"
    ): WikiGeoSearchResponse

    /** Get photo thumbnail + short description for one or more page IDs. */
    @GET("w/api.php")
    suspend fun getPageDetails(
        @Query("action")      action: String = "query",
        @Query("pageids")     pageIds: String,
        @Query("prop")        prop: String = "pageimages|extracts",
        @Query("exintro")     exIntro: Int = 1,
        @Query("exchars")     exChars: Int = 250,
        @Query("explaintext") exPlainText: Int = 1,
        @Query("pithumbsize") thumbSize: Int = 400,
        @Query("format")      format: String = "json"
    ): WikiPageDetailsResponse

    /**
     * Search Wikipedia for tourist destinations by keyword.
     * Uses generator=search so we get photos + descriptions + coordinates in one call.
     * Note: "1" is used instead of true for boolean params — MediaWiki accepts both
     * but "1" is the canonical form that works consistently.
     */
    @GET("w/api.php")
    suspend fun searchDestinations(
        @Query("action")      action: String = "query",
        @Query("generator")   generator: String = "search",
        @Query("gsrsearch")   query: String,
        @Query("gsrlimit")    limit: Int = 50,
        @Query("prop")        prop: String = "pageimages|extracts|coordinates",
        @Query("exintro")     exIntro: Int = 1,
        @Query("exchars")     exChars: Int = 250,
        @Query("explaintext") exPlainText: Int = 1,
        @Query("pithumbsize") thumbSize: Int = 500,
        @Query("coprop")      coProp: String = "type",
        @Query("colimit")     coLimit: Int = 1,
        @Query("redirects")   redirects: Int = 1,
        @Query("format")      format: String = "json"
    ): WikiSearchDestinationsResponse

    /** Get thumbnail image for a known place name (used for destination photos). */
    @GET("w/api.php")
    suspend fun getPlacePhoto(
        @Query("action")      action: String = "query",
        @Query("titles")      titles: String,
        @Query("prop")        prop: String = "pageimages",
        @Query("format")      format: String = "json",
        @Query("pithumbsize") thumbSize: Int = 400,
        @Query("redirects")   redirects: Int = 1
    ): WikimediaResponse
}

// ── GeoSearch response ────────────────────────────────────────────────────────

data class WikiGeoSearchResponse(
    @SerializedName("query") val query: WikiGeoSearchQuery? = null
)

data class WikiGeoSearchQuery(
    @SerializedName("geosearch") val geoSearch: List<WikiGeoPlace> = emptyList()
)

data class WikiGeoPlace(
    @SerializedName("pageid") val pageId: Long = 0L,
    @SerializedName("title")  val title: String = "",
    @SerializedName("lat")    val lat: Double = 0.0,
    @SerializedName("lon")    val lon: Double = 0.0,
    @SerializedName("dist")   val distanceMeters: Double = 0.0
)

// ── Page details response ─────────────────────────────────────────────────────

data class WikiPageDetailsResponse(
    @SerializedName("query") val query: WikiPageDetailsQuery? = null
)

data class WikiPageDetailsQuery(
    @SerializedName("pages") val pages: Map<String, WikiPageDetail> = emptyMap()
)

data class WikiPageDetail(
    @SerializedName("pageid")    val pageId: Int = -1,
    @SerializedName("title")     val title: String = "",
    @SerializedName("extract")   val extract: String? = null,
    @SerializedName("thumbnail") val thumbnail: WikimediaThumbnail? = null
)

// ── Shared thumbnail / photo ──────────────────────────────────────────────────

data class WikimediaResponse(
    @SerializedName("query") val query: WikimediaQuery? = null
)

data class WikimediaQuery(
    @SerializedName("pages") val pages: Map<String, WikimediaPage> = emptyMap()
)

data class WikimediaPage(
    @SerializedName("pageid")    val pageId: Int = -1,
    @SerializedName("title")     val title: String = "",
    @SerializedName("thumbnail") val thumbnail: WikimediaThumbnail? = null
)

data class WikimediaThumbnail(
    @SerializedName("source") val source: String = "",
    @SerializedName("width")  val width: Int = 0,
    @SerializedName("height") val height: Int = 0
)

// ── Search Destinations response (generator=search) ───────────────────────────

data class WikiSearchDestinationsResponse(
    @SerializedName("query") val query: WikiSearchDestinationsQuery? = null
)

data class WikiSearchDestinationsQuery(
    @SerializedName("pages") val pages: Map<String, WikiSearchPage> = emptyMap()
)

data class WikiSearchPage(
    @SerializedName("pageid")      val pageId: Long = 0L,
    @SerializedName("title")       val title: String = "",
    @SerializedName("extract")     val extract: String? = null,
    @SerializedName("thumbnail")   val thumbnail: WikimediaThumbnail? = null,
    @SerializedName("coordinates") val coordinates: List<WikiCoordinate> = emptyList()
)

data class WikiCoordinate(
    @SerializedName("lat")  val lat: Double = 0.0,
    @SerializedName("lon")  val lon: Double = 0.0
)
