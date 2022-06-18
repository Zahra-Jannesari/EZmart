package com.zarisa.ezmart.data.product

import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.model.OrderByEnum
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.Review
import com.zarisa.ezmart.ui.Resource
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productRemoteDataSource: ProductRemoteDataSource) {
    private suspend fun getListOfProducts(order: OrderByEnum): Resource<List<Product>> {
        return productRemoteDataSource.getProductsList(order).apply {
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

    suspend fun getSpecialOffers(): Resource<Product> {
        return productRemoteDataSource.getSpecialOffers()
    }

    suspend fun getProductReviews(productId: Int): Resource<List<Review>> {
        return productRemoteDataSource.getProductReviews(listOf(productId))
    }
}