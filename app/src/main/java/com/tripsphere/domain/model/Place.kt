package com.tripsphere.domain.model

data class Place(
    val id: String,
    val name: String,
    val category: String,
    val categoryIconUrl: String,
    val address: String,
    val distanceMeters: Int,
    val rating: Double?,
    val photoUrl: String?,
    val isOpenNow: Boolean?,
    val latitude: Double,
    val longitude: Double,
    val description: String = "",
    val wikipediaPageId: Long = 0L
)

data class PlaceSuggestion(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String?,
    val category: String
)
