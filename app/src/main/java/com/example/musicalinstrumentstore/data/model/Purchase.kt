package com.example.musicalinstrumentstore.data.model

data class Purchase(
    val uid: Int,
    val iid: Int,
    val purDate: String,
    val quantity: Int,
    val totalCost: Float
)
