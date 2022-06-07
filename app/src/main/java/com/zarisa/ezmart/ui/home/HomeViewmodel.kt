package com.zarisa.ezmart.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    val newestProductsList = MutableLiveData<List<Product>>()
    val mostSeenProductsList = MutableLiveData<List<Product>>()
    val highRateProductsList = MutableLiveData<List<Product>>()

    fun getMainProductsLists() {
        viewModelScope.launch {
            getNewestProducts()
            getMostSeenProducts()
            getHighRateProducts()
        }

    }

    private suspend fun getHighRateProducts() {
        highRateProductsList.value = productRepository.getListOfHighRatedProducts()
    }

    private suspend fun getMostSeenProducts() {
        mostSeenProductsList.value = productRepository.getListOfMostSeenProducts()
    }

    private suspend fun getNewestProducts() {
        newestProductsList.value = productRepository.getListOfNewestProducts()
    }
}

