package com.example.musicalinstrumentstore.data.repository

import android.content.ContentValues
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository class responsible for handling user-related database operations
class UserRepository(private val database: AppDatabase) {

    suspend fun registerUser(user: User): Result<User> {
        // Dispatchers.IO is used for operations that involve disk or network I/O, ensuring they don't block the main thread
        // withContext switches execution to the specified dispatcher and runs the code inside it sequentially (not in parallel)
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase

            // Prepare user data for insertion into the database
            val values = ContentValues().apply {
                put("email", user.email)
                put("name", user.name)
                put("surname", user.surname)
                put("phone", user.phone)
                put("address", user.address)
                put("password", user.passWord) // Storing password in plain text is insecure; consider hashing it
                put("role", "CUSTOMER")
            }

            // Insert the user into the "users" table; returns the new row ID or -1 if the insertion failed
            val id = db.insert("users", null, values)

            if (id != -1L) { // If insertion was successful, return success with user data
                Result.success(user)
            } else { // If insertion failed, return failure with an exception
                Result.failure(Exception("Registration failed, user already exists"))
            }
        }
    }

    suspend fun loginUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO) { // Ensure database operations run on the IO thread
            val db = database.writableDatabase

            // Query the "users" table to check if the provided email and password match an existing user
            val cursor = db.query(
                "users",
                null,
                "email = ? and password = ?", // SQL WHERE condition
                arrayOf(email, password), // Arguments replacing the '?' placeholders
                null, null, null
            )

            cursor.use {
                // Move cursor to the first result; if it exists, retrieve user data
                if (it.moveToFirst()) {
                    User(
                        id = it.getInt(it.getColumnIndexOrThrow("id")), // Throws an error if column is missing
                        email = it.getString(it.getColumnIndexOrThrow("email")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        surname = it.getString(it.getColumnIndexOrThrow("surname")),
                        phone = it.getString(it.getColumnIndexOrThrow("phone")),
                        address = it.getString(it.getColumnIndexOrThrow("address")),
                        passWord = it.getString(it.getColumnIndexOrThrow("password")),
                        role = it.getString(it.getColumnIndexOrThrow("role")),
                    )
                } else null // If no matching user is found, return null
            }
        }
    }


}