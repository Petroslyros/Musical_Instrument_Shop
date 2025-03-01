package com.example.musicalinstrumentstore.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicalinstrumentstore.R

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val editUsersBtn = findViewById<Button>(R.id.editUsersBtn)
        val editProductsBtn = findViewById<Button>(R.id.editProductsBtn)


        editUsersBtn.setOnClickListener {
            val intent = Intent(this, AdminEditUsersActivity::class.java)
            startActivity(intent)
        }
        editProductsBtn.setOnClickListener {
            val intent = Intent(this, AdminEditProductsActivity::class.java)
            startActivity(intent)
        }



    }
}