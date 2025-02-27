package com.example.musicalinstrumentstore.ui.auth.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.musicalinstrumentstore.AdminActivity
import com.example.musicalinstrumentstore.EmployeeActivity
import com.example.musicalinstrumentstore.ui.customer.CustomerActivity
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase

import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.auth.viewmodel.LoginViewModel



class LoginFragment : Fragment() {

    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val clickMeText = view.findViewById<TextView>(R.id.clickMeText)
        loginBtn = view.findViewById(R.id.loginBtn)

        val database = AppDatabase(requireContext())
        val userRepository = UserRepository(database)
        val loginViewModel = LoginViewModel(userRepository)

        //attaches an observer that listens for changes in loginResult, Since login happens asynchronously ,
        // the UI updates automatically when the result is available.
        //It also prevents UI from freezing while waiting for a response
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                if (user.role == UserRole.CUSTOMER){
                    // sharedpreference allows the app to remember the logged-in user even after closing the app
                    val sharedPref = activity?.getSharedPreferences("cookies",Context.MODE_PRIVATE)
                    val editor = sharedPref?.edit()
                    editor?.putString("email", user.email)
                    editor?.apply()
                    val intent = Intent(requireContext(), CustomerActivity::class.java)
                    startActivity(intent)

                }
                else if(user.role == UserRole.EMPLOYEE){
                    val intent = Intent(requireContext(),EmployeeActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(requireContext(),AdminActivity::class.java)
                    startActivity(intent)
                }

                Toast.makeText(
                    requireContext(),
                    "Logged in as ${user.email}",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure { exception ->
                Toast.makeText(
                    requireContext(),
                    exception.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        clickMeText.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            transaction.replace(
                R.id.mainFragmentContainer,
                RegisterFragment()) // Replaces LoginFragment with RegisterFragment
            transaction.addToBackStack(null) // Enables back navigation
            transaction.commit()
        }


        loginBtn.setOnClickListener {
            login(view, loginViewModel)
        }


        return view
    }

    private fun login(view: View, loginViewModel: LoginViewModel) {
        val email = view.findViewById<EditText>(R.id.emailET).text.toString()
        val password = view.findViewById<EditText>(R.id.passwordET).text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "All fields need to be completed", Toast.LENGTH_SHORT)
                .show()
            return
        }

        loginViewModel.login(email, password)

    }


}