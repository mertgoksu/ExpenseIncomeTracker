package com.mertg.gelirgidertakip.model

data class GiderModel(
    val title: String = "",
    val category: String = "",
    val amount: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "Gider",
    val date: String = "" // Yeni tarih alanı
)
