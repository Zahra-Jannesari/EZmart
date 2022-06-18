package com.zarisa.ezmart.data.category

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.model.*
import com.zarisa.ezmart.ui.NetworkCall
import com.zarisa.ezmart.ui.Resource
import retrofit2.Response
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCategoriesList(): Resource<List<Category>> {
        return object : NetworkCall<List<Category>>() {
            override suspend fun createCall(): Response<List<Category>> {
                return apiService.getListOfAllCategories()
            }
        }.fetch()
    }

    suspend fun getCategoryById(categoryId: Int): Resource<Category> {
        return object : NetworkCall<Category>() {
            override suspend fun createCall(): Response<Category> {
                return apiService.getCategoryById(categoryId)
            }
        }.fetch()
    }

    suspend fun getListOfCategoryProducts(categoryId: Int): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getProductsByCategory(categoryId = categoryId.toString())
            }
        }.fetch()
    }
}