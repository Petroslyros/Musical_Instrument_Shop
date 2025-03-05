package com.example.musicalinstrumentstore.ui.customer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.databinding.ActivityFinalizePurchaseBinding

class FinalizePurchaseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFinalizePurchaseBinding
    private var instrumentsList = ArrayList<CartInstrument>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFinalizePurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        instrumentsList = intent.getParcelableArrayListExtra<CartInstrument>("instruments")?: ArrayList()

        





    }
}