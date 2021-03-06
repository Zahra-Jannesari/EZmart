package com.zarisa.ezmart.ui.all_categories

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentCategoriesBinding
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.MainActivity
import com.zarisa.ezmart.adapter.CategoryListRecyclerView
import com.zarisa.ezmart.domain.NetworkStatusViewHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    val viewModel: CategoriesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupAppbar()
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setupAppbar() {
        (requireActivity() as MainActivity).supportActionBar?.show()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategoriesList()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvCategories.adapter =
            CategoryListRecyclerView { category -> onCategoryClick(category) }
        statusObserver()
    }

    private fun getCategoriesList() {
        viewModel.getCategoriesList()
    }

    private fun statusObserver() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            NetworkStatusViewHandler(
                it,
                binding.rvCategories,
                binding.lStatus, { getCategoriesList() }, viewModel.statusMessage
            )
        }
    }

    private fun onCategoryClick(category: Category) {
        val bundle = bundleOf(CATEGORY_ITEM to category)
        findNavController().navigate(R.id.action_categoriesFragment_to_categoryFragment, bundle)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                val bundle = bundleOf(SEARCH_ORIGIN to SEARCH_IN_ALL)
                findNavController().navigate(
                    R.id.action_categoriesFragment_to_searchFragment,
                    bundle
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}