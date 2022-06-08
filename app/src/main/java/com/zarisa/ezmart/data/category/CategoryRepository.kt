package com.zarisa.ezmart.data.category

import com.zarisa.ezmart.model.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryRemoteDataSource: CategoryRemoteDataSource) {
    suspend fun getCategoriesList(): List<Category> {
        return categoryRemoteDataSource.getCategoriesList()
    }
}