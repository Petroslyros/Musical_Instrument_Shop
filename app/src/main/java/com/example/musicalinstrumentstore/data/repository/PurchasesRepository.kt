package com.example.musicalinstrumentstore.data.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.data.model.Purchase
import com.example.musicalinstrumentstore.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.database.sqlite.transaction

class PurchasesRepository(val database : AppDatabase) {

    suspend fun finalizePurchase(instruments: ArrayList<CartInstrument>, user: User): Boolean {
        return withContext(Dispatchers.IO) {
            val db = database.writableDatabase
            db.transaction() {
                try {
                    for (instrument in instruments) {
                        val values = ContentValues().apply {
                            put("uid", user.id)
                            put("iid", instrument.instrument.id)
                            put("purDate", getCurrentDate())
                            put("quantity", instrument.quantity)
                            put("totalCost", instrument.quantity * instrument.instrument.cost)
                        }
                        val id = insert("purchases", null, values)
                        if (id == -1L) {
                            throw Exception("Purchase failed, try again")
                        }
                    }
                    true // Return true if all purchases succeed
                } catch (e: Exception) {
                    false // Return false if any insertion fails
                } finally {
                }
            }
        }
    }

    suspend fun getAllPurchases(): ArrayList<Purchase> {
        return withContext(Dispatchers.IO) {
            val db = database.readableDatabase
            val purchaseList = ArrayList<Purchase>()

            val query = """
                SELECT p.uid, p.iid, p.purdate, p.quantity, p.total_cost
                FROM purchases p
                INNER JOIN instruments i ON p.iid = i.id
                ORDER BY p.purdate DESC
            """.trimIndent()

            val cursor: Cursor = db.rawQuery(query, null)
            cursor.use {
                while (it.moveToNext()) {
                    val purchase = Purchase(
                        uid = it.getInt(it.getColumnIndexOrThrow("uid")),
                        iid = it.getInt(it.getColumnIndexOrThrow("iid")),
                        purDate = it.getString(it.getColumnIndexOrThrow("purdate")),
                        quantity = it.getInt(it.getColumnIndexOrThrow("quantity")),
                        totalCost = it.getFloat(it.getColumnIndexOrThrow("total_cost"))
                    )
                    purchaseList.add(purchase)
                }
            }
            purchaseList
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }

}