package com.zarisa.ezmart.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.NetworkStatus
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    val networkStatusLiveData = MutableLiveData<NetworkStatus>()
    val currentProduct = MutableLiveData<Product>()
    val reviewsList = MutableLiveData<List<Review>>()
    fun initialProduct(id: Int) {
        networkStatusLiveData.value = NetworkStatus.LOADING
        viewModelScope.launch {
            try {
                getProductById(id)
                getReviews(id)
                networkStatusLiveData.value = NetworkStatus.SUCCESSFUL
            } catch (e: Exception) {
                networkStatusLiveData.value = NetworkStatus.ERROR
            }
        }
    }

    private suspend fun getProductById(id: Int) {
        currentProduct.value = productRepository.getProductById(id)
    }

    private suspend fun getReviews(id: Int) {
        reviewsList.value = productRepository.getProductReviews(id)
    }
}
