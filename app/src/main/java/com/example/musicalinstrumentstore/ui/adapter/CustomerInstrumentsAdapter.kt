package com.example.musicalinstrumentstore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.musicalinstrumentstore.ui.customer.CustomerActivity
import com.example.musicalinstrumentstore.R
import com.example.musicalinstrumentstore.data.model.Instrument

class CustomerInstrumentsAdapter(val context: Context, private val instruments: ArrayList<Instrument>) :
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
            .inflate(R.layout.customer_product_view, parent, false)

        val titleET = view.findViewById<TextView>(R.id.titleTV)
        val brandET = view.findViewById<TextView>(R.id.brandTV)
        val modelET = view.findViewById<TextView>(R.id.modelTV)
        val descriptionET = view.findViewById<TextView>(R.id.descriptionTV)
        val costET = view.findViewById<TextView>(R.id.costTV)
        val stockET = view.findViewById<TextView>(R.id.stockTV)
        val addToCartBtn = view.findViewById<Button>(R.id.addToCartBtn)


        titleET.text = instruments[position].title
        brandET.text = instruments[position].brand
        modelET.text = instruments[position].model
        descriptionET.text = instruments[position].description
        costET.text = "Cost: ${instruments[position].cost}"
        stockET.text = "Stock: ${instruments[position].stock}"

        addToCartBtn.setOnClickListener {
            CustomerActivity.cartList.add(instruments[position])
            Toast.makeText(context,"Item has been added to cart",Toast.LENGTH_LONG).show()
            notifyDataSetChanged()
        }


        return view
    }
}