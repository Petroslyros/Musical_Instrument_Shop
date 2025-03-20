package com.example.musicalinstrumentstore.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.data.repository.PurchasesRepository
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.databinding.ActivityCheckOutBinding
import com.example.musicalinstrumentstore.ui.adapter.CheckOutAdapter
import com.example.musicalinstrumentstore.ui.customer.CustomerActivity.Companion.cart
import com.example.musicalinstrumentstore.ui.customer.viewModel.CheckOutViewModel
import kotlinx.coroutines.launch

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
        val userRepository = UserRepository(database)



        repository = InstrumentsRepository(database)
        purchasesRepository = PurchasesRepository(database)

        checkOutAdapter = CheckOutAdapter(this, instrumentsList, checkOutViewModel)
        binding.productsLV.adapter = checkOutAdapter

        checkOutViewModel.calculateTotalCost(instrumentsList)
        checkOutViewModel.totalCost.observe(this) { result ->
            binding.totalCostTV.text = "$result $"
        }
        binding.goBackBtn.setOnClickListener {
            instrumentsList.clear()
            cart.clear()
            checkOutAdapter.notifyDataSetChanged()
            val intent = Intent(this, CustomerActivity::class.java)
            startActivity(intent)
            finish() //ensures current activity is removed from the back stack
        }

        binding.finalizePurchaseBtn.setOnClickListener {
            if(instrumentsList.isEmpty()){
                Toast.makeText(this,"No items in order to finalize purchase",Toast.LENGTH_SHORT).show()
            }
            else{val intent = Intent(this@CheckOutActivity, FinalizePurchaseActivity::class.java)
                intent.putParcelableArrayListExtra("instruments", instrumentsList)
                startActivity(intent)
            }

//            val sharedPref = getSharedPreferences("cookies", Context.MODE_PRIVATE)
//            val userMail = sharedPref.getString("email", "") ?: ""
//
//            lifecycleScope.launch {
//                val user = userRepository.fetchUser(userMail)
//                if (user != null) {
//                    showFinalizePurchasePopup(user)
//                } else {
//                    Toast.makeText(this@CheckOutActivity, "User not found", Toast.LENGTH_SHORT).show()
//                }
//            }
        }


    }
    private fun showFinalizePurchasePopup(user: User) {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_finalize_purchase, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val totalCostTV = popupView.findViewById<TextView>(R.id.totalCostPopupTV)
        val confirmBtn = popupView.findViewById<Button>(R.id.confirmPurchaseBtn)
        val cancelBtn = popupView.findViewById<Button>(R.id.cancelPurchaseBtn)

        // calculate my total cost
        val totalCost = instrumentsList.sumOf { it.quantity * it.instrument.cost.toDouble() }.toFloat()

        totalCostTV.text = "Total Cost: $totalCost $"

        confirmBtn.setOnClickListener {
            lifecycleScope.launch {
                val success = purchasesRepository.finalizePurchase(instrumentsList, user)
                if (success) {
                    Toast.makeText(this@CheckOutActivity, "Purchase Successful!", Toast.LENGTH_LONG).show()


                    val purchasedItems = ArrayList(instrumentsList)

                    instrumentsList.clear()
                    cart.clear()

                    checkOutAdapter.notifyDataSetChanged()
                    popupWindow.dismiss()

                    val intent = Intent(this@CheckOutActivity, FinalizePurchaseActivity::class.java)
                    intent.putParcelableArrayListExtra("instruments", purchasedItems)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CheckOutActivity, "Purchase Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        cancelBtn.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.popup_background))
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }
}
