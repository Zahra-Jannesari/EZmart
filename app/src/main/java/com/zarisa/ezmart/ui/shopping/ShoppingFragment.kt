package com.zarisa.ezmart.ui.shopping

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zarisa.ezmart.adapter.CartListRecyclerViewAdapter
import com.zarisa.ezmart.databinding.FragmentShoppingBinding
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import com.zarisa.ezmart.model.CUSTOMER
import com.zarisa.ezmart.model.CUSTOMER_ID
import com.zarisa.ezmart.model.ORDER_ID
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
        initSharedPref()
        cartSituation()
        bindView()
        statusObserver()
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvOrderItems.adapter =
            CartListRecyclerViewAdapter({ id -> deleteItem(id) },
                { id -> addOneItem(id) },
                { id -> removeOneItem(id) })
    }

    private fun cartSituation() {
        if (viewModel.customerId != 0 || viewModel.orderId != 0) {
            viewModel.getOrder()
            binding.imageViewEmptyCart.visibility = View.GONE
            binding.lMain.visibility = View.VISIBLE
        } else {
            binding.imageViewEmptyCart.visibility = View.VISIBLE
            binding.lMain.visibility = View.GONE
        }
        viewModel.emptyCart.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.imageViewEmptyCart.visibility = View.VISIBLE
                binding.lMain.visibility = View.GONE
            } else {
                binding.imageViewEmptyCart.visibility = View.GONE
                binding.lMain.visibility = View.VISIBLE
            }
        }
    }

    private fun initSharedPref() {
        sharedPref = requireActivity().getSharedPreferences(CUSTOMER, Context.MODE_PRIVATE)
        viewModel.customerId = sharedPref.getInt(CUSTOMER_ID, 0)
        viewModel.orderId = sharedPref.getInt(ORDER_ID, 0)
    }

    private fun addOneItem(id: Int) {

    }

    private fun removeOneItem(id: Int) {

    }

    private fun deleteItem(id: Int) {

    }

    private fun statusObserver() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.lMain,
                binding.lStatus, { cartSituation() }, viewModel.statusMessage
            )
        }
    }
}