package com.mertg.gelirgidertakip.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OptionsDialog(onDismiss: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Seçenekler") },
        text = {
            Column {
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(text = "Düzenle", color = Color.Black)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(Color.LightGray)
                ) {
                    Text(text = "Sil", color = Color.Black)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text(text = "İptal", color = Color.Black)
            }
        }
    )
}

@Composable
fun EditDialog(
    initialTitle: String,
    initialAmount: String,
    prefix: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var amount by remember { mutableStateOf(initialAmount) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Düzenle") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") }
                )
                TextField(
                    value = amount,
                    onValueChange = {
                        if (!it.startsWith(prefix)) {
                            amount = prefix + it.filter { char -> char.isDigit() }
                        } else if (it == prefix) {
                            amount = it
                        } else if (it.length > 1 && it.drop(1).all { char -> char.isDigit() }) {
                            val amountWithoutPrefix = it.drop(1)
                            if (!amountWithoutPrefix.startsWith("0")) {
                                amount = it
                            }
                        }
                    },
                    label = { Text("Miktar") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (amount.isBlank() || amount == prefix) {
                        amount = prefix + "0"
                    }
                    onSave(title, amount)
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text("Kaydet", color = Color.Black)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text("İptal", color = Color.Black)
            }
        }
    )
}
