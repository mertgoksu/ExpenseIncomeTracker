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
import com.mertg.gelirgidertakip.model.GelirModel
import com.mertg.gelirgidertakip.ui.theme.CustomTuruncu
import com.mertg.gelirgidertakip.ui.theme.CustomYesil
import com.mertg.gelirgidertakip.viewmodel.GelirGiderViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GelirItem(gelir: GelirModel, viewModel: GelirGiderViewModel, yuzde: Float? = null) {
    val icon = when (gelir.category) {
        "Maaş" -> Icons.Default.Money
        "Yatırımlar" -> Icons.Default.TrendingUp
        "Part Time/Ek İş" -> Icons.Default.Work
        "Cashback/Bonus" -> Icons.Default.CardGiftcard
        "Alınan Borç" -> Icons.Default.AttachMoney
        "Diğer" -> Icons.Default.HelpOutline
        else -> Icons.Default.Money
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
                viewModel.gelirSil(gelir)
                showOptionsDialog = false
            }
        )
    }

    if (showEditDialog) {
        EditDialog(
            initialTitle = gelir.title,
            initialAmount = gelir.amount,
            prefix = "+",
            onDismiss = { showEditDialog = false },
            onSave = { title, amount ->
                viewModel.gelirGuncelle(gelir, title, gelir.category, amount)
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
                    contentDescription = "Gelir Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center.takeIf { yuzde == null } ?: Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = gelir.title,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    if (yuzde == null) {
                        Text(
                            text = gelir.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            if (yuzde == null) {
                Text(
                    text = "${gelir.amount}₺",
                    fontSize = 18.sp,
                    color = CustomYesil,
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