package com.mertg.gelirgidertakip.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mertg.gelirgidertakip.R
import com.mertg.gelirgidertakip.navigation.Screen
import com.mertg.gelirgidertakip.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.gelir_gider_takip_logo),
            contentDescription = "Gelir Gider Takip Logo"
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "Giriş Yap", fontSize = 16.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Gray,
                cursorColor = Color.Black,
                errorCursorColor = Color.Red,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Gray,
                disabledIndicatorColor = Color.LightGray,
                errorIndicatorColor = Color.Red,
                disabledLeadingIconColor = Color.LightGray,
                errorLeadingIconColor = Color.Red,
                disabledTrailingIconColor = Color.LightGray,
                errorTrailingIconColor = Color.Red,
                focusedLabelColor = Color.Blue,
                unfocusedLabelColor = Color.Gray,
                disabledLabelColor = Color.LightGray,
                errorLabelColor = Color.Red,
                disabledPlaceholderColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = email,
            onValueChange = {
                email = it
                emailError = email.isBlank()
            },
            label = { Text(text = "Email") },
            isError = emailError
        )
        if (emailError) {
            Text(text = "Email boş bırakılamaz", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Gray,
                cursorColor = Color.Black,
                errorCursorColor = Color.Red,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Gray,
                disabledIndicatorColor = Color.LightGray,
                errorIndicatorColor = Color.Red,
                disabledLeadingIconColor = Color.LightGray,
                errorLeadingIconColor = Color.Red,
                disabledTrailingIconColor = Color.LightGray,
                errorTrailingIconColor = Color.Red,
                focusedLabelColor = Color.Blue,
                unfocusedLabelColor = Color.Gray,
                disabledLabelColor = Color.LightGray,
                errorLabelColor = Color.Red,
                disabledPlaceholderColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = password,
            onValueChange = {
                password = it
                passwordError = password.isBlank()
            },
            label = { Text(text = "Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError
        )
        if (passwordError) {
            Text(text = "Şifre boş bırakılamaz", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.login(email, password)
                } else {
                    if (email.isBlank()) emailError = true
                    if (password.isBlank()) passwordError = true
                }
            }
        ) {
            Text(text = "Giriş Yap")
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onClick = { navController.navigate(Screen.RegisterPage.route) }
        ) {
            Text(text = "Kayıt Ol")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (authState) {
            is AuthViewModel.AuthState.Loading -> Text("Yükleniyor...")
            is AuthViewModel.AuthState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.MainScaffold.route) {
                        popUpTo(Screen.LoginPage.route) { inclusive = true }
                    }
                }
            }
            is AuthViewModel.AuthState.Error -> Text("Hata: ${(authState as AuthViewModel.AuthState.Error).message}")
            else -> {}
        }
    }
}
