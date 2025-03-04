package com.example.musicalinstrumentstore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.model.CartInstrument
import com.example.musicalinstrumentstore.data.model.Instrument
import com.example.musicalinstrumentstore.ui.customer.viewModel.CheckOutViewModel

class CheckOutAdapter(
    private val context: Context,
    private val instruments: ArrayList<CartInstrument>,
    private val viewModel: CheckOutViewModel
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
        val quantityTV = view.findViewById<TextView>(R.id.quantityTV)
        val removeBtn = view.findViewById<Button>(R.id.removeBtn)

        titleTV.text = instruments[position].instrument.title
        brandTV.text = instruments[position].instrument.brand
        modelTV.text = instruments[position].instrument.model
        descriptionTV.text = instruments[position].instrument.description
        costTV.text = instruments[position].instrument.cost.toString()


        removeBtn.setOnClickListener {
            instruments.removeAt(position)
            viewModel.calculateTotalCost(instruments)
            notifyDataSetChanged()
        }


        return view
    }


}