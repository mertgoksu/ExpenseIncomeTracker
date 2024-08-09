package com.mertg.gelirgidertakip.model

data class GelirModel(
    val title: String = "",
    val category: String = "",
    val amount: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "Gelir",
    val date: String = "" // Yeni tarih alanı
)