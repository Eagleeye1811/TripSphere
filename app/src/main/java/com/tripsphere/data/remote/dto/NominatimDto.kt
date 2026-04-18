package com.tripsphere.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NominatimResult(
    @SerializedName("place_id")    val placeId: Long = 0L,
    @SerializedName("display_name") val displayName: String = "",
    @SerializedName("name")        val name: String? = null,
    @SerializedName("type")        val type: String = "",
    @SerializedName("class")       val clazz: String = "",
    @SerializedName("lat")         val lat: String = "0",
    @SerializedName("lon")         val lon: String = "0",
    @SerializedName("address")     val address: NominatimAddress? = null
)

data class NominatimAddress(
    @SerializedName("city")        val city: String? = null,
    @SerializedName("town")        val town: String? = null,
    @SerializedName("village")     val village: String? = null,
    @SerializedName("country")     val country: String? = null,
    @SerializedName("state")       val state: String? = null
) {
    val locality: String get() = city ?: town ?: village ?: state ?: country ?: ""
}
