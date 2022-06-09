package com.zarisa.ezmart.ui.specific_category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentCategoryBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.model.NetworkStatus
import com.zarisa.ezmart.ui.components.ProductByCategoryListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    lateinit var binding: FragmentCategoryBinding
    val viewModel: CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initCategory(requireArguments().getInt(ITEM_ID))
        bindView()
        statusObserver()
    }

    private fun statusObserver() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                NetworkStatus.LOADING -> {
                    binding.lMain.visibility = View.GONE
                    binding.lStatus.tvNetworkStatus.visibility = View.GONE
                    binding.lStatus.imageViewNetworkStatus.let { imageView ->
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.loading_animation)
                    }
                }
                NetworkStatus.ERROR -> {
                    binding.lMain.visibility = View.GONE
                    binding.lStatus.tvNetworkStatus.visibility = View.VISIBLE
                    binding.lStatus.imageViewNetworkStatus.let { imageView ->
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.network_error)
                    }
                }
                else -> {
                    binding.lMain.visibility = View.VISIBLE
                    binding.lStatus.root.visibility = View.GONE
                }
            }
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvCategoryProducts.adapter =
            ProductByCategoryListAdapter { id -> onProductItemClick(id) }
    }

    private fun onProductItemClick(id: Int) {
        val bundle = bundleOf(ITEM_ID to id)
        findNavController().navigate(R.id.action_categoryFragment_to_productDetailFragment, bundle)
    }
}