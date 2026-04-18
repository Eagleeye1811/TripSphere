package com.tripsphere

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tripsphere.presentation.components.TripSphereBottomBar
import com.tripsphere.presentation.navigation.Screen
import com.tripsphere.presentation.navigation.TripSphereNavGraph
import com.tripsphere.presentation.ui.theme.TripSphereTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* permissions handled reactively in composables */ }

    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* handled reactively */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Request location permission on start
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            TripSphereTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val bottomBarRoutes = setOf(
                    Screen.Home.route,
                    Screen.Explore.route,
                    Screen.MyTrips.route,
                    Screen.Profile.route
                )
                val showBottomBar = currentRoute in bottomBarRoutes

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                ) { paddingValues ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        TripSphereNavGraph(
                            navController = navController,
                            startDestination = Screen.Splash.route,
                            modifier = Modifier.padding(
                                top = paddingValues.calculateTopPadding(),
                                bottom = 0.dp
                            )
                        )

                        if (showBottomBar) {
                            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                TripSphereBottomBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { screen ->
                                        if (screen == Screen.Home) {
                                            navController.popBackStack(
                                                route = Screen.Home.route,
                                                inclusive = false
                                            )
                                        } else {
                                            navController.navigate(screen.route) {
                                                popUpTo(Screen.Home.route) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
