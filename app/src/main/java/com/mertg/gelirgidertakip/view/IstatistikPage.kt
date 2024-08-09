package com.mertg.gelirgidertakip.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mertg.gelirgidertakip.items.GelirItem
import com.mertg.gelirgidertakip.items.GiderItem
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.model.GiderModel
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel

@Composable
fun IstatistikPage(navController: NavController, viewModel: GelirGiderViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(4f)
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(text = "Gelir İstatistikleri", fontSize = 24.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(viewModel.kategoriYuzdeleri) { (kategori, yuzde) ->
                        val gelir = GelirModel(title = kategori, category = kategori, amount = "", timestamp = 0)
                        GelirItem(gelir, viewModel,yuzde)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .weight(4f)
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                Text(text = "Gider İstatistikleri", fontSize = 24.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(viewModel.giderKategoriYuzdeleri) { (kategori, yuzde) ->
                        val gider = GiderModel(title = kategori, category = kategori, amount = "", timestamp = 0)
                        GiderItem(gider,viewModel, yuzde)
                    }
                }
            }
        }
    }
}
