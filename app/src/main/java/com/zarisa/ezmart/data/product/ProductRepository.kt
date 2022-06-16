package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.model.OrderByEnum
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.Review
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productRemoteDataSource: ProductRemoteDataSource) {
    private suspend fun getListOfProducts(order: OrderByEnum): List<Product> {
        return productRemoteDataSource.getProductsList(order).filter {
            it.id != NetworkParams.SPECIAL_OFFERS
        }
    }

    suspend fun getListOfNewestProducts(): List<Product> {
        return getListOfProducts(OrderByEnum.DATE)
    }

    suspend fun getListOfMostSeenProducts(): List<Product> {
        return getListOfProducts(OrderByEnum.POPULARITY)
    }

    suspend fun getListOfHighRatedProducts(): List<Product> {
        return getListOfProducts(OrderByEnum.RATING)
    }

    suspend fun getProductById(id: Int): Product {
        return productRemoteDataSource.getProductById(id)
    }

    suspend fun getSpecialOffers(): Product {
        return productRemoteDataSource.getSpecialOffers()
    }

    suspend fun getProductReviews(productId: Int): List<Review> {
        return productRemoteDataSource.getProductReviews(listOf(productId))
    }
}