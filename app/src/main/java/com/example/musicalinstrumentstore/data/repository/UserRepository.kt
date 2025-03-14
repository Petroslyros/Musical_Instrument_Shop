package com.example.musicalinstrumentstore.data.repository

import android.content.ContentValues
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repository class responsible for handling user-related database operations
class UserRepository(private val database: AppDatabase) {

    suspend fun registerUser(user: User): Result<User> {
        // Dispatchers.IO  used for operations that involve network calls, ensuring they don't block the main thread
        // withContext switches execution to the specified dispatcher and runs the code inside it sequentially (not in parallel)
        // as we don't know when this function is going to get called
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase

            // Prepare user data for insertion into the database
            val values = ContentValues().apply {
                put("email", user.email)
                put("name", user.name)
                put("surname", user.surname)
                put("phone", user.phone)
                put("address", user.address)
                put("password", user.password) //HASH PASSWORDS LATER
                put("role", "CUSTOMER")
            }

            // Insert the user into the users table, returns the new row ID or -1 if the insertion failed
            val id = db.insert("users", null, values)

            if (id != -1L) {
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed, user already exists"))
            }
        }
    }

    suspend fun updateUserRole(id: Int, newRole: UserRole): Int {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase
            val values = ContentValues().apply {
                put("role", newRole.name)

            }
            db.update("users", values, "id = ?", arrayOf(id.toString()))
        }
    }


    suspend fun loginUser(email: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase

            // Query the "users" table to check if the provided email and password match an existing user
            val cursor = db.query(
                "users",
                null,
                "email = ? AND password = ?", // SQL WHERE condition
                arrayOf(email, password), // Arguments replacing the '?' placeholders
                null, null, null
            )

            cursor.use {
                // Move cursor to the first result, if it exists, retrieve user data
                if (cursor.moveToFirst()) {
                    User(
                        id = it.getInt(it.getColumnIndexOrThrow("id")), // Throws an error if column is missing
                        email = it.getString(it.getColumnIndexOrThrow("email")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        surname = it.getString(it.getColumnIndexOrThrow("surname")),
                        phone = it.getString(it.getColumnIndexOrThrow("phone")),
                        address = it.getString(it.getColumnIndexOrThrow("address")),
                        password = it.getString(it.getColumnIndexOrThrow("password")),
                        role = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("role"))),
                    )
                } else null // If no matching user is found, return null
            }
        }
    }

    suspend fun fetchAllUsers(): ArrayList<User> {
        val users = ArrayList<User>()
        return withContext(Dispatchers.IO) {
            val db = database.readableDatabase
            val cursor = db.query(
                "users",
                null,
                null,
                null,
                null, null, null
            )

            cursor.use {
                while (cursor.moveToNext()) {
                    users.add(
                        User(
                            id = it.getInt(it.getColumnIndexOrThrow("id")), // Throws an error if column is missing
                            email = it.getString(it.getColumnIndexOrThrow("email")),
                            name = it.getString(it.getColumnIndexOrThrow("name")),
                            surname = it.getString(it.getColumnIndexOrThrow("surname")),
                            phone = it.getString(it.getColumnIndexOrThrow("phone")),
                            address = it.getString(it.getColumnIndexOrThrow("address")),
                            password = it.getString(it.getColumnIndexOrThrow("password")),
                            role = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("role"))),
                        )
                    )
                }
            }
            users
        }

    }

    suspend fun fetchUser(email: String): User? {

        return withContext(Dispatchers.IO) {
            val db = database.readableDatabase
            val cursor = db.query(
                "users",
                null,
                "email = ?",
                arrayOf(email),
                null, null, null
            )

            cursor.use {
                if (it.moveToFirst()) {
                    User(
                        id = it.getInt(it.getColumnIndexOrThrow("id")), // Throws an error if column is missing
                        email = it.getString(it.getColumnIndexOrThrow("email")),
                        name = it.getString(it.getColumnIndexOrThrow("name")),
                        surname = it.getString(it.getColumnIndexOrThrow("surname")),
                        phone = it.getString(it.getColumnIndexOrThrow("phone")),
                        address = it.getString(it.getColumnIndexOrThrow("address")),
                        password = it.getString(it.getColumnIndexOrThrow("password")),
                        role = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("role"))),
                    )
                } else null

            }

        }

    }

    suspend fun searchUsers(query: String): ArrayList<User>? {
        val userList = ArrayList<User>()
        return withContext(Dispatchers.IO) {
            val db = database.readableDatabase
            val cursor = db.query(
                "users",
                null, "email like ?",
                arrayOf("%$query%"), null, null, null
            )

            cursor.use {
                while (cursor.moveToNext()) {
                    userList.add(
                        User(
                            id = it.getInt(it.getColumnIndexOrThrow("id")), // Throws an error if column is missing
                            email = it.getString(it.getColumnIndexOrThrow("email")),
                            name = it.getString(it.getColumnIndexOrThrow("name")),
                            surname = it.getString(it.getColumnIndexOrThrow("surname")),
                            phone = it.getString(it.getColumnIndexOrThrow("phone")),
                            address = it.getString(it.getColumnIndexOrThrow("address")),
                            password = it.getString(it.getColumnIndexOrThrow("password")),
                            role = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("role"))),
                        )
                    )
                }
            }
            userList
        }
    }


    suspend fun deleteUser(id: Int): Int {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase
            db.delete(
                "users",
                "id = ?",
                arrayOf(id.toString())
            )
        }
    }

    suspend fun updateUserRoleToAdmin(email: String, newRole: UserRole): Boolean {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase
            val values = ContentValues().apply {
                put("role", newRole.name) // Use .name to store the Enum as a String
            }
            val rowsAffected = db.update("users", values, "email = ?", arrayOf(email))
            rowsAffected > 0  // Return true if update was successful
        }
    }


}