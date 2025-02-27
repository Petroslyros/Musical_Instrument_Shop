package com.example.musicalinstrumentstore

import android.os.Bundle
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.ui.adapter.AdminUsersAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminEditUsersActivity : AppCompatActivity() {

    private lateinit var searchET : EditText
    private lateinit var usersLV : ListView
    private lateinit var userRepository: UserRepository
    private var usersList = ArrayList<User>()
    private lateinit var usersAdapter: AdminUsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_edit_users)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = AppDatabase(this)
        userRepository = UserRepository(db)
        usersAdapter = AdminUsersAdapter(this,usersList)

        usersLV = findViewById(R.id.usersLV)
        searchET = findViewById(R.id.searchET)

        fetchAndPopulate()


    }

    private fun fetchAndPopulate(){
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO){
                userRepository.fetchAllUsers()
            }
            usersList.clear()
            usersList.addAll(users)
            usersAdapter.notifyDataSetChanged()
            usersLV.adapter = usersAdapter
        }
    }



}