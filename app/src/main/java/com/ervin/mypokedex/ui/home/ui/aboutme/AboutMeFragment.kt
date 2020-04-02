package com.ervin.mypokedex.ui.home.ui.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ervin.mypokedex.R
import com.ervin.mypokedex.viewmodel.ViewModelFactory

class AboutMeFragment : Fragment() {

    private val aboutMeViewModel: AboutMeViewModel by lazy {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@AboutMeFragment.activity!!.application)
        return@lazy ViewModelProvider(this@AboutMeFragment, factory).get(AboutMeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_about_me, container, false)
        val textView: TextView = root.findViewById(R.id.text_send)
        aboutMeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}