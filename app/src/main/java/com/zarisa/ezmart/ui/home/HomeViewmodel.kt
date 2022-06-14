package com.zarisa.ezmart.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.NetworkStatus
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    val networkStatusLiveData = MutableLiveData<NetworkStatus>()

    val newestProductsList = MutableLiveData<List<Product>>()
    val mostSeenProductsList = MutableLiveData<List<Product>>()
    val highRateProductsList = MutableLiveData<List<Product>>()
    val specialOffers = MutableLiveData<Product>()

    fun getMainProductsLists() {
        networkStatusLiveData.value = NetworkStatus.LOADING
        viewModelScope.launch {
            try {
                getNewestProducts()
                getMostSeenProducts()
                getHighRateProducts()
                getSpecialOffers()
                networkStatusLiveData.value = NetworkStatus.SUCCESSFUL
            } catch (e: Exception) {
                networkStatusLiveData.value = NetworkStatus.ERROR
            }

        }
    }

    private suspend fun getSpecialOffers() {
        specialOffers.value = productRepository.getSpecialOffers()
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


