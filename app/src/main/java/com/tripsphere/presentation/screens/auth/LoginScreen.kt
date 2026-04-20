package com.tripsphere.presentation.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.tripsphere.BuildConfig
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.AuthState
import com.tripsphere.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onContinueAsGuest: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var googleError by remember { mutableStateOf<String?>(null) }

    // Google Sign-In intent launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    viewModel.loginWithGoogle(idToken)
                } else {
                    googleError = "Google sign-in returned no token. Ensure Google Sign-In is enabled in Firebase Console."
                }
            } catch (e: ApiException) {
                googleError = "Google sign-in failed (code ${e.statusCode}). Add SHA-1/SHA-256 fingerprints in Firebase Console."
            }
        }
    }

    fun launchGoogleSignIn() {
        val webClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        if (webClientId.isBlank()) {
            googleError = "Google Sign-In not configured. Set GOOGLE_WEB_CLIENT_ID in app/build.gradle.kts."
            return
        }
        googleError = null
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .requestProfile()
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        // Sign out first so the account picker always shows
        client.signOut().addOnCompleteListener {
            googleSignInLauncher.launch(client.signInIntent)
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full-screen trip scene background
        TripSceneBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // App logo
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TravelExplore,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "TripSphere",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(Modifier.height(10.dp))

            // Destination badges row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AuthDestBadge(Icons.Default.LocationOn, "Paris")
                AuthDestBadge(Icons.Default.BeachAccess, "Bali")
                AuthDestBadge(Icons.Default.Landscape, "Tokyo")
            }

            Spacer(Modifier.height(20.dp))

            // Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Log in to plan your next adventure",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    // Google Sign-In button
                    OutlinedButton(
                        onClick = { launchGoogleSignIn() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp, MaterialTheme.colorScheme.outline
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        enabled = authState !is AuthState.Loading
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Google "G" logo using colored squares as a stand-in
                            GoogleLogo(modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Continue with Google",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Google error message
                    if (googleError != null) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = googleError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = TextHint.copy(alpha = 0.4f))
                        Text("  or sign in with email  ", style = MaterialTheme.typography.bodySmall, color = TextHint)
                        Divider(modifier = Modifier.weight(1f), color = TextHint.copy(alpha = 0.4f))
                    }

                    Spacer(Modifier.height(16.dp))

                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("hello@tripsphere.com") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = TextHint) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TripBlue,
                            unfocusedBorderColor = TextHint.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null, tint = TextHint
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TripBlue,
                            unfocusedBorderColor = TextHint.copy(alpha = 0.5f)
                        )
                    )

                    // Error message
                    if (authState is AuthState.Error) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.bodySmall,
                        color = TripBlue,
                        modifier = Modifier.align(Alignment.End).clickable { }
                    )

                    Spacer(Modifier.height(20.dp))

                    // Login button
                    Button(
                        onClick = { viewModel.login(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TripAccent),
                        enabled = authState !is AuthState.Loading
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Login →", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = TextHint.copy(alpha = 0.4f))
                        Text("  OR  ", style = MaterialTheme.typography.bodySmall, color = TextHint)
                        Divider(modifier = Modifier.weight(1f), color = TextHint.copy(alpha = 0.4f))
                    }

                    Spacer(Modifier.height(16.dp))

                    // Continue as guest
                    OutlinedButton(
                        onClick = onContinueAsGuest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, TextHint.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            "Continue as Guest",
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Sign up link
            val signupText = buildAnnotatedString {
                withStyle(SpanStyle(color = Color.White)) {
                    append("Don't have an account? ")
                }
                withStyle(SpanStyle(color = TripBlue, fontWeight = FontWeight.Bold)) {
                    append("Sign Up")
                }
            }
            Text(
                text = signupText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable(onClick = onNavigateToSignUp)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Shared auth screen background ────────────────────────────────────────────

@Composable
internal fun TripSceneBackground() {
    Box(Modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Sky — deep night fading to warm sunset horizon
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF040810), Color(0xFF08152E), Color(0xFF111F52),
                        Color(0xFF2E1860), Color(0xFF5E2040), Color(0xFF8A3A18),
                        Color(0xFFAA5518)
                    ),
                    startY = 0f, endY = h * 0.74f
                )
            )

            // Stars
            val stars = listOf(
                0.07f to 0.035f, 0.17f to 0.055f, 0.27f to 0.025f,
                0.38f to 0.048f, 0.50f to 0.018f, 0.62f to 0.040f,
                0.73f to 0.028f, 0.84f to 0.052f, 0.93f to 0.038f,
                0.11f to 0.110f, 0.24f to 0.095f, 0.42f to 0.125f,
                0.56f to 0.085f, 0.69f to 0.115f, 0.88f to 0.098f,
                0.04f to 0.185f, 0.33f to 0.165f, 0.51f to 0.200f,
                0.76f to 0.175f, 0.96f to 0.160f,
            )
            stars.forEachIndexed { i, (rx, ry) ->
                val radius = if (i % 3 == 0) 2.4f else if (i % 3 == 1) 1.6f else 1.2f
                val alpha = if (i % 4 == 0) 0.90f else if (i % 4 == 1) 0.70f else 0.55f
                drawCircle(Color.White.copy(alpha = alpha), radius, Offset(w * rx, h * ry))
            }

            // Crescent moon — top right
            drawCircle(Color(0xFFFFF5D0).copy(0.88f), 26f, Offset(w * 0.82f, h * 0.072f))
            drawCircle(Color(0xFF08152E), 22f, Offset(w * 0.845f, h * 0.065f))

            // Horizon sun glow
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        Color(0xFFFF8C42).copy(0.55f),
                        Color(0xFFFF5C1A).copy(0.22f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.74f),
                    radius = h * 0.28f
                ),
                radius = h * 0.28f,
                center = Offset(w * 0.5f, h * 0.74f)
            )

            // Mountain layer 1 — far, blue-indigo tint
            drawPath(Path().apply {
                moveTo(0f, h * 0.57f)
                lineTo(w * 0.10f, h * 0.40f)
                lineTo(w * 0.21f, h * 0.48f)
                lineTo(w * 0.33f, h * 0.33f)
                lineTo(w * 0.45f, h * 0.45f)
                lineTo(w * 0.57f, h * 0.37f)
                lineTo(w * 0.68f, h * 0.47f)
                lineTo(w * 0.80f, h * 0.38f)
                lineTo(w * 0.92f, h * 0.45f)
                lineTo(w, h * 0.41f)
                lineTo(w, h * 0.59f); lineTo(0f, h * 0.59f); close()
            }, Color(0xFF1C2B6A).copy(0.78f))

            // Mountain layer 2 — mid
            drawPath(Path().apply {
                moveTo(0f, h * 0.63f)
                lineTo(w * 0.07f, h * 0.50f)
                lineTo(w * 0.17f, h * 0.57f)
                lineTo(w * 0.29f, h * 0.45f)
                lineTo(w * 0.41f, h * 0.54f)
                lineTo(w * 0.53f, h * 0.47f)
                lineTo(w * 0.65f, h * 0.56f)
                lineTo(w * 0.77f, h * 0.48f)
                lineTo(w * 0.89f, h * 0.55f)
                lineTo(w, h * 0.51f)
                lineTo(w, h * 0.66f); lineTo(0f, h * 0.66f); close()
            }, Color(0xFF0C1834).copy(0.92f))

            // Mountain layer 3 — nearest, darkest
            drawPath(Path().apply {
                moveTo(0f, h * 0.70f)
                lineTo(w * 0.11f, h * 0.58f)
                lineTo(w * 0.22f, h * 0.64f)
                lineTo(w * 0.34f, h * 0.56f)
                lineTo(w * 0.46f, h * 0.63f)
                lineTo(w * 0.58f, h * 0.57f)
                lineTo(w * 0.70f, h * 0.64f)
                lineTo(w * 0.82f, h * 0.58f)
                lineTo(w * 0.94f, h * 0.63f)
                lineTo(w, h * 0.60f)
                lineTo(w, h * 0.72f); lineTo(0f, h * 0.72f); close()
            }, Color(0xFF060B18))

            // Solid dark ground
            drawRect(
                Color(0xFF050912),
                topLeft = Offset(0f, h * 0.71f),
                size = Size(w, h - h * 0.71f)
            )

            // Airplane trail — dashed line upper sky
            drawPath(Path().apply {
                moveTo(w * 0.06f, h * 0.225f)
                lineTo(w * 0.52f, h * 0.085f)
            }, Color.White.copy(0.20f), style = Stroke(
                width = 1.8f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
            ))
            drawCircle(Color.White.copy(0.65f), 3.5f, Offset(w * 0.52f, h * 0.085f))

            // Dark scrim — transparent → opaque so card area is readable
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color(0xFF050912).copy(0.50f),
                        Color(0xFF050912).copy(0.88f),
                        Color(0xFF050912)
                    ),
                    startY = h * 0.20f, endY = h
                )
            )
        }

    }
}

@Composable
internal fun AuthDestBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, Modifier.size(12.dp), tint = Color(0xFF4FC3F7))
            Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Draws the Google "G" logo using four colored arcs represented as a compact Box. */
@Composable
private fun GoogleLogo(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = size.width * 0.12f
            val inset = stroke / 2f
            val rect = androidx.compose.ui.geometry.Rect(inset, inset, size.width - inset, size.height - inset)
            // Blue arc (top-right → bottom-right)
            drawArc(color = Color(0xFF4285F4), startAngle = -45f, sweepAngle = 90f, useCenter = false,
                topLeft = rect.topLeft, size = androidx.compose.ui.geometry.Size(rect.width, rect.height),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
            // Red arc (top-left)
            drawArc(color = Color(0xFFEA4335), startAngle = -135f, sweepAngle = -90f, useCenter = false,
                topLeft = rect.topLeft, size = androidx.compose.ui.geometry.Size(rect.width, rect.height),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
            // Yellow arc (bottom-left)
            drawArc(color = Color(0xFFFBBC05), startAngle = 135f, sweepAngle = 90f, useCenter = false,
                topLeft = rect.topLeft, size = androidx.compose.ui.geometry.Size(rect.width, rect.height),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
            // Green arc (bottom-right)
            drawArc(color = Color(0xFF34A853), startAngle = 45f, sweepAngle = 90f, useCenter = false,
                topLeft = rect.topLeft, size = androidx.compose.ui.geometry.Size(rect.width, rect.height),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
        }
    }
}
