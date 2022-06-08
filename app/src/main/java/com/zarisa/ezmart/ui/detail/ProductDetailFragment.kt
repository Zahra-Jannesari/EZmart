package com.zarisa.ezmart.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zarisa.ezmart.databinding.FragmentProductDetailBinding
import com.zarisa.ezmart.model.ITEM_ID
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
    }

    private fun bindView() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun getCurrentProduct() {
        viewModel.getProductById(requireArguments().getInt(ITEM_ID))
    }

}