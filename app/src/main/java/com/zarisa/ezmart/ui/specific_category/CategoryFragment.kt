package com.zarisa.ezmart.ui.specific_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentCategoryBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity
import com.zarisa.ezmart.ui.components.NetworkStatusViewHandler
import com.zarisa.ezmart.adapter.ProductHorizontalViewListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    lateinit var binding: FragmentCategoryBinding
    private var category: Category? = null
    val viewModel: CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        category = requireArguments().getParcelable(CATEGORY_ITEM)
        setupAppbar()
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.show()
        (requireActivity() as MainActivity).supportActionBar?.title = category?.name
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryDetail()
        bindView()
        statusObserver()
    }

    private fun initCategoryDetail() {
        category?.id?.let { viewModel.initCategory(it) }
    }

    private fun statusObserver() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.rvCategoryProducts,
                binding.lStatus
            ) { initCategoryDetail() }
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvCategoryProducts.adapter =
            ProductHorizontalViewListAdapter { id -> onProductItemClick(id) }
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_categoryFragment_to_productDetailFragment, bundle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                val bundle = bundleOf(SEARCH_ORIGIN to category?.name, ITEM_ID to category?.id)
                findNavController().navigate(
                    R.id.action_categoryFragment_to_searchFragment,
                    bundle
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}