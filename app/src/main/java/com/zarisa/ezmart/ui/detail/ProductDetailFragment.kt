package com.zarisa.ezmart.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zarisa.ezmart.databinding.FragmentProductDetailBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.ui.components.NetworkStatusViewHandler
import com.zarisa.ezmart.ui.components.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentProduct()
        bindView()
        statusObserver()
    }

    private fun statusObserver() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.scrollViewDetails,
                binding.lStatus
            ) { getCurrentProduct() }
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        bindViewPager()
    }

    private fun bindViewPager() {
        viewModel.currentProduct.observe(viewLifecycleOwner) { product ->
            binding.productImgViewPager.let { viewPager ->
                viewPager.adapter = ViewPagerAdapter(product.images, requireContext())
                (binding.circleIndicator as CircleIndicator).setViewPager(viewPager)
            }
        }
    }

    private fun getCurrentProduct() {
        viewModel.getProductById(requireArguments().getInt(ITEM_ID))
    }

}