package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.model.OrderByEnum
import com.zarisa.ezmart.model.Product
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProductsList(order: OrderByEnum): List<Product> {
        return when (order) {
            OrderByEnum.DATE -> apiService.getListOfProducts(options = NetworkParams.getDateOption())
            OrderByEnum.POPULARITY -> apiService.getListOfProducts(options = NetworkParams.getPopularityOption())
            else -> apiService.getListOfProducts(options = NetworkParams.getRateOption())
        }
    }

    suspend fun getProductById(id: Int): Product {
        return apiService.getProductById(id)
    }

    suspend fun getSpecialOffers(): Product {
        return apiService.getProductById(NetworkParams.SPECIAL_OFFERS)
    }
}