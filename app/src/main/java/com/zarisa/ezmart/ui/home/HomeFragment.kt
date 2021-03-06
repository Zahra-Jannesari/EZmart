package com.zarisa.ezmart.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.adapter.ProductVerticalViewRecyclerViewAdapter
import com.zarisa.ezmart.adapter.SliderAdapter
import com.zarisa.ezmart.databinding.FragmentHomeBinding
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.model.SEARCH_IN_ALL
import com.zarisa.ezmart.model.SEARCH_ORIGIN
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.show()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        getHomeLists()
        statusObserver()
    }

    private fun bindSpecialsSlider() {
        val adapter = SliderAdapter(this, mutableListOf())
        binding.specialsImgViewPager.adapter = adapter
        viewModel.specialOffersList.observe(viewLifecycleOwner) {
            it?.let { product ->
                adapter.newImageList(product.images)
            }
            binding.circleIndicator.setViewPager(binding.specialsImgViewPager)
        }
    }

    private fun getHomeLists() {
        viewModel.getMainProductsLists()
    }

    private fun statusObserver() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.scrollViewLists,
                binding.lStatus, { getHomeLists() }, viewModel.statusMessage
            )
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvNewest.adapter =
            ProductVerticalViewRecyclerViewAdapter { id -> onProductItemClick(id) }
        binding.rvMostSeen.adapter =
            ProductVerticalViewRecyclerViewAdapter { id -> onProductItemClick(id) }
        binding.rvHighRates.adapter =
            ProductVerticalViewRecyclerViewAdapter { id -> onProductItemClick(id) }
        bindSpecialsSlider()
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                val bundle = bundleOf(SEARCH_ORIGIN to SEARCH_IN_ALL)
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment, bundle)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}