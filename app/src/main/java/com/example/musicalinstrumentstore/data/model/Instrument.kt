package com.example.musicalinstrumentstore.data.model

data class Instrument(
    val id: Int,
    val title: String,
    val brand: String,
    val model: String,
    val itype: String,
    val description: String,
    val cost: Float,
    val stock: Int
)

