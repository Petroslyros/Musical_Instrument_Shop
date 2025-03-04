package com.example.musicalinstrumentstore.ui.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.repository.UserRepository
import com.example.musicalinstrumentstore.databinding.ActivityAdminEditUsersBinding
import com.example.musicalinstrumentstore.ui.adapter.AdminUsersAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminEditUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEditUsersBinding
    private lateinit var userRepository: UserRepository
    private var usersList = ArrayList<User>()
    private lateinit var usersAdapter: AdminUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding
        binding = ActivityAdminEditUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = AppDatabase(this)
        userRepository = UserRepository(db)
        usersAdapter = AdminUsersAdapter(this, usersList)

        binding.usersLV.adapter = usersAdapter

        fetchAndPopulate()

        binding.searchET.addTextChangedListener {
            val query = binding.searchET.text.toString()
            lifecycleScope.launch {
                if (query.isNotBlank()) {
                    usersList.clear()
                    val temp = userRepository.searchUsers(query)
                    temp?.let { usersList.addAll(it) }
                    usersAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun fetchAndPopulate() {
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) {
                userRepository.fetchAllUsers()
            }
            usersList.clear()
            usersList.addAll(users)
            usersAdapter.notifyDataSetChanged()
        }
    }
}
