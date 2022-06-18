package com.zarisa.ezmart.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.Status
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    val networkStatusLiveData = MutableLiveData<Status>()
    val currentProduct = MutableLiveData<Product?>()
    val reviewsList = MutableLiveData<List<Review>?>()
    var statusMessage = ""
    fun initialProduct(id: Int) {
        networkStatusLiveData.value = Status.LOADING
        viewModelScope.launch {
            productRepository.getProductById(id).let {
                networkStatusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL) {
                    currentProduct.value = it.data
                    reviewsList.value = productRepository.getProductReviews(id).data
                }
            }
        }
    }
}