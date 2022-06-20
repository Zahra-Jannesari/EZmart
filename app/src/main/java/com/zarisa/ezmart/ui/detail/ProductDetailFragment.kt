package com.zarisa.ezmart.ui.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zarisa.ezmart.adapter.ReviewAdapter
import com.zarisa.ezmart.databinding.FragmentProductDetailBinding
import com.zarisa.ezmart.ui.MainActivity
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import com.zarisa.ezmart.adapter.ViewPagerAdapter
import com.zarisa.ezmart.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: ProductDetailViewModel by viewModels()
    private var customerId = 0
    private var orderId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPref()
        getCurrentProduct()
        bindView()
        statusObserver()
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        customerId = sharedPref.getInt(CUSTOMER_ID, 0)
        orderId = sharedPref.getInt(ORDER_ID, 0)
    }

    private fun statusObserver() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.scrollViewDetails,
                binding.lStatus, { getCurrentProduct() }, viewModel.statusMessage
            )
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvReviews.adapter = ReviewAdapter()
        bindViewPager()
        binding.btnAddToCart.setOnClickListener {
            onAddToCartClick()
        }
    }


    private fun onAddToCartClick() {
        val editor = sharedPref.edit()
        if (orderId == 0) {
            lifecycleScope.launch {
                val orderId = viewModel.createOrder(order = Order(customer_id = customerId).apply {
                    line_items = listOf(viewModel.currentProduct.value!!.let {
                        OrderItem(
                            productName = it.name, product_id = it.id
                        )
                    })
                })
                editor.putInt(ORDER_ID, orderId).apply()
            }

        } else
            viewModel.updateOrder(orderId, viewModel.currentProduct.value!!.let {
                OrderItem(
                    productName = it.name, product_id = it.id
                )
            })

    }

    private fun bindViewPager() {
        viewModel.currentProduct.observe(viewLifecycleOwner) { product ->
            binding.productImgViewPager.let { viewPager ->
                viewPager.adapter = product?.images?.let { ViewPagerAdapter(it, requireContext()) }
                binding.circleIndicator.setViewPager(viewPager)
            }
        }
    }

    private fun getCurrentProduct() {
        viewModel.initialProduct(requireArguments().getInt(ITEM_ID))
    }

}