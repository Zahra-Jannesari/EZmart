package com.zarisa.ezmart.ui.shopping

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zarisa.ezmart.R
import com.zarisa.ezmart.adapter.CartListRecyclerViewAdapter
import com.zarisa.ezmart.databinding.FragmentShoppingBinding
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import com.zarisa.ezmart.model.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoppingFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentShoppingBinding
    private val viewModel: ShoppingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentShoppingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCart()
        bindView()
        observer()
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvOrderItems.adapter =
            CartListRecyclerViewAdapter(
                { id, operation -> editItem(id, operation) },
                { id -> onProductItemClick(id) }
            )
        binding.btnCompleteShopping.setOnClickListener {
            completeShopping()
        }
    }

    private fun completeShopping() {
        if (viewModel.customerId == 0) {
            val snackbar =
                Snackbar.make(
                    binding.btnCompleteShopping,
                    "برای تکمیل خرید باید ابتدا ثبت نام کنید.",
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.do_register) {
                    findNavController().navigate(R.id.action_shoppingFragment_to_profileFragment)
                }
            snackbar.view.layoutDirection = View.LAYOUT_DIRECTION_RTL
            snackbar.show()
        } else {
            viewModel.completeOrder()
        }
    }

    private fun initCart() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        viewModel.customerId = sharedPref.getInt(CUSTOMER_ID, 0)
        viewModel.orderId.postValue(sharedPref.getInt(ORDER_ID, 0))
    }

    private fun editItem(id: Int, operation: Int) {
        viewModel.updateOrder(id, operation)
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_shoppingFragment_to_productDetailFragment, bundle)
    }

    private fun observer() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            if (it == Status.EMPTY_CART) {
                binding.lMain.visibility = View.GONE
                binding.lStatus.root.visibility = View.GONE
                binding.imageViewEmptyCart.visibility = View.VISIBLE
            } else {
                binding.imageViewEmptyCart.visibility = View.GONE
                binding.lMain.visibility = View.VISIBLE
                binding.lStatus.root.visibility = View.VISIBLE
                NetworkStatusViewHandler(
                    it, binding.lMain,
                    binding.lStatus, { viewModel.getOrder() }, viewModel.statusMessage
                )
            }

        }
        viewModel.editCartStatus.observe(viewLifecycleOwner) {
            makeToastForStatus(it)
            viewModel.editCartStatus.postValue(null)
        }
        viewModel.completeShoppingStatus.observe(viewLifecycleOwner) {
            makeToastForStatus(it)
            viewModel.completeShoppingStatus.postValue(null)
        }
        viewModel.orderId.observe(viewLifecycleOwner) {
            viewModel.getOrder()
            sharedPref.edit().putInt(ORDER_ID, it).apply()
        }
    }

    private fun makeToastForStatus(it: Status?) {
        if (it != null)
            Toast.makeText(
                requireContext(),
                when (it) {
                    Status.SUCCESSFUL -> "عملیات با موفقیت انجام شد."
                    Status.NETWORK_ERROR -> "لطفا اینترنت خود را چک کنید و مجددا تلاش کنید"
                    Status.LOADING -> "در حال انجام عملیات..."
                    else -> "خطایی رخ داده. لطفا مجددا تلاش کنید"
                },
                Toast.LENGTH_SHORT
            ).show()
    }
}