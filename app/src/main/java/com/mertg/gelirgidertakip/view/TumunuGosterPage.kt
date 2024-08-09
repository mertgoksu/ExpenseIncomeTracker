package com.mertg.gelirgidertakip.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mertg.gelirgidertakip.items.GelirItem
import com.mertg.gelirgidertakip.items.GiderItem
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.model.GiderModel
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel

@Composable
fun TumunuGosterPage(navController: NavController, viewModel: GelirGiderViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(8f)
                .padding(12.dp)
        ) {
            LazyColumn {
                items(viewModel.finansalListe){ item ->
                    when (item) {
                        is GelirModel -> GelirItem(item,viewModel)
                        is GiderModel -> GiderItem(item,viewModel)
                    }
                }
            }
        }

    }
}