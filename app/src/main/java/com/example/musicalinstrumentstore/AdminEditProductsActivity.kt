package com.example.musicalinstrumentstore

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.adapter.AdminInstrumentsAdapter
import com.example.musicalinstrumentstore.ui.customer.viewModel.ProductsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminEditProductsActivity : AppCompatActivity() {

    private lateinit var searchET: EditText
    private lateinit var instrumentsLV: ListView
    private lateinit var repository: InstrumentsRepository
    private lateinit var adminInstrumentsAdapter: AdminInstrumentsAdapter
    private var instrumentsList = ArrayList<Instrument>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_edit_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = AppDatabase(this)
        repository = InstrumentsRepository(database)
        adminInstrumentsAdapter = AdminInstrumentsAdapter(this, instrumentsList)
        val userRepo = UserRepository(database)


        //observe the user name and change it dynamically
        val sharedPref = getSharedPreferences("cookies", Context.MODE_PRIVATE)
        val userMail = sharedPref.getString("email", "") ?: ""
        val productsViewModel = ProductsViewModel(userRepo)

        val titleTV = findViewById<TextView>(R.id.titleTV)

        productsViewModel.getUserName(userMail)

        productsViewModel.userName.observe(this) { result ->
            result.onSuccess { userName ->
                titleTV.text = "Welcome $userName"
            }
            result.onFailure {
                Toast.makeText(this, "Something went wrong with the user name", Toast.LENGTH_SHORT)
                    .show()
            }

        }



        instrumentsLV = findViewById(R.id.instrumentsLV)
        searchET = findViewById(R.id.searchET)

        fetchAndPopulate()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            showAddPopUp()
        }

//        searchET.addTextChangedListener {
//            val query = searchET.text.toString()
//            lifecycleScope.launch {
//                if (query.isNotBlank()) {
//                    userRepo.searchUser(query) { results ->
//                        instrumentsList.clear()
//                        instrumentsList.addAll(results)
//                        adminInstrumentsAdapter.notifyDataSetChanged()
//                    }
//                }
//            }
//
//        }


    }

    private fun fetchAndPopulate() {
        lifecycleScope.launch {
            val instruments = withContext(Dispatchers.IO) {
                repository.fetchAllInstruments()
            }
            instrumentsList.clear()
            instrumentsList.addAll(instruments)
            adminInstrumentsAdapter.notifyDataSetChanged()
            instrumentsLV.adapter = adminInstrumentsAdapter
        }
    }

    private fun showAddPopUp() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.admin_product_create, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val titleET = popupView.findViewById<EditText>(R.id.titleET)
        val brandET = popupView.findViewById<EditText>(R.id.brandET)
        val modelET = popupView.findViewById<EditText>(R.id.modelET)
        val descriptionET = popupView.findViewById<EditText>(R.id.descriptionET)
        val costET = popupView.findViewById<EditText>(R.id.costET)
        val stockET = popupView.findViewById<EditText>(R.id.stockET)
        val addItemBtn = popupView.findViewById<Button>(R.id.addItemBtn)

        addItemBtn.setOnClickListener {
            val title = titleET.text.toString()
            val brand = brandET.text.toString()
            val model = modelET.text.toString()
            val desc = descriptionET.text.toString()
            val cost = costET.text.toString().toFloat()
            val stock = stockET.text.toString().toInt()

            lifecycleScope.launch {
                val instrument = Instrument(
                    id = 0, title = title, brand = brand, model = model,
                    description = desc, cost = cost, stock = stock, itype = "String_Instrument"
                )
                try {
                    repository.addInstrument(instrument)
                    fetchAndPopulate()
                    popupWindow.dismiss()
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(
                        this@AdminEditProductsActivity,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        popupWindow.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.popup_background
            )
        )
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

    }


}