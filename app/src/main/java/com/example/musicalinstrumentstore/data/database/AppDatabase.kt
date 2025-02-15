package com.example.musicalinstrumentstore.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// SQLite database helper class for managing the local database.
class AppDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "music_store.db" // Database file name
        private const val DATABASE_VERSION = 1 // Database version for schema management

        // Singleton instance to ensure only one database instance is used throughout the app.
        @Volatile
        private var instance: AppDatabase? = null

        // Returns the singleton instance of the database.
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: AppDatabase(context).also { instance = it }
            }
        }
    }

    // Called when the database is first created.
    override fun onCreate(db: SQLiteDatabase?) {
        // Create users table
        db?.execSQL(
            """
            CREATE TABLE users(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            email TEXT UNIQUE NOT NULL,           
            name TEXT NOT NULL,                   
            surname TEXT NOT NULL,                
            phone TEXT,                           
            address TEXT,                         
            password TEXT NOT NULL,               
            role TEXT NOT NULL                    
            )     
            """.trimIndent()
        )

        // Create instruments table
        db?.execSQL(
            """
            CREATE TABLE instruments(
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            title TEXT NOT NULL,                 
            brand TEXT NOT NULL,                 
            model TEXT NOT NULL,                 
            itype TEXT,                           
            description TEXT,                     
            cost FLOAT NOT NULL,                 
            stock INTEGER NOT NULL DEFAULT 0     
            )     
            """.trimIndent()
        )

        // Create purchases table to track instrument purchases
        db?.execSQL(
            """
            CREATE TABLE purchases(
            uid INTEGER,                          
            iid INTEGER,                         
            purdate TEXT NOT NULL,                
            quantity INTEGER NOT NULL,            
            total_cost FLOAT NOT NULL,            
            FOREIGN KEY (uid) REFERENCES users(id),  -- Link to users table
            FOREIGN KEY(iid) REFERENCES instruments(id), -- Link to instruments table
            PRIMARY KEY (uid, iid, purdate)       -- primary key to track purchases uniquely
            )     
            """.trimIndent()
        )

        // Insert an initial admin user into the users table
        db?.execSQL(
            """
                INSERT INTO users(email, name, surname, phone, address, password, role)
                VALUES('admin@music.com', 'admin', 'admin', '', '', 'admin123', 'ADMIN')
            """.trimIndent()
        )
    }

    // Called when the database needs to be upgraded (version change)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop existing tables to recreate them
        db?.execSQL("DROP TABLE IF EXISTS instruments")
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS purchases")
        onCreate(db) // recreate the database
    }
}
