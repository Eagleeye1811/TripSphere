package com.tripsphere.utils

import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory

object DummyData {

    val destinations = listOf(
        Destination(
            id = 1,
            name = "Santorini",
            country = "Greece",
            description = "Santorini is one of the Cyclades islands in the Aegean Sea. Famous for its whitewashed buildings with blue-domed roofs, volcanic landscape, and stunning sunsets over the caldera.",
            imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Apr – Oct",
            budgetEstimate = "\$150–\$250/day",
            rating = 4.9f,
            topAttractions = listOf("Oia Sunset Point", "Red Beach", "Akrotiri", "Fira Town", "Wine Museum"),
            latitude = 36.3932,
            longitude = 25.4615
        ),
        Destination(
            id = 2,
            name = "Bali",
            country = "Indonesia",
            description = "Bali is a living postcard – an Indonesian paradise that feels like a fantasy. With incredible rice terraces, Hindu temples, and world-class surf beaches, it's the perfect tropical escape.",
            imageUrl = "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Apr – Sep",
            budgetEstimate = "\$50–\$100/day",
            rating = 4.8f,
            topAttractions = listOf("Tegallalang Rice Terrace", "Uluwatu Temple", "Mount Batur", "Seminyak Beach", "Ubud Monkey Forest"),
            latitude = -8.4095,
            longitude = 115.1889
        ),
        Destination(
            id = 3,
            name = "Paris",
            country = "France",
            description = "The City of Light dazzles visitors with iconic landmarks, world-class cuisine, and art that spans centuries. Paris is the most visited city in the world for good reason.",
            imageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Jun – Sep",
            budgetEstimate = "\$150–\$300/day",
            rating = 4.7f,
            topAttractions = listOf("Eiffel Tower", "Louvre Museum", "Notre-Dame", "Champs-Élysées", "Versailles"),
            latitude = 48.8566,
            longitude = 2.3522
        ),
        Destination(
            id = 4,
            name = "New York",
            country = "USA",
            description = "New York City is an overwhelming, exciting, and endlessly fascinating metropolis. The city that never sleeps offers world-class museums, Broadway shows, and iconic skylines.",
            imageUrl = "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Apr – Jun",
            budgetEstimate = "\$200–\$400/day",
            rating = 4.7f,
            topAttractions = listOf("Central Park", "Times Square", "Statue of Liberty", "Brooklyn Bridge", "Metropolitan Museum"),
            latitude = 40.7128,
            longitude = -74.0060
        ),
        Destination(
            id = 5,
            name = "Kyoto",
            country = "Japan",
            description = "Kyoto was once the capital of Japan, and it shows. With over 1,600 Buddhist temples, 400 Shinto shrines, and 17 UNESCO World Heritage Sites, Kyoto is a living cultural museum.",
            imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Mar – May",
            budgetEstimate = "\$100–\$200/day",
            rating = 4.9f,
            topAttractions = listOf("Fushimi Inari", "Arashiyama Bamboo", "Kinkaku-ji", "Nishiki Market", "Gion District"),
            latitude = 35.0116,
            longitude = 135.7681
        ),
        Destination(
            id = 6,
            name = "Machu Picchu",
            country = "Peru",
            description = "The ancient Lost City of the Incas sits high in the Andes mountains. Built in the 15th century, this UNESCO World Heritage site is one of the world's greatest archaeological wonders.",
            imageUrl = "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "May – Sep",
            budgetEstimate = "\$80–\$150/day",
            rating = 4.8f,
            topAttractions = listOf("Sun Gate", "Huayna Picchu", "Temple of the Sun", "Inca Trail", "Sacred Valley"),
            latitude = -13.1631,
            longitude = -72.5450
        ),
        Destination(
            id = 7,
            name = "Swiss Alps",
            country = "Switzerland",
            description = "The Swiss Alps offer some of the most breathtaking mountain scenery in the world. With pristine ski resorts, charming alpine villages, and dramatic peaks, it's a year-round paradise.",
            imageUrl = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Dec – Mar",
            budgetEstimate = "\$200–\$400/day",
            rating = 4.9f,
            topAttractions = listOf("Matterhorn", "Jungfraujoch", "Grindelwald", "Zermatt", "Lake Lucerne"),
            latitude = 46.8182,
            longitude = 8.2275
        ),
        Destination(
            id = 8,
            name = "Amalfi Coast",
            country = "Italy",
            description = "The Amalfi Coast is a stretch of coastline in southern Italy, renowned for its steep cliffs, turquoise waters, colorful fishing villages, and exceptional cuisine.",
            imageUrl = "https://images.unsplash.com/photo-1533104816931-20fa691ff6ca?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "May – Oct",
            budgetEstimate = "\$150–\$300/day",
            rating = 4.8f,
            topAttractions = listOf("Positano", "Ravello", "Amalfi Cathedral", "Path of the Gods", "Capri Island"),
            latitude = 40.6340,
            longitude = 14.6027
        )
    )
}
