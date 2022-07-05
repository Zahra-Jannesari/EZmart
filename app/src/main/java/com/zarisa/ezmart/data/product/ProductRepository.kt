package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.*
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productRemoteDataSource: ProductRemoteDataSource) {
    suspend fun getListOfProducts(
        order: OrderByEnum = OrderByEnum.DATE,
        include: List<Int> = emptyList()
    ): Resource<List<Product>> {
        return productRemoteDataSource.getProductsList(order, include).apply {
            this.data?.filter {
                it.id != NetworkParams.SPECIAL_OFFERS
            }
        }
    }

    suspend fun getListOfNewestProducts(): Resource<List<Product>> {
        return getListOfProducts(OrderByEnum.DATE)
    }

    suspend fun getListOfMostSeenProducts(): Resource<List<Product>> {
        return getListOfProducts(OrderByEnum.POPULARITY)
    }

    suspend fun getListOfHighRatedProducts(): Resource<List<Product>> {
        return getListOfProducts(OrderByEnum.RATING)
    }

    suspend fun getProductById(id: Int): Resource<Product> {
        return productRemoteDataSource.getProductById(id)
    }

    suspend fun getCartProductById(id: Int): Resource<CartProduct> {
        return productRemoteDataSource.getCartProductById(id)
    }

    suspend fun getSpecialOffers(): Resource<Product> {
        return productRemoteDataSource.getSpecialOffers()
    }

    suspend fun getProductReviews(productId: Int): Resource<List<Review>> {
        return productRemoteDataSource.getProductReviews(listOf(productId))
    }

    suspend fun createReview(reviewBody: Review): Resource<Review> {
        return productRemoteDataSource.createReview(reviewBody)
    }

    suspend fun deleteReview(reviewId: Int): Resource<ReviewDeleted> {
        return productRemoteDataSource.deleteReview(reviewId)
    }

    suspend fun updateReview(review: Review): Resource<Review> {
        return productRemoteDataSource.updateReview(review)
    }
}