package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.domain.NetworkCall
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.*
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

    suspend fun createReview(reviewBody: Review): Resource<Review> {
        return object : NetworkCall<Review>() {
            override suspend fun createCall(): Response<Review> {
                return apiService.createReview(review = reviewBody)
            }
        }.fetch()
    }

    suspend fun deleteReview(reviewId: Int): Resource<ReviewDeleted> {
        return object : NetworkCall<ReviewDeleted>() {
            override suspend fun createCall(): Response<ReviewDeleted> {
                return apiService.deleteReview(reviewId)
            }
        }.fetch()
    }

    suspend fun updateReview(review: Review): Resource<Review> {
        return object : NetworkCall<Review>() {
            override suspend fun createCall(): Response<Review> {
                return apiService.updateReview(review.id, review = review)
            }
        }.fetch()
    }

    suspend fun getListOfRelatedProducts(include: String): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getListOfRelatedProducts(include)
            }
        }.fetch()
    }
}