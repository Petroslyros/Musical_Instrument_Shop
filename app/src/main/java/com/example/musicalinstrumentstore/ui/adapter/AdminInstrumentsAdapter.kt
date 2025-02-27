package com.example.musicalinstrumentstore.ui.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.model.UserRole
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminInstrumentsAdapter(
    val context: Context,
    private val instruments: ArrayList<Instrument>
) :
    BaseAdapter() {
    override fun getCount(): Int {
        return instruments.size
    }

    override fun getItem(position: Int): Any {
        return instruments[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.admin_product_view, parent, false)

        val titleET = view.findViewById<TextView>(R.id.titleTV)
        val brandET = view.findViewById<TextView>(R.id.brandTV)
        val modelET = view.findViewById<TextView>(R.id.modelTV)
        val descriptionET = view.findViewById<TextView>(R.id.descriptionTV)
        val costET = view.findViewById<TextView>(R.id.costTV)
        val stockET = view.findViewById<TextView>(R.id.stockTV)
        val editBtn = view.findViewById<Button>(R.id.editBtn)
        val deleteBtn = view.findViewById<Button>(R.id.deleteBtn)

        titleET.text = instruments[position].title
        brandET.text = instruments[position].brand
        modelET.text = instruments[position].model
        descriptionET.text = instruments[position].description
        costET.text = instruments[position].cost.toString()
        stockET.text = instruments[position].stock.toString()

        editBtn.setOnClickListener {
            editItem(position)
        }

        deleteBtn.setOnClickListener {
            delete(position)
        }


        return view
    }

    private fun editItem(position: Int) {
        val inflater = LayoutInflater.from(context)
        val myPopupView = inflater.inflate(R.layout.admin_product_create, null)
        val popupWindow = PopupWindow(myPopupView, 1200, 1500, true)
        popupWindow.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.popup_background
            )
        )
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(myPopupView, Gravity.CENTER, 0, 0)

        val titleET = myPopupView.findViewById<EditText>(R.id.titleET)
        val brandET = myPopupView.findViewById<EditText>(R.id.brandET)
        val modelET = myPopupView.findViewById<EditText>(R.id.modelET)
        val descriptionET = myPopupView.findViewById<EditText>(R.id.descriptionET)
        val costET = myPopupView.findViewById<EditText>(R.id.costET)
        val stockET = myPopupView.findViewById<EditText>(R.id.stockET)
        val addItemBtn = myPopupView.findViewById<Button>(R.id.addItemBtn)


        val currentInstrument = instruments[position]
        titleET.setText(currentInstrument.title)
        brandET.setText(currentInstrument.brand)
        modelET.setText(currentInstrument.model)
        descriptionET.setText(currentInstrument.description)
        costET.setText(currentInstrument.cost.toString())
        stockET.setText(currentInstrument.stock.toString())

        addItemBtn.setOnClickListener {
            val updatedInstrument = currentInstrument.copy(
                title = titleET.text.toString(),
                brand = brandET.text.toString(),
                model = modelET.text.toString(),
                description = descriptionET.text.toString(),
                cost = costET.text.toString().toFloat(),
                stock = stockET.text.toString().toInt()
            )

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase(context)
                val instrumentRepo = InstrumentsRepository(db)
                withContext(Dispatchers.Main){
                    instrumentRepo.updateInstrument(instruments[position].id,updatedInstrument)
                    instruments[position] = updatedInstrument
                    notifyDataSetChanged()
                    popupWindow.dismiss()
                }

            }

        }


    }

    private fun delete(position: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val instrumentToDelete = instruments[position]
            val db = AppDatabase(context)
            val instrumentsRepo = InstrumentsRepository(db)

                withContext(Dispatchers.Main){
                    instrumentsRepo.deleteInstrument(instrumentToDelete.id)
                    instruments.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }

}