package com.tripsphere.data.local

// ── Category enum ─────────────────────────────────────────────────────────────

enum class AttractionCategory(val label: String, val emoji: String) {
    LANDMARK("Landmark",     "🏛️"),
    TEMPLE  ("Temple",       "⛩️"),
    NATURE  ("Nature",       "🌿"),
    BEACH   ("Beach",        "🏖️"),
    MARKET  ("Market",       "🛍️"),
    MUSEUM  ("Museum",       "🎨"),
    ADVENTURE("Adventure",   "🧗"),
    VIEWPOINT("Viewpoint",   "🌅"),
    VILLAGE ("Village",      "🏘️"),
    FOOD    ("Food & Drink", "🍜")
}

// ── Data model ────────────────────────────────────────────────────────────────

data class Attraction(
    val name: String,
    val category: AttractionCategory,
    val description: String,
    val suggestedTime: String,   // "09:00 AM"
    val durationHours: Float,    // approx visit duration
    val lat: Double,             // real-world coordinate
    val lon: Double,
    val suggestedDay: Int = 1    // which day of a 3-day trip this best fits
)

// ── Dataset ───────────────────────────────────────────────────────────────────

object DestinationAttractionsDataset {

    private val data: Map<String, List<Attraction>> = mapOf(

        "Santorini" to listOf(
            Attraction("Oia Village",              AttractionCategory.VIEWPOINT,  "The most photographed village in Greece, famous for blue-domed churches and the world's most beautiful sunset.",     "05:30 PM", 3.0f,  36.4618,  25.3753, 1),
            Attraction("Fira Town",                AttractionCategory.VILLAGE,    "Santorini's vibrant capital perched on the caldera cliff with winding alleys, cafés and stunning views.",             "10:00 AM", 2.5f,  36.4168,  25.4320, 1),
            Attraction("Akrotiri Ruins",           AttractionCategory.LANDMARK,   "A remarkably preserved Minoan Bronze Age settlement buried by the 1600 BC volcanic eruption.",                        "09:00 AM", 2.0f,  36.3519,  25.4040, 2),
            Attraction("Red Beach",                AttractionCategory.BEACH,      "Striking volcanic beach with dramatic red and black cliffs towering over the deep blue Aegean.",                     "11:30 AM", 2.0f,  36.3467,  25.3957, 2),
            Attraction("Santo Wines",              AttractionCategory.FOOD,       "Cliff-top winery with panoramic caldera views and Santorini's unique Assyrtiko white wine tastings.",                 "02:00 PM", 1.5f,  36.4013,  25.4327, 2),
            Attraction("Perissa Black Beach",      AttractionCategory.BEACH,      "Unique volcanic black-sand beach backed by the towering Mesa Vouno cliff.",                                          "10:00 AM", 3.0f,  36.3535,  25.4705, 3),
            Attraction("Nea Kameni Volcano",       AttractionCategory.ADVENTURE,  "Take a boat to walk across an active volcanic crater — one of the most unique experiences in Europe.",               "09:00 AM", 3.0f,  36.4044,  25.3964, 3),
            Attraction("Imerovigli",               AttractionCategory.VIEWPOINT,  "The highest point on the caldera ridge, known as the 'balcony to the Aegean' with breathtaking panoramas.",          "06:00 PM", 1.5f,  36.4313,  25.4237, 1)
        ),

        "Bali" to listOf(
            Attraction("Tegallalang Rice Terraces", AttractionCategory.NATURE,    "UNESCO-recognised cascading green rice fields sculpted over centuries into the hillside near Ubud.",                "09:00 AM", 2.0f,  -8.4438,  115.2756, 1),
            Attraction("Ubud Monkey Forest",       AttractionCategory.NATURE,     "Sacred jungle sanctuary home to 700+ long-tailed macaques and three ancient Hindu temples.",                        "11:00 AM", 1.5f,  -8.5187,  115.2598, 1),
            Attraction("Uluwatu Temple",           AttractionCategory.TEMPLE,     "Stunning sea temple perched 70 metres above the Indian Ocean on a sheer cliff at Bali's southern tip.",              "04:00 PM", 2.5f,  -8.8291,  115.0849, 1),
            Attraction("Mount Batur Sunrise",      AttractionCategory.ADVENTURE,  "Pre-dawn hike up an active volcano for a spectacular sunrise above the clouds — Bali's best adventure.",            "05:30 AM", 5.0f,  -8.2417,  115.3754, 2),
            Attraction("Tanah Lot Temple",         AttractionCategory.TEMPLE,     "Iconic sea temple perched on a rocky offshore islet, magical at sunset with crashing waves.",                       "05:00 PM", 2.0f,  -8.6215,  115.0864, 2),
            Attraction("Seminyak Beach",           AttractionCategory.BEACH,      "Bali's trendiest beach strip with world-class surf, beach clubs, and an unforgettable sunset scene.",                "09:00 AM", 3.0f,  -8.6907,  115.1594, 3),
            Attraction("Kecak Fire Dance",         AttractionCategory.LANDMARK,   "Ancient Balinese performance of the Ramayana epic, held at sunset in an open-air temple amphitheatre.",             "06:00 PM", 1.5f,  -8.5069,  115.2624, 1),
            Attraction("Ubud Art Market",          AttractionCategory.MARKET,     "Vibrant market overflowing with handmade textiles, carvings, jewellery and traditional Balinese crafts.",            "10:00 AM", 1.5f,  -8.5055,  115.2622, 1)
        ),

        "Paris" to listOf(
            Attraction("Eiffel Tower",             AttractionCategory.LANDMARK,   "The world's most iconic iron lattice tower — stunning by day and magical when it sparkles at night.",               "09:00 AM", 2.5f,  48.8584,   2.2945, 1),
            Attraction("Louvre Museum",            AttractionCategory.MUSEUM,     "World's largest art museum. Home to the Mona Lisa, Venus de Milo and 35,000 other works across 60,000 m².",         "12:00 PM", 3.5f,  48.8606,   2.3376, 1),
            Attraction("Seine River Cruise",       AttractionCategory.LANDMARK,   "Evening boat cruise through the heart of Paris watching iconic monuments glow in golden light.",                    "08:00 PM", 1.5f,  48.8566,   2.3522, 1),
            Attraction("Notre-Dame Cathedral",     AttractionCategory.TEMPLE,     "Gothic masterpiece from 1163 with flying buttresses, rose windows and gargoyles overlooking the Île de la Cité.",    "09:30 AM", 1.5f,  48.8530,   2.3499, 2),
            Attraction("Musée d'Orsay",            AttractionCategory.MUSEUM,     "World's finest collection of Impressionist art inside a stunning converted Beaux-Arts railway station.",            "02:00 PM", 2.5f,  48.8600,   2.3266, 2),
            Attraction("Sacré-Cœur Basilica",     AttractionCategory.TEMPLE,     "Gleaming white Romano-Byzantine basilica crowning Montmartre hill with a panoramic view over all of Paris.",         "05:00 PM", 1.5f,  48.8867,   2.3431, 2),
            Attraction("Palace of Versailles",     AttractionCategory.LANDMARK,   "The Sun King's opulent 17th-century palace with the dazzling Hall of Mirrors and vast formal gardens.",             "09:00 AM", 5.0f,  48.8049,   2.1204, 3),
            Attraction("Sainte-Chapelle",          AttractionCategory.TEMPLE,     "Medieval gem with 15 floor-to-ceiling stained-glass windows — the finest Gothic chapel in the world.",              "11:00 AM", 1.0f,  48.8554,   2.3450, 2),
            Attraction("Arc de Triomphe",          AttractionCategory.LANDMARK,   "Napoleon's triumphal arch at the heart of Paris, with a rooftop terrace offering 360° city views.",                "04:00 PM", 1.0f,  48.8738,   2.2950, 1),
            Attraction("Montmartre Village",       AttractionCategory.VILLAGE,    "Paris's bohemian hilltop neighbourhood, historic home of Picasso, Monet and Toulouse-Lautrec.",                     "03:00 PM", 2.0f,  48.8867,   2.3431, 2)
        ),

        "New York" to listOf(
            Attraction("Central Park",             AttractionCategory.NATURE,     "Iconic 341-hectare urban green space with the Bethesda Fountain, Bow Bridge, rowboat lake and famous skyline views.","09:00 AM", 2.0f,  40.7851,  -73.9683, 1),
            Attraction("Metropolitan Museum",      AttractionCategory.MUSEUM,     "One of the world's great art museums spanning 5,000 years of culture across 17 curatorial departments.",            "11:00 AM", 3.0f,  40.7794,  -73.9632, 1),
            Attraction("Times Square",             AttractionCategory.LANDMARK,   "The electrifying 'Crossroads of the World' — a sensory overload of giant screens, Broadway marquees and street life.","03:00 PM", 1.0f,  40.7580,  -73.9855, 1),
            Attraction("Statue of Liberty",        AttractionCategory.LANDMARK,   "America's defining symbol of freedom — a 93m copper statue on Liberty Island in New York Harbor.",                  "09:00 AM", 4.0f,  40.6892,  -74.0445, 2),
            Attraction("Brooklyn Bridge",          AttractionCategory.LANDMARK,   "Walk across the 1883 suspension bridge for jaw-dropping views of lower Manhattan and the East River.",               "02:00 PM", 1.0f,  40.7061,  -73.9969, 2),
            Attraction("9/11 Memorial & Museum",   AttractionCategory.MUSEUM,     "Deeply moving tribute at the original Twin Towers site — twin reflecting pools inscribed with 2,983 names.",         "10:00 AM", 2.5f,  40.7115,  -74.0134, 3),
            Attraction("High Line",                AttractionCategory.NATURE,     "Elevated linear park built on a disused freight railway with curated gardens, art installations and Hudson views.",    "01:00 PM", 2.0f,  40.7480,  -74.0048, 3),
            Attraction("DUMBO Brooklyn",           AttractionCategory.VIEWPOINT,  "Cobblestoned waterfront neighbourhood famous for the perfect Manhattan Bridge framed street photo.",                 "12:00 PM", 1.5f,  40.7033,  -73.9902, 2)
        ),

        "Kyoto" to listOf(
            Attraction("Fushimi Inari Shrine",     AttractionCategory.TEMPLE,     "Thousands of vermillion torii gates winding up a forested mountain — Japan's most iconic Shinto shrine.",           "08:00 AM", 2.5f,  34.9671,  135.7727, 1),
            Attraction("Arashiyama Bamboo Grove",  AttractionCategory.NATURE,     "A cathedral of towering bamboo stalks that click and sway in the breeze — go before 9 AM to beat the crowds.",      "08:30 AM", 1.5f,  35.0095,  135.6700, 2),
            Attraction("Kinkaku-ji Golden Pavilion",AttractionCategory.TEMPLE,    "Japan's most photographed building: a Zen Buddhist pavilion sheathed entirely in gold leaf on a mirror pond.",       "02:30 PM", 1.5f,  35.0394,  135.7292, 2),
            Attraction("Gion District",            AttractionCategory.VILLAGE,    "Kyoto's historic geisha quarter — cobbled Hanamikoji street lined with 17th-century ochaya (teahouse) facades.",     "05:00 PM", 2.0f,  35.0037,  135.7749, 1),
            Attraction("Kiyomizu-dera Temple",     AttractionCategory.TEMPLE,     "A spectacular wooden stage temple projecting over a hillside, offering sweeping views of central Kyoto.",            "06:00 PM", 1.5f,  34.9948,  135.7850, 1),
            Attraction("Nijo Castle",              AttractionCategory.LANDMARK,   "Former shogun's Kyoto palace famous for its 'nightingale floors' that creak to warn of approaching intruders.",      "09:00 AM", 2.0f,  35.0142,  135.7484, 3),
            Attraction("Philosopher's Path",       AttractionCategory.NATURE,     "A 2km stone canal path flanked by hundreds of cherry trees — transcendently beautiful in spring.",                   "11:00 AM", 1.5f,  35.0244,  135.7933, 3),
            Attraction("Nishiki Market",           AttractionCategory.MARKET,     "Kyoto's narrow 400-year-old 'kitchen street' packed with vendors selling pickles, tofu, matcha and street food.",    "11:00 AM", 1.5f,  35.0044,  135.7665, 1)
        ),

        "Machu Picchu" to listOf(
            Attraction("Machu Picchu Citadel",     AttractionCategory.LANDMARK,   "The 15th-century Inca citadel — arguably the world's greatest archaeological wonder — hidden in Andean cloud forest.","07:00 AM", 4.0f, -13.1631,  -72.5450, 1),
            Attraction("Sun Gate (Inti Punku)",    AttractionCategory.VIEWPOINT,  "The original Inca Trail entrance to Machu Picchu offers the most breathtaking first view of the citadel.",           "07:30 AM", 2.0f, -13.1746,  -72.5444, 1),
            Attraction("Temple of the Sun",        AttractionCategory.TEMPLE,     "Machu Picchu's finest stonework — a tower aligned so sunlight streams perfectly through its windows at solstice.",    "09:30 AM", 1.0f, -13.1637,  -72.5448, 1),
            Attraction("Huayna Picchu Mountain",   AttractionCategory.ADVENTURE,  "The dramatic steep peak soaring above the ruins — book the strictly limited entry tickets months in advance.",        "05:30 AM", 3.0f, -13.1564,  -72.5443, 2),
            Attraction("Sacred Valley",            AttractionCategory.NATURE,     "The broad Andean valley below Cusco, dotted with Inca terraces, markets, salt pans and fortress ruins.",             "10:00 AM", 4.0f, -13.5157,  -71.9782, 2),
            Attraction("Ollantaytambo Fortress",   AttractionCategory.LANDMARK,   "One of the best-preserved Inca sites — massive stone terraces rising up a cliff still used by the local community.", "03:00 PM", 2.5f, -13.2593,  -72.2633, 2),
            Attraction("Aguas Calientes",          AttractionCategory.VILLAGE,    "The gateway town to Machu Picchu, with thermal baths, local markets and the train station.",                         "01:00 PM", 2.0f, -13.1543,  -72.5278, 1)
        ),

        "Swiss Alps" to listOf(
            Attraction("Zermatt Village",          AttractionCategory.VILLAGE,    "The car-free mountain village at the foot of the Matterhorn, with charming chalets, ski shops and fondue restaurants.","09:00 AM", 2.0f,  46.0207,   7.7491, 1),
            Attraction("Matterhorn Viewpoint",     AttractionCategory.VIEWPOINT,  "The iconic pyramid-shaped 4,478m peak — the most photographed mountain in the world.",                               "10:00 AM", 1.0f,  45.9763,   7.6579, 1),
            Attraction("Gornergrat Train",         AttractionCategory.ADVENTURE,  "Europe's highest open-air railway climbs to 3,089m for a jaw-dropping panorama of 29 peaks over 4,000m.",           "11:00 AM", 3.0f,  45.9837,   7.7472, 1),
            Attraction("Jungfraujoch",             AttractionCategory.ADVENTURE,  "The 'Top of Europe' at 3,454m — Europe's highest railway station with a glacier, ice palace and snow park.",        "09:00 AM", 5.0f,  46.5474,   7.9855, 3),
            Attraction("Grindelwald Glacier Walk", AttractionCategory.NATURE,     "Walk through the Grindelwald valley with imposing glacier views and the Eiger's north face as a dramatic backdrop.",  "02:00 PM", 3.0f,  46.6241,   8.0421, 3),
            Attraction("Schilthorn Piz Gloria",    AttractionCategory.VIEWPOINT,  "Revolving 360° summit restaurant (the James Bond mountain) at 2,970m above the Bernese Oberland.",                  "10:00 AM", 3.0f,  46.5583,   7.8346, 2),
            Attraction("Lake Lucerne Cruise",      AttractionCategory.NATURE,     "Glide across Switzerland's most beautiful lake past medieval towns, forested hills and snow-capped peaks.",           "01:00 PM", 2.5f,  46.9481,   8.3773, 2),
            Attraction("Swiss Cheese Fondue",      AttractionCategory.FOOD,       "Authentic raclette or cheese fondue dinner in a traditional alpine hut — the definitive Swiss mountain experience.",  "07:00 PM", 2.0f,  46.0207,   7.7491, 1)
        ),

        "Amalfi Coast" to listOf(
            Attraction("Positano Village",         AttractionCategory.VILLAGE,    "The jewel of the Amalfi Coast — pastel-coloured houses cascade steeply down the cliff to a pebble beach.",          "09:00 AM", 3.0f,  40.6284,  14.4840, 1),
            Attraction("Path of the Gods",         AttractionCategory.NATURE,     "A spectacular 7km clifftop hike above the villages with vertigo-inducing views of the Tyrrhenian Sea.",              "04:00 PM", 3.5f,  40.6439,  14.5606, 1),
            Attraction("Amalfi Cathedral",         AttractionCategory.TEMPLE,     "Stunning 9th-century Arab-Norman cathedral with a gilded mosaic facade dominating Amalfi's main piazza.",            "09:00 AM", 1.0f,  40.6340,  14.6027, 2),
            Attraction("Ravello Gardens",          AttractionCategory.NATURE,     "Villa Rufolo's terraced gardens with jaw-dropping sea views — Wagner composed Parsifal while staying here.",          "11:00 AM", 2.0f,  40.6468,  14.6131, 2),
            Attraction("Capri Island Day Trip",    AttractionCategory.NATURE,     "The glamorous island of Capri — home to the Blue Grotto sea cave, designer boutiques and the Villa Jovis ruins.",    "09:00 AM", 8.0f,  40.5509,  14.2227, 2),
            Attraction("Emerald Grotto",           AttractionCategory.NATURE,     "A sea cave where refracted sunlight turns the water an extraordinary luminous emerald green — accessible by boat.",   "09:00 AM", 1.5f,  40.6321,  14.4741, 3),
            Attraction("Atrani Village",           AttractionCategory.VILLAGE,    "The smallest and most authentically unspoilt village on the coast — a short walk from Amalfi town.",                 "11:00 AM", 1.5f,  40.6373,  14.6178, 3),
            Attraction("Limoncello Tasting",       AttractionCategory.FOOD,       "Visit a family-run limoncello distillery and taste the iconic Amalfi lemon liqueur with coastal panoramas.",          "04:00 PM", 1.0f,  40.6340,  14.6027, 1)
        )
    )

    // ── Public API ────────────────────────────────────────────────────────────

    fun getAttractionsForDestination(destinationName: String): List<Attraction> {
        val key = data.keys.find {
            destinationName.contains(it, ignoreCase = true) ||
                    it.contains(destinationName.substringBefore(",").trim(), ignoreCase = true)
        }
        return data[key] ?: emptyList()
    }

    fun getAttractionsByDay(destinationName: String, day: Int): List<Attraction> =
        getAttractionsForDestination(destinationName).filter { it.suggestedDay == day }

    fun findAttractionByName(destinationName: String, attractionName: String): Attraction? =
        getAttractionsForDestination(destinationName).find {
            it.name.equals(attractionName, ignoreCase = true) ||
                    attractionName.contains(it.name, ignoreCase = true)
        }

    val allDestinationNames: List<String> get() = data.keys.toList()
}
