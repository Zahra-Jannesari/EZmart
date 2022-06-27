package com.zarisa.ezmart.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.Status
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    val statusLiveData = MutableLiveData<Status>()
    var statusMessage = ""
    val newestProductsList = MutableLiveData<List<Product>?>()
    val bestSellerProductsList = MutableLiveData<List<Product>?>()
    val highRateProductsList = MutableLiveData<List<Product>?>()
    val specialOffersList = MutableLiveData<Product?>()
    fun getMainProductsLists() {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            val specialOffers = productRepository.getSpecialOffers()
            val highRateProducts = productRepository.getListOfHighRatedProducts()
            val bestSellerProducts = productRepository.getListOfMostSeenProducts()
            val newestProducts = productRepository.getListOfNewestProducts()
            if (highRateProducts.status == Status.SUCCESSFUL) {
                if (newestProducts.status == bestSellerProducts.status && newestProducts.status == Status.SUCCESSFUL) {
                    statusLiveData.value = Status.SUCCESSFUL
                    highRateProductsList.value = highRateProducts.data?.filter { it.id!=NetworkParams.SPECIAL_OFFERS }
                    bestSellerProductsList.value = bestSellerProducts.data?.filter { it.id!=NetworkParams.SPECIAL_OFFERS }
                    newestProductsList.value = newestProducts.data?.filter { it.id!=NetworkParams.SPECIAL_OFFERS }
                    specialOffersList.value = specialOffers.data
                }
            } else {
                statusMessage = highRateProducts.message
                statusLiveData.value = highRateProducts.status
            }
        }
    }
}


