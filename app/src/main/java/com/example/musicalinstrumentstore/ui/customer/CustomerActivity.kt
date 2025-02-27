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
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.adapter.CustomerInstrumentsAdapter
import com.example.musicalinstrumentstore.ui.customer.viewModel.ProductsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerActivity : AppCompatActivity() {

    private lateinit var searchET: EditText
    private lateinit var instrumentsLV: ListView
    private lateinit var repository: InstrumentsRepository
    private lateinit var customerInstrumentAdapter: CustomerInstrumentsAdapter
    private var instrumentsList = ArrayList<Instrument>()
    private lateinit var checkOutBtn: Button

    companion object{
        var cardList = ArrayList<Instrument>()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val database = AppDatabase(this)
        repository = InstrumentsRepository(database)
        customerInstrumentAdapter = CustomerInstrumentsAdapter(this, instrumentsList)
        val userRepo = UserRepository(database)


        //change the user name dynamically when it's changed
        val sharedPref = getSharedPreferences("cookies", Context.MODE_PRIVATE)
        val userMail = sharedPref.getString("email", "") ?: ""
        val productsViewModel = ProductsViewModel(userRepo)

        val titleTV = findViewById<TextView>(R.id.titleTV)

        productsViewModel.userName.observe(this) { result ->
            result.onSuccess { userName ->
                titleTV.text = "Welcome $userName"
            }
            result.onFailure {
                Toast.makeText(this, "Something went wrong with the user name", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        productsViewModel.getUserName(userMail)


        instrumentsLV = findViewById(R.id.instrumentsLV)
        searchET = findViewById(R.id.searchET)
        checkOutBtn = findViewById(R.id.checkOutBtn)

        fetchAndPopulate()

        checkOutBtn.setOnClickListener {
            val intent = Intent(this, CheckOutActivity::class.java)
            intent.putParcelableArrayListExtra("instruments", cardList)
            startActivity(intent)
        }


    }

    private fun fetchAndPopulate() {
        lifecycleScope.launch {
            val instruments = withContext(Dispatchers.IO) {
                repository.fetchAllInstruments()
            }
            instrumentsList.clear()
            instrumentsList.addAll(instruments)
            customerInstrumentAdapter.notifyDataSetChanged()
            instrumentsLV.adapter = customerInstrumentAdapter
        }
    }




}