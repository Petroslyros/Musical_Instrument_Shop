package com.example.musicalinstrumentstore.data.model


data class User(

    val id: Int,
    val email: String,
    val name: String,
    var surname: String,
    val phone: String,
    val passWord : String,
    val address: String,
    val role: UserRole
)


