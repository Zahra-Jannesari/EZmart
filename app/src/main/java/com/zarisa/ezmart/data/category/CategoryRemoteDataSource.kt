package com.zarisa.ezmart.data.category

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCategoriesList(): List<Category> {
        return apiService.getListOfAllCategories()
    }

    suspend fun getCategoryById(categoryId: Int): Category {
        return apiService.getCategoryById(categoryId)
    }

    suspend fun getListOfCategoryProducts(categoryId: Int): List<Product> {
        return apiService.getProductsByCategory(categoryId = categoryId.toString())
    }
}