package com.zarisa.ezmart.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentHomeBinding
import com.zarisa.ezmart.ui.components.RecyclerViewAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvNewest.adapter = RecyclerViewAdapter()
        binding.rvMostSeen.adapter = RecyclerViewAdapter()
        binding.rvHighRates.adapter = RecyclerViewAdapter()
    }


}