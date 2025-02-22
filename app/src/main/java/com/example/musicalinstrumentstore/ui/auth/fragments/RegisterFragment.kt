package com.example.musicalinstrumentstore.ui.auth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.auth.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {


    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var nameET: EditText
    private lateinit var surnameET: EditText
    private lateinit var phoneET: EditText
    private lateinit var addressET: EditText
    private lateinit var registerBtn : Button
    private lateinit var registerViewModel: RegisterViewModel

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

        val database = AppDatabase(requireContext())
        val userRepository = UserRepository(database)
        registerViewModel = RegisterViewModel(userRepository)

        registerViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show()
                backToLogin()
            }.onFailure {
                Toast.makeText(requireContext(),"Registration failed ",Toast.LENGTH_SHORT).show()
            }
        }

        registerBtn.setOnClickListener {
            register()
        }



        return view
    }
    private fun register(){
        val user = createUser()

        if (user.email.isBlank() || user.password.isBlank() || user.name.isBlank() || user.surname.isBlank()) {
            Toast.makeText(requireContext(), "All fields except phone and address are required", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            registerViewModel.register(user)
        }
    }

    private fun createUser(): User {
        return User(
            email = emailET.text.toString(),
            password = passwordET.text.toString(),
            name = nameET.text.toString(),
            surname = surnameET.text.toString(),
            phone = phoneET.text.toString(),
            address = addressET.text.toString(),
            id = 0,
            role = UserRole.CUSTOMER
        )
    }
    private fun backToLogin(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(
            R.id.mainFragmentContainer,
            LoginFragment()) // Replaces LoginFragment with RegisterFragment
        transaction.addToBackStack(null) // Enables back navigation
        transaction.commit()
    }
}

