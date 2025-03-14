package com.example.musicalinstrumentstore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminUsersAdapter(val context: Context, private val users: ArrayList<User>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.admin_user_view, parent, false)

        val emailTV = view.findViewById<TextView>(R.id.emailTV)
        val nameTV = view.findViewById<TextView>(R.id.nameTV)
        val surnameTV = view.findViewById<TextView>(R.id.surnameTV)
        val addressTV = view.findViewById<TextView>(R.id.addressTV)
        val phoneTV = view.findViewById<TextView>(R.id.phoneTV)
        val roleTV = view.findViewById<TextView>(R.id.roleTV)
        val deleteUserBtn = view.findViewById<Button>(R.id.deleteUser)
        val makeEmployee = view.findViewById<Button>(R.id.makeEmployee)
        val detailsBtn = view.findViewById<Button>(R.id.detailsBtn)

        emailTV.text = "Email: ${users[position].email}"
        nameTV.text = "Name: ${users[position].name}"
        surnameTV.text = "Surname: ${users[position].surname}"
        addressTV.text = "Address: ${users[position].address}"
        phoneTV.text = "Phone: ${users[position].phone}"
        roleTV.text = "Role : ${users[position].role.toString().lowercase()}"


        deleteUserBtn.setOnClickListener {
            delete(position)
        }

        makeEmployee.setOnClickListener {
            updateUserRole(position)
        }
        detailsBtn.setOnClickListener {

        }


        return view
    }

    private fun delete(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val userToDelete = users[position]
            val db = AppDatabase(context)
            val userRepo = UserRepository(db)
            withContext(Dispatchers.Main) {
            if (users[position].role == UserRole.ADMIN) {
                Toast.makeText(context, "Admin user cannot be deleted", Toast.LENGTH_SHORT).show()
            } else {

                    userRepo.deleteUser(userToDelete.id)
                    users.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateUserRole(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase(context)
            val userRepo = UserRepository(db)
            withContext(Dispatchers.Main){
            if (users[position].role == UserRole.ADMIN || users[position].role == UserRole.EMPLOYEE) {
                Toast.makeText(
                    context,
                    "User cannot be changed to employee as the user already has admin rights",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                    userRepo.updateUserRole(users[position].id,UserRole.EMPLOYEE)
                    notifyDataSetChanged()
                    Toast.makeText(context,"User has been changed to Employee",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}