package com.zarisa.ezmart.data.category

import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryRemoteDataSource: CategoryRemoteDataSource) {
    suspend fun getCategoriesList(): List<Category> {
        return categoryRemoteDataSource.getCategoriesList()
    }

    suspend fun getProductsOfSpecificCategory(categoryId: Int): List<Product> {
        return categoryRemoteDataSource.getListOfCategoryProducts(categoryId)
    }

    suspend fun getCurrentCategory(categoryId: Int): Category {
        return categoryRemoteDataSource.getCategoryById(categoryId)
    }
}