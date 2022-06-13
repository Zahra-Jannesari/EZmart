package com.zarisa.ezmart.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentHomeBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.ui.components.NetworkStatusViewHandler
import com.zarisa.ezmart.ui.components.ProductRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        getHomeLists()
        statusObserver()
    }

    private fun getHomeLists() {
        viewModel.getMainProductsLists()
    }

    private fun statusObserver() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.scrollViewLists,
                binding.lStatus
            ) { getHomeLists() }
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvNewest.adapter = ProductRecyclerViewAdapter { id -> onProductItemClick(id) }
        binding.rvMostSeen.adapter = ProductRecyclerViewAdapter { id -> onProductItemClick(id) }
        binding.rvHighRates.adapter = ProductRecyclerViewAdapter { id -> onProductItemClick(id) }
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
    }
}