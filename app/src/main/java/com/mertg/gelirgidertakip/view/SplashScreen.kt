package com.mertg.gelirgidertakip.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.mertg.gelirgidertakip.R
import com.mertg.gelirgidertakip.navigation.Screen
import com.mertg.gelirgidertakip.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.authState

    LaunchedEffect(authState) {
        delay(2000) // 2 saniye gecikme
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                navController.navigate(Screen.MainScaffold.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
            }
            else -> {
                navController.navigate(Screen.LoginPage.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.gelir_gider_takip_logo),
            contentDescription = "Splash Logo"
        )
    }
}
