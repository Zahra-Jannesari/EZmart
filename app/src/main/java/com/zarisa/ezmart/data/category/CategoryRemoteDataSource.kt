package com.zarisa.ezmart.data.category

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.model.Category
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCategoriesList(): List<Category> {
        return apiService.getListOfAllCategories()
    }
}