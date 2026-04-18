package com.tripsphere.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tripsphere.presentation.screens.auth.LoginScreen
import com.tripsphere.presentation.screens.auth.SignUpScreen
import com.tripsphere.presentation.screens.explore.DestinationDetailScreen
import com.tripsphere.presentation.screens.explore.ExploreScreen
import com.tripsphere.presentation.screens.home.HomeScreen
import com.tripsphere.presentation.screens.mytrips.MyTripsScreen
import com.tripsphere.presentation.screens.onboarding.OnboardingScreen
import com.tripsphere.presentation.screens.onboarding.SplashScreen
import com.tripsphere.presentation.screens.overview.TripOverviewScreen
import com.tripsphere.presentation.screens.profile.ProfileScreen
import com.tripsphere.presentation.screens.trip.ActiveTripScreen
import com.tripsphere.presentation.screens.trip.CreateTripScreen
import com.tripsphere.presentation.screens.trip.TripWorkspaceScreen
import com.tripsphere.presentation.viewmodel.ExploreViewModel
import com.tripsphere.utils.DummyData
import java.net.URLDecoder

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TripSphereNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) +
                    fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) +
                    fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) +
                    fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) +
                    fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onContinueAsGuest = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToExplore = { navController.navigate(Screen.Explore.route) },
                onNavigateToCreateTrip = { navController.navigate(Screen.CreateTrip.createRoute()) },
                onNavigateToMyTrips = { navController.navigate(Screen.MyTrips.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToActiveTrip = { tripId ->
                    navController.navigate(Screen.ActiveTrip.createRoute(tripId))
                },
                onNavigateToOverview = { tripId ->
                    navController.navigate(Screen.TripOverview.createRoute(tripId))
                }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                onDestinationClick = { destinationId ->
                    navController.navigate(Screen.DestinationDetail.createRoute(destinationId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DestinationDetail.route,
            arguments = listOf(navArgument("destinationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val destinationId = backStackEntry.arguments?.getInt("destinationId") ?: return@composable

            // Resolve the destination from ExploreViewModel's live state (contains Wikipedia data),
            // falling back to DummyData for destinations navigated from other screens.
            val exploreEntry = remember(backStackEntry) {
                runCatching { navController.getBackStackEntry(Screen.Explore.route) }.getOrNull()
            }
            val exploreVm: ExploreViewModel? = exploreEntry?.let { hiltViewModel(it) }
            val destination =
                (exploreVm?.uiState?.value?.allDestinations ?: emptyList())
                    .find { it.id == destinationId }
                    ?: DummyData.destinations.find { it.id == destinationId }
                    ?: return@composable

            DestinationDetailScreen(
                destination = destination,
                onPlanTrip = {
                    navController.navigate(
                        Screen.CreateTrip.createRoute(
                            destination = destination.name + ", " + destination.country,
                            imageUrl = destination.imageUrl
                        )
                    )
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CreateTrip.route,
            arguments = listOf(
                navArgument("destination") { type = NavType.StringType; defaultValue = "" },
                navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            CreateTripScreen(
                initialDestination = destination,
                initialImageUrl = imageUrl,
                onNavigateToWorkspace = { dest, start, end, budget, imgUrl ->
                    navController.navigate(
                        Screen.TripWorkspace.createRoute(dest, start, end, budget, imgUrl)
                    )
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TripWorkspace.route,
            arguments = listOf(
                navArgument("destination") { type = NavType.StringType },
                navArgument("startDate") { type = NavType.StringType },
                navArgument("endDate") { type = NavType.StringType },
                navArgument("budget") { type = NavType.FloatType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            val startDate = backStackEntry.arguments?.getString("startDate") ?: ""
            val endDate = backStackEntry.arguments?.getString("endDate") ?: ""
            val budget = backStackEntry.arguments?.getFloat("budget")?.toDouble() ?: 0.0
            val imageUrl = URLDecoder.decode(backStackEntry.arguments?.getString("imageUrl") ?: "", "UTF-8")

            TripWorkspaceScreen(
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                budget = budget,
                imageUrl = imageUrl,
                onNavigateToOverview = { tripId ->
                    navController.navigate(Screen.TripOverview.createRoute(tripId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TripOverview.route,
            arguments = listOf(navArgument("tripId") { type = NavType.LongType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong("tripId") ?: return@composable
            TripOverviewScreen(
                tripId = tripId,
                onNavigateToActiveTrip = {
                    navController.navigate(Screen.ActiveTrip.createRoute(tripId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MyTrips.route) {
            MyTripsScreen(
                onTripClick = { tripId ->
                    navController.navigate(Screen.TripOverview.createRoute(tripId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ActiveTrip.route,
            arguments = listOf(navArgument("tripId") { type = NavType.LongType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong("tripId") ?: return@composable
            ActiveTripScreen(
                tripId = tripId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToMyTrips = { navController.navigate(Screen.MyTrips.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
