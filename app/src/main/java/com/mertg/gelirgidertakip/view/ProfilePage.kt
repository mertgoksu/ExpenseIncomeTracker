package com.mertg.gelirgidertakip.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mertg.gelirgidertakip.MainActivity
import com.mertg.gelirgidertakip.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(navController: NavController) {
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser
    val email = currentUser?.email ?: "Bilinmiyor"
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = email, fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 8.dp),
            onClick = {
                Toast.makeText(context, "Çok Yakında Google Play'de", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Tavsiye et",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Arkadaşlarına Tavsiye Et", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 8.dp),
            onClick = {
                Toast.makeText(context, "Çok Yakında Google Play'de", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Oyla",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Uygulamayı Oyla", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 8.dp),
            onClick = {
                coroutineScope.launch {
                    auth.signOut()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                }
            }
        ) {
            Text(text = "Çıkış Yap", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(10.dp))
        }
    }
}

@Preview
@Composable
private fun ProfilePrev() {
    ProfilePage(navController = rememberNavController())
}
