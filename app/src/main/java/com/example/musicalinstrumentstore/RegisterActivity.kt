package com.example.musicalinstrumentstore

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.auth.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel : RegisterViewModel
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var nameET: EditText
    private lateinit var surnameET: EditText
    private lateinit var phoneET: EditText
    private lateinit var addressET: EditText
    private lateinit var registerBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailET = findViewById(R.id.emailET)
        passwordET = findViewById(R.id.passwordET)
        nameET = findViewById(R.id.nameET)
        surnameET = findViewById(R.id.surnameET)
        phoneET = findViewById(R.id.phoneET)
        addressET = findViewById(R.id.addressET)
        registerBtn = findViewById(R.id.registerBtn)

        val database = AppDatabase.getInstance(this)
        val repository = UserRepository(database)
        viewModel = RegisterViewModel(repository)

        registerBtn.setOnClickListener {
            viewModel.register(createUser())


            Log.d("UserCheck","email: ${createUser().email}")
        }



    }

    private fun createUser(): User {
        return User(
            email = emailET.text.toString(),
            passWord = passwordET.text.toString(),
            name = nameET.text.toString(),
            surname = surnameET.text.toString(),
            phone = phoneET.text.toString(),
            address = addressET.text.toString(),
            id = 0,  // SQLite assigns an actual ID
            role = "CUSTOMER"
        )
    }



}