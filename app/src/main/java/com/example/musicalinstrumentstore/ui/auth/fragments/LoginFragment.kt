package com.example.musicalinstrumentstore.ui.auth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.musicalinstrumentstore.R


class LoginFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val clickMeText = view.findViewById<TextView>(R.id.clickMeText)

        clickMeText.setOnClickListener {

        }

        return view
    }


}