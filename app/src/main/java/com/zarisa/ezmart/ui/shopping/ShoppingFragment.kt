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
    var isCartEmpty = true
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
        initSharedPref()
        cartSituation()
        bindView()
        statusObserver()
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvOrderItems.adapter =
            CartListRecyclerViewAdapter(
                { id, operation -> editItem(id, operation) },
                { id -> onProductItemClick(id) }
            )
    }

    private fun cartSituation() {
        if (viewModel.customerId != 0 || viewModel.orderId != 0)
            viewModel.getOrder()
        viewModel.emptyCart.observe(viewLifecycleOwner) {
            isCartEmpty = it
        }
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        viewModel.customerId = sharedPref.getInt(CUSTOMER_ID, 0)
        viewModel.orderId = sharedPref.getInt(ORDER_ID, 0)
    }

    private fun editItem(id: Int, operation: Int) {
        viewModel.updateOrder(id, operation)
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_shoppingFragment_to_productDetailFragment, bundle)
    }

    private fun statusObserver() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            if (it == Status.SUCCESSFUL && isCartEmpty) {
                binding.lMain.visibility = View.GONE
                binding.lStatus.root.visibility = View.GONE
                binding.imageViewEmptyCart.visibility = View.VISIBLE
            } else if (it == Status.SUCCESSFUL && !isCartEmpty) {
                binding.lMain.visibility = View.VISIBLE
                binding.lStatus.root.visibility = View.GONE
                binding.imageViewEmptyCart.visibility = View.GONE
            } else {
                binding.imageViewEmptyCart.visibility = View.GONE
                NetworkStatusViewHandler(
                    it,
                    binding.lMain,
                    binding.lStatus, { cartSituation() }, viewModel.statusMessage
                )
            }

        }
        viewModel.editCartStatus.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                when (it) {
                    Status.NETWORK_ERROR -> "لطفا اینترنت خود را چک کنید و مجددا تلاش کنید"
                    Status.LOADING -> "در حال انجام عملیات..."
                    else -> "خطایی رخ داده. لطفا مجددا تلاش کنید"
                },
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}