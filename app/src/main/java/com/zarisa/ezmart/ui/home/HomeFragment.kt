package com.zarisa.ezmart.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentHomeBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.model.SEARCH_IN_ALL
import com.zarisa.ezmart.model.SEARCH_ORIGIN
import com.zarisa.ezmart.ui.MainActivity
import com.zarisa.ezmart.ui.components.NetworkStatusViewHandler
import com.zarisa.ezmart.ui.components.ProductRecyclerViewAdapter
import com.zarisa.ezmart.ui.components.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator

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
        bindViewPager()
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
    }

    private fun bindViewPager() {
        viewModel.specialOffers.observe(viewLifecycleOwner) { product ->
            binding.specialsImgViewPager.let { viewPager ->
                viewPager.adapter = ViewPagerAdapter(product.images, requireContext())
                binding.circleIndicator.setViewPager(viewPager)
            }
        }
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