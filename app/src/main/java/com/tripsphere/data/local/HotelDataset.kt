package com.tripsphere.data.local

/**
 * Static hotel dataset covering all destinations and hub cities in TripSphere.
 * distanceMeters is computed at query time by LocalHotelRepositoryImpl.
 */
data class HotelEntry(
    val id: String,
    val name: String,
    val type: String,
    val stars: Int?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String? = null,
    val website: String? = null,
    val estimatedPriceRange: String,
    val imageUrl: String = ""
)

object HotelDataset {

    val all: List<HotelEntry> = buildList {

        // ── PARIS, France ───────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("PAR001", "Le Meurice", "Hotel", 5, "228 Rue de Rivoli, 75001 Paris", 48.8651, 2.3277, "+33-1-44-58-10-10", "https://www.dorchestercollection.com/le-meurice", "₹65,000–1,20,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("PAR002", "Four Seasons Hotel George V", "Hotel", 5, "31 Avenue George V, 75008 Paris", 48.8697, 2.3003, "+33-1-49-52-70-00", "https://www.fourseasons.com/paris", "₹75,000–1,50,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("PAR003", "Hôtel Plaza Athénée", "Hotel", 5, "25 Avenue Montaigne, 75008 Paris", 48.8666, 2.3028, "+33-1-53-67-66-65", "https://www.dorchestercollection.com/plaza-athenee", "₹70,000–1,40,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("PAR004", "Hotel de Crillon", "Hotel", 5, "10 Place de la Concorde, 75008 Paris", 48.8651, 2.3217, "+33-1-44-71-15-00", "https://www.rosewoodhotels.com/crillon", "₹80,000–1,60,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("PAR005", "Hôtel Lutetia", "Hotel", 5, "45 Blvd Raspail, 75006 Paris", 48.8494, 2.3237, "+33-1-49-54-46-46", "https://www.hotellutetia.com", "₹50,000–95,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("PAR006", "Sofitel Paris Le Faubourg", "Hotel", 5, "15 Rue Boissy d'Anglas, 75008 Paris", 48.8703, 2.3198, "+33-1-44-94-14-14", "https://www.sofitel-paris-faubourg.com", "₹40,000–75,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("PAR007", "Hotel du Louvre", "Hotel", 4, "Place André Malraux, 75001 Paris", 48.8640, 2.3375, "+33-1-44-58-38-38", "https://www.hoteldulouvre.com", "₹18,000–35,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("PAR008", "Novotel Paris Centre Tour Eiffel", "Hotel", 4, "61 Quai de Grenelle, 75015 Paris", 48.8487, 2.2876, "+33-1-40-58-20-00", "https://www.novotel.com", "₹15,000–28,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("PAR009", "Hôtel des Arts Montmartre", "Hotel", 3, "5 Rue Tholozé, 75018 Paris", 48.8837, 2.3392, "+33-1-46-06-30-52", "https://www.arts-hotel-paris.com", "₹8,500–14,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("PAR010", "Hotel Eiffel Rive Gauche", "Hotel", 3, "6 Rue du Gros-Caillou, 75007 Paris", 48.8572, 2.2986, "+33-1-45-51-24-56", "https://www.hotel-eiffel.com", "₹7,000–12,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("PAR011", "Generator Paris", "Hostel", null, "9-11 Place du Colonel Fabien, 75010 Paris", 48.8776, 2.3712, "+33-1-70-98-84-00", "https://staygenerator.com/paris", "₹2,500–5,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("PAR012", "Ibis Paris Gare du Nord", "Hotel", 2, "31 Rue de St-Quentin, 75010 Paris", 48.8803, 2.3554, "+33-1-40-34-43-43", "https://www.ibis.com", "₹5,500–9,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("PAR013", "Hotel Saint-Paul Le Marais", "Hotel", 3, "8 Rue de Sévigné, 75004 Paris", 48.8558, 2.3610, "+33-1-48-04-97-27", null, "₹9,000–16,000/night", "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=700&q=80"),
            HotelEntry("PAR014", "Le Bristol Paris", "Hotel", 5, "112 Rue du Faubourg Saint-Honoré, 75008 Paris", 48.8727, 2.3143, "+33-1-53-43-43-00", "https://www.oetkercollection.com/le-bristol-paris", "₹85,000–1,80,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
        ))

        // ── DUBAI, UAE ─────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("DXB001", "Burj Al Arab Jumeirah", "Hotel", 7, "Jumeirah Rd, Umm Suqeim 3, Dubai", 25.1412, 55.1853, "+971-4-301-7777", "https://www.jumeirah.com/burj-al-arab", "₹1,50,000–5,00,000/night", "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=700&q=80"),
            HotelEntry("DXB002", "Atlantis The Palm", "Hotel", 5, "Crescent Rd, The Palm Jumeirah, Dubai", 25.1314, 55.1177, "+971-4-426-2000", "https://www.atlantis.com/dubai", "₹40,000–1,20,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
            HotelEntry("DXB003", "Address Downtown Dubai", "Hotel", 5, "Mohammed Bin Rashid Blvd, Downtown Dubai", 25.1900, 55.2779, "+971-4-436-8888", "https://www.addresshotels.com", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("DXB004", "JW Marriott Marquis Dubai", "Hotel", 5, "Sheikh Zayed Rd, Business Bay, Dubai", 25.2234, 55.3001, "+971-4-414-0000", "https://www.marriott.com/dubai-jw-marquis", "₹25,000–70,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("DXB005", "Sofitel Dubai The Palm", "Hotel", 5, "East Crescent, Palm Jumeirah, Dubai", 25.1418, 55.1399, "+971-4-455-6677", "https://www.sofitel.com", "₹30,000–80,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("DXB006", "Rove Downtown Dubai", "Hotel", 3, "Al Asayel St, Downtown Dubai", 25.1922, 55.2844, "+971-4-561-9000", "https://www.rovehotels.com", "₹7,000–12,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("DXB007", "Novotel Dubai Al Barsha", "Hotel", 4, "Sheikh Zayed Rd, Al Barsha, Dubai", 25.1124, 55.2001, "+971-4-435-5555", "https://www.novotel.com", "₹12,000–22,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("DXB008", "ibis Dubai Mall of the Emirates", "Hotel", 2, "Sheikh Zayed Rd, Al Barsha, Dubai", 25.1183, 55.2003, "+971-4-209-9000", "https://www.ibis.com", "₹5,000–8,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("DXB009", "Premier Inn Dubai Airport", "Motel", 3, "Garhoud, Dubai", 25.2534, 55.3644, "+971-4-213-3500", "https://www.premierinn.com", "₹6,000–10,000/night", "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=700&q=80"),
            HotelEntry("DXB010", "Wyndham Dubai Deira", "Hotel", 4, "Al Maktoum Rd, Deira, Dubai", 25.2730, 55.3220, "+971-4-266-6677", "https://www.wyndham.com", "₹15,000–28,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("DXB011", "Dubai Youth Hostel", "Hostel", null, "Al Qusais, Dubai", 25.2798, 55.3843, "+971-4-298-8151", null, "₹1,800–3,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("DXB012", "Hyatt Regency Dubai", "Hotel", 5, "Deira Corniche, Dubai", 25.2753, 55.3017, "+971-4-209-1234", "https://www.hyatt.com", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("DXB013", "Waldorf Astoria Dubai Palm Jumeirah", "Hotel", 5, "West Crescent, Palm Jumeirah, Dubai", 25.1158, 55.1376, "+971-4-818-2222", "https://www.waldorfastoria.com", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
        ))

        // ── TOKYO, Japan ────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("TYO001", "The Peninsula Tokyo", "Hotel", 5, "1-8-1 Yurakucho, Chiyoda City, Tokyo", 35.6720, 139.7601, "+81-3-6270-2888", "https://www.peninsula.com/tokyo", "₹45,000–1,00,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("TYO002", "Park Hyatt Tokyo", "Hotel", 5, "3-7-1-2 Nishi Shinjuku, Shinjuku City, Tokyo", 35.6860, 139.6919, "+81-3-5322-1234", "https://www.hyatt.com/park-hyatt-tokyo", "₹55,000–1,20,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("TYO003", "Mandarin Oriental Tokyo", "Hotel", 5, "2-1-1 Nihonbashi Muromachi, Chuo City, Tokyo", 35.6864, 139.7735, "+81-3-3270-8800", "https://www.mandarinoriental.com/tokyo", "₹50,000–1,10,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("TYO004", "Aman Tokyo", "Hotel", 5, "The Otemachi Tower, 1-5-6 Otemachi, Chiyoda City", 35.6866, 139.7641, "+81-3-5224-3333", "https://www.aman.com/tokyo", "₹70,000–1,80,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("TYO005", "The Prince Gallery Tokyo Kioicho", "Hotel", 5, "1-2 Kioi-cho, Chiyoda City, Tokyo", 35.6802, 139.7299, "+81-3-3234-1111", "https://www.marriott.com/prince-gallery-tokyo", "₹40,000–85,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("TYO006", "Hotel New Otani Tokyo", "Hotel", 4, "4-1 Kioi-cho, Chiyoda City, Tokyo", 35.6836, 139.7283, "+81-3-3265-1111", "https://www.newotani.co.jp/en/tokyo", "₹18,000–38,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("TYO007", "Cerulean Tower Tokyu Hotel", "Hotel", 4, "26-1 Sakuragaoka-cho, Shibuya City, Tokyo", 35.6548, 139.6977, "+81-3-3476-3000", "https://www.ceruleantower-hotel.com", "₹20,000–42,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("TYO008", "Shinjuku Washington Hotel", "Hotel", 3, "3-2-9 Nishi-Shinjuku, Shinjuku City, Tokyo", 35.6898, 139.6899, "+81-3-3343-3111", "https://www.washington-hotels.jp", "₹8,000–15,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("TYO009", "Remm Akihabara", "Hotel", 3, "1-6-9 Soto-Kanda, Chiyoda City, Tokyo", 35.6989, 139.7713, "+81-3-3257-7222", "https://www.hankyu-hotel.com/remm-akihabara", "₹7,500–13,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("TYO010", "Dormy Inn Shibuya", "Hotel", 3, "19-2 Maruyamacho, Shibuya City, Tokyo", 35.6593, 139.6965, "+81-3-5428-2910", "https://www.dormyinn.com", "₹7,000–12,000/night", "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=700&q=80"),
            HotelEntry("TYO011", "Khaosan Tokyo Kabuki", "Hostel", null, "2-16-3 Asakusa, Taito City, Tokyo", 35.7146, 139.7989, "+81-3-5830-7677", "https://www.khaosan-tokyo.com", "₹2,000–4,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("TYO012", "K's House Tokyo Oasis", "Hostel", null, "3-15-1 Kuramae, Taito City, Tokyo", 35.7064, 139.7954, "+81-3-5833-0555", "https://kshouse.jp/tokyo-e", "₹1,800–4,000/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("TYO013", "APA Hotel Shinjuku Kabukicho Tower", "Hotel", 3, "1-29-3 Kabukicho, Shinjuku City, Tokyo", 35.6953, 139.7046, "+81-3-5155-6111", "https://www.apahotel.com", "₹6,500–11,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
        ))

        // ── NEW YORK, USA ───────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("NYC001", "The Plaza Hotel", "Hotel", 5, "768 5th Ave, New York, NY 10019", 40.7646, -73.9742, "+1-212-759-3000", "https://www.theplazany.com", "₹55,000–1,30,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("NYC002", "St. Regis New York", "Hotel", 5, "2 E 55th St, New York, NY 10022", 40.7614, -73.9739, "+1-212-753-4500", "https://www.marriott.com/st-regis-new-york", "₹60,000–1,50,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("NYC003", "The Ritz-Carlton New York", "Hotel", 5, "50 Central Park S, New York, NY 10019", 40.7648, -73.9781, "+1-212-308-9100", "https://www.ritzcarlton.com/new-york-central-park", "₹65,000–1,60,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("NYC004", "Four Seasons New York", "Hotel", 5, "57 E 57th St, New York, NY 10022", 40.7612, -73.9710, "+1-212-758-5700", "https://www.fourseasons.com/newyorkfs", "₹70,000–1,70,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("NYC005", "The Mark Hotel", "Hotel", 5, "25 E 77th St, New York, NY 10075", 40.7758, -73.9620, "+1-212-744-4300", "https://www.themarkhotel.com", "₹50,000–1,10,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("NYC006", "The Standard High Line", "Hotel", 4, "848 Washington St, New York, NY 10014", 40.7393, -74.0083, "+1-212-645-4646", "https://www.standardhotels.com/high-line", "₹25,000–55,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("NYC007", "Hotel 48LEX New York", "Hotel", 4, "517 Lexington Ave, New York, NY 10017", 40.7546, -73.9734, "+1-212-888-2898", "https://www.48lexnewyork.com", "₹20,000–40,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("NYC008", "YOTEL New York", "Hotel", 3, "570 10th Ave, New York, NY 10036", 40.7572, -74.0007, "+1-646-449-7700", "https://www.yotel.com/new-york", "₹10,000–18,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("NYC009", "The Row NYC", "Hotel", 3, "700 8th Ave, New York, NY 10036", 40.7591, -73.9884, "+1-212-869-3600", "https://www.therownyc.com", "₹12,000–22,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("NYC010", "HI NYC Hostel", "Hostel", null, "891 Amsterdam Ave, New York, NY 10025", 40.7972, -73.9663, "+1-212-932-2300", "https://hiusa.org/new-york", "₹3,000–6,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("NYC011", "The Langham New York", "Hotel", 5, "400 Fifth Ave, New York, NY 10018", 40.7481, -73.9830, "+1-212-695-4005", "https://www.langhamhotels.com/new-york", "₹40,000–90,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
            HotelEntry("NYC012", "Marriott Marquis Times Square", "Hotel", 4, "1535 Broadway, New York, NY 10036", 40.7575, -73.9859, "+1-212-398-1900", "https://www.marriott.com/times-square", "₹22,000–48,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("NYC013", "Pod 51 Hotel", "Hotel", 2, "230 E 51st St, New York, NY 10022", 40.7548, -73.9697, "+1-212-355-0300", "https://www.thepodhotel.com", "₹6,500–11,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
        ))

        // ── LONDON, UK ─────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("LON001", "The Ritz London", "Hotel", 5, "150 Piccadilly, St. James's, London W1J 9BR", 51.5070, -0.1424, "+44-20-7493-8181", "https://www.theritzlondon.com", "₹55,000–1,30,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("LON002", "Claridge's", "Hotel", 5, "Brook St, Mayfair, London W1K 4HR", 51.5131, -0.1470, "+44-20-7629-8860", "https://www.claridges.co.uk", "₹60,000–1,50,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("LON003", "The Savoy", "Hotel", 5, "Strand, London WC2R 0EZ", 51.5101, -0.1207, "+44-20-7836-4343", "https://www.thesavoylondon.com", "₹65,000–1,60,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("LON004", "The Langham London", "Hotel", 5, "1C Portland Pl, Marylebone, London W1B 1JA", 51.5183, -0.1444, "+44-20-7636-1000", "https://www.langhamhotels.com/london", "₹40,000–90,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("LON005", "Shangri-La The Shard", "Hotel", 5, "31 St Thomas St, London SE1 9QU", 51.5045, -0.0861, "+44-20-7234-8000", "https://www.shangri-la.com/london", "₹45,000–1,00,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("LON006", "Hotel 41", "Hotel", 5, "41 Buckingham Palace Rd, London SW1W 0PS", 51.4967, -0.1435, "+44-20-7300-0041", "https://www.41hotel.com", "₹30,000–65,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
            HotelEntry("LON007", "citizenM London Tower of London", "Hotel", 3, "40 Trinity Square, London EC3N 4DJ", 51.5103, -0.0761, "+44-20-3519-4830", "https://www.citizenm.com/london", "₹12,000–22,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("LON008", "Premier Inn London City", "Hotel", 3, "1 Holywell Ln, Shoreditch, London EC2A 3ET", 51.5236, -0.0797, "+44-333-003-8101", "https://www.premierinn.com", "₹8,000–14,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("LON009", "Travelodge London Central", "Hotel", 2, "1 City Rd, London EC1Y 1AG", 51.5241, -0.0878, "+44-871-984-6191", "https://www.travelodge.co.uk", "₹5,000–9,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("LON010", "YHA London Central", "Hostel", null, "104 Bolsover St, Fitzrovia, London W1W 5NU", 51.5213, -0.1430, "+44-345-371-9154", "https://www.yha.org.uk", "₹2,200–4,500/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("LON011", "The Ned London", "Hotel", 5, "27 Poultry, London EC2R 8AJ", 51.5133, -0.0912, "+44-20-3828-2000", "https://www.thened.com/london", "₹35,000–80,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("LON012", "InterContinental London Park Lane", "Hotel", 5, "One Hamilton Pl, Park Lane, London W1J 7QY", 51.5049, -0.1530, "+44-20-7409-3131", "https://www.ihg.com/intercontinental/london", "₹42,000–95,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("LON013", "Hilton London Bankside", "Hotel", 4, "2-8 Great Suffolk St, London SE1 0UG", 51.5042, -0.1001, "+44-20-7902-8000", "https://www.hilton.com/london-bankside", "₹18,000–35,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
        ))

        // ── SINGAPORE ──────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("SIN001", "Marina Bay Sands", "Hotel", 5, "10 Bayfront Ave, Singapore 018956", 1.2834, 103.8607, "+65-6688-8868", "https://www.marinabaysands.com", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?w=700&q=80"),
            HotelEntry("SIN002", "Raffles Singapore", "Hotel", 5, "1 Beach Rd, Singapore 189673", 1.2949, 103.8525, "+65-6337-1886", "https://www.raffles.com/singapore", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("SIN003", "The Fullerton Hotel Singapore", "Hotel", 5, "1 Fullerton Square, Singapore 049178", 1.2861, 103.8531, "+65-6733-8388", "https://www.fullertonhotels.com", "₹30,000–70,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("SIN004", "Capella Singapore", "Hotel", 5, "1 The Knolls, Sentosa Island, Singapore 098297", 1.2533, 103.8249, "+65-6377-8888", "https://www.capellahotels.com/singapore", "₹55,000–1,30,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("SIN005", "Mandarin Oriental Singapore", "Hotel", 5, "5 Raffles Ave, Marina Square, Singapore 039797", 1.2909, 103.8574, "+65-6338-0066", "https://www.mandarinoriental.com/singapore", "₹28,000–65,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("SIN006", "Andaz Singapore", "Hotel", 5, "5 Fraser St, Singapore 189354", 1.3020, 103.8557, "+65-6408-1234", "https://www.hyatt.com/andaz-singapore", "₹25,000–55,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("SIN007", "Hotel Indigo Singapore Katong", "Hotel", 4, "86 East Coast Rd, Singapore 428788", 1.3046, 103.9007, "+65-6723-9000", "https://www.ihg.com/hotelindigo/singapore-katong", "₹15,000–28,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("SIN008", "Oasia Hotel Novena", "Hotel", 4, "8 Sinaran Dr, Singapore 307470", 1.3197, 103.8448, "+65-6664-3333", "https://www.oasiahotel.com", "₹12,000–22,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("SIN009", "ibis Singapore on Bencoolen", "Hotel", 2, "170 Bencoolen St, Singapore 189657", 1.2994, 103.8494, "+65-6593-2888", "https://www.ibis.com", "₹5,500–9,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("SIN010", "The Pod Boutique Capsule Hotel", "Hostel", null, "289 Beach Rd, Singapore 199552", 1.3041, 103.8599, "+65-6298-8505", "https://thepod.sg", "₹2,500–4,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("SIN011", "Park Hotel Clarke Quay", "Hotel", 4, "1 Unity St, Robertson Quay, Singapore 237983", 1.2893, 103.8447, "+65-6593-8888", "https://www.parkhotelgroup.com/clarke-quay", "₹18,000–35,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("SIN012", "W Singapore Sentosa Cove", "Hotel", 5, "21 Ocean Way, Sentosa Cove, Singapore 098374", 1.2489, 103.8283, "+65-6808-7288", "https://www.marriott.com/w-singapore", "₹32,000–75,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
        ))

        // ── ROME, Italy ────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("ROM001", "Hotel Hassler Roma", "Hotel", 5, "Piazza Trinità dei Monti, 6, 00187 Rome", 41.9060, 12.4831, "+39-06-699-340", "https://www.hotelhasslerroma.com", "₹40,000–95,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("ROM002", "The St. Regis Rome", "Hotel", 5, "Via Vittorio Emanuele Orlando, 3, 00185 Rome", 41.9021, 12.4930, "+39-06-47091", "https://www.marriott.com/st-regis-rome", "₹35,000–85,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("ROM003", "Hotel Eden", "Hotel", 5, "Via Ludovisi, 49, 00187 Rome", 41.9072, 12.4872, "+39-06-478-121", "https://www.dorchestercollection.com/hotel-eden", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("ROM004", "Rome Cavalieri Waldorf Astoria", "Hotel", 5, "Via Alberto Cadlolo, 101, 00136 Rome", 41.9221, 12.4424, "+39-06-35091", "https://www.waldorfastoria.com", "₹38,000–90,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("ROM005", "Palazzo Manfredi", "Hotel", 5, "Via Labicana, 125, 00184 Rome", 41.8896, 12.4941, "+39-06-7759-1380", "https://www.palazzomanfredi.com", "₹42,000–1,00,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("ROM006", "Hotel Artemide", "Hotel", 4, "Via Nazionale, 22, 00184 Rome", 41.8997, 12.4939, "+39-06-489-911", "https://www.hotelartemide.it", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("ROM007", "Hotel Antico Palazzo Rospigliosi", "Hotel", 3, "Via Liberiana, 21, 00185 Rome", 41.9021, 12.4996, "+39-06-482-5921", null, "₹8,000–15,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("ROM008", "The Yellow Hostel", "Hostel", null, "Via Palestro, 44, 00185 Rome", 41.9056, 12.5021, "+39-06-4938-2682", "https://www.the-yellow.com", "₹2,000–4,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("ROM009", "Hotel San Pietrino", "Hotel", 2, "Via Giovanni Bettolo, 43, 00195 Rome", 41.9064, 12.4600, "+39-06-370-5132", "https://www.sanpietrino.it", "₹5,000–9,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("ROM010", "J.K. Place Roma", "Hotel", 5, "Via di Monte d'Oro, 30, 00186 Rome", 41.9026, 12.4734, "+39-06-982-634", "https://www.jkplace.com/rome", "₹50,000–1,20,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
            HotelEntry("ROM011", "Portrait Roma", "Hotel", 5, "Via Bocca di Leone, 23, 00187 Rome", 41.9052, 12.4813, "+39-06-6938-0742", "https://www.lungarnocollection.com/portrait-roma", "₹48,000–1,15,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("ROM012", "Hotel Nazionale Roma", "Hotel", 4, "Piazza Montecitorio, 131, 00186 Rome", 41.9007, 12.4783, "+39-06-695-001", "https://www.hotelnazionale.it", "₹15,000–30,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
        ))

        // ── BANGKOK, Thailand ──────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("BKK001", "Mandarin Oriental Bangkok", "Hotel", 5, "48 Oriental Ave, Bang Rak, Bangkok 10500", 13.7251, 100.5148, "+66-2-659-9000", "https://www.mandarinoriental.com/bangkok", "₹30,000–75,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("BKK002", "The Peninsula Bangkok", "Hotel", 5, "333 Charoennakorn Rd, Khlong San, Bangkok 10600", 13.7254, 100.5103, "+66-2-020-2888", "https://www.peninsula.com/bangkok", "₹32,000–80,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("BKK003", "Capella Bangkok", "Hotel", 5, "300-302 Charoenkrung Rd, Bang Rak, Bangkok 10500", 13.7192, 100.5134, "+66-2-098-3888", "https://www.capellahotels.com/bangkok", "₹40,000–1,00,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("BKK004", "Rosewood Bangkok", "Hotel", 5, "1041/38 Phloen Chit Rd, Pathum Wan, Bangkok 10330", 13.7457, 100.5489, "+66-2-080-0088", "https://www.rosewoodhotels.com/bangkok", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("BKK005", "SO/ Bangkok", "Hotel", 5, "2 N Sathorn Rd, Silom, Bang Rak, Bangkok 10500", 13.7265, 100.5263, "+66-2-624-0000", "https://www.sobangkok.com", "₹28,000–65,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("BKK006", "Novotel Bangkok Sukhumvit 20", "Hotel", 4, "19/9 Sukhumvit Soi 20, Khlong Toei, Bangkok 10110", 13.7278, 100.5680, "+66-2-009-9999", "https://www.novotel.com", "₹10,000–20,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("BKK007", "Bangkok Marriott Sukhumvit", "Hotel", 4, "2 Sukhumvit Soi 57, Vadhana, Bangkok 10110", 13.7206, 100.5794, "+66-2-797-0000", "https://www.marriott.com/bangkok", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("BKK008", "ibis Bangkok Siam", "Hotel", 2, "927 Rama 1 Rd, Wang Mai, Pathum Wan, Bangkok 10330", 13.7459, 100.5302, "+66-2-659-2888", "https://www.ibis.com", "₹3,500–7,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("BKK009", "NapPark Hostel at Khao San", "Hostel", null, "5 Trok Rong Mai, Khao San Rd, Phra Nakhon, Bangkok", 13.7584, 100.4967, "+66-2-282-2324", "https://www.nappark.com", "₹1,200–2,800/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("BKK010", "TRYP by Wyndham Bangkok", "Hotel", 3, "67 Sukhumvit Soi 33, Vadhana, Bangkok 10110", 13.7266, 100.5713, "+66-2-258-0500", "https://www.wyndham.com/tryp-bangkok", "₹7,000–13,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("BKK011", "Avani+ Riverside Bangkok Hotel", "Hotel", 5, "257 Charoennakorn Rd, Thonburi, Bangkok 10600", 13.7216, 100.5083, "+66-2-431-9100", "https://www.avanihotels.com/bangkok-riverside", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
            HotelEntry("BKK012", "The Siam Hotel", "Hotel", 5, "3/2 Khao Rd, Vachirapayabal, Dusit, Bangkok 10300", 13.7728, 100.5072, "+66-2-206-6999", "https://www.thesiamhotel.com", "₹38,000–85,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
        ))

        // ── BARCELONA, Spain ───────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("BCN001", "Hotel Arts Barcelona", "Hotel", 5, "Carrer de la Marina, 19-21, 08005 Barcelona", 41.3865, 2.1966, "+34-93-221-1000", "https://www.hotelartsbarcelona.com", "₹32,000–80,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("BCN002", "W Barcelona", "Hotel", 5, "Plaça de la Rosa dels Vents, 1, 08039 Barcelona", 41.3698, 2.1875, "+34-93-295-2800", "https://www.marriott.com/w-barcelona", "₹28,000–70,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("BCN003", "Mandarin Oriental Barcelona", "Hotel", 5, "Passeig de Gràcia, 38-40, 08007 Barcelona", 41.3924, 2.1650, "+34-93-151-8888", "https://www.mandarinoriental.com/barcelona", "₹35,000–85,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("BCN004", "Cotton House Hotel", "Hotel", 5, "Gran Via de les Corts Catalanes, 670, 08010 Barcelona", 41.3920, 2.1657, "+34-93-450-5045", "https://www.hotelcottonhouse.com", "₹30,000–75,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("BCN005", "El Palace Barcelona", "Hotel", 5, "Gran Via de les Corts Catalanes, 668, 08010 Barcelona", 41.3922, 2.1652, "+34-93-510-1130", "https://www.hotelpalacebarcelona.com", "₹38,000–90,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("BCN006", "NH Collection Barcelona Gran Hotel", "Hotel", 4, "Rambla de Catalunya, 26, 08007 Barcelona", 41.3921, 2.1621, "+34-93-301-0000", "https://www.nhcollection.com", "₹15,000–30,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("BCN007", "Hotel Praktik Rambla", "Hotel", 3, "La Rambla, 58, 08002 Barcelona", 41.3812, 2.1726, "+34-93-343-6690", "https://www.hotelpraktikrambla.com", "₹8,000–15,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("BCN008", "Catalonia Barcelona Plaza", "Hotel", 4, "Plaça d'Espanya, 6-8, 08014 Barcelona", 41.3737, 2.1489, "+34-93-426-2600", "https://www.hoteles-catalonia.com", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("BCN009", "Sant Jordi Hostels Gracia", "Hostel", null, "Carrer de Còrsega, 5, 08010 Barcelona", 41.3980, 2.1641, "+34-93-368-6116", "https://www.santjordihostels.com", "₹1,800–4,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("BCN010", "Ibis Barcelona Centro", "Hotel", 2, "Carrer de Vilamarí, 54, 08015 Barcelona", 41.3773, 2.1514, "+34-93-454-6161", "https://www.ibis.com", "₹5,000–9,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("BCN011", "Casa Camper Barcelona", "Hotel", 4, "Carrer d'Elisabets, 11, 08001 Barcelona", 41.3820, 2.1674, "+34-93-342-6280", "https://www.casacamper.com/barcelona", "₹20,000–42,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("BCN012", "Sofia Barcelona Hotel", "Hotel", 5, "Passeig de la Reina Elisenda, 1, 08034 Barcelona", 41.3972, 2.1225, "+34-93-393-6060", "https://www.sofiabarcelonahotel.com", "₹25,000–60,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
        ))

        // ── BALI, Indonesia ────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("BAL001", "Four Seasons Resort Bali at Sayan", "Hotel", 5, "Sayan, Ubud, Gianyar Regency, Bali 80571", -8.4872, 115.2519, "+62-361-977-577", "https://www.fourseasons.com/sayan", "₹55,000–1,30,000/night", "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=700&q=80"),
            HotelEntry("BAL002", "Amandari Bali", "Hotel", 5, "Kedewatan, Ubud, Gianyar Regency, Bali 80571", -8.4816, 115.2373, "+62-361-975-333", "https://www.aman.com/amandari", "₹70,000–1,80,000/night", "https://images.unsplash.com/photo-1540541338287-41700207dee6?w=700&q=80"),
            HotelEntry("BAL003", "Como Uma Ubud", "Hotel", 5, "Jalan Raya Sanggingan, Ubud, Bali 80571", -8.4977, 115.2491, "+62-361-972-448", "https://www.comohotels.com/ubud", "₹40,000–95,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("BAL004", "Viceroy Bali", "Hotel", 5, "Jl. Lanyahan, Br. Nagi, Ubud, Bali 80571", -8.5056, 115.2600, "+62-361-971-777", "https://www.viceroybali.com", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("BAL005", "Komaneka at Bisma", "Hotel", 5, "Jalan Bisma, Ubud, Gianyar Regency, Bali 80571", -8.5053, 115.2617, "+62-361-971-933", "https://www.komaneka.com/bisma", "₹38,000–85,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("BAL006", "Alaya Resort Ubud", "Hotel", 4, "Jalan Hanoman, Ubud, Gianyar Regency, Bali 80571", -8.5155, 115.2636, "+62-361-972-200", "https://www.alayahotels.com", "₹18,000–38,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("BAL007", "Bisma Eight Ubud", "Hotel", 4, "Jalan Bisma, Ubud, Gianyar Regency, Bali 80571", -8.5059, 115.2619, "+62-361-974-674", "https://www.bismaeight.com", "₹22,000–50,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("BAL008", "Swasti Eco Cottages", "Hotel", 3, "Jalan Subak Sok Wayah, Ubud, Bali 80571", -8.5032, 115.2552, "+62-361-974-079", "https://www.swasticottages.com", "₹6,000–12,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("BAL009", "Pondok Pundi Village Inn", "Guest House", null, "Jalan Karna Ubud, Gianyar, Bali 80571", -8.5092, 115.2589, "+62-361-975-524", null, "₹3,500–7,000/night", "https://images.unsplash.com/photo-1509660933844-6910e12765a0?w=700&q=80"),
            HotelEntry("BAL010", "Tegalalang Hostel & Villa", "Hostel", null, "Jalan Raya Tegalalang, Gianyar, Bali 80561", -8.4340, 115.2784, "+62-361-901-5000", null, "₹1,500–3,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("BAL011", "The Mulia Bali", "Hotel", 5, "Jalan Raya Nusa Dua Selatan, Nusa Dua, Bali 80363", -8.7944, 115.2222, "+62-361-301-7777", "https://www.themulia.com", "₹48,000–1,20,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("BAL012", "Potato Head Suites & Studios", "Hotel", 4, "Jalan Petitenget 51B, Seminyak, Bali 80361", -8.6826, 115.1528, "+62-361-473-7979", "https://www.ptthead.com", "₹25,000–55,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
        ))

        // ── ISTANBUL, Turkey ───────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("IST001", "Four Seasons Istanbul at Sultanahmet", "Hotel", 5, "Tevkifhane Sk. No:1, Sultanahmet, 34110 Istanbul", 41.0041, 28.9768, "+90-212-402-3000", "https://www.fourseasons.com/istanbul", "₹40,000–1,00,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("IST002", "The Ritz-Carlton Istanbul", "Hotel", 5, "Suzer Plaza, Elmadağ, Şişli, 34367 Istanbul", 41.0453, 28.9919, "+90-212-334-4444", "https://www.ritzcarlton.com/istanbul", "₹35,000–85,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("IST003", "Ciragan Palace Kempinski", "Hotel", 5, "Çırağan Cd. No:32, Beşiktaş, 34349 Istanbul", 41.0466, 29.0063, "+90-212-326-4646", "https://www.kempinski.com/istanbul", "₹42,000–1,05,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("IST004", "Raffles Istanbul", "Hotel", 5, "Zorlu Center, Levazım Mah., Beşiktaş, 34340 Istanbul", 41.0659, 29.0121, "+90-212-924-0404", "https://www.raffles.com/istanbul", "₹38,000–95,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("IST005", "Mandarin Oriental Bosphorus Istanbul", "Hotel", 5, "Çırağan Cd. No:43, Beşiktaş, 34349 Istanbul", 41.0488, 29.0076, "+90-212-347-4444", "https://www.mandarinoriental.com/istanbul", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("IST006", "Soho House Istanbul", "Hotel", 4, "Meşrutiyet Cd. No:51, Beyoğlu, 34430 Istanbul", 41.0329, 28.9776, "+90-212-377-7300", "https://www.sohohouse.com/istanbul", "₹20,000–45,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("IST007", "Dosso Dossi Hotels Old City", "Hotel", 4, "Hüsrev Gerede Cd. No:57, Teşvikiye, 34367 Istanbul", 41.0501, 28.9951, "+90-212-901-9797", "https://www.dossodossi.com", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("IST008", "Hotel Amira Istanbul", "Hotel", 3, "Küçük Ayasofya Mah., Fatih, 34122 Istanbul", 41.0027, 28.9747, "+90-212-516-1640", "https://www.amiraisanbul.com", "₹7,000–13,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("IST009", "World House Hostel Istanbul", "Hostel", null, "Garibaldi Cd. No:1, Beyoğlu, 34430 Istanbul", 41.0322, 28.9788, "+90-212-293-5520", "https://www.worldhousehostel.com", "₹1,500–3,500/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("IST010", "Wyndham Grand Istanbul Levent", "Hotel", 5, "Büyükdere Cd. No:174, Levent, 34394 Istanbul", 41.0847, 29.0127, "+90-212-323-8000", "https://www.wyndham.com/istanbul", "₹25,000–60,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("IST011", "Hotel Ibrahim Pasha", "Hotel", 3, "Terzihane Sk. No:7, Sultanahmet, 34122 Istanbul", 41.0039, 28.9742, "+90-212-518-0394", "https://www.ibrahimpasha.com", "₹8,500–16,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("IST012", "Vault Karakoy The House Hotel", "Hotel", 4, "Bankalar Cd. No:5, Karaköy, 34425 Istanbul", 41.0238, 28.9749, "+90-212-244-0707", "https://www.thehousehotel.com/karakoy", "₹15,000–32,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
        ))

        // ── MALDIVES ───────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("MDV001", "Soneva Jani", "Hotel", 5, "Noonu Atoll, Maldives", 5.6535, 73.5025, "+960-656-0304", "https://www.soneva.com/soneva-jani", "₹1,50,000–5,00,000/night", "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=700&q=80"),
            HotelEntry("MDV002", "Six Senses Laamu", "Hotel", 5, "Laamu Atoll, Maldives", 1.8503, 73.5214, "+960-680-0800", "https://www.sixsenses.com/laamu", "₹1,20,000–4,00,000/night", "https://images.unsplash.com/photo-1540541338287-41700207dee6?w=700&q=80"),
            HotelEntry("MDV003", "One&Only Reethi Rah", "Hotel", 5, "North Malé Atoll, Maldives", 4.5003, 73.4697, "+960-664-8800", "https://www.oneandonlyresorts.com/reethi-rah", "₹1,30,000–4,50,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("MDV004", "Gili Lankanfushi Maldives", "Hotel", 5, "North Malé Atoll, Maldives", 4.2386, 73.4928, "+960-664-0304", "https://www.gili-lankanfushi.com", "₹1,00,000–3,50,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("MDV005", "Velaa Private Island", "Hotel", 5, "Noonu Atoll, Maldives", 5.9142, 73.1578, "+960-656-3600", "https://www.velaaprivateisland.com", "₹2,00,000–8,00,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("MDV006", "Emerald Maldives Resort & Spa", "Hotel", 5, "Raa Atoll, Maldives", 5.5919, 72.9611, "+960-658-8888", "https://www.emeraldmaldives.com", "₹80,000–2,00,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("MDV007", "Kandima Maldives", "Hotel", 4, "Dhaalu Atoll, Maldives", 2.7814, 72.9203, "+960-676-0077", "https://www.kandima.com", "₹45,000–1,20,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("MDV008", "Meeru Island Resort & Spa", "Hotel", 4, "North Malé Atoll, Maldives", 4.3811, 73.6019, "+960-664-3157", "https://www.meeru.com", "₹30,000–80,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("MDV009", "Fun Island Resort", "Hotel", 3, "South Malé Atoll, Maldives", 3.8731, 73.4258, "+960-664-0558", "https://www.funislandresort.com", "₹18,000–40,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("MDV010", "Hulhule Island Hotel", "Hotel", 3, "Hulhule Island, Malé Atoll, Maldives", 4.1875, 73.5181, "+960-333-0888", "https://www.hulhuleislandhotel.com", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("MDV011", "Anantara Veli Maldives Resort", "Hotel", 5, "South Malé Atoll, Maldives", 3.9312, 73.4356, "+960-664-4100", "https://www.anantara.com/veli-maldives", "₹90,000–2,50,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("MDV012", "Baros Maldives", "Hotel", 5, "North Malé Atoll, Maldives", 4.2711, 73.4706, "+960-664-2672", "https://www.baros.com", "₹75,000–1,80,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
        ))

        // ── SANTORINI, Greece ──────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("SNT001", "Grace Hotel Santorini", "Hotel", 5, "Imerovigli, Santorini 847 00, Greece", 36.4224, 25.4215, "+30-22860-73300", "https://www.gracehotels.com/santorini", "₹42,000–1,10,000/night", "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=700&q=80"),
            HotelEntry("SNT002", "Canaves Oia Estate", "Hotel", 5, "Oia, Santorini 847 02, Greece", 36.4621, 25.3757, "+30-22860-71453", "https://www.canaves.com", "₹55,000–1,40,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("SNT003", "Chromata Hotel", "Hotel", 5, "Imerovigli, Santorini 847 00, Greece", 36.4206, 25.4218, "+30-22860-76023", "https://www.chromatahotel.com", "₹38,000–90,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("SNT004", "Mystique Santorini", "Hotel", 5, "Oia, Santorini 847 02, Greece", 36.4619, 25.3773, "+30-22860-71114", "https://www.mystique.gr", "₹48,000–1,20,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("SNT005", "Katikies Hotel Santorini", "Hotel", 5, "Oia, Santorini 847 02, Greece", 36.4611, 25.3768, "+30-22860-71401", "https://www.katikies.com", "₹50,000–1,30,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
            HotelEntry("SNT006", "Vedema Resort Santorini", "Hotel", 5, "Megalochori, Santorini 847 00, Greece", 36.3728, 25.4283, "+30-22860-81796", "https://www.vedema.gr", "₹35,000–80,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("SNT007", "Astra Suites", "Hotel", 4, "Imerovigli, Santorini 847 00, Greece", 36.4211, 25.4219, "+30-22860-23641", "https://www.astrasuites.com", "₹22,000–50,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("SNT008", "Santorini Palace", "Hotel", 4, "Fira, Santorini 847 00, Greece", 36.4178, 25.4318, "+30-22860-22771", "https://www.santorinipalace.gr", "₹15,000–32,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("SNT009", "Hotel Keti Santorini", "Hotel", 3, "Agiou Mina, Fira, Santorini 847 00, Greece", 36.4174, 25.4313, "+30-22860-22324", "https://www.hotelketi.gr", "₹8,000–16,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("SNT010", "Youth Hostel Fira Santorini", "Hostel", null, "Fira, Santorini 847 00, Greece", 36.4163, 25.4320, "+30-22860-23387", null, "₹2,000–4,500/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("SNT011", "Andronis Luxury Suites", "Hotel", 5, "Oia, Santorini 847 02, Greece", 36.4615, 25.3769, "+30-22860-72041", "https://www.andronisgroup.com", "₹60,000–1,60,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("SNT012", "Oia Suites", "Hotel", 4, "Oia, Santorini 847 02, Greece", 36.4601, 25.3782, "+30-22860-71784", "https://www.oiasuites.com", "₹20,000–45,000/night", "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=700&q=80"),
        ))

        // ── KYOTO, Japan ───────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("KYO001", "The Ritz-Carlton Kyoto", "Hotel", 5, "Kamogawa Nijo-Ohashi Hotori, Nakagyo, Kyoto 604-0902", 35.0148, 135.7695, "+81-75-746-5555", "https://www.ritzcarlton.com/kyoto", "₹55,000–1,30,000/night", "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=700&q=80"),
            HotelEntry("KYO002", "ROKU KYOTO", "Hotel", 5, "362-2 Murasakino Monzencho, Kita, Kyoto 603-8231", 35.0527, 135.7393, "+81-75-204-1214", "https://www.marriott.com/roku-kyoto", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("KYO003", "Suiran Kyoto", "Hotel", 5, "12 Sagatenryuji Susukinobaba-cho, Ukyo, Kyoto 616-8385", 35.0163, 135.6727, "+81-75-872-0101", "https://www.surainkyo.com", "₹42,000–1,00,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("KYO004", "Hyatt Regency Kyoto", "Hotel", 5, "644-2 Sanjusangendo-mawari, Higashiyama, Kyoto 605-0941", 34.9882, 135.7741, "+81-75-541-1234", "https://www.hyatt.com/hyatt-regency-kyoto", "₹30,000–75,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("KYO005", "Noku Kyoto", "Hotel", 5, "Nijo-dori Shimodachiuri Noboru, Kamigyo, Kyoto 602-0843", 35.0241, 135.7584, "+81-75-241-6836", "https://www.nokyoto.com", "₹28,000–65,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("KYO006", "Hotel The Celestine Kyoto Gion", "Hotel", 4, "Gion-Shijo-agaru, Yamatooji, Higashiyama, Kyoto 605-0811", 35.0031, 135.7759, "+81-75-532-3111", "https://www.celestinekyoto.com", "₹18,000–38,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("KYO007", "Kyoto Tower Hotel", "Hotel", 3, "Karasuma-dori Shichijo-Kudaru, Shimogyo, Kyoto 600-8216", 34.9857, 135.7584, "+81-75-361-3211", "https://www.kyoto-tower-hotel.co.jp", "₹9,000–18,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("KYO008", "Hotel Anteroom Kyoto", "Hotel", 3, "7 Higashi Kujyo Tokiwa-cho, Minami, Kyoto 601-8012", 34.9754, 135.7601, "+81-75-681-5656", "https://www.anteroom.jp/kyoto", "₹8,000–15,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("KYO009", "K's House Kyoto", "Hostel", null, "418 Nayacho, Higashiyama, Kyoto 605-0905", 34.9947, 135.7722, "+81-75-342-2444", "https://kshouse.jp/kyoto-e", "₹1,800–4,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("KYO010", "Piece Hostel Kyoto", "Hostel", null, "21 Higashikujo Nishisanno-cho, Minami, Kyoto 601-8004", 34.9769, 135.7617, "+81-75-694-3300", "https://www.piecehostel.com/kyoto", "₹1,500–3,500/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("KYO011", "APA Hotel Kyoto-Ekimae", "Hotel", 2, "Aburanokoji-dori Shichijo-Kudaru 3-chome, Shimogyo, Kyoto 600-8237", 34.9853, 135.7552, "+81-75-343-8111", "https://www.apahotel.com", "₹5,500–10,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("KYO012", "Kyoto Granbell Hotel", "Hotel", 4, "Shimogawara-cho, Higashiyama, Kyoto 605-0853", 35.0008, 135.7751, "+81-75-533-1211", "https://www.granbellhotel.jp/kyoto", "₹15,000–30,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
        ))

        // ── MACHU PICCHU, Peru ─────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("MPC001", "Belmond Sanctuary Lodge", "Hotel", 5, "Machu Picchu, Cusco Region, Peru", -13.1631, -72.5446, "+51-1-610-8300", "https://www.belmond.com/sanctuary-lodge", "₹95,000–2,50,000/night", "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=700&q=80"),
            HotelEntry("MPC002", "Sumaq Machu Picchu Hotel", "Hotel", 5, "Avenida Hermanos Ayar, Aguas Calientes, Cusco", -13.1543, -72.5279, "+51-84-211-059", "https://www.sumaqhotelperu.com", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("MPC003", "Inkaterra Machu Picchu Pueblo Hotel", "Hotel", 5, "Avenida Pachacutec s/n, Aguas Calientes, Cusco", -13.1533, -72.5298, "+51-84-211-122", "https://www.inkaterra.com/machu-picchu", "₹55,000–1,30,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("MPC004", "El MaPi by Inkaterra", "Hotel", 4, "Calle Pachacutec 109, Aguas Calientes, Cusco", -13.1531, -72.5284, "+51-84-211-011", "https://www.inkaterra.com/el-mapi", "₹18,000–40,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("MPC005", "Tierra Viva Machu Picchu Hotel", "Hotel", 3, "Calle Wiraccocha 110, Aguas Calientes, Cusco", -13.1529, -72.5271, "+51-84-211-201", "https://www.tierravivaperu.com", "₹10,000–20,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("MPC006", "Hotel La Cabaña Machu Picchu", "Hotel", 3, "Avenida Pachacutec 805, Aguas Calientes, Cusco", -13.1538, -72.5265, "+51-84-211-048", null, "₹8,000–15,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("MPC007", "Rupa Wasi Lodge", "Hotel", 3, "Calle Huanacaure 180, Aguas Calientes, Cusco", -13.1527, -72.5270, "+51-84-211-101", "https://www.rupawasi.net", "₹7,500–14,000/night", "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=700&q=80"),
            HotelEntry("MPC008", "Hostal Taypikala Machu Picchu", "Hotel", 2, "Calle Tupac Yupanqui 103, Aguas Calientes, Cusco", -13.1540, -72.5276, "+51-84-211-009", "https://www.taypikala.com", "₹4,500–8,500/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("MPC009", "Machu Picchu Viajes Hostel", "Hostel", null, "Calle Pachacutec 135, Aguas Calientes, Cusco", -13.1534, -72.5280, "+51-84-211-222", null, "₹2,000–4,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("MPC010", "Camping Machu Picchu", "Guest House", null, "Aguas Calientes, Cusco Region, Peru", -13.1525, -72.5283, null, null, "₹1,500–3,000/night", "https://images.unsplash.com/photo-1509660933844-6910e12765a0?w=700&q=80"),
        ))

        // ── SWISS ALPS, Switzerland ────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("SWS001", "The Chedi Andermatt", "Hotel", 5, "Gotthardstrasse 4, 6490 Andermatt, Switzerland", 46.6360, 8.5939, "+41-41-888-7474", "https://www.ghmhotels.com/andermatt", "₹55,000–1,40,000/night", "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=700&q=80"),
            HotelEntry("SWS002", "Palace Hotel Gstaad", "Hotel", 5, "Palacestrasse 28, 3780 Gstaad, Switzerland", 46.4748, 7.2857, "+41-33-748-5000", "https://www.palace.ch", "₹65,000–1,80,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("SWS003", "Badrutt's Palace Hotel St. Moritz", "Hotel", 5, "Via Serlas 27, 7500 St. Moritz, Switzerland", 46.4972, 9.8397, "+41-81-837-1000", "https://www.badruttspalace.com", "₹70,000–2,00,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("SWS004", "Grand Hotel Kronenhof Pontresina", "Hotel", 5, "Via Maistra 130, 7504 Pontresina, Switzerland", 46.4957, 9.9000, "+41-81-830-3030", "https://www.kronenhof.com", "₹60,000–1,60,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("SWS005", "Victoria-Jungfrau Grand Hotel Interlaken", "Hotel", 5, "Höheweg 41, 3800 Interlaken, Switzerland", 46.6863, 7.8650, "+41-33-828-2828", "https://www.victoria-jungfrau.ch", "₹45,000–1,20,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("SWS006", "Kulm Hotel St. Moritz", "Hotel", 5, "Via Veglia 18, 7500 St. Moritz, Switzerland", 46.4951, 9.8382, "+41-81-836-8000", "https://www.kulmhotelstmoritz.ch", "₹55,000–1,50,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("SWS007", "Sunstar Hotel Wengen", "Hotel", 3, "Sunstar Hotel, 3823 Wengen, Switzerland", 46.6084, 7.9222, "+41-33-856-5200", "https://www.sunstar.ch/wengen", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("SWS008", "Hotel Bellevue des Alpes Wengen", "Hotel", 3, "3823 Kleine Scheidegg, Switzerland", 46.5850, 7.9606, "+41-33-855-1212", "https://www.bellevue-des-alpes.ch", "₹15,000–30,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("SWS009", "Backpackers Hostel Interlaken", "Hostel", null, "Alpenstrassse 16, 3800 Interlaken, Switzerland", 46.6844, 7.8666, "+41-33-826-1090", "https://www.backpackers.ch", "₹3,500–7,000/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
            HotelEntry("SWS010", "YHA Grindelwald", "Hostel", null, "Terrassenweg 15, 3818 Grindelwald, Switzerland", 46.6238, 8.0414, "+41-33-853-1009", "https://www.youthhostel.ch/grindelwald", "₹3,000–6,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("SWS011", "Hotel Bahnhof Zermatt", "Hotel", 2, "Bahnhofstrasse 27, 3920 Zermatt, Switzerland", 46.0207, 7.7486, "+41-27-967-2406", "https://www.hotel-bahnhof.ch", "₹8,000–15,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("SWS012", "Mont Cervin Palace Zermatt", "Hotel", 5, "Bahnhofstrasse 31, 3920 Zermatt, Switzerland", 46.0214, 7.7494, "+41-27-966-8888", "https://www.seilaclub.ch/montcervin", "₹50,000–1,30,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
        ))

        // ── AMSTERDAM, Netherlands ────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("AMS001", "Waldorf Astoria Amsterdam", "Hotel", 5, "Herengracht 542-556, 1017 CG Amsterdam", 52.3618, 4.8983, "+31-20-718-4600", "https://www.waldorfastoria.com/amsterdam", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("AMS002", "Pulitzer Amsterdam", "Hotel", 5, "Prinsengracht 315-331, 1016 GZ Amsterdam", 52.3700, 4.8845, "+31-20-523-5235", "https://www.pulitzeramsterdam.com", "₹30,000–75,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("AMS003", "The Dylan Amsterdam", "Hotel", 5, "Keizersgracht 384, 1016 GB Amsterdam", 52.3677, 4.8835, "+31-20-530-2010", "https://www.dylanamsterdam.com", "₹35,000–85,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("AMS004", "Conservatorium Hotel", "Hotel", 5, "Van Baerlestraat 27, 1071 AN Amsterdam", 52.3577, 4.8777, "+31-20-570-0000", "https://www.conservatoriumhotel.com", "₹28,000–70,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("AMS005", "Hotel V Nesplein", "Hotel", 4, "Nes 49, 1012 KD Amsterdam", 52.3731, 4.8965, "+31-20-662-3233", "https://www.hotelv.nl", "₹22,000–50,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("AMS006", "Hotel Arena Amsterdam", "Hotel", 4, "'s-Gravesandestraat 51, 1092 AA Amsterdam", 52.3578, 4.9114, "+31-20-850-2400", "https://www.hotelarena.nl", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("AMS007", "ibis Amsterdam Centre", "Hotel", 2, "Warmoestraat 25, 1012 HT Amsterdam", 52.3749, 4.8993, "+31-20-721-9172", "https://www.ibis.com", "₹6,000–11,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("AMS008", "Meininger Amsterdam City West", "Hostel", null, "Orlyplein 1, 1043 DP Amsterdam", 52.3884, 4.8393, "+31-20-760-3400", "https://www.meininger-hotels.com/amsterdam", "₹2,500–5,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
        ))

        // ── PRAGUE, Czech Republic ─────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("PRG001", "Four Seasons Hotel Prague", "Hotel", 5, "Veleslavínova 2a, 110 00 Prague 1", 50.0856, 14.4128, "+420-221-427-000", "https://www.fourseasons.com/prague", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("PRG002", "Mandarin Oriental Prague", "Hotel", 5, "Nebovidská 459/1, 118 00 Prague", 50.0872, 14.4057, "+420-233-088-888", "https://www.mandarinoriental.com/prague", "₹30,000–80,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("PRG003", "Grand Hotel Bohemia", "Hotel", 5, "Králodvorská 4, 110 00 Prague 1", 50.0877, 14.4233, "+420-234-608-111", "https://www.grandhotelbohemia.cz", "₹28,000–65,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("PRG004", "The Emblem Hotel Prague", "Hotel", 5, "Platnéřská 19, 110 00 Prague 1", 50.0879, 14.4173, "+420-226-202-500", "https://www.emblemprague.com", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("PRG005", "Hotel Josef Prague", "Hotel", 4, "Rybná 20, 110 00 Prague 1", 50.0885, 14.4268, "+420-221-700-111", "https://www.hoteljosef.com", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("PRG006", "MOOo by the Castle", "Hotel", 3, "Keplerova 5, 118 00 Prague 1", 50.0918, 14.3932, "+420-220-513-930", "https://www.hotelmooo.com", "₹8,000–16,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("PRG007", "Czech Inn Hostel", "Hostel", null, "Francouzská 76, 101 00 Prague 10", 50.0750, 14.4524, "+420-267-267-600", "https://www.czech-inn.com", "₹2,000–4,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("PRG008", "Mosaic House Prague", "Hostel", null, "Odborů 4, 120 00 Prague 2", 50.0720, 14.4213, "+420-246-008-324", "https://www.mosaichouse.com", "₹2,200–5,000/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
        ))

        // ── BANFF, Canada ──────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("BNF001", "Fairmont Banff Springs", "Hotel", 5, "405 Spray Ave, Banff, AB T1L 1J4", 51.1648, -115.5586, "+1-403-762-2211", "https://www.fairmont.com/banff-springs", "₹45,000–1,20,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("BNF002", "Fairmont Chateau Lake Louise", "Hotel", 5, "111 Lake Louise Dr, Lake Louise, AB T0L 1E0", 51.4254, -116.1773, "+1-403-522-3511", "https://www.fairmont.com/lake-louise", "₹55,000–1,50,000/night", "https://images.unsplash.com/photo-1543716091-a840c05249ec?w=700&q=80"),
            HotelEntry("BNF003", "Rimrock Resort Hotel Banff", "Hotel", 4, "300 Mountain Ave, Banff, AB T1L 1J2", 51.1564, -115.5723, "+1-403-762-3356", "https://www.rimrockresort.com", "₹25,000–60,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("BNF004", "The Juniper Hotel & Bistro", "Hotel", 3, "1 Juniper Way, Banff, AB T1L 1B3", 51.1767, -115.5744, "+1-403-762-2281", "https://www.thejuniper.com", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("BNF005", "Banff Aspen Lodge", "Hotel", 3, "401 Banff Ave, Banff, AB T1L 1A7", 51.1789, -115.5618, "+1-403-762-4401", "https://www.banffaspenlodge.com", "₹10,000–20,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("BNF006", "Storm Mountain Lodge", "Hotel", 3, "Bow Valley Pkwy, Banff, AB T1L 1A3", 51.1964, -115.6972, "+1-403-762-4155", "https://www.stormmountainlodge.com", "₹14,000–28,000/night", "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=700&q=80"),
            HotelEntry("BNF007", "HI Banff Alpine Centre", "Hostel", null, "801 Hidden Ridge Way, Banff, AB T1L 1B9", 51.1736, -115.5504, "+1-403-762-4122", "https://www.hihostels.com/banff", "₹3,000–6,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
        ))

        // ── QUEENSTOWN, New Zealand ────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("QTN001", "Eichardt's Private Hotel", "Hotel", 5, "Marine Parade, Queenstown 9300", -45.0311, 168.6600, "+64-3-441-0450", "https://www.eichardts.com", "₹40,000–1,00,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("QTN002", "The Rees Hotel Queenstown", "Hotel", 5, "377 Lake Esplanade, Queenstown 9300", -45.0332, 168.6628, "+64-3-450-1100", "https://www.therees.co.nz", "₹30,000–75,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("QTN003", "Millbrook Resort Arrowtown", "Hotel", 4, "Malaghans Rd, Arrowtown 9302", -44.9384, 168.8297, "+64-3-441-7000", "https://www.millbrook.co.nz", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1507699622108-4be3abd695ad?w=700&q=80"),
            HotelEntry("QTN004", "Novotel Queenstown Lakeside", "Hotel", 4, "Marine Parade, Queenstown 9300", -45.0321, 168.6584, "+64-3-442-7750", "https://www.novotel.com/queenstown", "₹15,000–32,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("QTN005", "Peppers Beacon Hotel", "Hotel", 4, "6-8 Lake Side Rd, Queenstown 9300", -45.0298, 168.6612, "+64-3-450-0400", "https://www.peppers.co.nz/beacon", "₹18,000–40,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("QTN006", "Queenstown YHA", "Hostel", null, "88-90 Lake Esplanade, Queenstown 9300", -45.0329, 168.6563, "+64-3-442-8413", "https://www.yha.co.nz/queenstown", "₹2,500–5,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
            HotelEntry("QTN007", "Base Queenstown Hostel", "Hostel", null, "40 Shotover St, Queenstown 9300", -45.0309, 168.6617, "+64-3-441-8090", "https://www.stayatbase.com/queenstown", "₹2,000–4,500/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
        ))

        // ── REYKJAVÍK / ICELAND ────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("ICL001", "101 Hotel Reykjavík", "Hotel", 5, "Hverfisgata 10, 101 Reykjavík", 64.1448, -21.9311, "+354-580-0101", "https://www.101hotel.is", "₹30,000–80,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("ICL002", "ION Adventure Hotel", "Hotel", 4, "Nesjavellir, 801 Selfoss, South Iceland", 64.0856, -21.2614, "+354-482-3415", "https://www.ioniceland.is", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1476610182048-b716b8518aae?w=700&q=80"),
            HotelEntry("ICL003", "Hótel Rangá", "Hotel", 4, "Rangárþing eystra, 851 Hella", 63.8382, -20.4073, "+354-487-5700", "https://www.hotelranga.is", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("ICL004", "Hotel Borg Reykjavík", "Hotel", 4, "Pósthússtræti 11, 101 Reykjavík", 64.1478, -21.9344, "+354-551-1440", "https://www.hotelborg.is", "₹25,000–60,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("ICL005", "Centerhotel Arnarhvoll", "Hotel", 4, "Ingólfsstræti 1, 101 Reykjavík", 64.1499, -21.9235, "+354-595-8540", "https://www.centerhotels.com/arnarhvoll", "₹18,000–40,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("ICL006", "Fosshotel Reykjavík", "Hotel", 3, "Þórunnartún 1, 105 Reykjavík", 64.1399, -21.9105, "+354-562-4000", "https://www.islandshotel.is/fosshotel-reykjavik", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("ICL007", "Kex Hostel Reykjavík", "Hostel", null, "Skúlagata 28, 101 Reykjavík", 64.1499, -21.9405, "+354-561-6060", "https://www.kexhostel.is", "₹2,500–5,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
        ))

        // ── PHUKET, Thailand ───────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("PHK001", "Amanpuri Phuket", "Hotel", 5, "Pansea Beach, Cherngtalay, Phuket 83110", 8.0456, 98.2734, "+66-76-324-333", "https://www.aman.com/resorts/amanpuri", "₹80,000–2,50,000/night", "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=700&q=80"),
            HotelEntry("PHK002", "Banyan Tree Phuket", "Hotel", 5, "33/27 Moo 4, Srisoonthorn Rd, Cherngtalay, Phuket 83110", 8.0134, 98.2928, "+66-76-372-400", "https://www.banyantree.com/phuket", "₹45,000–1,20,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("PHK003", "The Nai Harn Phuket", "Hotel", 5, "23/3 Vises Rd, Rawai, Phuket 83130", 7.7680, 98.3025, "+66-76-380-200", "https://www.thenaiharn.com", "₹30,000–80,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("PHK004", "Trisara Phuket", "Hotel", 5, "60/1 Moo 6, Srisoonthorn Rd, Cherngtalay, Phuket 83110", 8.0538, 98.2805, "+66-76-310-100", "https://www.trisara.com", "₹55,000–1,80,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("PHK005", "Katathani Phuket Beach Resort", "Hotel", 4, "14 Kata Noi Rd, Kata Noi, Phuket 83100", 7.8244, 98.2981, "+66-76-330-124", "https://www.katathani.com", "₹15,000–35,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("PHK006", "Holiday Inn Phuket Patong", "Hotel", 4, "21 Jungceylon, Patong, Phuket 83150", 7.8993, 98.2959, "+66-76-370-200", "https://www.ihg.com/holidayinn/phuket", "₹10,000–22,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("PHK007", "ibis Phuket Patong", "Hotel", 2, "52 Thaweewong Rd, Patong, Phuket 83150", 7.8956, 98.2962, "+66-76-689-999", "https://www.ibis.com/phuket", "₹4,500–8,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
            HotelEntry("PHK008", "Lub d Phuket Patong", "Hostel", null, "42 Phang Muang Sai Kor Rd, Patong, Phuket 83150", 7.8908, 98.2992, "+66-76-601-400", "https://www.lubd.com/phuket", "₹2,000–4,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
        ))

        // ── KOH SAMUI, Thailand ────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("KSM001", "Four Seasons Resort Koh Samui", "Hotel", 5, "219 Moo 5, Ang Thong, Koh Samui, Surat Thani 84140", 9.5796, 100.0665, "+66-77-243-000", "https://www.fourseasons.com/kohsamui", "₹55,000–1,50,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("KSM002", "Vana Belle Koh Samui", "Hotel", 5, "9/99 Moo 3, Bophut, Koh Samui 84320", 9.5511, 100.0158, "+66-77-915-555", "https://www.marriott.com/vana-belle", "₹40,000–1,00,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("KSM003", "SALA Samui Chaweng Beach", "Hotel", 5, "110/3 Moo 2, Chaweng Beach, Koh Samui 84320", 9.5333, 100.0611, "+66-77-422-222", "https://www.salaresorts.com/samui", "₹25,000–65,000/night", "https://images.unsplash.com/photo-1552465011-b4e21bf6e79a?w=700&q=80"),
            HotelEntry("KSM004", "Centara Grand Beach Resort Samui", "Hotel", 5, "38/2 Moo 3, Borpud, Koh Samui 84320", 9.5411, 100.0598, "+66-77-230-500", "https://www.centarahotelsresorts.com/samui", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("KSM005", "The Library Koh Samui", "Hotel", 4, "14/1 Moo 2, Chaweng Beach Rd, Koh Samui 84320", 9.5309, 100.0588, "+66-77-422-767", "https://www.thelibrary.co.th", "₹20,000–48,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("KSM006", "ibis Samui Bophut", "Hotel", 3, "Moo 1, Bophut Rd, Koh Samui 84320", 9.5481, 100.0152, "+66-77-908-800", "https://www.ibis.com/samui", "₹5,000–10,000/night", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=700&q=80"),
        ))

        // ── TULUM, Mexico ──────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("TLM001", "Azulik Tulum", "Hotel", 5, "Carretera Boca Paila Km 5, Tulum, Quintana Roo 77780", 20.1724, -87.4642, "+52-984-206-0701", "https://www.azulik.com", "₹50,000–1,50,000/night", "https://images.unsplash.com/photo-1552799446-159ba9523315?w=700&q=80"),
            HotelEntry("TLM002", "Be Tulum Boutique Hotel", "Hotel", 5, "Carretera Tulum-Boca Paila Km 3.5, Tulum 77780", 20.1813, -87.4596, "+52-984-875-5110", "https://www.betulum.com", "₹45,000–1,20,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("TLM003", "Sanará Tulum", "Hotel", 4, "Carretera Tulum-Boca Paila Km 2, Tulum 77780", 20.1865, -87.4561, "+52-984-160-8060", "https://www.sanatulum.com", "₹30,000–75,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("TLM004", "Papaya Playa Project", "Hotel", 4, "Carretera Boca Paila Km 4.5, Tulum 77780", 20.1748, -87.4628, "+52-984-116-2869", "https://www.papayaplayaproject.com", "₹20,000–55,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("TLM005", "Aldea Canzul Hotel Tulum", "Hotel", 3, "Avenida Sol Oriente, Tulum 77760", 20.2111, -87.4622, "+52-984-871-2408", null, "₹8,000–18,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("TLM006", "Vagabundo Hostel Tulum", "Hostel", null, "Avenida Tulum, Tulum Centro 77760", 20.2136, -87.4658, "+52-984-871-2506", null, "₹1,500–3,500/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
        ))

        // ── SEYCHELLES ─────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("SEY001", "Six Senses Zil Pasyon", "Hotel", 5, "Félicité Island, Seychelles", -4.3158, 55.8618, "+248-467-1000", "https://www.sixsenses.com/zilpasyon", "₹2,00,000–8,00,000/night", "https://images.unsplash.com/photo-1589979481223-deb893043163?w=700&q=80"),
            HotelEntry("SEY002", "Four Seasons Resort Seychelles", "Hotel", 5, "Petite Anse, Mahé, Seychelles", -4.7372, 55.4953, "+248-439-3000", "https://www.fourseasons.com/seychelles", "₹1,00,000–3,50,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("SEY003", "Raffles Seychelles Praslin", "Hotel", 5, "Anse Takamaka, Praslin, Seychelles", -4.3208, 55.7418, "+248-429-6000", "https://www.raffles.com/seychelles", "₹80,000–2,50,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("SEY004", "Constance Lemuria Resort", "Hotel", 5, "Anse Kerlan, Praslin, Seychelles", -4.2917, 55.7097, "+248-428-8000", "https://www.constancehotels.com/lemuria", "₹70,000–2,00,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("SEY005", "Anantara Maia Seychelles Villas", "Hotel", 5, "Anse Louis, Mahé, Seychelles", -4.7578, 55.4653, "+248-439-5000", "https://www.anantara.com/maia-seychelles", "₹90,000–2,80,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("SEY006", "L'Union Estate Guesthouse", "Guest House", null, "La Digue, Seychelles", -4.3634, 55.8340, "+248-423-4240", null, "₹15,000–30,000/night", "https://images.unsplash.com/photo-1509660933844-6910e12765a0?w=700&q=80"),
        ))

        // ── MAUI, USA ──────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("MAU001", "Four Seasons Resort Maui at Wailea", "Hotel", 5, "3900 Wailea Alanui Dr, Wailea, Maui, HI 96753", 20.6825, -156.4404, "+1-808-874-8000", "https://www.fourseasons.com/maui", "₹80,000–2,50,000/night", "https://images.unsplash.com/photo-1507876466758-e54b3bdbe44c?w=700&q=80"),
            HotelEntry("MAU002", "Andaz Maui at Wailea Resort", "Hotel", 5, "3550 Wailea Alanui Dr, Wailea, Maui, HI 96753", 20.6897, -156.4427, "+1-808-573-1234", "https://www.hyatt.com/andaz-maui", "₹55,000–1,50,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("MAU003", "Hotel Wailea", "Hotel", 5, "555 Kaukahi St, Wailea, Maui, HI 96753", 20.6815, -156.4294, "+1-808-874-0500", "https://www.hotelwailea.com", "₹65,000–1,80,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("MAU004", "Travaasa Hana", "Hotel", 4, "5031 Hana Hwy, Hana, Maui, HI 96713", 20.7574, -155.9895, "+1-808-270-5000", "https://www.travaasa.com/hana", "₹45,000–1,20,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("MAU005", "Westin Maui Resort & Spa", "Hotel", 4, "2365 Kaanapali Pkwy, Lahaina, Maui, HI 96761", 20.9195, -156.6955, "+1-808-667-2525", "https://www.marriott.com/westin-maui", "₹25,000–65,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("MAU006", "Maui Seaside Hotel", "Hotel", 3, "100 W Kaahumanu Ave, Kahului, Maui, HI 96732", 20.8916, -156.4765, "+1-808-877-3311", "https://www.mauiseasidehotel.com", "₹8,000–18,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
        ))

        // ── NORWEGIAN FJORDS / BERGEN ──────────────────────────────────────────
        addAll(listOf(
            HotelEntry("NOR001", "Hotel Union Øye", "Hotel", 5, "Norangsfjorden, 6196 Øye, Norway", 62.1484, 6.9614, "+47-70-06-21-00", "https://www.unionoye.no", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1520769490027-f729c0d295e6?w=700&q=80"),
            HotelEntry("NOR002", "Strand Hotel Bergen", "Hotel", 4, "Strandkaien 2-4, 5013 Bergen, Norway", 60.3973, 5.3226, "+47-55-59-34-00", "https://www.choice.no/strandbergen", "₹18,000–40,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("NOR003", "Fretheim Hotel Flåm", "Hotel", 3, "Fretheim, 5742 Flåm, Norway", 60.8635, 7.1176, "+47-57-63-63-00", "https://www.fretheim-hotel.no", "₹18,000–38,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("NOR004", "Walaker Hotel Solvorn", "Hotel", 3, "Solvorn, 6877 Luster, Norway", 61.3414, 7.3138, "+47-57-68-20-80", "https://www.walaker.com", "₹20,000–42,000/night", "https://images.unsplash.com/photo-1531248020715-f2ee21f88b5e?w=700&q=80"),
            HotelEntry("NOR005", "Thon Hotel Bristol Bergen", "Hotel", 4, "Torgallmenningen 11, 5014 Bergen, Norway", 60.3921, 5.3250, "+47-55-30-32-00", "https://www.thonhotels.no/bristol-bergen", "₹15,000–30,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
            HotelEntry("NOR006", "Fosshaugane Campus Hostel", "Hostel", null, "Fretheim 4, 5742 Flåm, Norway", 60.8631, 7.1172, "+47-57-63-25-00", null, "₹3,000–6,000/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
        ))

        // ── COSTA RICA ─────────────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("CRC001", "Nayara Springs Arenal", "Hotel", 5, "Arenal Volcano National Park, Alajuela, Costa Rica", 10.4696, -84.6990, "+506-2479-1600", "https://www.nayarasprings.com", "₹65,000–1,80,000/night", "https://images.unsplash.com/photo-1518638150340-f706e86654de?w=700&q=80"),
            HotelEntry("CRC002", "Hacienda AltaGracia Aparta Hotel", "Hotel", 5, "Cajón District, Pérez Zeledón, San José, Costa Rica", 9.3282, -83.4614, "+1-888-777-4110", "https://www.haciendaaltagracia.com", "₹80,000–2,50,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("CRC003", "Lapa Rios Lodge Osa Peninsula", "Hotel", 4, "Osa Peninsula, Puntarenas, Costa Rica", 8.3945, -83.4598, "+506-4070-0606", "https://www.laparios.com", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("CRC004", "Finca Rosa Blanca Coffee Plantation", "Hotel", 4, "Santa Bárbara, Heredia, Costa Rica", 9.9914, -84.1193, "+506-2269-9392", "https://www.fincarosablanca.com", "₹25,000–60,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("CRC005", "Hotel Bougainvillea San José", "Hotel", 3, "800 m Norte, Heredia, San José, Costa Rica", 9.9984, -84.0789, "+506-2244-1414", "https://www.bougainvillea.co.cr", "₹8,000–18,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("CRC006", "Selina Manuel Antonio", "Hostel", null, "Av. Central, Manuel Antonio, Puntarenas, Costa Rica", 9.3910, -84.1642, "+1-888-735-4621", "https://www.selina.com/costa-rica/manuel-antonio", "₹3,000–7,000/night", "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=700&q=80"),
        ))

        // ── NEPAL / KATHMANDU (Everest Base Camp & Himalayas) ─────────────────
        addAll(listOf(
            HotelEntry("NPL001", "Dwarika's Hotel Kathmandu", "Hotel", 5, "Battisputali, Kathmandu 44600, Nepal", 27.7133, 85.3367, "+977-1-4479-488", "https://www.dwarikas.com", "₹30,000–80,000/night", "https://images.unsplash.com/photo-1544735716-392fe2489ffa?w=700&q=80"),
            HotelEntry("NPL002", "Hyatt Regency Kathmandu", "Hotel", 5, "Taragaon, Boudha, Kathmandu 44600, Nepal", 27.7168, 85.3606, "+977-1-4491-234", "https://www.hyatt.com/hyatt-regency-kathmandu", "₹22,000–55,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("NPL003", "Yak & Yeti Hotel Kathmandu", "Hotel", 4, "Durbar Marg, Kathmandu 44600, Nepal", 27.7115, 85.3154, "+977-1-4248-999", "https://www.yakandyeti.com", "₹15,000–35,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("NPL004", "Hotel Everest View", "Hotel", 3, "Syangboche, Namche Bazaar, Solukhumbu, Nepal", 27.8121, 86.7175, "+977-1-4488-345", "https://www.hoteleverestview.com", "₹18,000–40,000/night", "https://images.unsplash.com/photo-1509099836639-18ba1795216d?w=700&q=80"),
            HotelEntry("NPL005", "Namche Bazaar Guesthouse", "Guest House", null, "Namche Bazaar, Solukhumbu, Nepal", 27.8063, 86.7155, "+977-38-540-140", null, "₹4,000–9,000/night", "https://images.unsplash.com/photo-1509660933844-6910e12765a0?w=700&q=80"),
            HotelEntry("NPL006", "Summit Hotel Kathmandu", "Hotel", 4, "Kupondol, Lalitpur, Kathmandu 44700, Nepal", 27.6823, 85.3154, "+977-1-5521-810", "https://www.summithotel.com.np", "₹12,000–28,000/night", "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=700&q=80"),
        ))

        // ── PATAGONIA / TORRES DEL PAINE ───────────────────────────────────────
        addAll(listOf(
            HotelEntry("PTG001", "explora Patagonia", "Hotel", 5, "Torres del Paine National Park, Magallanes, Chile", -51.0128, -72.9741, "+56-2-2395-2800", "https://www.explora.com/patagonia", "₹1,00,000–3,00,000/night", "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=700&q=80"),
            HotelEntry("PTG002", "Singular Patagonia", "Hotel", 5, "Puerto Bories, Puerto Natales, Magallanes, Chile", -51.7196, -72.5064, "+56-61-2722-030", "https://www.thesingular.com/patagonia", "₹65,000–1,80,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("PTG003", "EcoCamp Patagonia", "Hotel", 4, "Torres del Paine National Park, Magallanes, Chile", -51.1534, -72.9484, "+56-2-2923-5950", "https://www.ecocamp.travel", "₹55,000–1,50,000/night", "https://images.unsplash.com/photo-1508193638397-1c4234db14d8?w=700&q=80"),
            HotelEntry("PTG004", "Las Torres Patagonia", "Hotel", 4, "Torres del Paine National Park, Magallanes, Chile", -51.1082, -72.8726, "+56-61-2617-450", "https://www.lastorres.com", "₹35,000–90,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("PTG005", "Hostal Estanislava Puerto Natales", "Hostel", null, "Baquedano 155, Puerto Natales, Magallanes, Chile", -51.7283, -72.4916, "+56-61-2411-945", null, "₹2,000–5,000/night", "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?w=700&q=80"),
        ))

        // ── AMALFI COAST, Italy ────────────────────────────────────────────────
        addAll(listOf(
            HotelEntry("AML001", "Belmond Hotel Caruso", "Hotel", 5, "Piazza San Giovanni del Toro 2, 84010 Ravello SA, Italy", 40.6480, 14.6120, "+39-089-858-801", "https://www.belmond.com/hotel-caruso", "₹55,000–1,40,000/night", "https://images.unsplash.com/photo-1533104816931-20fa691ff6ca?w=700&q=80"),
            HotelEntry("AML002", "Le Sirenuse", "Hotel", 5, "Via Cristoforo Colombo, 30, 84017 Positano SA, Italy", 40.6280, 14.4895, "+39-089-875-066", "https://www.sirenuse.it", "₹60,000–1,60,000/night", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=700&q=80"),
            HotelEntry("AML003", "Villa Cimbrone Hotel", "Hotel", 5, "Via Santa Chiara, 26, 84010 Ravello SA, Italy", 40.6465, 14.6128, "+39-089-857-459", "https://www.villacimbrone.com", "₹45,000–1,10,000/night", "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=700&q=80"),
            HotelEntry("AML004", "Hotel Santa Caterina Amalfi", "Hotel", 5, "Via Mauro Comite, 9, 84011 Amalfi SA, Italy", 40.6344, 14.6033, "+39-089-871-012", "https://www.hotelsantacaterina.it", "₹40,000–1,00,000/night", "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=700&q=80"),
            HotelEntry("AML005", "Monastero Santa Rosa Hotel & Spa", "Hotel", 5, "Via Roma, 2, 84010 Conca dei Marini SA, Italy", 40.6158, 14.5534, "+39-089-832-1199", "https://www.monasterosantarosa.com", "₹48,000–1,20,000/night", "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=700&q=80"),
            HotelEntry("AML006", "Hotel Luna Convento Amalfi", "Hotel", 4, "Via Pantaleone Comite 33, 84011 Amalfi SA, Italy", 40.6338, 14.6041, "+39-089-871-002", "https://www.lunahotel.it", "₹18,000–38,000/night", "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80"),
            HotelEntry("AML007", "Hotel Villa Franca Positano", "Hotel", 4, "Viale Pasitea, 318, 84017 Positano SA, Italy", 40.6274, 14.4856, "+39-089-875-655", "https://www.villafrancahotel.it", "₹22,000–48,000/night", "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=700&q=80"),
            HotelEntry("AML008", "Miramare Sea Resort Praiano", "Hotel", 3, "Via Rezzola, 27, 84010 Praiano SA, Italy", 40.6137, 14.5325, "+39-089-874-024", "https://www.miramare-praiano.it", "₹12,000–25,000/night", "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80"),
            HotelEntry("AML009", "Hotel Il Saraceno", "Hotel", 3, "Via Cunfictelle 47, 84011 Amalfi SA, Italy", 40.6351, 14.6052, "+39-089-831-148", "https://www.saraceno.it", "₹10,000–20,000/night", "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=700&q=80"),
            HotelEntry("AML010", "Albergo Sant'Alfonso Ravello", "Guest House", null, "Via dell'Annunziata, 18, 84010 Ravello SA, Italy", 40.6491, 14.6098, "+39-089-857-190", null, "₹6,000–12,000/night", "https://images.unsplash.com/photo-1509660933844-6910e12765a0?w=700&q=80"),
            HotelEntry("AML011", "Hotel Residence Positano", "Hotel", 3, "Viale Pasitea, 40, 84017 Positano SA, Italy", 40.6308, 14.4864, "+39-089-875-600", "https://www.hotelresidencepositano.it", "₹8,500–17,000/night", "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?w=700&q=80"),
            HotelEntry("AML012", "Palazzo Avino Ravello", "Hotel", 5, "Via San Giovanni del Toro, 28, 84010 Ravello SA, Italy", 40.6478, 14.6124, "+39-089-818-181", "https://www.palazzoavino.com", "₹50,000–1,30,000/night", "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=700&q=80"),
        ))
    }
}
