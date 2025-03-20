package com.example.musicalinstrumentstore.ui.customer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.databinding.ActivityFinalizePurchaseBinding
import com.example.musicalinstrumentstore.ui.adapter.CheckOutAdapter
import com.example.musicalinstrumentstore.ui.adapter.FinalizeAdapter
import com.example.musicalinstrumentstore.ui.customer.viewModel.CheckOutViewModel

class FinalizePurchaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFinalizePurchaseBinding
    private var instrumentsList = ArrayList<CartInstrument>()
    private lateinit var finalizeAdapter: FinalizeAdapter
    private lateinit var repository: InstrumentsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFinalizePurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instrumentsList = intent.getParcelableArrayListExtra("instruments")?: ArrayList()


        finalizeAdapter = FinalizeAdapter(this,instrumentsList)
        binding.productsLV.adapter = finalizeAdapter

        val checkOutViewModel = CheckOutViewModel()
        val database = AppDatabase(this)
        repository = InstrumentsRepository(database)

        checkOutViewModel.calculateTotalCost(instrumentsList)
        checkOutViewModel.totalCost.observe(this) { result ->
            binding.totalCostTV.text = "Price :$result $"
        }

        binding.backToStoreBtn.setOnClickListener {
            instrumentsList.clear()
            CustomerActivity.cart.clear()
            finalizeAdapter.notifyDataSetChanged()
            val intent = Intent(this, CustomerActivity::class.java)
            startActivity(intent)
        }



    }
}