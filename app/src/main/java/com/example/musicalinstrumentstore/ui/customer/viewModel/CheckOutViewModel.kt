package com.example.musicalinstrumentstore.ui.customer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.data.model.Instrument

class CheckOutViewModel : ViewModel() {


    private val _totalCost = MutableLiveData<Float>()
    val totalCost : LiveData<Float> = _totalCost



    fun calculateTotalCost(instrumentList : ArrayList<CartInstrument>){
        var total = 0f
        for(instrument in instrumentList){
            total += instrument.instrument.cost
        }
        _totalCost.value = total
    }


}