package com.zarisa.ezmart.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zarisa.ezmart.R
import com.zarisa.ezmart.adapter.AttrListAdapter
import com.zarisa.ezmart.adapter.AttrTermsListAdapter
import com.zarisa.ezmart.adapter.ProductHorizontalViewListAdapter
import com.zarisa.ezmart.databinding.FragmentSearchBinding
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    val viewModel: SearchViewModel by viewModels()
    private var isAttrListEmpty = true
    private val attrListAdapter: AttrListAdapter by lazy {
        AttrListAdapter({ attr ->
            onAttrClicked(attr)
        }, { view: View, isItemSelected: Boolean -> setItemBackground(view, isItemSelected) })
    }
    private val attrTermListAdapter: AttrTermsListAdapter by lazy {
        AttrTermsListAdapter { attrTerm -> onAttrTermClicked(attrTerm) }
    }


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

    private fun initCategoryDetail() {
        viewModel.getSearchResults()
    }

    private fun bindView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvSearchResultProducts.adapter =
            ProductHorizontalViewListAdapter { id -> onProductItemClick(id) }
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            controlViewByStatus(it)
        }
        binding.btnSearchFilter.setOnClickListener {
            if (isAttrListEmpty)
                Toast.makeText(
                    requireContext(),
                    "در حال دریافت اطلاعات از سرور...\nلطفا مجددا تلاش کنید.",
                    Toast.LENGTH_SHORT
                ).show()
            else
                showFilterDialog()
        }
        viewModel.listOfAttributeTerms.observe(viewLifecycleOwner) {
            isAttrListEmpty = it.isNullOrEmpty()
        }
        viewModel.selectedAttrTerm.observe(viewLifecycleOwner) {
            binding.btnSearchFilter.text =
                if (it == null) "بدون فیلتر" else "${viewModel.selectedAttr?.name} ${it.name}"
        }
    }

    private fun controlViewByStatus(it: Status?) {
        when (it) {
            Status.LOADING -> {
                binding.lLoader.visibility = View.VISIBLE
                binding.lStatus.root.visibility = View.GONE
                binding.rvSearchResultProducts.visibility = View.GONE
            }
            null -> {
                binding.lLoader.visibility = View.GONE
                binding.lStatus.root.visibility = View.VISIBLE
                binding.rvSearchResultProducts.visibility = View.VISIBLE
            }
            else -> {
                binding.lLoader.visibility = View.GONE
                binding.lStatus.root.visibility = View.VISIBLE
                binding.rvSearchResultProducts.visibility = View.VISIBLE
                NetworkStatusViewHandler(
                    it,
                    binding.rvSearchResultProducts,
                    binding.lStatus, { initCategoryDetail() }, viewModel.statusMessage
                )
            }
        }
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_searchFragment_to_productDetailFragment, bundle)
    }

    private fun setupSearchEditText() {
        binding.textFieldSearch.setEndIconOnClickListener {
            viewModel.searchText = binding.editTextSearch.text.toString()
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
                    "پرفروش ترین" -> viewModel.searchListOrder = SearchOrder.BEST_SELLERS
                    "بیشترین قیمت" -> viewModel.searchListOrder = SearchOrder.HIGH_PRICE
                    "کمترین قیمت" -> viewModel.searchListOrder = SearchOrder.LOW_PRICE
                    else -> {
                        viewModel.searchListOrder = SearchOrder.NEWEST
                    }
                }
                viewModel.getSearchResults()
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

    /*-----------------------------------filter part----------------------------------------------*/
    private fun showFilterDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.layout_filter_dialog, null)
        val btnDoFilter = dialogView.findViewById<MaterialButton>(R.id.btn_doFilter)
        val btnResetFilter = dialogView.findViewById<MaterialButton>(R.id.btn_resetFilter)
        initDialogRecyclerViews(dialogView)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).show()
        btnDoFilter.setOnClickListener {
            viewModel.getSearchResults()
            alertDialog.dismiss()
        }
        btnResetFilter.setOnClickListener {
            viewModel.resetFilter()
            attrTermListAdapter.currentAttrIndex = -1
            alertDialog.dismiss()
        }
    }

    private fun initDialogRecyclerViews(dialogView: View) {
        val rvAttrList = dialogView.findViewById<RecyclerView>(R.id.rv_attrList)
        val rvAttrTerms = dialogView.findViewById<RecyclerView>(R.id.rv_attrTerms)
        val statusImg = dialogView.findViewById<ImageView>(R.id.imageView_Status)
        rvAttrList.adapter = attrListAdapter
        viewModel.listOfAttributes.observe(viewLifecycleOwner) { attrList ->
            attrListAdapter.submitList(attrList)
        }
        rvAttrTerms.adapter = attrTermListAdapter
        viewModel.listOfAttributeTerms.observe(viewLifecycleOwner) { attrList ->
            attrTermListAdapter.submitList(attrList)
        }
        viewModel.attrTermStatus.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> {
                    rvAttrTerms.visibility = View.GONE
                    statusImg.visibility = View.VISIBLE
                    statusImg.setImageResource(R.drawable.loading_animation)
                }
                Status.SUCCESSFUL -> {
                    rvAttrTerms.visibility = View.VISIBLE
                    statusImg.visibility = View.GONE
                }
                Status.NETWORK_ERROR -> {
                    rvAttrTerms.visibility = View.GONE
                    statusImg.visibility = View.VISIBLE
                    statusImg.setImageResource(R.drawable.ic_no_internet)
                }
                else -> {
                    rvAttrTerms.visibility = View.GONE
                    statusImg.visibility = View.VISIBLE
                    statusImg.setImageResource(R.drawable.ic_server_error)
                }
            }
        }
    }

    private fun onAttrClicked(attr: AttrProps) {
        viewModel.selectedAttr = attr
        viewModel.getAttrTerms()
    }

    private fun onAttrTermClicked(attrTerm: AttrProps) {
        viewModel.selectedAttrTerm.postValue(attrTerm)
    }

    private fun setItemBackground(view: View, isItemSelected: Boolean) {
        view.background = AppCompatResources.getDrawable(
            requireContext(),
            if (isItemSelected) R.drawable.selected_background else R.drawable.not_selected_background
        )
    }
}

