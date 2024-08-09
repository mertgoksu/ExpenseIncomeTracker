package com.mertg.gelirgidertakip.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mertg.gelirgidertakip.items.GelirItem
import com.mertg.gelirgidertakip.items.GiderItem
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.model.GiderModel
import com.mertg.gelirgidertakip.navigation.Screen
import com.mertg.gelirgidertakip.ui.theme.CustomYesil
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel

@Composable
fun GelirleriGosterPage(navController: NavController, viewModel: GelirGiderViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(6f)
                .padding(12.dp)
        ) {
            LazyColumn {
                items(viewModel.finansalListeFiltered.filterIsInstance<GelirModel>()) { item ->
                    GelirItem(item,viewModel)
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, start = 20.dp)
                .weight(1f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                onClick = {  }
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Gelirler Toplamı: ",
                            fontSize = 15.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "+${viewModel.toplamGelir.value}₺",
                            fontSize = 18.sp,
                            color = CustomYesil,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Row( // Gelir ekle - Gider ekle buttons
            Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, start = 20.dp)
                .weight(2f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 5.dp),
                    onClick = { navController.navigate(Screen.GelirEklePage.route) },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Gelir Ekle",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold

                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 5.dp),
                    onClick = { navController.navigate(Screen.GiderEklePage.route) },
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Gider Ekle",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold

                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun prev() {
    GelirleriGosterPage(navController = rememberNavController(), viewModel = GelirGiderViewModel())
}
