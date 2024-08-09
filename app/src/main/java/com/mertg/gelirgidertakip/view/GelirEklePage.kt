package com.mertg.gelirgidertakip.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.navigation.Screen
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel

val categoriesGelir = listOf(
    "Maaş",
    "Yatırımlar",
    "Part Time/Ek İş",
    "Cashback/Bonus",
    "Alınan Borç",
    "Diğer"
)

@Composable
fun GelirEklePage(navController: NavController, viewModel: GelirGiderViewModel) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        // Input fields and labels
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(16.dp)
                .weight(8f),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(text = "Başlık", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                }
                Column(
                    Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        label = {
                            Text("Giriniz", color = Color.Black)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        value = title,
                        onValueChange = { title = it },
                    )
                }
            }

            // Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.weight(1f)
                ){
                    Text(text = "Kategori", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White)
                        .border(1.dp, Color.Gray)
                        .clickable { isDropdownExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectedCategory.ifEmpty { "Seçiniz" },
                            color = if (selectedCategory.isEmpty()) Color.Gray else Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown Arrow")
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        categoriesGelir.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = category, color = Color.Black) },
                                onClick = {
                                    selectedCategory = category
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(text = "Tutar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Column(
                    Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        label = {
                            Text("Giriniz", color = Color.Black)
                        },
                        value = amount,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) {
                                amount = it
                            }
                        }
                    )
                }

            }
        }

        Row( // Gelir ekle - Gider ekle buttons
            Modifier
                .padding(end = 20.dp, start = 20.dp)
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
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
                    onClick = {
                        if (amount.isNotEmpty() && title.isNotEmpty() && selectedCategory.isNotEmpty()) {
                            viewModel.gelirEkle(GelirModel(title, selectedCategory, amount))
                            viewModel.fetchFinancialData()
                            navController.navigate(Screen.MainPage.route)
                        }
                    },
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
private fun GelirEklePreview() {
    GelirEklePage(navController = rememberNavController(),GelirGiderViewModel())
}
