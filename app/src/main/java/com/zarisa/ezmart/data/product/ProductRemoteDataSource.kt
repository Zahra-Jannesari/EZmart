package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.domain.NetworkCall
import com.zarisa.ezmart.domain.Resource
import retrofit2.Response
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProductsList(order: OrderByEnum): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return when (order) {
                    OrderByEnum.DATE -> apiService.getListOfProducts(options = NetworkParams.getDateOption())
                    OrderByEnum.POPULARITY -> apiService.getListOfProducts(options = NetworkParams.getPopularityOption())
                    else -> apiService.getListOfProducts(options = NetworkParams.getRateOption())
                }
            }
        }.fetch()
    }

    suspend fun getProductById(id: Int): Resource<Product> {
        return object : NetworkCall<Product>() {
            override suspend fun createCall(): Response<Product> {
                return apiService.getProductById(id)
            }
        }.fetch()
    }

    suspend fun getSpecialOffers(): Resource<Product> {
        return object : NetworkCall<Product>() {
            override suspend fun createCall(): Response<Product> {
                return apiService.getProductById(NetworkParams.SPECIAL_OFFERS)
            }
        }.fetch()
    }

    suspend fun getProductReviews(productId: List<Int>): Resource<List<Review>> {
        return object : NetworkCall<List<Review>>() {
            override suspend fun createCall(): Response<List<Review>> {
                return apiService.getProductReviews(productId = productId)
            }
        }.fetch()
    }
}