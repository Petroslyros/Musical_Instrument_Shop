package com.example.musicalinstrumentstore.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "music_store.db"
        private const val DATABASE_VERSION = 1

        //singleton (this class is only for ONE object
        @Volatile
        private var instance: AppDatabase? = null

        //crete and return the instance
        fun getInstance(context: Context): AppDatabase {
            return instance?: synchronized(this){
                instance?: AppDatabase(context).also { instance = it }
            }
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE users(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT UNIQUE NOT NULL,
            name TEXT NOT NULL,
            surname TEXT NOT NULL,
            phone TEXT,
            address TEXT,
            role TEXT NOT NULL
            )     
            """.trimIndent()
        )
        db?.execSQL(
            """
            CREATE TABLE instruments(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            brand TEXT NOT NULL,
            model TEXT NOT NULL,
            itype TEXT,
            description TEXT,
            cost FLOAT NOT NULL
            )     
            """.trimIndent()
        )
        db?.execSQL(
            """
            CREATE TABLE purchases(
            uid INTEGER,
            iid INTEGER,
            purdate TEXT NOT NULL,
            quantity INTEGER NOT NULL,
            total_cost REAL NOT NULL,
            FOREIGN KEY (uid) REFERENCES user(iid),
            FOREIGN KEY(iid) REFERENCES instruments(iid),
            PRIMARY KEY (uid, iid, purdate)
            )     
            """.trimIndent()
        )

        db?.execSQL(
            """
                INSERT INTO users(email, name, surname, phone, address, role)
                VALUES('admin@music.com','admin','admin','','','ADMIN')
            """.trimIndent()
        )



    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}