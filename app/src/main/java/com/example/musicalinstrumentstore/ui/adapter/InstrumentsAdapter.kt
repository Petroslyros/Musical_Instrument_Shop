package com.example.musicalinstrumentstore.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.musicalinstrumentstore.data.model.Instrument

class InstrumentsAdapter(val context: Context, private val instruments: ArrayList<Instrument>): BaseAdapter() {
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
        TODO("Not yet implemented")
    }
}