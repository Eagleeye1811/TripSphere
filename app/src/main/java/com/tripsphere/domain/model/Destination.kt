package com.tripsphere.domain.model

data class Destination(
    val id: Int,
    val name: String,
    val country: String,
    val description: String,
    val imageUrl: String,
    val category: DestinationCategory,
    val bestTimeToVisit: String,
    val budgetEstimate: String,
    val rating: Float,
    val topAttractions: List<String>,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val highlights: String = "",
    val priceLevel: String = "Mid-Range",
    val reviewCount: Int = 0
)

enum class DestinationCategory {
    BEACH, MOUNTAIN, CITY, ADVENTURE, ALL
}
