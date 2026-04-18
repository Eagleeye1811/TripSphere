package com.tripsphere.data.remote.api

import com.tripsphere.data.remote.dto.NominatimResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * OpenStreetMap Nominatim API — free geocoding & place search, no key needed.
 * Usage policy: must send a valid User-Agent header.
 */
interface NominatimApiService {

    @GET("search")
    suspend fun search(
        @Header("User-Agent") userAgent: String = "TripSphere/1.0",
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 6,
        @Query("addressdetails") addressDetails: Int = 1
    ): List<NominatimResult>
}
