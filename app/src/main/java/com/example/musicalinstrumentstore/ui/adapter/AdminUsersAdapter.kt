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
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.model.UserRole

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
        val deleteUserBtn = view.findViewById<Button>(R.id.deleteUser)
        val makeEmployee = view.findViewById<TextView>(R.id.makeEmployee)

        emailTV.text = "email: ${users[position].email}"
        nameTV.text = "name: ${users[position].name}"
        surnameTV.text = "surname: ${users[position].surname}"
        addressTV.text = "address: ${users[position].address}"
        phoneTV.text = "phone: ${users[position].phone}"

        deleteUserBtn.setOnClickListener {
            if(users[position].role == UserRole.ADMIN){
                Toast.makeText(context,"User cannot be deleted",Toast.LENGTH_SHORT).show()
            }
            else {

            }
        }

        makeEmployee.setOnClickListener {
            if(users[position].role == UserRole.ADMIN || users[position].role == UserRole.EMPLOYEE){
                Toast.makeText(context,"User cannot be changed to employee as the user already has admin rights",Toast.LENGTH_SHORT).show()
            }
            else {

            }
        }


        return view
    }
}