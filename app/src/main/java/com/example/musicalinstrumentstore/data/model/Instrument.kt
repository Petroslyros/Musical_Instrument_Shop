package com.example.musicalinstrumentstore.data.model

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

@Parcelize
data class Instrument(
    val id: Int,
    val title: String,
    val brand: String,
    val model: String,
    val itype: String,
    val description: String,
    val cost: Float,
    val stock: Int
) : Parcelable

