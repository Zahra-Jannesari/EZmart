package com.zarisa.ezmart.ui.all_categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentCategoriesBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.model.NetworkStatus
import com.zarisa.ezmart.ui.components.CategoryListRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    val viewModel: CategoriesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategoriesList()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvCategories.adapter = CategoryListRecyclerView { id -> onCategoryClick(id) }
        viewModel.networkStatusLiveDatac.observe(viewLifecycleOwner) {
            when (it) {
                NetworkStatus.LOADING -> {
                    binding.rvCategories.visibility = View.GONE
                    binding.lStatus.tvNetworkStatus.visibility = View.GONE
                    binding.lStatus.imageViewNetworkStatus.let { imageView ->
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.loading_animation)
                    }
                }
                NetworkStatus.ERROR -> {
                    binding.rvCategories.visibility = View.GONE
                    binding.lStatus.tvNetworkStatus.visibility = View.VISIBLE
                    binding.lStatus.imageViewNetworkStatus.let { imageView ->
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.network_error)
                    }
                }
                else -> {
                    binding.rvCategories.visibility = View.VISIBLE
                    binding.lStatus.root.visibility = View.GONE
                }
            }
        }
    }

    private fun onCategoryClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_categoriesFragment_to_categoryFragment, bundle)
    }
}