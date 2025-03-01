package com.example.musicalinstrumentstore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.database.AppDatabase
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.data.repository.InstrumentsRepository
import com.example.musicalinstrumentstore.ui.customer.viewModel.CheckOutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckOutAdapter(
    private val context: Context,
    private val instruments: ArrayList<Instrument>,private val viewModel: CheckOutViewModel
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
            .inflate(R.layout.checkout_product_view, parent, false)

        val titleTV = view.findViewById<TextView>(R.id.titleTV)
        val brandTV = view.findViewById<TextView>(R.id.brandTV)
        val modelTV = view.findViewById<TextView>(R.id.modelTV)
        val descriptionTV = view.findViewById<TextView>(R.id.descriptionTV)
        val costTV = view.findViewById<TextView>(R.id.costTV)
        val removeBtn = view.findViewById<Button>(R.id.removeBtn)

        titleTV.text = instruments[position].title
        brandTV.text = instruments[position].brand
        modelTV.text = instruments[position].model
        descriptionTV.text = instruments[position].description
        costTV.text = instruments[position].cost.toString()

        removeBtn.setOnClickListener {
            instruments.removeAt(position)
            viewModel.calculateTotalCost(instruments)
            notifyDataSetChanged()
        }



        return view
    }


}