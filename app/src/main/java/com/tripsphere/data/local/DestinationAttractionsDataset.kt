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
        ),

        // ── BEACH (remaining) ─────────────────────────────────────────────────────

        "Maldives" to listOf(
            Attraction("Biyadhoo Island Snorkelling", AttractionCategory.BEACH,   "World-class house reef snorkelling with sea turtles, reef sharks and vivid coral gardens just steps from shore.",   "08:00 AM", 3.0f,   4.1150,  73.4299, 1),
            Attraction("Banana Reef Diving",        AttractionCategory.ADVENTURE, "One of the Maldives' first and most celebrated dive sites — overhangs, caves and abundant marine life at 30 m.",     "09:00 AM", 3.0f,   4.2167,  73.5000, 1),
            Attraction("Baa Atoll Biosphere",       AttractionCategory.NATURE,    "UNESCO Biosphere Reserve where manta rays and whale sharks gather seasonally in the Hanifaru Bay plankton blooms.",    "07:00 AM", 4.0f,   5.0500,  72.9500, 2),
            Attraction("Male Friday Mosque",        AttractionCategory.TEMPLE,    "The 17th-century Hukuru Miskiy mosque built from coral stone — the oldest and most beautiful mosque in the Maldives.", "09:30 AM", 1.0f,   4.1748,  73.5089, 2),
            Attraction("Hulhumale Beach",           AttractionCategory.BEACH,     "A long white-sand beach on the reclaimed island of Hulhumale — ideal for sunset walks and swimming.",                 "04:30 PM", 2.0f,   4.2114,  73.5409, 1),
            Attraction("Sandbank Picnic",           AttractionCategory.BEACH,     "A private excursion to a deserted sandbank in the middle of the Indian Ocean — nothing but sea, sky and silence.",    "11:00 AM", 3.0f,   3.2028,  73.2207, 3),
            Attraction("Sunset Dolphin Cruise",     AttractionCategory.NATURE,    "An evening boat cruise through channels where spinner dolphins leap at the bow in the golden hour light.",             "05:30 PM", 2.0f,   4.0000,  73.3000, 3),
            Attraction("Underwater Restaurant",     AttractionCategory.FOOD,      "Dine 5 metres beneath the lagoon surface in a glass-walled restaurant with reef fish swimming past your table.",     "07:00 PM", 2.0f,   4.1750,  73.5091, 2)
        ),

        "Phuket" to listOf(
            Attraction("Phi Phi Islands",           AttractionCategory.BEACH,     "Impossibly photogenic limestone karst islands rising from turquoise water — snorkel Maya Bay from 'The Beach'.",     "08:00 AM", 8.0f,   7.7407,  98.7784, 1),
            Attraction("Big Buddha Viewpoint",      AttractionCategory.TEMPLE,    "A 45-metre white marble Buddha on Nakkerd Hill with sweeping 360° views across Phuket and the Andaman Sea.",         "09:00 AM", 1.5f,   7.8275,  98.3133, 2),
            Attraction("Old Phuket Town",           AttractionCategory.VILLAGE,   "Charming Sino-Portuguese colonial quarter with pastel shophouses, street art murals and outstanding local cuisine.",  "03:00 PM", 2.5f,   7.8847,  98.3853, 2),
            Attraction("Phang Nga Bay Kayaking",    AttractionCategory.ADVENTURE, "Paddle through a limestone karst bay, entering sea caves and hidden hongs (lagoons) in James Bond country.",          "08:00 AM", 5.0f,   8.2765,  98.5121, 3),
            Attraction("Patong Beach",              AttractionCategory.BEACH,     "Phuket's most vibrant beach strip — swim by day and experience the legendary Bangla Road nightlife after sunset.",    "09:00 AM", 3.0f,   7.8912,  98.2980, 1),
            Attraction("Wat Chalong Temple",        AttractionCategory.TEMPLE,    "Phuket's most important Buddhist temple complex, with ornate pagodas and a relic of the Lord Buddha.",               "10:00 AM", 1.5f,   7.8450,  98.3368, 3),
            Attraction("Promthep Cape Sunset",      AttractionCategory.VIEWPOINT, "The island's southernmost tip — arrive an hour early for the best position for a spectacular Andaman sunset.",        "05:30 PM", 1.5f,   7.7670,  98.3027, 1),
            Attraction("Thai Cooking Class",        AttractionCategory.FOOD,      "Hands-on half-day cooking class: visit a morning market then master pad thai, green curry and mango sticky rice.",    "09:00 AM", 4.0f,   7.8804,  98.3923, 2)
        ),

        "Seychelles" to listOf(
            Attraction("Anse Source d'Argent",      AttractionCategory.BEACH,     "Consistently ranked the world's most beautiful beach — ancient granite boulders against powdery coral sand.",       "09:00 AM", 3.0f,  -4.3681,  55.8384, 1),
            Attraction("Vallée de Mai",             AttractionCategory.NATURE,    "UNESCO World Heritage prehistoric palm forest — home of the coco de mer, the world's largest seed.",                 "09:00 AM", 3.0f,  -4.3266,  55.7389, 2),
            Attraction("Beau Vallon Beach",         AttractionCategory.BEACH,     "The liveliest beach on Mahé, perfect for watersports, beach volleyball and a Wednesday night street food market.",    "10:00 AM", 2.5f,  -4.6186,  55.4242, 1),
            Attraction("Cousin Island Reserve",     AttractionCategory.NATURE,    "A privately managed nature reserve — home to hawksbill turtles, Seychelles warblers and thousands of seabirds.",     "09:00 AM", 3.0f,  -4.3369,  55.6682, 3),
            Attraction("Victoria Local Market",     AttractionCategory.MARKET,    "The colourful capital's bustling market packed with tropical spices, fresh tuna, island crafts and curries.",         "08:00 AM", 2.0f,  -4.6185,  55.4520, 2),
            Attraction("Anse Lazio",                AttractionCategory.BEACH,     "Praslin's finest beach — crystal-clear water, gentle waves and a backdrop of lush green forest.",                    "10:00 AM", 3.0f,  -4.2869,  55.7236, 2),
            Attraction("Aldabra Atoll Excursion",   AttractionCategory.ADVENTURE, "The world's second-largest coral atoll — habitat for 150,000 giant tortoises and pristine reef ecosystems.",         "07:00 AM", 8.0f,  -9.4167,  46.3667, 3),
            Attraction("Creole Dinner at Mahek",    AttractionCategory.FOOD,      "Authentic Seychellois Creole feast: grilled red snapper, coconut curry, breadfruit chips and fresh tropical fruit.",   "07:00 PM", 2.0f,  -4.6185,  55.4520, 1)
        ),

        "Positano" to listOf(
            Attraction("Spiaggia Grande",           AttractionCategory.BEACH,     "Positano's main pebble beach — rent a sun lounger and gaze up at the colourful houses stacked against the cliff.",   "10:00 AM", 3.0f,  40.6281,  14.4851, 1),
            Attraction("Church of Santa Maria",     AttractionCategory.TEMPLE,    "The 13th-century church with a stunning majolica-tiled dome that has become the symbol of Positano.",                "09:00 AM", 1.0f,  40.6284,  14.4849, 1),
            Attraction("Sentiero degli Dei",        AttractionCategory.NATURE,    "The 'Path of the Gods' — a cliffside trail from Bomerano to Positano with the most dramatic coastal views in Italy.", "08:00 AM", 4.0f,  40.6439,  14.5607, 2),
            Attraction("Li Galli Islands",          AttractionCategory.NATURE,    "A private archipelago once owned by Rudolf Nureyev — boat trip reveals stunning sea grottos and crystal water.",       "10:00 AM", 3.0f,  40.5705,  14.4251, 2),
            Attraction("Fornillo Beach",            AttractionCategory.BEACH,     "A quieter cove beyond the headland from Spiaggia Grande — far fewer crowds and a wonderful beach bar.",               "11:00 AM", 2.0f,  40.6273,  14.4828, 1),
            Attraction("Ceramic Workshop Tour",     AttractionCategory.MARKET,    "Positano's artisan ceramic tradition dates back centuries — tour a studio and paint your own handcrafted tile.",      "03:00 PM", 2.0f,  40.6281,  14.4851, 3),
            Attraction("Sunset Aperitivo",          AttractionCategory.FOOD,      "Sip limoncello spritz at a terrace bar watching the sun melt into the Tyrrhenian Sea — the quintessential moment.",   "06:30 PM", 1.5f,  40.6284,  14.4840, 1),
            Attraction("Praiano Day Trip",          AttractionCategory.VILLAGE,   "The unspoilt fishing village 5 km along the coast — quieter than Positano with equally stunning cliffs.",             "10:00 AM", 3.0f,  40.6165,  14.5381, 3)
        ),

        "Maui" to listOf(
            Attraction("Haleakalā Sunrise",         AttractionCategory.VIEWPOINT, "Drive to the 3,055m summit before dawn to watch the sun rise above the clouds — a profound Hawaiian experience.",     "04:00 AM", 4.0f,  20.7097, -156.2530, 1),
            Attraction("Road to Hana",              AttractionCategory.ADVENTURE, "A legendary 64-bridge, 620-waterfall drive through rainforest, bamboo groves and dramatic black-sand coastline.",     "08:00 AM", 8.0f,  20.7564, -156.0198, 2),
            Attraction("Molokini Crater Snorkelling",AttractionCategory.BEACH,    "A submerged volcanic crater forming a natural marine sanctuary — some of Hawaii's clearest water and best snorkelling.","08:00 AM", 3.0f,  20.6311, -156.4967, 1),
            Attraction("Napili Bay",                AttractionCategory.BEACH,     "A protected crescent bay on the West Maui coast — calm water ideal for swimming and frequent spinner dolphin sightings.", "09:00 AM", 3.0f,  20.9989, -156.6706, 3),
            Attraction("Lahaina Historic Town",     AttractionCategory.LANDMARK,  "The old royal capital of the Hawaiian Kingdom — walk among historic whaling-era buildings and a vast banyan tree.",     "01:00 PM", 2.0f,  20.8724, -156.6753, 1),
            Attraction("Pā'ia Town",                AttractionCategory.VILLAGE,   "Maui's laid-back surf town with colourful boutiques, the world's best fish tacos and the gateway to the North Shore.", "10:00 AM", 2.0f,  20.9136, -156.3680, 3),
            Attraction("Humpback Whale Watching",   AttractionCategory.NATURE,    "Dec–Apr: cruise the Maui Channel where thousands of humpback whales breach, sing and nurse their newborns.",           "08:00 AM", 3.0f,  20.8000, -156.5000, 2),
            Attraction("Maui Fish Market Lunch",    AttractionCategory.FOOD,      "A legendary no-frills fish market serving the freshest poke, grilled ahi and garlic shrimp on the island.",            "12:00 PM", 1.0f,  20.8893, -156.4729, 1)
        ),

        "Tulum" to listOf(
            Attraction("Tulum Archaeological Zone", AttractionCategory.LANDMARK,  "13th-century Maya cliff-top ruins with a turquoise Caribbean backdrop — one of the most scenic sites in Mexico.",    "07:00 AM", 2.5f,  20.2143,  -87.4654, 1),
            Attraction("Gran Cenote",               AttractionCategory.ADVENTURE, "A stunning open-air cenote with crystal-clear freshwater, stalactites and turtles — best visited at opening time.",    "09:00 AM", 2.0f,  20.2336,  -87.5053, 1),
            Attraction("Cenote Dos Ojos Diving",    AttractionCategory.ADVENTURE, "A world-famous cave-diving system with two connected caverns — crystal blue light filters through underwater openings.", "09:00 AM", 3.0f,  20.2774,  -87.5156, 2),
            Attraction("Sian Ka'an Biosphere",      AttractionCategory.NATURE,    "Float down a Maya channel through a UNESCO-listed reserve — spot manatees, crocodiles and tropical birds.",           "08:00 AM", 5.0f,  19.6814,  -87.4894, 2),
            Attraction("Playa Paraiso",             AttractionCategory.BEACH,     "Tulum's most beautiful stretch of beach — wide, white, calm Caribbean waves and iconic palapa beach clubs.",           "10:00 AM", 3.0f,  20.2200,  -87.4600, 1),
            Attraction("Coba Maya Ruins",           AttractionCategory.LANDMARK,  "Ancient Maya city deep in the jungle — climb the 42m Nohoch Mul pyramid for panoramic rainforest views.",             "09:00 AM", 4.0f,  20.4900,  -87.7200, 3),
            Attraction("Tulum Beach Sunset",        AttractionCategory.VIEWPOINT, "Watch the Caribbean sunset from the cliff below the ruins, then walk the beach strip as the restaurants light up.",     "05:30 PM", 2.0f,  20.2114,  -87.4654, 1),
            Attraction("Mexican Street Tacos",      AttractionCategory.FOOD,      "Eat at a roadside taqueria in Tulum Pueblo — al pastor, cochinita pibil and freshly made tortillas from the comal.",    "07:30 PM", 1.5f,  20.2130,  -87.4630, 2)
        ),

        "Koh Samui" to listOf(
            Attraction("Chaweng Beach",             AttractionCategory.BEACH,     "Koh Samui's most famous beach — 7km of powder-white sand, warm Gulf water and a buzzing beachfront scene.",          "09:00 AM", 3.0f,   9.5386, 100.0621, 1),
            Attraction("Big Buddha Temple",         AttractionCategory.TEMPLE,    "A 12m golden Buddha seated on a small island connected by a causeway — the island's most iconic landmark.",           "09:30 AM", 1.0f,   9.5630, 100.0613, 1),
            Attraction("Ang Thong Marine Park",     AttractionCategory.NATURE,    "An archipelago of 42 uninhabited islands — snorkel, kayak sea caves and hike to the famous emerald lake.",            "08:00 AM", 8.0f,   9.7607, 100.0543, 2),
            Attraction("Na Muang Waterfall",        AttractionCategory.NATURE,    "Two stunning jungle waterfalls in the island's interior — the first has a natural swimming pool beneath it.",           "09:00 AM", 2.5f,   9.4830, 100.0118, 3),
            Attraction("Fisherman's Village",       AttractionCategory.VILLAGE,   "Bophut's charming old fishing quarter with boutique shops, oceanfront restaurants and a Friday night walking street.",  "07:00 PM", 2.0f,   9.5613, 100.0049, 1),
            Attraction("Secret Buddha Garden",      AttractionCategory.TEMPLE,    "A hidden hilltop garden of fairy-tale stone statues in the jungle interior — reached by a bumpy mountain track.",      "10:00 AM", 2.0f,   9.5120, 100.0136, 3),
            Attraction("Lamai Beach",               AttractionCategory.BEACH,     "The island's second-longest beach — quieter than Chaweng with rock formations, good snorkelling and cheaper bars.",    "10:00 AM", 3.0f,   9.4782, 100.0570, 2),
            Attraction("Thai Seafood BBQ Dinner",   AttractionCategory.FOOD,      "Fresh prawns, whole fish and squid grilled over charcoal at a beachfront restaurant as longtail boats pass by.",       "07:00 PM", 2.0f,   9.5386, 100.0621, 1)
        ),

        // ── MOUNTAIN (remaining) ──────────────────────────────────────────────────

        "Banff" to listOf(
            Attraction("Moraine Lake",              AttractionCategory.NATURE,    "The Valley of the Ten Peaks reflected in a lake of impossible turquoise — arrive at 6 AM before the crowds.",          "06:00 AM", 3.0f,  51.3217, -116.1860, 1),
            Attraction("Lake Louise",               AttractionCategory.NATURE,    "A glacier-fed emerald lake with the Victoria Glacier as a backdrop — rent a canoe for the definitive view.",           "09:00 AM", 2.5f,  51.4168, -116.2181, 1),
            Attraction("Icefields Parkway Drive",   AttractionCategory.ADVENTURE, "The 230km mountain highway between Banff and Jasper — widely considered the world's most spectacular road.",           "09:00 AM", 8.0f,  52.2197, -117.1858, 2),
            Attraction("Johnston Canyon Ice Walk",  AttractionCategory.ADVENTURE, "Walk catwalks bolted to canyon walls beside frozen waterfalls — a magical winter experience.",                          "09:00 AM", 3.0f,  51.2469, -115.8399, 3),
            Attraction("Sulphur Mountain Gondola",  AttractionCategory.VIEWPOINT, "A gondola to 2,281m above Banff townsite for sweeping Rockies panoramas and the famous ridge boardwalk.",              "10:00 AM", 2.5f,  51.1542, -115.5653, 2),
            Attraction("Banff Upper Hot Springs",   AttractionCategory.ADVENTURE, "Soak in natural mineral hot springs at 1,585m with mountain views on all sides — delightful after a long hike.",       "03:00 PM", 2.0f,  51.1554, -115.5617, 2),
            Attraction("Bow Valley Parkway",        AttractionCategory.NATURE,    "A quieter parallel route to the Trans-Canada with wildlife sightings — bear, elk, wolf and bighorn sheep are common.",   "07:00 AM", 3.0f,  51.1784, -115.7014, 3),
            Attraction("Banff Elk & Oats Dinner",   AttractionCategory.FOOD,      "Canadian Rocky Mountain cuisine: local elk, bison and wild mushrooms served in a rustic log-cabin dining room.",        "07:00 PM", 2.0f,  51.1784, -115.5708, 1)
        ),

        "Dolomites" to listOf(
            Attraction("Tre Cime di Lavaredo",      AttractionCategory.VIEWPOINT, "Three iconic rock towers rising from a high plateau — the classic 9km loop hike is the Dolomites' most famous walk.",  "08:00 AM", 4.0f,  46.6174,  12.3016, 1),
            Attraction("Lake Braies (Pragser Wildsee)",AttractionCategory.NATURE, "An impossibly turquoise lake under a forested amphitheatre of cliffs — the most beautiful lake in the Alps.",          "07:00 AM", 2.5f,  46.6944,  12.0861, 2),
            Attraction("Seceda Ridge",              AttractionCategory.VIEWPOINT, "Cable car to an extraordinary serrated ridge above Val Gardena — the most dramatic panorama in the Dolomites.",        "09:00 AM", 3.0f,  46.5883,  11.7231, 1),
            Attraction("Cortina d'Ampezzo Town",    AttractionCategory.VILLAGE,   "The glamorous queen of the Dolomites — explore luxury boutiques, Belle Époque cafés and the famous corso on foot.",    "02:00 PM", 2.0f,  46.5369,  12.1353, 3),
            Attraction("Alpe di Siusi Plateau",     AttractionCategory.NATURE,    "Europe's largest high-altitude alpine meadow at 1,800m — dotted with wildflowers and picture-perfect barns.",         "09:00 AM", 4.0f,  46.5465,  11.6320, 2),
            Attraction("Sella Ronda Ski Circuit",   AttractionCategory.ADVENTURE, "The world's greatest ski circuit links four valleys with 26km of non-stop slopes around the Sella Massif.",            "09:00 AM", 7.0f,  46.5167,  11.8667, 3),
            Attraction("Rifugio Dinner",            AttractionCategory.FOOD,      "A traditional mountain hut dinner at altitude: canederli dumplings, venison goulash and warm apple strudel.",           "12:30 PM", 2.0f,  46.6174,  12.3016, 1),
            Attraction("Gardena Pass Sunrise",      AttractionCategory.ADVENTURE, "Cycle or drive the 2,121m Gardena Pass at dawn — a serpentine road through the most photogenic section of the Dolomites.", "05:30 AM", 3.0f,  46.5617,  11.8295, 2)
        ),

        "Patagonia" to listOf(
            Attraction("Torres del Paine Towers",   AttractionCategory.ADVENTURE, "The iconic granite towers: an 8-hour return hike to the base where the three towers are reflected in a lake.",         "06:00 AM", 8.0f, -50.9423,  -72.9875, 1),
            Attraction("Perito Moreno Glacier",     AttractionCategory.NATURE,    "A 30km wall of brilliant blue ice advancing into a lake — watch and listen for massive ice calving events.",           "09:00 AM", 3.0f, -50.4969,  -73.1271, 2),
            Attraction("Grey Glacier Ice Trek",     AttractionCategory.ADVENTURE, "Strap on crampons and trek across the ancient ice of Grey Glacier with a certified Patagonian guide.",                 "09:00 AM", 5.0f, -51.0333,  -73.2000, 3),
            Attraction("El Chaltén Village",        AttractionCategory.VILLAGE,   "Argentina's trekking capital beneath Mount Fitz Roy — a village entirely dedicated to hiking and mountaineering.",    "09:00 AM", 2.0f, -49.3306,  -72.8885, 2),
            Attraction("Mount Fitz Roy Laguna",     AttractionCategory.VIEWPOINT, "The 2-hour hike to Laguna de los Tres at sunrise for the most photographed view of Fitz Roy's jagged peak.",           "05:30 AM", 5.0f, -49.2728,  -72.8558, 2),
            Attraction("Lake Pehoé Kayaking",       AttractionCategory.ADVENTURE, "Paddle the electric-blue glacial lake with the Cuernos del Paine rising 2,000m directly above your kayak.",           "10:00 AM", 3.0f, -51.1000,  -72.9833, 3),
            Attraction("Condor Viewpoint",          AttractionCategory.NATURE,    "Watch Andean condors — the world's largest flying birds — soaring on thermals above the Patagonian steppe.",           "02:00 PM", 2.0f, -51.0500,  -72.9500, 1),
            Attraction("Patagonian Lamb Asado",     AttractionCategory.FOOD,      "A whole Patagonian lamb slow-roasted gaucho-style over an open wood fire — the ultimate regional experience.",         "07:00 PM", 2.5f, -51.7330,  -72.5000, 1)
        ),

        "Mount Fuji" to listOf(
            Attraction("Fuji Summit Crater",        AttractionCategory.ADVENTURE, "Climb Japan's sacred mountain — most attempt via the Yoshida Trail, reaching the crater rim at dawn for Goraiko.",    "11:00 PM", 8.0f,  35.3606, 138.7274, 1),
            Attraction("Chureito Pagoda",           AttractionCategory.VIEWPOINT, "The iconic five-storey pagoda framing Fuji at eye level — the single most photographed view of the mountain.",         "09:00 AM", 1.5f,  35.3903, 138.7789, 2),
            Attraction("Lake Kawaguchiko",          AttractionCategory.NATURE,    "The closest and most accessible of the Fuji Five Lakes — rent a bicycle and circle the lake for reflected summit views.", "09:00 AM", 3.0f,  35.5072, 138.7570, 2),
            Attraction("Fuji-Q Highland",           AttractionCategory.ADVENTURE, "Adrenaline theme park at Fuji's base featuring Japan's most extreme roller coasters with mountain views.",             "10:00 AM", 4.0f,  35.4835, 138.7824, 3),
            Attraction("Aokigahara Forest",         AttractionCategory.NATURE,    "The 'Sea of Trees' at Fuji's base — a dense lava forest with an eerie silence and an ancient spiritual significance.", "10:00 AM", 2.5f,  35.4600, 138.6300, 3),
            Attraction("Oshino Hakkai Springs",     AttractionCategory.NATURE,    "Eight crystal-clear ponds fed by Fuji's snowmelt, so pure you can see the sandy bottom at 4-metre depths.",          "11:00 AM", 1.5f,  35.4803, 138.8364, 2),
            Attraction("Hakone Onsen Soak",         AttractionCategory.ADVENTURE, "Soak in volcanic hot springs with Fuji framed in the rotenburo (outdoor bath) — a profoundly Japanese experience.",   "03:00 PM", 3.0f,  35.2278, 139.1058, 2),
            Attraction("Fujinomiya Yakisoba",       AttractionCategory.FOOD,      "Fuji's local speciality: thick flat noodles stir-fried with pork and local vegetables — best in Fujinomiya City.",    "12:00 PM", 1.0f,  35.2255, 138.6165, 1)
        ),

        "Himalayas" to listOf(
            Attraction("Everest Base Camp Trek",    AttractionCategory.ADVENTURE, "The world's most iconic 14-day high-altitude walk to 5,364m at the foot of the world's highest mountain.",           "07:00 AM", 9.0f,  28.0025,  86.8530, 1),
            Attraction("Namche Bazaar",             AttractionCategory.VILLAGE,   "The Sherpa capital at 3,440m — the last major resupply hub on the Everest trail, with a famous Saturday market.",     "09:00 AM", 2.0f,  27.8056,  86.7139, 2),
            Attraction("Tengboche Monastery",       AttractionCategory.TEMPLE,    "The Himalayas' most sacred monastery at 3,867m — prayer ceremonies at dawn with Ama Dablam as the backdrop.",        "07:00 AM", 2.0f,  27.8367,  86.7642, 2),
            Attraction("Annapurna Circuit",         AttractionCategory.ADVENTURE, "A multi-week circumnavigation of the Annapurna Massif — some of the most varied mountain trekking on the planet.",    "07:00 AM", 9.0f,  28.5971,  83.9750, 3),
            Attraction("Kathmandu Durbar Square",   AttractionCategory.LANDMARK,  "A UNESCO-listed royal square of ancient palaces, temples and living goddess courtyards in the heart of Kathmandu.",   "09:00 AM", 3.0f,  27.7042,  85.3075, 1),
            Attraction("Poon Hill Sunrise",         AttractionCategory.VIEWPOINT, "The 3,210m viewpoint above Ghorepani — a 3-day trek to see the Annapurna range lit pink at the break of dawn.",       "04:00 AM", 5.0f,  28.3999,  83.6995, 2),
            Attraction("Pashupatinath Temple",      AttractionCategory.TEMPLE,    "Nepal's holiest Hindu temple complex beside the Bagmati River — a spiritual journey unlike anywhere else on Earth.",   "09:00 AM", 2.0f,  27.7107,  85.3484, 1),
            Attraction("Dal Bhat Dinner",           AttractionCategory.FOOD,      "The Nepali national dish — a wholesome set meal of lentil soup, curried vegetables and rice, refilled unlimited.",    "07:00 PM", 1.5f,  27.7172,  85.3240, 1)
        ),

        "Rocky Mountains" to listOf(
            Attraction("Yellowstone Geysers",       AttractionCategory.NATURE,    "Old Faithful and 10,000 other geothermal features in the world's first national park — a geological wonderland.",    "08:00 AM", 5.0f,  44.4280, -110.5885, 1),
            Attraction("Grand Prismatic Spring",    AttractionCategory.NATURE,    "The world's third-largest hot spring, with vivid rainbow rings of thermophile bacteria visible from the overlook.",   "09:00 AM", 2.0f,  44.5250, -110.8380, 1),
            Attraction("Grand Teton National Park", AttractionCategory.NATURE,    "Cathedral spires of the Teton Range rise 2,000m above the Jackson Hole valley floor — iconic American wilderness.",  "08:00 AM", 6.0f,  43.7904, -110.6818, 2),
            Attraction("Going-to-the-Sun Road",     AttractionCategory.ADVENTURE, "A 80km mountain highway crossing Glacier National Park at Logan Pass — only open in summer due to snow.",             "08:00 AM", 4.0f,  48.6960, -113.7180, 3),
            Attraction("Rocky Mountain National Park",AttractionCategory.NATURE,  "Over 60 peaks above 3,650m with 500km of trails through alpine tundra, elk meadows and crystal-clear lakes.",         "07:00 AM", 6.0f,  40.3428, -105.6836, 2),
            Attraction("Lamar Valley Wildlife Drive",AttractionCategory.NATURE,   "The world's best road wildlife watching — wolves, bison herds, grizzly bears and pronghorn in Yellowstone's Serengeti.", "06:30 AM", 4.0f,  44.8947, -110.2218, 1),
            Attraction("Whitewater Rafting",        AttractionCategory.ADVENTURE, "Raft the Arkansas or Snake River rapids through dramatic Rocky Mountain canyons — suitable for all levels.",           "09:00 AM", 4.0f,  44.4280, -110.5885, 3),
            Attraction("Wyoming Bison Burger",      AttractionCategory.FOOD,      "A grass-fed bison burger from a ranch-to-table kitchen in Jackson, Wyoming — the real American West on a plate.",     "12:00 PM", 1.0f,  43.4799, -110.7624, 1)
        ),

        "Norwegian Fjords" to listOf(
            Attraction("Geirangerfjord Cruise",     AttractionCategory.NATURE,    "Sail the UNESCO-listed fjord past the Seven Sisters waterfall and farms that cling impossibly to sheer cliff faces.",  "09:00 AM", 3.0f,  62.1000,   7.0833, 1),
            Attraction("Trolltunga Cliff Hike",     AttractionCategory.ADVENTURE, "A 10-hour hike to a tongue of rock jutting horizontally above a lake 700m below — Norway's most dramatic viewpoint.",  "06:00 AM", 10.0f, 60.1240,   6.7393, 2),
            Attraction("Preikestolen (Pulpit Rock)", AttractionCategory.ADVENTURE,"A flat rock platform 604m above Lysefjord — a 4-hour return hike to one of Norway's most visited viewpoints.",        "07:00 AM", 4.0f,  58.9868,   6.1896, 3),
            Attraction("Flåm Scenic Railway",       AttractionCategory.ADVENTURE, "A 20km railway descending 863m through waterfalls, hairpin curves and tunnels — one of the world's most beautiful train rides.", "10:00 AM", 2.0f, 60.8630, 7.1195, 1),
            Attraction("Nærøyfjord Kayaking",       AttractionCategory.ADVENTURE, "Paddle the world's narrowest fjord where 1,700m walls close to just 250m width — absolute silence and wild grandeur.",  "09:00 AM", 5.0f,  60.9280,   6.9680, 3),
            Attraction("Stegastein Viewpoint",      AttractionCategory.VIEWPOINT, "A cantilevered viewing platform projecting 30m over the Aurlandsfjord — best reached early morning in the mist.",       "09:00 AM", 1.0f,  60.8700,   7.1200, 2),
            Attraction("Bergen Bryggen Wharf",      AttractionCategory.LANDMARK,  "The UNESCO-listed wooden wharf of Bergen, gateway to the fjords — colourful Hanseatic buildings line the waterfront.",  "10:00 AM", 2.0f,  60.3974,   5.3246, 1),
            Attraction("Norwegian Salmon Dinner",   AttractionCategory.FOOD,      "Fresh Norwegian salmon grilled over birchwood and served with dill cream and cucumber at a fjordside restaurant.",      "07:00 PM", 2.0f,  62.1000,   7.0833, 1)
        ),

        "Yosemite" to listOf(
            Attraction("Valley View & El Capitan",  AttractionCategory.VIEWPOINT, "The classic first glimpse of Yosemite Valley — El Capitan's 900m granite monolith framed by pines and the Merced River.", "09:00 AM", 1.5f, 37.7161, -119.6482, 1),
            Attraction("Half Dome Trail",           AttractionCategory.ADVENTURE, "The park's iconic 24km round-trip with cables at the summit — a bucket-list hike requiring a permit.",                  "05:00 AM", 10.0f, 37.7459, -119.5332, 2),
            Attraction("Yosemite Falls Hike",       AttractionCategory.NATURE,    "Hike to the base of North America's tallest waterfall (739m) for a full-body soaking in the spring runoff.",           "09:00 AM", 3.0f,  37.7562, -119.5973, 1),
            Attraction("Glacier Point Panorama",    AttractionCategory.VIEWPOINT, "A dramatic 975m-high balcony overlooking the entire valley — Half Dome, Nevada Falls and the High Sierra at a glance.",  "02:00 PM", 2.0f,  37.7297, -119.5736, 1),
            Attraction("Mariposa Grove Sequoias",   AttractionCategory.NATURE,    "Walk among 500 ancient giant sequoias, including the 1,000-year-old Grizzly Giant — the largest organisms on Earth.",  "09:00 AM", 2.5f,  37.5133, -119.6016, 3),
            Attraction("Mirror Lake Stroll",        AttractionCategory.NATURE,    "A gentle 8km loop around a lake reflecting Half Dome — spectacular in spring when snowmelt fills the lake fully.",      "09:00 AM", 2.0f,  37.7459, -119.5582, 3),
            Attraction("Ahwahnee Hotel Brunch",     AttractionCategory.FOOD,      "Brunch in Yosemite's legendary 1927 granite hotel with cathedral-ceiling Great Lounge and valley views.",              "10:00 AM", 2.0f,  37.7478, -119.5752, 2),
            Attraction("Tunnel View Sunrise",       AttractionCategory.VIEWPOINT, "The tunnel-framed panorama at dawn when mist fills the valley and every photographer in the park converges here.",       "05:30 AM", 1.0f,  37.7161, -119.6792, 1)
        ),

        "Faroe Islands" to listOf(
            Attraction("Gásadalur & Múlafossur",    AttractionCategory.VIEWPOINT, "A waterfall tumbling directly into the ocean beside a lone farmhouse — the defining image of the Faroe Islands.",       "09:00 AM", 2.5f,  62.1086,  -7.3933, 1),
            Attraction("Sørvágsvatn Cliff Hike",    AttractionCategory.ADVENTURE, "The lake that appears to float above the ocean — a 2-hour hike to the famous optical-illusion viewpoint.",             "09:00 AM", 3.0f,  62.0612,  -7.3050, 1),
            Attraction("Puffin Watching on Mykines", AttractionCategory.NATURE,   "Take the ferry to Mykines, walk the ridge path to the lighthouse and sit among thousands of nesting Atlantic puffins.",  "09:00 AM", 6.0f,  62.1028,  -7.6457, 2),
            Attraction("Kirkjubøur Historic Farm",  AttractionCategory.LANDMARK,  "The oldest inhabited wooden farmhouse in the world (dating to c. 1100) with a ruined 14th-century Gothic cathedral.",   "10:00 AM", 2.0f,  61.9511,  -6.8832, 2),
            Attraction("Vestmanna Sea Cave Tour",   AttractionCategory.ADVENTURE, "A boat weaves through towering sea cliffs and into dripping sea caves packed with nesting seabirds.",                   "10:00 AM", 2.5f,  62.1553,  -7.1682, 3),
            Attraction("Eiðiskollur Hike",          AttractionCategory.ADVENTURE, "A ridge walk on the island of Esvágoy with sheer 375m cliffs falling into the Atlantic on both sides.",                "09:00 AM", 4.0f,  62.2753,  -7.0556, 3),
            Attraction("Tórshavn Old Town",         AttractionCategory.VILLAGE,   "One of the world's smallest capitals — a cluster of turf-roofed timber buildings crammed onto a promontory.",          "02:00 PM", 2.0f,  62.0071,  -6.7941, 1),
            Attraction("Faroese Lamb Dinner",       AttractionCategory.FOOD,      "Slow-braised Faroese mountain lamb with root vegetables — the islands' definitive dish, served in a wood-panelled inn.","07:00 PM", 2.0f,  62.0071,  -6.7941, 1)
        ),

        // ── CITY (remaining) ──────────────────────────────────────────────────────

        "Dubai" to listOf(
            Attraction("Burj Khalifa Observation Deck",AttractionCategory.VIEWPOINT,"The 124th/148th floor observation decks of the world's tallest building — book At the Top SKY for the ultimate view.", "09:00 AM", 2.0f,  25.1972,  55.2744, 1),
            Attraction("Dubai Frame",               AttractionCategory.LANDMARK,  "A 150m picture frame on the border of old and new Dubai — one side views historic Deira, the other gleaming towers.",    "10:00 AM", 1.5f,  25.2350,  55.3000, 1),
            Attraction("Dubai Desert Safari",       AttractionCategory.ADVENTURE, "Dune-bashing by 4x4, camel riding, sandboarding and a Bedouin camp dinner under the stars in the Arabian Desert.",       "03:00 PM", 6.0f,  24.8000,  55.6500, 2),
            Attraction("Gold & Spice Souks",        AttractionCategory.MARKET,    "Dubai's ancient trading heritage — wander the glittering covered Gold Souk and the aromatic Spice Souk beside the Creek.", "10:00 AM", 2.0f,  25.2697,  55.3095, 1),
            Attraction("Palm Jumeirah",             AttractionCategory.LANDMARK,  "Board the Palm Monorail or take an abra boat to view the world's largest artificial island and its luxury hotels.",        "11:00 AM", 2.0f,  25.1124,  55.1390, 3),
            Attraction("Dubai Mall Aquarium",       AttractionCategory.LANDMARK,  "The world's largest suspended aquarium — a 10-million-litre tank with sand tiger sharks, manta rays and thousands of fish.", "02:00 PM", 2.0f,  25.1972,  55.2797, 3),
            Attraction("Abra Creek Crossing",       AttractionCategory.ADVENTURE, "Cross the historic Dubai Creek on a traditional wooden abra boat — 1 AED and 5 minutes connects two worlds.",             "09:00 AM", 1.0f,  25.2657,  55.3050, 1),
            Attraction("Emirati Tasting Menu",      AttractionCategory.FOOD,      "Sample harees, machboos, luqaimat and camel meat at a heritage restaurant for an authentic Emirati culinary experience.",  "07:00 PM", 2.5f,  25.2048,  55.2708, 2)
        ),

        "Barcelona" to listOf(
            Attraction("Sagrada Família",           AttractionCategory.LANDMARK,  "Gaudí's towering masterpiece — 142 years under construction, with the interior a forest of light-filtering stone.",     "09:00 AM", 2.5f,  41.4036,   2.1744, 1),
            Attraction("Park Güell",                AttractionCategory.LANDMARK,  "Gaudí's mosaic-covered hilltop park with the famous dragon staircase and a panoramic terrace over the city.",            "10:00 AM", 2.0f,  41.4145,   2.1527, 1),
            Attraction("La Boqueria Market",        AttractionCategory.MARKET,    "Barcelona's legendary covered food market on La Rambla — fresh Jamón, colourful fruit stalls and tapas bars.",            "09:00 AM", 1.5f,  41.3817,   2.1720, 2),
            Attraction("Gothic Quarter",            AttractionCategory.VILLAGE,   "Medieval lanes, Roman walls and hidden plazas in the oldest part of Barcelona — best discovered without a map.",          "04:00 PM", 2.5f,  41.3831,   2.1770, 2),
            Attraction("Casa Batlló",               AttractionCategory.LANDMARK,  "Gaudí's 'House of Bones' — a dragon-scale rooftop, skeletal façade and interior that flows like a continuous ocean.",    "11:00 AM", 1.5f,  41.3916,   2.1649, 2),
            Attraction("La Barceloneta Beach",      AttractionCategory.BEACH,     "Barcelona's iconic city beach — packed in summer but walkable year-round with great seafood chiringuitos (beach bars).", "10:00 AM", 3.0f,  41.3785,   2.1889, 3),
            Attraction("Camp Nou Tour",             AttractionCategory.LANDMARK,  "The home of FC Barcelona — Europe's largest football stadium with a museum covering the club's unparalleled history.",    "10:00 AM", 2.5f,  41.3809,   2.1228, 3),
            Attraction("Catalan Dinner & Cava",     AttractionCategory.FOOD,      "Share plates of patatas bravas, jamón ibérico, fresh anchovies and crema catalana at a classic Eixample bodega.",         "09:00 PM", 2.5f,  41.3887,   2.1680, 1)
        ),

        "Amsterdam" to listOf(
            Attraction("Rijksmuseum",               AttractionCategory.MUSEUM,    "The Netherlands' national art museum — Rembrandt's Night Watch and Vermeer's Milkmaid in a palace of Dutch Golden Age art.", "09:00 AM", 3.0f, 52.3600,  4.8852, 1),
            Attraction("Anne Frank House",          AttractionCategory.MUSEUM,    "The secret annex where Anne Frank hid for 2 years — one of the world's most profound and moving historical experiences.",   "09:00 AM", 1.5f,  52.3752,   4.8839, 1),
            Attraction("Van Gogh Museum",           AttractionCategory.MUSEUM,    "The world's largest collection of Van Gogh's works — 200 paintings and 500 drawings in a purpose-built museum.",           "01:00 PM", 2.0f,  52.3584,   4.8811, 1),
            Attraction("Canal Boat Tour",           AttractionCategory.NATURE,    "Glide through the UNESCO-listed canal ring past 17th-century merchant houses — best experienced at twilight.",              "04:00 PM", 1.5f,  52.3676,   4.9041, 2),
            Attraction("Jordaan Neighbourhood",     AttractionCategory.VILLAGE,   "Amsterdam's most charming quarter — antique bookshops, boutique galleries, cosy brown cafés and Saturday markets.",       "10:00 AM", 2.5f,  52.3743,   4.8793, 2),
            Attraction("Vondelpark",                AttractionCategory.NATURE,    "Amsterdam's beloved green lung — locals picnic, cycle and rollerskate among ponds, rose gardens and open-air concerts.",   "11:00 AM", 1.5f,  52.3580,   4.8680, 3),
            Attraction("Keukenhof Gardens",         AttractionCategory.NATURE,    "Mar–May only: the world's largest flower garden with 7 million tulip bulbs in full bloom — a spectacular Dutch icon.",    "09:00 AM", 3.0f,  52.2697,   4.5462, 3),
            Attraction("Heineken Experience",       AttractionCategory.FOOD,      "Tour the original 1864 Heineken brewery, learn the brewing process and enjoy fresh beer in the tasting room.",             "12:00 PM", 2.0f,  52.3579,   4.8929, 2)
        ),

        "Singapore" to listOf(
            Attraction("Gardens by the Bay",        AttractionCategory.NATURE,    "Futuristic Supertrees glow in a spectacular nightly light show above the world's largest indoor cooled garden domes.",    "07:00 PM", 3.0f,   1.2816, 103.8636, 1),
            Attraction("Marina Bay Sands SkyPark",  AttractionCategory.VIEWPOINT, "The cantilevered rooftop deck and infinity pool 200m above the city — the most famous view of Singapore's skyline.",      "08:00 PM", 2.0f,   1.2838, 103.8607, 1),
            Attraction("Hawker Centre Food Tour",   AttractionCategory.FOOD,      "Navigate a local hawker centre for Hainanese chicken rice, laksa, char kway teow and cendol — under \$3 a dish.",         "12:00 PM", 2.0f,   1.3521, 103.8198, 1),
            Attraction("Sentosa Island Beach",      AttractionCategory.BEACH,     "Take the cable car to Sentosa for Universal Studios, Palawan Beach and the S.E.A. Aquarium complex.",                    "10:00 AM", 5.0f,   1.2494, 103.8303, 2),
            Attraction("Chinatown Heritage Centre", AttractionCategory.MUSEUM,    "Walk through the narrow streets, clan associations and restored shophouses of Singapore's Chinatown district.",            "10:00 AM", 2.0f,   1.2838, 103.8446, 3),
            Attraction("Little India Stroll",       AttractionCategory.VILLAGE,   "Technicolour temples, incense-filled garland shops and Tamil restaurants on Serangoon Road — vibrant and authentic.",     "10:00 AM", 2.0f,   1.3066, 103.8519, 3),
            Attraction("Singapore Botanic Gardens", AttractionCategory.NATURE,    "A UNESCO World Heritage tropical garden — 74 hectares with the world-famous National Orchid Garden.",                     "08:00 AM", 2.5f,   1.3138, 103.8159, 2),
            Attraction("Chilli Crab Dinner",        AttractionCategory.FOOD,      "Crack open a mud crab in spicy tomato-chilli sauce at a East Coast Park seafood restaurant — Singapore's national dish.", "07:00 PM", 2.5f,   1.3005, 103.9065, 1)
        ),

        "Prague" to listOf(
            Attraction("Prague Castle Complex",     AttractionCategory.LANDMARK,  "The world's largest ancient castle by area — palaces, a Gothic cathedral, galleries and golden lane in one complex.",    "09:00 AM", 3.5f,  50.0911,  14.4008, 1),
            Attraction("Charles Bridge at Dawn",    AttractionCategory.LANDMARK,  "The 14th-century pedestrian bridge lined with 30 Baroque statues — magical at 6 AM before the tour groups arrive.",     "06:00 AM", 1.0f,  50.0865,  14.4114, 1),
            Attraction("Old Town Square",           AttractionCategory.LANDMARK,  "Prague's medieval heart — the 600-year-old Astronomical Clock, twin-spired Týn Church and countless café terraces.",    "10:00 AM", 2.0f,  50.0875,  14.4213, 1),
            Attraction("Josefov Jewish Quarter",    AttractionCategory.MUSEUM,    "Europe's best-preserved Jewish quarter with six synagogues, the oldest Jewish cemetery in Europe and a moving museum.",   "02:00 PM", 2.5f,  50.0906,  14.4185, 2),
            Attraction("Vinohrady & Žižkov Walk",   AttractionCategory.VILLAGE,   "Explore two charming residential neighbourhoods with Art Nouveau buildings, local beer halls and a TV tower.",           "10:00 AM", 3.0f,  50.0769,  14.4415, 3),
            Attraction("Petřín Hill & Funicular",   AttractionCategory.NATURE,    "A forested hilltop park with a mini Eiffel Tower, mirror maze and sweeping views from the funicular railway summit.",    "02:00 PM", 2.0f,  50.0836,  14.3997, 2),
            Attraction("Kafka Museum",              AttractionCategory.MUSEUM,    "A fascinating exploration of Franz Kafka's life, works and relationship with Prague in an atmospheric riverside setting.",  "11:00 AM", 1.5f,  50.0892,  14.4063, 2),
            Attraction("Czech Beer & Svíčková Dinner",AttractionCategory.FOOD,    "Slow-braised sirloin in cream sauce with bread dumplings, washed down with an unpasteurised tank Pilsner Urquell.",     "07:00 PM", 2.0f,  50.0755,  14.4378, 1)
        ),

        "Istanbul" to listOf(
            Attraction("Hagia Sophia",              AttractionCategory.TEMPLE,    "Completed in 537 AD and the world's largest building for nearly a thousand years — a breathtaking fusion of civilisations.", "09:00 AM", 1.5f, 41.0086,  28.9802, 1),
            Attraction("Topkapi Palace",            AttractionCategory.LANDMARK,  "The Ottoman Empire's seat of power for 400 years — treasuries of imperial jewels, sacred relics and harem quarters.",    "10:30 AM", 3.0f,  41.0115,  28.9833, 1),
            Attraction("Grand Bazaar",              AttractionCategory.MARKET,    "One of the world's oldest and largest covered markets — 4,000 shops in a labyrinthine 15th-century complex.",            "11:00 AM", 2.5f,  41.0108,  28.9680, 2),
            Attraction("Blue Mosque",               AttractionCategory.TEMPLE,    "The Sultan Ahmed Mosque with six minarets and 20,000 hand-painted İznik tiles — visit between prayer times.",            "09:00 AM", 1.0f,  41.0054,  28.9768, 2),
            Attraction("Bosphorus Sunset Cruise",   AttractionCategory.NATURE,    "Ferry between two continents at golden hour — pass Ottoman waterfront palaces, fortresses and fishing villages.",         "05:30 PM", 2.0f,  41.0082,  28.9784, 1),
            Attraction("Spice Bazaar",              AttractionCategory.MARKET,    "The 17th-century Egyptian Market — mounds of saffron, dried figs, Turkish delight and hand-painted ceramics.",           "10:00 AM", 1.5f,  41.0161,  28.9704, 2),
            Attraction("Galata Tower",              AttractionCategory.VIEWPOINT, "A medieval 66m tower offering the best 360° panorama of Istanbul's two-continent skyline.",                               "06:00 PM", 1.0f,  41.0256,  28.9741, 3),
            Attraction("Bosphorus Fish Dinner",     AttractionCategory.FOOD,      "Grilled whole bream and meze at a waterfront fish restaurant beside the Bosphorus — Istanbul's finest culinary setting.",  "07:00 PM", 2.5f,  41.0451,  29.0057, 1)
        ),

        "Tokyo" to listOf(
            Attraction("Shinjuku Gyoen Garden",     AttractionCategory.NATURE,    "A former imperial retreat of 58 hectares — spectacular cherry blossoms in spring, serene Japanese landscape all year.",   "09:00 AM", 2.5f,  35.6852, 139.7100, 1),
            Attraction("Senso-ji Temple Asakusa",   AttractionCategory.TEMPLE,    "Tokyo's oldest and most atmospheric temple — lantern-lit Nakamise shopping street leads to the giant red thunder gate.",  "08:00 AM", 2.0f,  35.7148, 139.7967, 1),
            Attraction("Shibuya Scramble Crossing", AttractionCategory.LANDMARK,  "The world's busiest pedestrian crossing — up to 3,000 people cross simultaneously when all lights turn green.",           "09:00 PM", 0.5f,  35.6595, 139.7004, 2),
            Attraction("teamLab Planets Digital Art",AttractionCategory.MUSEUM,   "An immersive walk-through digital art museum where you wade through water into mirrored infinity rooms.",                 "11:00 AM", 2.5f,  35.6466, 139.7851, 2),
            Attraction("Tsukiji Outer Market",      AttractionCategory.FOOD,      "The world's most famous food market — eat fresh sushi, tamagoyaki and grilled scallops from vendor stalls at 7 AM.",      "07:00 AM", 2.0f,  35.6654, 139.7707, 1),
            Attraction("Meiji Shrine & Forest",     AttractionCategory.TEMPLE,    "A vast forested Shinto shrine in central Tokyo — 100,000 trees planted by citizens in 1920 create an urban sanctuary.",   "09:00 AM", 1.5f,  35.6764, 139.6993, 3),
            Attraction("Akihabara Electronics Town",AttractionCategory.MARKET,    "Tokyo's legendary electronics and anime district — 8 floors of manga, gadgets, retro games and maid cafés.",             "02:00 PM", 2.5f,  35.7024, 139.7742, 3),
            Attraction("Ramen Tasting Tour",        AttractionCategory.FOOD,      "Sample tonkotsu, shoyu and miso ramen at three local ramen shops in Shinjuku — start the evening at 7 PM.",              "07:00 PM", 3.0f,  35.6938, 139.7034, 2)
        ),

        // ── ADVENTURE (remaining) ─────────────────────────────────────────────────

        "Queenstown" to listOf(
            Attraction("Nevis Bungy Jump",          AttractionCategory.ADVENTURE, "A 134m bungy jump from a cable car cabin suspended above the Nevis River gorge — the highest in Australasia.",           "10:00 AM", 3.0f, -45.0242, 168.8356, 1),
            Attraction("Milford Sound Cruise",      AttractionCategory.NATURE,    "A day trip through the 'Eighth Wonder of the World' — sheer 1,200m cliffs, waterfalls and resident dolphins.",          "09:00 AM", 8.0f, -44.6413, 167.8971, 2),
            Attraction("Remarkables Ski Area",      AttractionCategory.ADVENTURE, "World-class skiing and snowboarding with spectacular views over Lake Wakatipu and the Queenstown basin.",                 "09:00 AM", 7.0f, -45.0600, 168.8167, 3),
            Attraction("Skyline Gondola & Luge",    AttractionCategory.ADVENTURE, "Cable car to Bob's Peak for panoramic views, then ride luge carts down the hillside — fun for all ages.",               "10:00 AM", 2.5f, -45.0286, 168.6615, 1),
            Attraction("Shotover Jet Boat Canyon",  AttractionCategory.ADVENTURE, "Skim the cliff walls of the Shotover River canyon at 85 km/h with 360° spins — the world's most exciting jet boat ride.", "01:00 PM", 1.5f, -45.0240, 168.7540, 1),
            Attraction("Arrowtown Historic Village",AttractionCategory.VILLAGE,   "A perfectly preserved 1860s gold-rush town with autumn trees, a Chinese miners' village and artisan shops.",              "10:00 AM", 2.0f, -44.9378, 168.8316, 2),
            Attraction("Lake Wakatipu Cruise",      AttractionCategory.NATURE,    "The TSS Earnslaw is a 1912 coal-fired steamship — cruise to Walter Peak station for a high-country farm experience.",    "12:00 PM", 3.0f, -45.0312, 168.6626, 3),
            Attraction("Central Otago Pinot Dinner",AttractionCategory.FOOD,      "Enjoy rack of lamb with world-renowned Central Otago Pinot Noir at a vineyard overlooking the lake at sunset.",          "06:30 PM", 3.0f, -45.0312, 168.6626, 1)
        ),

        "Costa Rica" to listOf(
            Attraction("Arenal Volcano Hike",       AttractionCategory.ADVENTURE, "Trek lava fields and rainforest trails around the perfectly conical Arenal Volcano, still gently steaming.",            "09:00 AM", 4.0f,  10.4630,  -84.7032, 1),
            Attraction("La Fortuna Waterfall",      AttractionCategory.NATURE,    "Descend 500 steps to a roaring 70m waterfall plunging into a blue swimming lagoon in the rainforest.",                  "10:00 AM", 2.5f,  10.4700,  -84.6565, 1),
            Attraction("Monteverde Cloud Forest",   AttractionCategory.NATURE,    "Walk hanging bridges through a UNESCO Reserve at 1,500m — spot resplendent quetzals and 400 species of orchid.",        "08:00 AM", 4.0f,  10.3046,  -84.8036, 2),
            Attraction("Manuel Antonio Beach",      AttractionCategory.BEACH,     "Where rainforest meets the Pacific — white-sand beaches backed by monkeys, sloths and scarlet macaws in the trees.",    "08:00 AM", 4.0f,   9.3908,  -84.1481, 3),
            Attraction("White Water Rafting Pacuare",AttractionCategory.ADVENTURE,"Raft the Pacuare River's Class IV rapids through pristine jungle — the best river in Central America for rafting.",     "08:00 AM", 8.0f,  10.0000,  -83.5000, 2),
            Attraction("Tortuguero Sea Turtle Nesting",AttractionCategory.NATURE, "July–Oct night tours to watch giant leatherback or green turtles emerging from the Caribbean to nest on the beach.",   "09:00 PM", 3.0f,  10.5422,  -83.5034, 3),
            Attraction("Zip-line Canopy Tour",      AttractionCategory.ADVENTURE, "Fly through the forest canopy at Monteverde on cables strung between 200-year-old trees — screaming is encouraged.",    "01:00 PM", 2.5f,  10.3046,  -84.8036, 1),
            Attraction("Casado & Guaro Sour",       AttractionCategory.FOOD,      "The Costa Rican casado: rice, black beans, plantains, salad and your choice of protein — paired with a guaro sour.",    "12:00 PM", 1.0f,   9.9281,  -84.0907, 1)
        ),

        "Iceland" to listOf(
            Attraction("Northern Lights Viewing",   AttractionCategory.NATURE,    "Sep–Mar: drive away from Reykjavík on a clear night to see the aurora borealis dance across an impossibly dark sky.",   "10:00 PM", 3.0f,  64.9631,  -19.0208, 1),
            Attraction("Golden Circle Day Trip",    AttractionCategory.NATURE,    "Geysir erupting every 8 minutes, Gullfoss double waterfall and Þingvellir rift valley in a single epic driving loop.",   "09:00 AM", 8.0f,  64.3133,  -20.3040, 2),
            Attraction("Blue Lagoon Geothermal Spa",AttractionCategory.ADVENTURE, "Soak in a milky-blue 38°C geothermal lagoon surrounded by black lava rock — the world's most famous outdoor spa.",       "02:00 PM", 3.0f,  63.8800,  -22.4494, 3),
            Attraction("Jökulsárlón Glacier Lagoon",AttractionCategory.NATURE,    "Float among cathedral-sized ice sculptures calved from the Vatnajökull glacier into a peacock-blue lagoon.",            "09:00 AM", 2.5f,  64.0784,  -16.2306, 2),
            Attraction("Landmannalaugar Hike",      AttractionCategory.ADVENTURE, "A highland trek through obsidian lava fields, steam vents and rhyolite mountains striped in surreal rainbow colours.",   "09:00 AM", 6.0f,  63.9960,  -19.0580, 3),
            Attraction("Skógafoss Waterfall",       AttractionCategory.NATURE,    "A 60m waterfall so powerful it creates a permanent rainbow — climb the 500 steps for a panorama of the south coast.",   "10:00 AM", 1.5f,  63.5320,  -19.5116, 2),
            Attraction("Reykjavík City Walk",       AttractionCategory.VILLAGE,   "Explore the colourful old harbour, Hallgrímskirkja church rocket and the hot dog stand where Bill Clinton once queued.",  "10:00 AM", 3.0f,  64.1355,  -21.8954, 1),
            Attraction("Lamb Soup at Café Loki",    AttractionCategory.FOOD,      "Traditional Icelandic hákarl (fermented shark), skyr, rye bread and lamb soup at a heritage café facing Hallgrímskirkja.", "12:00 PM", 1.5f, 64.1422,  -21.9269, 1)
        ),

        "Serengeti" to listOf(
            Attraction("Great Migration Game Drive",AttractionCategory.NATURE,    "Jul–Oct: witness 1.7 million wildebeest crossing the Mara River past waiting crocodiles — the greatest show on Earth.",  "05:30 AM", 6.0f,  -2.1540,  34.6857, 1),
            Attraction("Big Five Predator Drive",   AttractionCategory.ADVENTURE, "Dawn and dusk game drives tracking lions, leopards, cheetahs, elephants and buffalo with an expert Maasai guide.",        "05:30 AM", 4.0f,  -2.3333,  34.8333, 1),
            Attraction("Hot Air Balloon Safari",    AttractionCategory.ADVENTURE, "Float over the Serengeti at sunrise in a balloon, watching wildlife move below — ends with a bush champagne breakfast.",   "05:30 AM", 4.0f,  -2.3000,  34.8000, 2),
            Attraction("Ngorongoro Crater Drive",   AttractionCategory.NATURE,    "Descend into the world's largest intact volcanic caldera — a natural wildlife enclosure with the highest big cat density.", "07:00 AM", 6.0f,  -3.1733,  35.5872, 2),
            Attraction("Maasai Village Cultural Visit",AttractionCategory.VILLAGE,"Visit an authentic Maasai boma, watch the jumping dance, enter a family dwelling and buy handmade beadwork.",            "02:00 PM", 2.0f,  -2.3333,  34.8333, 3),
            Attraction("Olduvai Gorge Museum",      AttractionCategory.MUSEUM,    "The 'Cradle of Mankind' — the site where Leakey discovered 1.8 million-year-old Homo habilis fossils in 1959.",         "10:00 AM", 2.0f,  -2.9900,  35.3500, 3),
            Attraction("Sunset Sundowner",          AttractionCategory.VIEWPOINT, "Watch the African sun sink into the savannah from a raised camp deck while lions roar in the growing dark.",              "06:00 PM", 1.5f,  -2.3333,  34.8333, 1),
            Attraction("Bush Dinner Under Stars",   AttractionCategory.FOOD,      "A candlelit dinner table set on the open savannah — roasted meats, Tanzanian stew and a sky blazing with stars.",        "07:30 PM", 3.0f,  -2.3333,  34.8333, 2)
        ),

        "Borneo" to listOf(
            Attraction("Kinabatangan River Cruise",  AttractionCategory.NATURE,   "A slow boat down the wildlife-rich Kinabatangan — pygmy elephants, proboscis monkeys and orangutans on the banks.",     "07:00 AM", 4.0f,   5.4167, 118.0000, 1),
            Attraction("Sepilok Orangutan Centre",  AttractionCategory.NATURE,    "Watch semi-wild Bornean orangutans descend to the feeding platform twice daily in a rainforest rehabilitation centre.",   "09:00 AM", 2.0f,   5.8752, 117.9481, 1),
            Attraction("Mount Kinabalu Summit",     AttractionCategory.ADVENTURE, "A 2-day ascent of Southeast Asia's highest peak (4,095m) — the sunrise from Low's Peak is utterly unforgettable.",      "07:00 AM", 9.0f,   6.0750, 116.5597, 2),
            Attraction("Danum Valley Night Walk",   AttractionCategory.ADVENTURE, "A guided night walk through primary rainforest that is 130 million years old — fireflies, glowing fungi and leaf insects.", "08:30 PM", 2.5f,   5.0232, 117.7979, 3),
            Attraction("Mulu Caves National Park",  AttractionCategory.ADVENTURE, "Explore Sarawak Chamber — the world's largest underground chamber — and 3 million bats emerging at sunset.",             "08:00 AM", 8.0f,   4.0455, 114.8147, 3),
            Attraction("Sun Bear Sanctuary",        AttractionCategory.NATURE,    "Meet Borneo's smallest bear species at the Bornean Sun Bear Conservation Centre — close encounters in natural habitat.",  "09:00 AM", 1.5f,   5.8770, 117.9504, 1),
            Attraction("Coral Reef Diving Sipadan", AttractionCategory.ADVENTURE, "Sipadan Island has some of the world's richest reefs — dive with green turtles, hammerheads and schools of barracuda.",   "08:00 AM", 5.0f,   4.1126, 118.6232, 2),
            Attraction("Sarawak Laksa Breakfast",   AttractionCategory.FOOD,      "Sarawak laksa: a tangy sour-spicy coconut broth with rice noodles, prawns and omelette — Anthony Bourdain's 'breakfast of God'.", "08:00 AM", 1.0f, 1.5497, 110.3497, 1)
        ),

        "Amazon" to listOf(
            Attraction("Amazon River Boat Expedition",AttractionCategory.ADVENTURE,"Multi-day slow boat from Manaus into the tributary network — wake to pink river dolphins and howler monkeys.",          "07:00 AM", 8.0f,  -3.4653,  -62.2159, 1),
            Attraction("Anavilhanas Archipelago",   AttractionCategory.NATURE,    "The world's largest river archipelago — 400 islands creating a labyrinth of flooded forest channels to explore by canoe.", "09:00 AM", 5.0f,  -2.6672,  -60.7500, 2),
            Attraction("Piranha Fishing at Dusk",   AttractionCategory.ADVENTURE, "Fish for piranhas from a dugout canoe in a black-water lagoon as the jungle chorus begins and bats emerge overhead.",     "05:00 PM", 2.0f,  -3.2000,  -60.7000, 1),
            Attraction("Amazon Wildlife Night Walk",AttractionCategory.ADVENTURE, "A headlamp walk through the forest floor guided by an indigenous tracker — spotting tarantulas, tree frogs and caiman.",   "08:30 PM", 2.5f,  -3.1010,  -60.0250, 3),
            Attraction("Meeting of the Waters",     AttractionCategory.NATURE,    "Watch the dark Rio Negro run alongside the sandy Solimões for 6km without mixing — one of nature's strangest phenomena.", "10:00 AM", 1.5f,  -3.1360,  -59.9059, 2),
            Attraction("Manaus Amazon Theatre",     AttractionCategory.LANDMARK,  "An ornate 1896 opera house built at the height of the rubber boom — gilded with Portuguese tiles and Italian marble.",    "09:00 AM", 1.5f,  -3.1299,  -60.0231, 1),
            Attraction("Caiman Night Spotting",     AttractionCategory.ADVENTURE, "Spotting caiman eyes glowing red in the headlamp beam from a small boat after nightfall in the flooded forest.",          "09:30 PM", 2.0f,  -3.2000,  -60.8000, 2),
            Attraction("Amazonian Tacacá",          AttractionCategory.FOOD,      "The region's signature dish — a hot sour broth with jambu leaves, dried shrimp and tucupi in a gourd bowl.",             "12:00 PM", 1.0f,  -3.1190,  -60.0217, 1)
        ),

        "Torres del Paine" to listOf(
            Attraction("Base of the Towers Hike",   AttractionCategory.ADVENTURE, "The 8-hour return trail to the cirque lake beneath the three iconic granite towers — Patagonia's finest day hike.",       "06:00 AM", 8.0f, -50.9420,  -72.9873, 1),
            Attraction("French Valley W Circuit",   AttractionCategory.ADVENTURE, "A breathtaking valley walk beneath hanging glaciers and soaring rock walls — the most spectacular stage of the W Trek.",   "07:00 AM", 7.0f, -51.1167,  -73.0500, 2),
            Attraction("Grey Glacier Ice Trek",     AttractionCategory.ADVENTURE, "Crampons on — walk the ancient blue ice of Grey Glacier with a guide, exploring crevasses and ice caves.",               "09:00 AM", 5.0f, -51.0333,  -73.2000, 3),
            Attraction("Lake Pehoé Catamaran",      AttractionCategory.NATURE,    "Cross the turquoise glacier lake with the Cuernos del Paine as the backdrop — the most beautiful approach in the park.",  "08:00 AM", 1.0f, -51.1000,  -72.9833, 1),
            Attraction("Paine Grande Viewpoint",    AttractionCategory.VIEWPOINT, "A panoramic terrace above Lago Pehoé with the Cuernos reflected in the water — best at golden hour.",                    "06:00 PM", 2.0f, -51.0800,  -73.0500, 2),
            Attraction("Guanaco & Condor Watching", AttractionCategory.NATURE,    "Patagonian guanacos are everywhere and Andean condors soar on the Patagonian winds — bring a long telephoto lens.",      "08:00 AM", 3.0f, -51.0000,  -72.9500, 3),
            Attraction("Mirador del Toro",          AttractionCategory.VIEWPOINT, "A short hike to a classic viewpoint of the entire Paine Massif reflected in a calm lake — perfect at dawn.",             "06:00 AM", 2.0f, -51.1800,  -72.8500, 1),
            Attraction("Patagonian Estancia Dinner",AttractionCategory.FOOD,      "Dine at a working sheep estancia: slow-roasted Patagonian cordero al palo with criolla salad and Malbec.",               "07:30 PM", 2.5f, -51.7330,  -72.5000, 2)
        ),

        "Galápagos Islands" to listOf(
            Attraction("Giant Tortoise Reserve",    AttractionCategory.NATURE,    "Walk among free-roaming giant tortoises over 100 years old at the Charles Darwin Research Station on Santa Cruz.",      "09:00 AM", 2.5f,  -0.7393,  -90.3023, 1),
            Attraction("Marine Iguana Colonies",    AttractionCategory.NATURE,    "Watch the world's only ocean-going lizard sunning on black lava rocks before diving into the Pacific to graze on algae.", "10:00 AM", 1.5f,  -0.9538,  -90.9656, 1),
            Attraction("Snorkelling with Sea Lions",AttractionCategory.ADVENTURE, "Sea lions treat snorkellers as playmates in the Galápagos — genuinely the most intimate wildlife encounter in the ocean.", "09:00 AM", 3.0f,  -0.9380,  -90.9650, 1),
            Attraction("Blue-Footed Booby Colony",  AttractionCategory.NATURE,    "Watch the iconic booby's comical courtship dance — the male presents his bright blue feet in an irresistible display.",   "10:00 AM", 2.0f,  -0.6000,  -90.5000, 2),
            Attraction("Bartolomé Island Pinnacle", AttractionCategory.VIEWPOINT, "Climb wooden steps to the summit of Bartolomé Island for the most photographed volcanic landscape in the Galápagos.",    "08:00 AM", 3.0f,  -0.2833,  -90.5583, 2),
            Attraction("Darwin Research Station",   AttractionCategory.MUSEUM,    "The laboratory that provided the key scientific data to protect the Galápagos ecosystem — and houses captive-bred tortoises.", "02:00 PM", 1.5f, -0.7415, -90.3041, 3),
            Attraction("Lava Tunnel Walk",          AttractionCategory.ADVENTURE, "Walk through a 1km underground volcanic tube formed when the outer lava crust solidified around still-flowing magma.",    "11:00 AM", 1.0f,  -0.7000,  -90.3500, 3),
            Attraction("Galápagos Ceviche",         AttractionCategory.FOOD,      "Fresh tuna, sea bass and shrimp ceviche with ají amarillo at a harborside restaurant in Puerto Ayora.",                  "01:00 PM", 1.0f,  -0.7393,  -90.3023, 1)
        ),

        "Everest Base Camp" to listOf(
            Attraction("Everest Base Camp 5364m",   AttractionCategory.ADVENTURE, "The 14-day trek to the foot of Everest — stand at 5,364m surrounded by the Khumbu Icefall and towering 8,000m peaks.",  "07:00 AM", 9.0f,  28.0025,  86.8530, 1),
            Attraction("Kala Patthar Sunrise",      AttractionCategory.VIEWPOINT, "Summit the 5,643m viewpoint for the closest unrestricted panorama of Everest's summit — a life-changing dawn.",          "04:00 AM", 4.0f,  27.9875,  86.8303, 2),
            Attraction("Tengboche Monastery",       AttractionCategory.TEMPLE,    "The highest monastery in Nepal at 3,867m — attend the dawn puja ceremony with Ama Dablam towering behind the roof.",     "06:30 AM", 2.0f,  27.8367,  86.7642, 2),
            Attraction("Namche Bazaar Acclimatisation",AttractionCategory.VILLAGE,"The Sherpa capital — spend two nights acclimatising, explore the Saturday market and the Everest view hotel hill.",      "09:00 AM", 3.0f,  27.8056,  86.7139, 1),
            Attraction("Khumbu Icefall Viewpoint",  AttractionCategory.VIEWPOINT, "Gaze into the churning chaos of the Khumbu Icefall from Base Camp — a kilometre-deep crevasse field in constant motion.", "08:00 AM", 2.0f,  27.9878,  86.8574, 1),
            Attraction("Imja Tse Island Peak",      AttractionCategory.ADVENTURE, "A technically accessible 6,189m summit above the main trekking route — a true Himalayan mountaineering experience.",     "04:00 AM", 9.0f,  27.9268,  86.9294, 3),
            Attraction("Phakding Village Walk",     AttractionCategory.VILLAGE,   "The first day's gentle walk through pine and rhododendron forest along the Dudh Koshi River with yak caravans passing.", "08:00 AM", 4.0f,  27.7421,  86.7114, 1),
            Attraction("Sherpa Dal Bhat",           AttractionCategory.FOOD,      "Unlimited-refill dal bhat (lentil soup, rice, curried vegetables) at a teahouse at altitude — trekker's fuel.",         "07:00 AM", 1.0f,  27.8056,  86.7139, 1)
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
