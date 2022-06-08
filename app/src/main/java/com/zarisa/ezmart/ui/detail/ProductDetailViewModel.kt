package com.zarisa.ezmart.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    val currentProduct = MutableLiveData<Product>()
    fun getProductById(id:Int){
        viewModelScope.launch {
            currentProduct.value=productRepository.getProductById(id)
        }
    }
}
