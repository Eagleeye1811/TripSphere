package com.tripsphere.utils

import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory

object DummyData {

    val destinations = listOf(

        // ── BEACH ───────────────────────────────────────────────────────────────

        Destination(
            id = 1,
            name = "Santorini",
            country = "Greece",
            highlights = "Where sunsets rewrite your soul",
            description = "Perched on the rim of an ancient volcanic caldera, Santorini's iconic whitewashed buildings and azure-domed churches cascade down sheer cliffs into the deepest blue of the Aegean. From the labyrinthine streets of Oia to the blood-red sands of Red Beach and the Bronze Age ruins of Akrotiri, every corner of this island is a masterpiece of light, color, and history.",
            imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Apr – Oct",
            budgetEstimate = "\$180–\$350/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 28450,
            topAttractions = listOf("Oia Sunset Point", "Red Beach", "Akrotiri Archaeological Site", "Fira Town Caldera Walk", "Amoudi Bay"),
            latitude = 36.3932, longitude = 25.4615
        ),

        Destination(
            id = 2,
            name = "Bali",
            country = "Indonesia",
            highlights = "Where spirituality meets the surf",
            description = "Bali is an island of contrasts — terraced rice paddies glow emerald green beside Hindu temples draped in frangipani offerings, while world-class surf breaks pound dramatic coastlines. The Island of the Gods delivers equal parts adventure, tranquility, and cultural immersion, from Ubud's arts scene to Uluwatu's clifftop temple ceremonies above crashing waves.",
            imageUrl = "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Apr – Sep",
            budgetEstimate = "\$45–\$90/day",
            rating = 4.8f,
            priceLevel = "Budget",
            reviewCount = 45200,
            topAttractions = listOf("Tegallalang Rice Terraces", "Uluwatu Temple", "Mount Batur Sunrise Trek", "Seminyak Beach", "Ubud Monkey Forest"),
            latitude = -8.4095, longitude = 115.1889
        ),

        Destination(
            id = 3,
            name = "Maldives",
            country = "Maldives",
            highlights = "Heaven scattered across the Indian Ocean",
            description = "The Maldives is the world's lowest-lying country — a necklace of 1,200 coral islands strung across the equatorial Indian Ocean. Overwater bungalows hover above fluorescent coral gardens, and the water is so clear you can watch manta rays and whale sharks from your breakfast table. This is paradise in its most absolute and unreserved form.",
            imageUrl = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Nov – Apr",
            budgetEstimate = "\$400–\$1,200/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 19800,
            topAttractions = listOf("Biyadhoo Island Snorkeling", "Banana Reef Diving", "Baa Atoll UNESCO Biosphere", "Male Friday Mosque", "Hulhumale Beach"),
            latitude = 3.2028, longitude = 73.2207
        ),

        Destination(
            id = 4,
            name = "Amalfi Coast",
            country = "Italy",
            highlights = "La dolce vita balanced on a cliff edge",
            description = "The Amalfi Coast is a UNESCO-listed masterpiece of nature and human creativity — 50 km of towering limestone cliffs plunging into turquoise waters, studded with lemon groves, medieval fishing villages painted in ochre and rose, and trattorias where the pasta changes by the hour. Positano, Ravello, and the namesake town each carry their own distinct magic.",
            imageUrl = "https://images.unsplash.com/photo-1533104816931-20fa691ff6ca?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "May – Oct",
            budgetEstimate = "\$180–\$320/day",
            rating = 4.8f,
            priceLevel = "Luxury",
            reviewCount = 22100,
            topAttractions = listOf("Positano Village", "Ravello Gardens & Villas", "Amalfi Cathedral", "Path of the Gods Hike", "Capri Island Day Trip"),
            latitude = 40.6340, longitude = 14.6027
        ),

        Destination(
            id = 5,
            name = "Phuket",
            country = "Thailand",
            highlights = "Crystal waters by day, vibrant nights by sunset",
            description = "Phuket, Thailand's largest island, packs in postcard-perfect beaches, emerald limestone karst islands rising from turquoise bays, legendary street-food markets, and an Old Town quarter where colonial Sino-Portuguese mansions line cobblestone streets. The Phi Phi Islands, just a short boat ride away, rank among the world's most photogenic seascapes.",
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Nov – Apr",
            budgetEstimate = "\$40–\$80/day",
            rating = 4.6f,
            priceLevel = "Budget",
            reviewCount = 38700,
            topAttractions = listOf("Phi Phi Islands", "Big Buddha Viewpoint", "Phang Nga Bay Kayaking", "Old Phuket Town", "Patong Beach"),
            latitude = 7.8804, longitude = 98.3923
        ),

        Destination(
            id = 6,
            name = "Seychelles",
            country = "Seychelles",
            highlights = "An archipelago where time forgets to rush",
            description = "The Seychelles archipelago, scattered across the western Indian Ocean, is the world's last truly unspoiled tropical paradise. Ancient granite boulders sculpt surreal beaches of powdered coral, while the UNESCO-listed Vallée de Mai shelters prehistoric coco de mer palms in a forest unchanged for millions of years. Anse Source d'Argent is consistently ranked the most beautiful beach on Earth.",
            imageUrl = "https://images.unsplash.com/photo-1589979481223-deb893043163?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Apr – May, Oct – Nov",
            budgetEstimate = "\$300–\$700/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 14300,
            topAttractions = listOf("Anse Source d'Argent Beach", "Vallée de Mai Nature Reserve", "Beau Vallon Beach", "Cousin Island Bird Sanctuary", "Victoria Local Market"),
            latitude = -4.6796, longitude = 55.4920
        ),

        Destination(
            id = 7,
            name = "Positano",
            country = "Italy",
            highlights = "Colors stacked impossibly on a hillside",
            description = "Positano tumbles down a sheer cliff face in a riot of pastel-colored buildings, bougainvillea-draped terraces, and winding staircases descending to pebbled coves where fishing boats bob in clear green water. John Steinbeck wrote that it is 'a dream place that isn't quite real when you are there and becomes beckoningly real after you have gone.'",
            imageUrl = "https://images.unsplash.com/photo-1555990793-da11153b6c64?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "May – Sep",
            budgetEstimate = "\$140–\$260/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 17600,
            topAttractions = listOf("Spiaggia Grande Beach", "Church of Santa Maria Assunta", "Sentiero degli Dei Trail", "Li Galli Islands", "Local Ceramic Workshops"),
            latitude = 40.6281, longitude = 14.4851
        ),

        Destination(
            id = 8,
            name = "Maui",
            country = "USA",
            highlights = "Where the Pacific puts on its finest performance",
            description = "Maui earns its reputation as the Valley Isle with volcanic craters vast enough to swallow Manhattan, the Road to Hana's 64 bridges and 600 waterfalls, and humpback whales breaching in the channels from December through April. Molokini Crater delivers some of Hawaii's best snorkeling, while the sunrise from Haleakalā at 3,055 m is among the most magnificent on the planet.",
            imageUrl = "https://images.unsplash.com/photo-1507876466758-e54b3bdbe44c?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Apr – May, Sep – Nov",
            budgetEstimate = "\$220–\$450/day",
            rating = 4.8f,
            priceLevel = "Luxury",
            reviewCount = 31200,
            topAttractions = listOf("Haleakalā Volcano National Park", "Road to Hana", "Molokini Crater Snorkeling", "Napili Bay", "Lahaina Historic Town"),
            latitude = 20.7984, longitude = -156.3319
        ),

        Destination(
            id = 9,
            name = "Tulum",
            country = "Mexico",
            highlights = "Ancient Maya ruins above a turquoise sea",
            description = "Tulum occupies a breathtaking clifftop perch on the Yucatán Peninsula where well-preserved 13th-century Maya ruins survey a jewel-blue Caribbean far below. Beyond the archaeology, cenotes — underground rivers glowing electric blue beneath the jungle canopy — are among the world's finest swimming and diving environments, while boutique eco-hotels and yoga retreats attract global seekers of beauty and stillness.",
            imageUrl = "https://images.unsplash.com/photo-1552799446-159ba9523315?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Dec – Apr",
            budgetEstimate = "\$80–\$180/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 26400,
            topAttractions = listOf("Tulum Archaeological Zone", "Gran Cenote", "Sian Ka'an Biosphere Reserve", "Playa Paraiso", "Cenote Dos Ojos Diving"),
            latitude = 20.2114, longitude = -87.4654
        ),

        Destination(
            id = 10,
            name = "Koh Samui",
            country = "Thailand",
            highlights = "Thailand's gem shimmering in the Gulf",
            description = "Koh Samui is Thailand's second-largest island, ringed by white-sand beaches shaded by coconut palms and populated by everything from barefoot backpacker beach huts to opulent over-water villas. The island's interior rises into jungle-clad mountains dotted with thundering waterfalls, while the surrounding Gulf of Thailand holds vibrant coral reefs and the otherworldly Ang Thong Marine Park.",
            imageUrl = "https://images.unsplash.com/photo-1552465011-b4e21bf6e79a?w=800",
            category = DestinationCategory.BEACH,
            bestTimeToVisit = "Jan – Apr",
            budgetEstimate = "\$35–\$75/day",
            rating = 4.5f,
            priceLevel = "Budget",
            reviewCount = 29800,
            topAttractions = listOf("Chaweng Beach", "Big Buddha Temple", "Na Muang Waterfall", "Fisherman's Village Walking Street", "Ang Thong Marine Park"),
            latitude = 9.5120, longitude = 100.0136
        ),

        // ── MOUNTAIN ────────────────────────────────────────────────────────────

        Destination(
            id = 11,
            name = "Swiss Alps",
            country = "Switzerland",
            highlights = "Peaks that have inspired poets for centuries",
            description = "The Swiss Alps are the definitive alpine experience — the Matterhorn rises 4,478 m in a near-perfect pyramid over the car-free village of Zermatt, cable cars glide above glaciers where you can ski even in July, and valleys so impossibly green they appear color-corrected. The Jungfrau-Aletsch glacier, the largest in the Alps, is a UNESCO World Heritage site visible from the Top of Europe station at 3,454 m.",
            imageUrl = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Jun – Sep (hiking), Dec – Mar (skiing)",
            budgetEstimate = "\$220–\$450/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 34600,
            topAttractions = listOf("Matterhorn Glacier Paradise", "Jungfraujoch Top of Europe", "Aletsch Glacier Hike", "Grindelwald Valley", "Lake Lucerne Cruise"),
            latitude = 46.8182, longitude = 8.2275
        ),

        Destination(
            id = 12,
            name = "Banff",
            country = "Canada",
            highlights = "Turquoise lakes in a Rocky Mountain kingdom",
            description = "Banff National Park is Canada's oldest and most celebrated national park — 6,641 sq km of jagged Rocky Mountain peaks, impossible turquoise glacier-fed lakes (Moraine Lake and Lake Louise rank among the most photographed in the world), natural hot springs, and wildlife corridors where grizzly bears, wolves, and elk roam freely. The Icefields Parkway is widely considered the world's most scenic drive.",
            imageUrl = "https://images.unsplash.com/photo-1543716091-a840c05249ec?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Jun – Aug, Dec – Mar",
            budgetEstimate = "\$120–\$260/day",
            rating = 4.9f,
            priceLevel = "Mid-Range",
            reviewCount = 41300,
            topAttractions = listOf("Moraine Lake", "Lake Louise", "Icefields Parkway Drive", "Johnston Canyon Ice Walk", "Sulphur Mountain Gondola"),
            latitude = 51.4968, longitude = -115.9281
        ),

        Destination(
            id = 13,
            name = "Dolomites",
            country = "Italy",
            highlights = "Nature's own Gothic cathedral of pale rock",
            description = "The Dolomites are a UNESCO World Heritage site of otherworldly pale limestone towers rising abruptly from flower-meadow valleys in the northeastern Italian Alps. The Alta Via hiking routes traverse sheer rock faces past cozy rifugios serving polenta and strudel at altitude. In winter, the Sella Ronda ski circuit links 1,220 km of perfectly groomed pistes — the largest ski area in the world.",
            imageUrl = "https://images.unsplash.com/photo-1586348943529-beaae6c28db9?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Jun – Sep (hiking), Dec – Mar (skiing)",
            budgetEstimate = "\$100–\$200/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 23800,
            topAttractions = listOf("Tre Cime di Lavaredo", "Lake Braies (Pragser Wildsee)", "Seceda Ridge Sunrise", "Cortina d'Ampezzo", "Alpe di Siusi Plateau"),
            latitude = 46.4102, longitude = 11.8440
        ),

        Destination(
            id = 14,
            name = "Patagonia",
            country = "Chile/Argentina",
            highlights = "The most dramatic wilderness left on Earth",
            description = "Patagonia, split between southern Argentina and Chile, is one of the last truly wild places on the planet. Torres del Paine's granite towers pierce the sky above luminous turquoise lakes, while the Perito Moreno glacier — a 30 km wall of thundering blue ice — calves into a mountain lake year-round in a spectacle unlike anything else in the natural world. Wind is a fifth element here.",
            imageUrl = "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Nov – Mar",
            budgetEstimate = "\$90–\$180/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 18200,
            topAttractions = listOf("Torres del Paine W Trek", "Perito Moreno Glacier", "Grey Glacier Ice Hike", "El Chaltén Trekking Hub", "Tierra del Fuego National Park"),
            latitude = -51.1726, longitude = -73.0600
        ),

        Destination(
            id = 15,
            name = "Mount Fuji",
            country = "Japan",
            highlights = "Japan's sacred crown above the clouds",
            description = "Fuji-san, Japan's highest peak at 3,776 m, is a dormant stratovolcano of such symmetric perfection it appears to be a painting. Climbing the mountain between July and early September is a rite of passage for hundreds of thousands each year, with the summit crater glowing red at dawn. The Five Fuji Lakes reflect the peak in their stillness — a view that has shaped Japanese art for over a thousand years.",
            imageUrl = "https://images.unsplash.com/photo-1513407030348-c983a97b98d8?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Jul – Aug (climbing), Mar – May (cherry blossoms)",
            budgetEstimate = "\$80–\$150/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 52100,
            topAttractions = listOf("Fuji Summit Crater", "Chureito Pagoda Viewpoint", "Lake Kawaguchiko", "Aokigahara Forest", "Fuji-Q Highland Theme Park"),
            latitude = 35.3606, longitude = 138.7274
        ),

        Destination(
            id = 16,
            name = "Himalayas",
            country = "Nepal",
            highlights = "The roof of the world, humbling and magnificent",
            description = "The Himalayas contain nine of the world's ten highest peaks, including Everest at 8,849 m. Nepal is the gateway to this extraordinary range — the Annapurna Circuit and Everest Base Camp treks rank among the planet's greatest journeys, passing through Sherpa villages, ancient Buddhist monasteries adorned with prayer flags, and rhododendron forests that bloom crimson in spring above the clouds.",
            imageUrl = "https://images.unsplash.com/photo-1544735716-392fe2489ffa?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Mar – May, Sep – Nov",
            budgetEstimate = "\$30–\$60/day",
            rating = 4.9f,
            priceLevel = "Budget",
            reviewCount = 16700,
            topAttractions = listOf("Everest Base Camp Trek", "Annapurna Circuit", "Kathmandu Durbar Square", "Poon Hill Sunrise (3,210 m)", "Chitwan Jungle Safari"),
            latitude = 27.9881, longitude = 86.9250
        ),

        Destination(
            id = 17,
            name = "Rocky Mountains",
            country = "USA",
            highlights = "America's wild, rugged, untamed backbone",
            description = "The Rocky Mountains stretch 4,800 km from New Mexico to British Columbia and contain some of the American West's most epic landscapes. Yellowstone harbors the world's largest supervolcano and 10,000 geothermal features. Grand Teton's cathedral spires tower over Jackson Hole. Glacier National Park's Going-to-the-Sun Road crosses the Continental Divide through one of the continent's last great wilderness areas.",
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Jun – Sep",
            budgetEstimate = "\$100–\$200/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 38400,
            topAttractions = listOf("Yellowstone Geysers & Hot Springs", "Grand Teton National Park", "Going-to-the-Sun Road", "Rocky Mountain National Park", "Colorado Trail Hiking"),
            latitude = 43.7904, longitude = -110.6818
        ),

        Destination(
            id = 18,
            name = "Norwegian Fjords",
            country = "Norway",
            highlights = "Where glaciers carved a world of pure wonder",
            description = "Norway's fjords are the definitive natural wonder of northern Europe — glacier-carved valleys flooded by the sea create sheer walls of rock rising 1,000 m from mirror-flat water. Geirangerfjord and Nærøyfjord are UNESCO World Heritage listed. The Seven Sisters waterfall, isolated clifftop farms, and villages accessible only by boat create a landscape that feels genuinely mythological in its scale.",
            imageUrl = "https://images.unsplash.com/photo-1520769490027-f729c0d295e6?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "May – Sep",
            budgetEstimate = "\$200–\$400/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 27900,
            topAttractions = listOf("Geirangerfjord Cruise", "Trolltunga Cliff Hike (10 hrs)", "Flåm Scenic Railway", "Preikestolen (Pulpit Rock)", "Nærøyfjord Kayaking"),
            latitude = 61.2241, longitude = 6.8673
        ),

        Destination(
            id = 19,
            name = "Yosemite",
            country = "USA",
            highlights = "Granite cathedrals and thundering waterfalls",
            description = "Yosemite Valley is arguably the most spectacular valley on Earth — an 11 km granite canyon where El Capitan rises 900 m in a single vertical monolith, Half Dome crowns the skyline at 1,444 m above the valley floor, and Yosemite Falls drops 739 m in North America's highest waterfall. John Muir called it the grandest of all the special temples of Nature, and the millions who visit each year unanimously agree.",
            imageUrl = "https://images.unsplash.com/photo-1426604966848-d7adac402bff?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Apr – Jun, Sep – Oct",
            budgetEstimate = "\$100–\$220/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 47200,
            topAttractions = listOf("El Capitan", "Half Dome Trail (16 mi)", "Yosemite Falls", "Glacier Point Panorama", "Mariposa Grove Giant Sequoias"),
            latitude = 37.8651, longitude = -119.5383
        ),

        Destination(
            id = 20,
            name = "Faroe Islands",
            country = "Denmark",
            highlights = "An emerald archipelago lost in the North Atlantic",
            description = "The 18 Faroe Islands are perhaps the most dramatic archipelago in the world — basalt sea stacks, cliffs at the edge of the world plunging 400 m into the Atlantic, and hillsides so intensely green they seem lit from within, populated by more sheep than people. The Sørvágsvatn optical-illusion lake appears to hang impossibly above the ocean far below — a scene that stopped the internet.",
            imageUrl = "https://images.unsplash.com/photo-1531248020715-f2ee21f88b5e?w=800",
            category = DestinationCategory.MOUNTAIN,
            bestTimeToVisit = "Jun – Aug",
            budgetEstimate = "\$120–\$200/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 11400,
            topAttractions = listOf("Gásadalur & Múlafossur Waterfall", "Sørvágsvatn Cliff Hike", "Kirkjubøur Historic Farm", "Puffin Watching on Mykines", "Vestmanna Sea Cave Boat Tour"),
            latitude = 61.8926, longitude = -6.9118
        ),

        // ── CITY ────────────────────────────────────────────────────────────────

        Destination(
            id = 21,
            name = "Paris",
            country = "France",
            highlights = "The art of living, perfected over centuries",
            description = "Paris is the world's most visited city for good reason: few places achieve such a perfect synthesis of art, architecture, gastronomy, and joie de vivre. Haussmann's wide boulevards reveal the Eiffel Tower at dusk; the Louvre's 38,000 artworks represent civilization's most extraordinary achievement in stone; and every arrondissement holds its own distinct personality within this perfectly human-scaled capital of the Western world.",
            imageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Apr – Jun, Sep – Oct",
            budgetEstimate = "\$180–\$380/day",
            rating = 4.7f,
            priceLevel = "Luxury",
            reviewCount = 89200,
            topAttractions = listOf("Eiffel Tower", "Louvre Museum", "Notre-Dame Cathedral", "Palace of Versailles", "Musée d'Orsay"),
            latitude = 48.8566, longitude = 2.3522
        ),

        Destination(
            id = 22,
            name = "New York",
            country = "USA",
            highlights = "The city that never stops dreaming",
            description = "New York City is simultaneously the world's most dynamic cultural laboratory and its most recognized skyline. The Metropolitan Museum's collection spans 5,000 years of human history. Broadway stages 40+ productions on any given night. Central Park offers 341 hectares of green sanctuary in the world's most expensive real estate. And the five boroughs — each with its own fiercely distinct character — make the city endlessly, inexhaustibly discoverable.",
            imageUrl = "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Apr – Jun, Sep – Nov",
            budgetEstimate = "\$220–\$450/day",
            rating = 4.7f,
            priceLevel = "Luxury",
            reviewCount = 76500,
            topAttractions = listOf("Central Park", "Metropolitan Museum of Art", "The High Line", "Brooklyn Bridge Walk", "One World Observatory"),
            latitude = 40.7128, longitude = -74.0060
        ),

        Destination(
            id = 23,
            name = "Kyoto",
            country = "Japan",
            highlights = "A living museum of 1,200 years of civilization",
            description = "Kyoto was Japan's imperial capital for over a millennium, and its preserved heritage is extraordinary — 17 UNESCO World Heritage Sites, 1,600 Buddhist temples, 400 Shinto shrines, and the Gion district where geiko still traverse stone-paved lanes in kimono at dusk. In spring, cherry blossoms transform the Philosopher's Path into the world's most beautiful garden walk.",
            imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Mar – May, Oct – Nov",
            budgetEstimate = "\$100–\$220/day",
            rating = 4.9f,
            priceLevel = "Mid-Range",
            reviewCount = 58300,
            topAttractions = listOf("Fushimi Inari 10,000 Torii Gates", "Arashiyama Bamboo Grove", "Kinkaku-ji Golden Pavilion", "Gion District at Dusk", "Philosopher's Path"),
            latitude = 35.0116, longitude = 135.7681
        ),

        Destination(
            id = 24,
            name = "Dubai",
            country = "UAE",
            highlights = "The future has arrived and it is gleaming",
            description = "Dubai has transformed from a fishing village to the world's most audacious city in a single human lifetime. The Burj Khalifa pierces the clouds at 828 m. The Palm Jumeirah archipelago was sculpted from the sea. Ski Dubai brings Alpine slopes indoors in 45°C heat. Yet beneath the spectacle lies a genuine crossroads city of remarkable diversity where 200 nationalities coexist around some of the world's most exciting food.",
            imageUrl = "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Nov – Mar",
            budgetEstimate = "\$200–\$500/day",
            rating = 4.8f,
            priceLevel = "Luxury",
            reviewCount = 63400,
            topAttractions = listOf("Burj Khalifa Observation Deck", "Dubai Frame", "Dubai Desert Safari", "Gold & Spice Souks", "Palm Jumeirah"),
            latitude = 25.2048, longitude = 55.2708
        ),

        Destination(
            id = 25,
            name = "Barcelona",
            country = "Spain",
            highlights = "Gaudí's living canvas by the Mediterranean",
            description = "Barcelona is Europe's most architecturally exhilarating city — where Antoni Gaudí's organic Sagrada Família has been under construction for 142 years and will not be completed until 2026, Park Güell's mosaic terraces overlook the sea, and Casa Batlló's dragon-scale rooftop shimmers like a fairytale. La Barceloneta beach sits 20 minutes from Picasso's early studio, and the energy of the place is simply intoxicating.",
            imageUrl = "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "May – Jun, Sep – Oct",
            budgetEstimate = "\$120–\$240/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 71200,
            topAttractions = listOf("Sagrada Família", "Park Güell", "La Boqueria Market", "Gothic Quarter", "Camp Nou & FC Barcelona Museum"),
            latitude = 41.3874, longitude = 2.1686
        ),

        Destination(
            id = 26,
            name = "Amsterdam",
            country = "Netherlands",
            highlights = "Golden Age canals and a deeply open soul",
            description = "Amsterdam is built on 90 islands connected by 1,281 bridges across a network of concentric canals lined with narrow 17th-century merchant houses. The Rijksmuseum houses Rembrandt and Vermeer in a palace of art. Anne Frank's hiding place is among the world's most moving pilgrimages. And the Jordaan district's antique bookshops, brown cafés, and artisan cheese shops reward hours of purposeless, glorious wandering.",
            imageUrl = "https://images.unsplash.com/photo-1534351590666-13e3e96b5017?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Apr – May, Sep – Oct",
            budgetEstimate = "\$130–\$250/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 54800,
            topAttractions = listOf("Rijksmuseum", "Anne Frank House", "Van Gogh Museum", "Canal Boat Tour", "Vondelpark"),
            latitude = 52.3676, longitude = 4.9041
        ),

        Destination(
            id = 27,
            name = "Singapore",
            country = "Singapore",
            highlights = "The garden city-state that became a marvel",
            description = "Singapore has achieved what seemed impossible: a dense, hyper-clean, hyper-safe city-state of 5.8 million people that is also genuinely green, with 47% of its land covered by vegetation. Gardens by the Bay's Supertrees glow at night in a light show visible for miles. Hawker centres serve some of Asia's greatest cuisine for under \$3. And Changi Airport — with its waterfall and butterfly garden — is the world's best.",
            imageUrl = "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Feb – Apr",
            budgetEstimate = "\$120–\$280/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 47600,
            topAttractions = listOf("Gardens by the Bay", "Marina Bay Sands SkyPark", "Hawker Centre Food Tour", "Sentosa Island & Beaches", "Chinatown & Little India Walking"),
            latitude = 1.3521, longitude = 103.8198
        ),

        Destination(
            id = 28,
            name = "Prague",
            country = "Czech Republic",
            highlights = "A fairy-tale city that history spared",
            description = "Prague is Europe's most perfectly preserved medieval city — largely untouched by WWII bombing, it retains Gothic, Baroque, Art Nouveau, and Cubist architecture in extraordinary concentration across the whole city centre. The Old Town astronomical clock from 1410 still chimes hourly. Prague Castle is the world's largest ancient castle complex by area. And the Czech beer is genuinely excellent and costs less than mineral water.",
            imageUrl = "https://images.unsplash.com/photo-1541849546-216549ae216d?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Apr – May, Sep – Oct",
            budgetEstimate = "\$60–\$120/day",
            rating = 4.8f,
            priceLevel = "Budget",
            reviewCount = 43100,
            topAttractions = listOf("Prague Castle Complex", "Charles Bridge at Dawn", "Old Town Square", "Astronomical Clock (Orloj)", "Josefov Jewish Quarter"),
            latitude = 50.0755, longitude = 14.4378
        ),

        Destination(
            id = 29,
            name = "Istanbul",
            country = "Turkey",
            highlights = "Two continents, one unmissable city",
            description = "Istanbul is the world's only city that straddles two continents, and its 2,500-year layered history is correspondingly extraordinary. The Hagia Sophia stood as the world's largest building for nearly 1,000 years. The Topkapi Palace held the treasures of the Ottoman Empire for four centuries. The Grand Bazaar's 4,000 shops have traded continuously since 1455. The Bosphorus strait dividing Europe from Asia is best experienced at sunset from a ferry deck.",
            imageUrl = "https://images.unsplash.com/photo-1541432901042-2d8bd64b4a9b?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Apr – May, Sep – Oct",
            budgetEstimate = "\$70–\$150/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 49300,
            topAttractions = listOf("Hagia Sophia", "Topkapi Palace", "Grand Bazaar", "Blue Mosque", "Bosphorus Sunset Cruise"),
            latitude = 41.0082, longitude = 28.9784
        ),

        Destination(
            id = 30,
            name = "Tokyo",
            country = "Japan",
            highlights = "Hyper-modern meets timeless tradition at every turn",
            description = "Tokyo is the world's largest metropolitan area — 37 million people — and paradoxically its most orderly city. Trains run to the second. Three-Michelin-star restaurants outnumber all of France. The transition between a neon-lit gaming arcade and a 400-year-old Shinto shrine can take three minutes on foot. And yet the scale of it all never feels oppressive — every neighborhood has its own village-like intimacy.",
            imageUrl = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800",
            category = DestinationCategory.CITY,
            bestTimeToVisit = "Mar – May, Sep – Nov",
            budgetEstimate = "\$120–\$280/day",
            rating = 4.9f,
            priceLevel = "Mid-Range",
            reviewCount = 81700,
            topAttractions = listOf("Shinjuku Gyoen National Garden", "Senso-ji Temple Asakusa", "Shibuya Scramble Crossing", "Tsukiji Outer Market", "teamLab Planets Digital Art"),
            latitude = 35.6762, longitude = 139.6503
        ),

        // ── ADVENTURE ───────────────────────────────────────────────────────────

        Destination(
            id = 31,
            name = "Machu Picchu",
            country = "Peru",
            highlights = "The lost city that took the world's breath away",
            description = "Machu Picchu is the greatest archaeological site in the Western Hemisphere — a 15th-century Inca citadel built at 2,430 m in the Peruvian Andes with an engineering precision that still defies explanation. The classic four-day Inca Trail delivers trekkers through cloud forest, alpine tundra, and Inca mountain passes to the Sun Gate, where the ancient citadel is revealed in morning mist far below.",
            imageUrl = "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "May – Sep",
            budgetEstimate = "\$80–\$160/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 35600,
            topAttractions = listOf("Citadel Archaeological Site", "Huayna Picchu Summit", "Sun Gate (Inti Punku)", "Classic Inca Trail Trek", "Sacred Valley of the Incas"),
            latitude = -13.1631, longitude = -72.5450
        ),

        Destination(
            id = 32,
            name = "Queenstown",
            country = "New Zealand",
            highlights = "The adventure capital that never runs dry",
            description = "Queenstown sits on the shores of the glacier-carved Lake Wakatipu surrounded by the jagged Remarkables mountain range in New Zealand's South Island. The city invented modern bungee jumping and hasn't stopped creating adrenaline experiences since: skydiving at 15,000 ft, jet-boating through narrow canyons, heli-skiing virgin powder, canyon swinging. Between thrills, Pinot Noir vineyards and some of the Southern Hemisphere's finest restaurants restore equilibrium.",
            imageUrl = "https://images.unsplash.com/photo-1507699622108-4be3abd695ad?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Dec – Feb (summer), Jun – Aug (skiing)",
            budgetEstimate = "\$130–\$260/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 28900,
            topAttractions = listOf("AJ Hackett Nevis Bungy", "The Remarkables Ski Area", "Milford Sound Day Trip", "Skyline Gondola & Luge", "Shotover Jet Boat Canyon"),
            latitude = -45.0312, longitude = 168.6626
        ),

        Destination(
            id = 33,
            name = "Costa Rica",
            country = "Costa Rica",
            highlights = "Pura vida in a country that's 26% national park",
            description = "Costa Rica has achieved something remarkable: a stable democracy running on nearly 100% renewable energy that has protected over a quarter of its territory as national parks and reserves — all in a country the size of West Virginia. The result is extraordinary: active volcanoes steam above cloud forests, Pacific surf breaks flank Caribbean coral reefs, and a single square kilometer of rainforest can contain more tree species than all of Europe.",
            imageUrl = "https://images.unsplash.com/photo-1518638150340-f706e86654de?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Dec – Apr",
            budgetEstimate = "\$70–\$150/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 31400,
            topAttractions = listOf("Arenal Volcano National Park", "Manuel Antonio Beach & Wildlife", "Monteverde Cloud Forest Reserve", "Corcovado National Park", "Tortuguero Sea Turtle Nesting"),
            latitude = 9.7489, longitude = -83.7534
        ),

        Destination(
            id = 34,
            name = "Iceland",
            country = "Iceland",
            highlights = "Fire, ice, aurora, and absolute silence",
            description = "Iceland sits directly on the Mid-Atlantic Ridge, making it one of the most geologically active places on Earth — a landscape of lava fields, glacier-carved fjords, geysers erupting every six minutes, and waterfalls plunging with no handrails into mist-filled gorges. The aurora borealis transforms the winter sky from September to March, while the Midnight Sun bathes everything in golden light for the entire month of June.",
            imageUrl = "https://images.unsplash.com/photo-1476610182048-b716b8518aae?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Jun – Aug (midnight sun), Sep – Mar (northern lights)",
            budgetEstimate = "\$150–\$300/day",
            rating = 4.9f,
            priceLevel = "Mid-Range",
            reviewCount = 24700,
            topAttractions = listOf("Northern Lights Viewing", "Golden Circle (Geysir, Gullfoss, Þingvellir)", "Blue Lagoon Geothermal Spa", "Jökulsárlón Glacier Lagoon", "Landmannalaugar Highland Hike"),
            latitude = 64.9631, longitude = -19.0208
        ),

        Destination(
            id = 35,
            name = "Serengeti",
            country = "Tanzania",
            highlights = "The greatest wildlife show on Earth, unrehearsed",
            description = "The Serengeti-Mara ecosystem is the stage for the planet's most spectacular wildlife event: the Great Migration, in which 1.7 million wildebeest, 500,000 Thomson's gazelle, and 300,000 zebra move in a continuous clockwise circuit through Tanzania and Kenya, crossing crocodile-infested rivers in a spectacle of raw nature. The Serengeti also holds the highest density of large predators in Africa, in a near-continuous Big Five spectacle.",
            imageUrl = "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Jul – Oct (Great Migration)",
            budgetEstimate = "\$400–\$1,500/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 19600,
            topAttractions = listOf("Great Wildebeest Migration", "Ngorongoro Crater Floor Game Drive", "Hot Air Balloon Safari at Sunrise", "Maasai Village Cultural Visit", "Big Five Predator Game Drive"),
            latitude = -2.1540, longitude = 34.6857
        ),

        Destination(
            id = 36,
            name = "Borneo",
            country = "Malaysia",
            highlights = "The last great ancient rainforest on Earth",
            description = "Borneo's rainforest is 130 million years old — older than the Amazon by 60 million years — and contains biodiversity that staggers the imagination. Orangutans, pygmy elephants, and proboscis monkeys inhabit river corridors that have barely changed since the Jurassic. The Kinabatangan River wildlife corridor and Danum Valley Conservation Area offer encounters that rival anything the natural world has to offer.",
            imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Mar – Oct",
            budgetEstimate = "\$80–\$200/day",
            rating = 4.7f,
            priceLevel = "Mid-Range",
            reviewCount = 13200,
            topAttractions = listOf("Kinabatangan River Wildlife Cruise", "Sepilok Orangutan Rehabilitation Centre", "Mount Kinabalu Summit (4,095 m)", "Danum Valley Rainforest Walks", "Mulu Caves National Park"),
            latitude = 1.0000, longitude = 114.0000
        ),

        Destination(
            id = 37,
            name = "Amazon",
            country = "Brazil",
            highlights = "The lungs of the planet, teeming with life",
            description = "The Amazon basin contains the world's largest tropical rainforest — 5.5 million sq km producing 20% of Earth's oxygen and holding 10% of all species on the planet. The Amazon River discharges 20% of all freshwater entering the world's oceans. From the river city of Manaus, boats penetrate jungle waterways where pink river dolphins surface at dusk, piranhas inhabit black-water lagoons, and the night chorus of wildlife is louder than any concert.",
            imageUrl = "https://images.unsplash.com/photo-1535090467336-9f89d30ec51e?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Jun – Nov",
            budgetEstimate = "\$60–\$140/day",
            rating = 4.6f,
            priceLevel = "Mid-Range",
            reviewCount = 17800,
            topAttractions = listOf("Amazon River Boat Expedition", "Manaus Amazon Theatre Opera House", "Anavilhanas Archipelago", "Piranha Fishing at Dusk", "Meeting of the Waters (Rio Negro)"),
            latitude = -3.4653, longitude = -62.2159
        ),

        Destination(
            id = 38,
            name = "Torres del Paine",
            country = "Chile",
            highlights = "Patagonia's most dramatic crown jewel",
            description = "Torres del Paine National Park in Chilean Patagonia is one of the world's great wilderness areas — the granite towers of the Paine Massif rise 2,500 m from the Patagonian steppe in a landscape of cerulean glacial lakes, crashing waterfalls, and wild guanacos that have never learned to fear humans. The W Trek and O Circuit are considered among the world's finest multi-day hiking routes.",
            imageUrl = "https://images.unsplash.com/photo-1508193638397-1c4234db14d8?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Nov – Mar",
            budgetEstimate = "\$90–\$200/day",
            rating = 4.8f,
            priceLevel = "Mid-Range",
            reviewCount = 12600,
            topAttractions = listOf("Base of the Towers Hike", "Grey Glacier Ice Trek", "French Valley W Circuit", "Lake Pehoé Kayaking", "Mirador del Toro Sunrise"),
            latitude = -51.0000, longitude = -73.0000
        ),

        Destination(
            id = 39,
            name = "Galápagos Islands",
            country = "Ecuador",
            highlights = "Evolution's greatest living laboratory",
            description = "The Galápagos Islands, 1,000 km off Ecuador's Pacific coast, sit at the intersection of three ocean currents that deliver extraordinary marine life. Marine iguanas — the world's only ocean-going lizards — share beaches with sea lions that treat snorkellers as playmates. Giant tortoises over 100 years old still roam their original habitat. Darwin's 1835 observations here directly produced his theory of evolution by natural selection.",
            imageUrl = "https://images.unsplash.com/photo-1519355101879-b44cb1eafab1?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Jun – Dec",
            budgetEstimate = "\$300–\$800/day",
            rating = 4.9f,
            priceLevel = "Luxury",
            reviewCount = 16300,
            topAttractions = listOf("Giant Tortoise Reserve", "Marine Iguana Colonies", "Snorkeling with Sea Lions & Sharks", "Blue-Footed Booby Nesting Grounds", "Charles Darwin Research Station"),
            latitude = -0.9538, longitude = -90.9656
        ),

        Destination(
            id = 40,
            name = "Everest Base Camp",
            country = "Nepal",
            highlights = "A journey to the foot of the world",
            description = "The Everest Base Camp Trek is the world's most iconic high-altitude walk — 14 days through the Khumbu Valley, passing through Sherpa villages where prayer flags snap in the wind, ancient Buddhist monasteries, and rhododendron forests ablaze in spring red, ascending to 5,364 m at the foot of the world's highest mountain. The views of the Khumbu Icefall and surrounding 7,000 m peaks deliver a visual experience unlike any other on Earth.",
            imageUrl = "https://images.unsplash.com/photo-1509099836639-18ba1795216d?w=800",
            category = DestinationCategory.ADVENTURE,
            bestTimeToVisit = "Mar – May, Sep – Nov",
            budgetEstimate = "\$35–\$70/day",
            rating = 4.8f,
            priceLevel = "Budget",
            reviewCount = 21400,
            topAttractions = listOf("Everest Base Camp (5,364 m)", "Kala Patthar Sunrise Panorama", "Tengboche Monastery", "Namche Bazaar Sherpa Town", "Khumbu Icefall Viewpoint"),
            latitude = 28.0025, longitude = 86.8530
        )
    )
}
