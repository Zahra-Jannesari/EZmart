package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.domain.NetworkCall
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.CartProduct
import com.zarisa.ezmart.model.OrderByEnum
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.Review
import retrofit2.Response
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getProductsList(order: OrderByEnum, include: List<Int>): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return when (order) {
                    OrderByEnum.DATE -> apiService.getListOfProducts(
                        NetworkParams.getDateOption(),
                        include.toString()
                    )
                    OrderByEnum.POPULARITY -> apiService.getListOfProducts(
                        NetworkParams.getPopularityOption(),
                        include.toString()
                    )
                    else -> apiService.getListOfProducts(
                        NetworkParams.getRateOption(),
                        include.toString()
                    )
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

    suspend fun getCartProductById(id: Int): Resource<CartProduct> {
        return object : NetworkCall<CartProduct>() {
            override suspend fun createCall(): Response<CartProduct> {
                return apiService.getCartProductById(id)
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