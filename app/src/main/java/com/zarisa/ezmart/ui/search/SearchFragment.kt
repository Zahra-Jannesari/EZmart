package com.zarisa.ezmart.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentSearchBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.model.SEARCH_IN_ALL
import com.zarisa.ezmart.model.SEARCH_ORIGIN
import com.zarisa.ezmart.model.SearchOrder
import com.zarisa.ezmart.ui.MainActivity
import com.zarisa.ezmart.adapter.ProductHorizontalViewListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    val viewModel: SearchViewModel by viewModels()
    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSearchContext()
        setupSpinnerOrder()
        setupSearchEditText()
        bindView()
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvSearchResultProducts.adapter =
            ProductHorizontalViewListAdapter { id -> onProductItemClick(id) }
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_searchFragment_to_productDetailFragment, bundle)
    }

    private fun setupSearchEditText() {
        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            if (!inputText.isNullOrBlank()) {
                viewModel.searchText = inputText.toString()
            }
        }
        binding.textFieldSearch.setEndIconOnClickListener {
            if (binding.editTextSearch.text.isNullOrBlank())
                binding.editTextSearch.error = "لطفا نام محصول مورد نظر را وارد کنید."
            else
                viewModel.getSearchResults()
        }
    }

    private fun setupSpinnerOrder() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.search_orders, R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerSort.adapter = adapter
        }
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p0?.getItemAtPosition(p2).toString()) {
                    "پرفروش ترین" -> viewModel.setOrder(SearchOrder.BEST_SELLERS)
                    "بیشترین قیمت" -> viewModel.setOrder(SearchOrder.HIGH_PRICE)
                    "کمترین قیمت" -> viewModel.setOrder(SearchOrder.LOW_PRICE)
                    else -> {
                        viewModel.setOrder(SearchOrder.NEWEST)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun getSearchContext() {
        requireArguments().getString(SEARCH_ORIGIN, "").let {
            if (it != SEARCH_IN_ALL) {
                viewModel.categoryName = it
                viewModel.categoryId = requireArguments().getInt(ITEM_ID, 0)
            }
        }
    }
}