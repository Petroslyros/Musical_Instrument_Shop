package com.example.musicalinstrumentstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.auth.fragments.LoginFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = AppDatabase.getInstance(this)
        userRepository = UserRepository(db)

        val loginFragment = LoginFragment()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.mainFragmentContainer,loginFragment)
        transaction.commit()



    }


    //testing user
    private fun testRegister(){
        lifecycleScope.launch {
            try {
                val testUser = User(
                    id = 0,
                    email = "test@test.com",
                    name = "test",
                    surname = "test",
                    phone = "123123",
                    address = "testAddress",
                    passWord = "testPassword",
                    role = "CUSTOMER"
                )
                val result = userRepository.registerUser(testUser)
                result.onSuccess { user ->
                    Log.d("TESTING","user : $user")
                }.onFailure {
                    Log.d("TESTING","something went wrong")
                }
            }
            catch (e: Exception){
                Log.d("testing","some exception")
            }

        }
    }
}