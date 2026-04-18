package com.tripsphere.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object DestinationDetail : Screen("destination/{destinationId}") {
        fun createRoute(id: Int) = "destination/$id"
    }
    object CreateTrip : Screen("create_trip?destination={destination}&imageUrl={imageUrl}") {
        fun createRoute(destination: String = "", imageUrl: String = "") =
            "create_trip?destination=${destination}&imageUrl=${imageUrl}"
    }
    object TripWorkspace : Screen("workspace/{destination}/{startDate}/{endDate}/{budget}/{imageUrl}") {
        fun createRoute(destination: String, startDate: String, endDate: String, budget: Double, imageUrl: String) =
            "workspace/$destination/$startDate/$endDate/$budget/${java.net.URLEncoder.encode(imageUrl, "UTF-8")}"
    }
    object TripOverview : Screen("overview/{tripId}") {
        fun createRoute(tripId: Long) = "overview/$tripId"
    }
    object MyTrips : Screen("my_trips")
    object ActiveTrip : Screen("active_trip/{tripId}") {
        fun createRoute(tripId: Long) = "active_trip/$tripId"
    }
    object Profile : Screen("profile")
}

val bottomNavScreens = listOf(Screen.Home, Screen.Explore, Screen.MyTrips, Screen.Profile)
