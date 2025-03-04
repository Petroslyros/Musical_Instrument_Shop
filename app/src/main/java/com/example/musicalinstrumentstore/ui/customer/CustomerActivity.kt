package com.example.musicalinstrumentstore.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.databinding.ActivityCustomerBinding
import com.example.musicalinstrumentstore.ui.adapter.CustomerInstrumentsAdapter
import com.example.musicalinstrumentstore.ui.customer.viewModel.ProductsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding
    private lateinit var repository: InstrumentsRepository
    private lateinit var customerInstrumentAdapter: CustomerInstrumentsAdapter
    private var instrumentsList = ArrayList<Instrument>()

    companion object {
        var cartList = ArrayList<Instrument>()
        var cart = ArrayList<CartInstrument>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase(this)
        repository = InstrumentsRepository(database)
        customerInstrumentAdapter = CustomerInstrumentsAdapter(this, instrumentsList)
        val userRepo = UserRepository(database)

        val sharedPref = getSharedPreferences("cookies", Context.MODE_PRIVATE)
        val userMail = sharedPref.getString("email", "") ?: ""
        val productsViewModel = ProductsViewModel(userRepo)

        productsViewModel.userName.observe(this) { result ->
            result.onSuccess { userName -> binding.titleTV.text = "Welcome $userName" }
            result.onFailure {
                Toast.makeText(this, "Something went wrong with the user name", Toast.LENGTH_SHORT).show()
            }
        }
        productsViewModel.getUserName(userMail)

        binding.instrumentsLV.adapter = customerInstrumentAdapter

        binding.checkOutBtn.setOnClickListener {
            if (cart.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckOutActivity::class.java)
                intent.putParcelableArrayListExtra("instruments", cart)
                startActivity(intent)
            }
        }

        binding.searchET.addTextChangedListener {
            val query = binding.searchET.text.toString()
            lifecycleScope.launch {
                if (query.isNotBlank()) {
                    instrumentsList.clear()
                    instrumentsList.addAll(repository.searchInstruments(query))
                    customerInstrumentAdapter.notifyDataSetChanged()
                } else {
                    fetchAndPopulate()
                }
            }
        }

        fetchAndPopulate()
    }

    private fun fetchAndPopulate() {
        lifecycleScope.launch {
            val instruments = withContext(Dispatchers.IO) { repository.fetchAllInstruments() }
            instrumentsList.clear()
            instrumentsList.addAll(instruments)
            customerInstrumentAdapter.notifyDataSetChanged()
        }
    }
}
