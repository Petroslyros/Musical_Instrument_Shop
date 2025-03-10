package com.example.musicalinstrumentstore.ui.customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.data.repository.PurchasesRepository
import com.example.musicalinstrumentstore.databinding.ActivityCheckOutBinding
import com.example.musicalinstrumentstore.ui.adapter.CheckOutAdapter
import com.example.musicalinstrumentstore.ui.customer.CustomerActivity.Companion.cart
import com.example.musicalinstrumentstore.ui.customer.viewModel.CheckOutViewModel

class CheckOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckOutBinding
    private lateinit var checkOutAdapter: CheckOutAdapter
    private lateinit var repository: InstrumentsRepository
    private var instrumentsList = ArrayList<CartInstrument>()
    private lateinit var purchasesRepository: PurchasesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        instrumentsList = intent.getParcelableArrayListExtra("instruments") ?: ArrayList()
        val checkOutViewModel = CheckOutViewModel()
        val database = AppDatabase(this)
        repository = InstrumentsRepository(database)

        checkOutAdapter = CheckOutAdapter(this, instrumentsList, checkOutViewModel)
        binding.productsLV.adapter = checkOutAdapter

        checkOutViewModel.calculateTotalCost(instrumentsList)
        checkOutViewModel.totalCost.observe(this) { result ->
            binding.totalCostTV.text = "$result $"
        }
        binding.goBackBtn.setOnClickListener {
            instrumentsList.clear()
            checkOutAdapter.notifyDataSetChanged()
            val intent = Intent(this, CustomerActivity::class.java)
            startActivity(intent)
        }

        binding.finalizePurchaseBtn.setOnClickListener {
            val intent = Intent(this, FinalizePurchaseActivity::class.java)
            intent.putParcelableArrayListExtra("instruments", instrumentsList)
            startActivity(intent)
        }


    }
}
