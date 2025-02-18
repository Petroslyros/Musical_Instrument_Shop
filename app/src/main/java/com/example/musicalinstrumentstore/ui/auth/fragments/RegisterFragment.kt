package com.example.musicalinstrumentstore.ui.auth.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.ui.auth.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_register, container, false)


        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)
        nameET = view.findViewById(R.id.nameET)
        surnameET = view.findViewById(R.id.surnameET)
        phoneET = view.findViewById(R.id.phoneET)
        addressET = view.findViewById(R.id.addressET)
        registerBtn = view.findViewById(R.id.registerBtn)

//        val database = AppDatabase.getInstance(this)
//        val repository = UserRepository(database)
//        viewModel = RegisterViewModel(repository)
        registerBtn.setOnClickListener {

        }



        return view
    }

    private fun createUser(): User {
        return User(
            email = emailET.text.toString(),
            passWord = passwordET.text.toString(),
            name = nameET.text.toString(),
            surname = surnameET.text.toString(),
            phone = phoneET.text.toString(),
            address = addressET.text.toString(),
            id = 0,
            role = UserRole.CUSTOMER
        )
    }
}

