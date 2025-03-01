package com.example.musicalinstrumentstore.data.repository

import android.content.ContentValues
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InstrumentsRepository(val database: AppDatabase) {

    suspend fun addInstrument(instrument: Instrument): Result<Instrument>{

        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase

            val values = ContentValues().apply {
                put("title", instrument.title)
                put("brand", instrument.brand)
                put("model", instrument.model)
                put("itype", instrument.itype)
                put("description", instrument.description)
                put("cost", instrument.cost)
                put("stock", instrument.stock)
            }

            val id = db.insert("instruments", null, values)

            if (id != -1L) {
                Result.success(instrument)
            } else {
                Result.failure(Exception("Registration failed, user already exists"))
            }
        }
    }
    suspend fun updateInstrument(id: Int, instrument: Instrument): Int {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase

            val values = ContentValues().apply {
                put("title", instrument.title)
                put("brand", instrument.brand)
                put("model", instrument.model)
                put("itype", instrument.itype)
                put("description", instrument.description)
                put("cost", instrument.cost)
                put("stock", instrument.stock)
            }
            db.update("instruments", values, "id = ?", arrayOf(id.toString()))
        }
    }

    suspend fun searchInstruments(query: String): ArrayList<Instrument> {
        val instrumentList = ArrayList<Instrument>()
        return withContext(Dispatchers.IO) {
            val db = database.readableDatabase
            val cursor = db.query(
                "instruments",
                null, "title like ?",
                arrayOf("%$query%"), null, null, null
            )

            cursor.use {
                while (cursor.moveToNext()) {
                    instrumentList.add(
                        Instrument(
                            id = it.getInt(it.getColumnIndexOrThrow("id")),
                            title = it.getString(it.getColumnIndexOrThrow("title")),
                            brand = it.getString(it.getColumnIndexOrThrow("brand")),
                            model = it.getString(it.getColumnIndexOrThrow("model")),
                            itype = it.getString(it.getColumnIndexOrThrow("itype")),
                            description = it.getString(it.getColumnIndexOrThrow("description")),
                            cost = it.getFloat(it.getColumnIndexOrThrow("cost")),
                            stock = it.getInt(it.getColumnIndexOrThrow("stock"))
                        )
                    )
                }
            }
            instrumentList
        }
    }




    suspend fun fetchAllInstruments(): ArrayList<Instrument> {
        val instruments = ArrayList<Instrument>()
        return withContext(Dispatchers.IO){
            val db = database.readableDatabase
            val cursor = db.query(
                "instruments",
                null,
                null,
                null,
                null, null, null
            )

            cursor.use {
                while (it.moveToNext()) {
                    instruments.add(
                        Instrument(
                            id = it.getInt(it.getColumnIndexOrThrow("id")),
                            title = it.getString(it.getColumnIndexOrThrow("title")),
                            brand = it.getString(it.getColumnIndexOrThrow("brand")),
                            model = it.getString(it.getColumnIndexOrThrow("model")),
                            itype = it.getString(it.getColumnIndexOrThrow("itype")),
                            description = it.getString(it.getColumnIndexOrThrow("description")),
                            cost = it.getFloat(it.getColumnIndexOrThrow("cost")),
                            stock = it.getInt(it.getColumnIndexOrThrow("stock"))
                        )
                    )
                }
            }
            instruments
        }

    }
    suspend fun deleteInstrument(id: Int): Int {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase
            db.delete(
                "instruments",
                "id = ?",
                arrayOf(id.toString())
            )
        }
    }


}