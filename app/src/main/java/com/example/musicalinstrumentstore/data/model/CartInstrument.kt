package com.example.musicalinstrumentstore.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartInstrument(
    var instrument: Instrument,
    var quantity : Int = 0
): Parcelable