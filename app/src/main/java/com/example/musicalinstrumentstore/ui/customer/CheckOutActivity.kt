package com.example.musicalinstrumentstore.ui.customer

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.ui.adapter.CheckOutAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckOutActivity : AppCompatActivity() {

    private lateinit var totalCostTV : TextView
    private lateinit var productsLV : ListView
    private lateinit var checkOutAdapter: CheckOutAdapter
    private lateinit var repository: InstrumentsRepository
    private var instrumentsList = ArrayList<Instrument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_out)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         instrumentsList = intent.getParcelableArrayListExtra("instruments")!!

        val database = AppDatabase(this)
        totalCostTV = findViewById(R.id.totalCostTV)
        productsLV = findViewById(R.id.productsLV)
        checkOutAdapter = CheckOutAdapter(this,instrumentsList)
        repository = InstrumentsRepository(database)

        fetchAndPopulate()
        totalCostTV.text = calculateTotalCost().toString()

    }


    private fun fetchAndPopulate() {
        lifecycleScope.launch {
            val instruments = withContext(Dispatchers.IO) {
                repository.fetchAllInstruments()
            }
            instrumentsList.clear()
            instrumentsList.addAll(instruments)
            checkOutAdapter.notifyDataSetChanged()
            productsLV.adapter = checkOutAdapter
        }
    }


    private fun calculateTotalCost() : Float{
        var totalCost = 0f

        for(i in instrumentsList.indices){
            totalCost += instrumentsList[i].cost
        }
        return totalCost
    }
}