package com.zarisa.ezmart.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zarisa.ezmart.R
import com.zarisa.ezmart.ui.MainActivity


class SearchFragment : Fragment() {

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupAppbar()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


}