package com.zarisa.ezmart.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zarisa.ezmart.R
import com.zarisa.ezmart.databinding.FragmentProductDetailBinding
import com.zarisa.ezmart.model.ITEM_ID
import com.zarisa.ezmart.model.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentProduct()
        bindView()
        statusObserver()
    }

    private fun statusObserver() {
        viewModel.networkStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                NetworkStatus.LOADING -> {
                    binding.scrollViewDetails.visibility = View.GONE
                    binding.lStatus.tvNetworkStatus.visibility = View.GONE
                    binding.lStatus.imageViewNetworkStatus.let { imageView ->
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.loading_animation)
                    }
                }
                NetworkStatus.ERROR -> {
                    binding.scrollViewDetails.visibility = View.GONE
                    binding.lStatus.tvNetworkStatus.visibility = View.VISIBLE
                    binding.lStatus.imageViewNetworkStatus.let { imageView ->
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.network_error)
                    }
                }
                else -> {
                    binding.scrollViewDetails.visibility = View.VISIBLE
                    binding.lStatus.root.visibility = View.GONE
                }
            }
        }
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun getCurrentProduct() {
        viewModel.getProductById(requireArguments().getInt(ITEM_ID))
    }

}