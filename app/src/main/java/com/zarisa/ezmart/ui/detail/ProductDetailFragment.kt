package com.zarisa.ezmart.ui.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.zarisa.ezmart.R
import com.zarisa.ezmart.adapter.ProductVerticalViewRecyclerViewAdapter
import com.zarisa.ezmart.adapter.ReviewAdapter
import com.zarisa.ezmart.adapter.ViewPagerAdapter
import com.zarisa.ezmart.databinding.FragmentProductDetailBinding
import com.zarisa.ezmart.dialog.ReviewDialog
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), ReviewDialog.DialogListener {
    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: ProductDetailViewModel by activityViewModels()
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
        initProduct()
        observer()
        bindView()
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(EZ_SHARED_PREF, Context.MODE_PRIVATE)
        customerId = sharedPref.getInt(CUSTOMER_ID, 0)
        orderId = sharedPref.getInt(ORDER_ID, 0)
        viewModel.customerName = sharedPref.getString(USER_NAME, "")
        viewModel.customerEmail = sharedPref.getString(USER_EMAIL, "")
    }

    private fun observer() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.lMain,
                binding.lStatus, { initProduct() }, viewModel.statusMessage
            )
        }
        viewModel.selectedPartToShow.observe(viewLifecycleOwner) {
            prepareShownPart(it)
        }
        viewModel.sideOptionsStatus.observe(viewLifecycleOwner) {
            if (it == Status.SUCCESSFUL) {
                binding.rvSideOptions.visibility = View.VISIBLE
                binding.imageVIewSideOptionStatus.visibility = View.GONE
            } else {
                binding.rvSideOptions.visibility = View.GONE
                binding.imageVIewSideOptionStatus.visibility = View.VISIBLE
            }
        }
        viewModel.reviewStatus.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(
                    requireContext(),
                    when (it) {
                        Status.LOADING -> "در حال انجام عملیات..."
                        Status.SUCCESSFUL -> "عملیات با موفقیت انجام شد."
                        Status.NETWORK_ERROR -> "لطفا اتصال اینترنت خود را چک کنید."
                        else -> "خطایی رخ داده است. لطفا مجددا تلاش کنید."
                    }, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun prepareShownPart(it: Int?) {
        when (it) {
            REVIEWS -> {
                binding.lReview.visibility = View.VISIBLE
                binding.tvReview.let {
                    it.isSelected = true
                    it.setTextColor(
                        MaterialColors.getColor(
                            it,
                            com.airbnb.lottie.R.attr.colorPrimary
                        )
                    )
                }
                binding.tvRelatedProducts.let {
                    it.isSelected = false
                    it.setTextColor(resources.getColor(R.color.drawable_tint))
                }
                ReviewAdapter(viewModel.customerEmail,
                    { reviewId -> deleteReview(reviewId) },
                    { review -> editReview(review) }).let { adapter ->
                    binding.rvSideOptions.adapter = adapter
                    viewModel.reviewsList.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(null)
                        adapter.submitList(list)
                    }
                }
            }
            RELATED_PRODUCTS -> {
                binding.lReview.visibility = View.GONE
                binding.tvReview.let {
                    it.isSelected = false
                    it.setTextColor(resources.getColor(R.color.drawable_tint))
                }
                binding.tvRelatedProducts.let {
                    it.isSelected = true
                    it.setTextColor(
                        MaterialColors.getColor(
                            it,
                            com.airbnb.lottie.R.attr.colorPrimary
                        )
                    )
                }
                ProductVerticalViewRecyclerViewAdapter { id -> viewModel.initialProduct(id) }.let { adapter ->
                    binding.rvSideOptions.adapter = adapter
                    viewModel.relatedProductsList.observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                    }
                }
            }
        }
    }

    private fun deleteReview(reviewId: Int) {
        viewModel.deleteReview(reviewId)
    }

    private fun editReview(review: Review) {
        showDialog(review)
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        bindViewPager()
        binding.btnAddToCart.setOnClickListener {
            onAddToCartClick()
        }
        binding.tvRelatedProducts.setOnClickListener {
            viewModel.selectedPartToShow.postValue(RELATED_PRODUCTS)
        }
        binding.tvReview.setOnClickListener {
            viewModel.selectedPartToShow.postValue(REVIEWS)
        }
        binding.btnSendReview.setOnClickListener {
            if (customerId == 0) {
                val snackbar = Snackbar.make(
                    binding.btnAddToCart,
                    "برای ثبت نظر باید ابتدا ثبت نام کنید.",
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.register) {
                    findNavController().navigate(R.id.action_productDetailFragment_to_profileFragment)
                }
                snackbar.view.layoutDirection = View.LAYOUT_DIRECTION_RTL
                snackbar.show()
            } else
                showDialog()
        }
    }

    private fun showDialog(review: Review? = null) {
        val dialog = ReviewDialog(review)
        activity?.supportFragmentManager?.let { dialog.show(it, "NoticeDialogFragment") }
    }

    private fun onAddToCartClick() {
        val editor = sharedPref.edit()
        val orderItem = OrderItem(
            product_id = viewModel.currentProduct.value!!.id,
            productName = viewModel.currentProduct.value!!.name,
            price = viewModel.currentProduct.value!!.price,
            total = viewModel.currentProduct.value!!.price
        )
        if (orderId == 0) {
            lifecycleScope.launch {
                val orderId = viewModel.createOrder(order = Order(customer_id = customerId).apply {
                    line_items = listOf(
                        orderItem
                    )
                })
                editor.putInt(ORDER_ID, orderId).apply()
            }
        } else
            viewModel.updateOrder(
                orderId, orderItem
            )
    }

    private fun bindViewPager() {
        viewModel.currentProduct.observe(viewLifecycleOwner) { product ->
            binding.productImgViewPager.let { viewPager ->
                viewPager.adapter = product?.images?.let { ViewPagerAdapter(it, requireContext()) }
                binding.circleIndicator.setViewPager(viewPager)
            }
        }
    }

    private fun initProduct() {
        viewModel.initialProduct(requireArguments().getInt(ITEM_ID))
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetStatuses()
    }

}