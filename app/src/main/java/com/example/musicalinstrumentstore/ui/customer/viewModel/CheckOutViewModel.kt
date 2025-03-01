package com.example.musicalinstrumentstore.ui.customer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicalinstrumentstore.data.model.Instrument

class CheckOutViewModel : ViewModel() {


    private val _totalCost = MutableLiveData<Float>()
    val totalCost : LiveData<Float> = _totalCost

    private val _quantity = MutableLiveData<Int>()
    val quantity : LiveData<Int> = _quantity

    fun calculateTotalCost(instrumentList : ArrayList<Instrument>){
        var total = 0f
        for(instrument in instrumentList){
            total += instrument.cost
        }
        _totalCost.value = total
    }

     fun displayQuantity(position: Int){

    }
}