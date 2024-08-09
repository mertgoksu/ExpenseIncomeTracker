package com.mertg.gelirgidertakip.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.font.FontWeight
import com.mertg.gelirgidertakip.model.GiderModel
import com.mertg.gelirgidertakip.ui.theme.CustomKirmizi
import com.mertg.gelirgidertakip.ui.theme.CustomTuruncu
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GiderItem(gider: GiderModel, viewModel: GelirGiderViewModel, yuzde: Float? = null) {
    val icon = when (gider.category) {
        "Alışveriş" -> Icons.Default.ShoppingCart
        "Eğlence" -> Icons.Default.VideogameAsset
        "Eğitim" -> Icons.Default.School
        "Yiyecek" -> Icons.Default.Fastfood
        "Ulaşım" -> Icons.Default.DirectionsCar
        "Kişisel Bakım" -> Icons.Default.Spa
        "Sağlık" -> Icons.Default.LocalHospital
        "Spor" -> Icons.Default.FitnessCenter
        "Tamirat/Tadilat" -> Icons.Default.Build
        "Yatırımlar" -> Icons.Default.TrendingUp
        "Verilen Borç" -> Icons.Default.MoneyOff
        "Hediye" -> Icons.Default.CardGiftcard
        "Bağış" -> Icons.Default.Favorite
        "Diğer" -> Icons.Default.HelpOutline
        else -> Icons.Default.ShoppingCart
    }

    var showOptionsDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    if (showOptionsDialog) {
        OptionsDialog(
            onDismiss = { showOptionsDialog = false },
            onEdit = {
                showOptionsDialog = false
                showEditDialog = true
            },
            onDelete = {
                viewModel.giderSil(gider)
                showOptionsDialog = false
            }
        )
    }

    if (showEditDialog) {
        EditDialog(
            initialTitle = gider.title,
            initialAmount = gider.amount,
            prefix = "-",
            onDismiss = { showEditDialog = false },
            onSave = { title, amount ->
                viewModel.giderGuncelle(gider, title, gider.category, amount)
                showEditDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { /* Normal tıklama işlemi */ },
                onLongClick = { if (yuzde == null) showOptionsDialog = true }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically.takeIf { yuzde == null } ?: Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically.takeIf { yuzde == null } ?: Alignment.Top,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Gider Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center.takeIf { yuzde == null } ?: Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = gider.title,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    if (yuzde == null) {
                        Text(
                            text = gider.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            if (yuzde == null) {
                Text(
                    text = "${gider.amount}₺",
                    fontSize = 18.sp,
                    color = CustomKirmizi,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "%.2f%%".format(yuzde),
                    fontSize = 18.sp,
                    color = CustomTuruncu,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}