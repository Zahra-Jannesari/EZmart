package com.zarisa.ezmart.data.category

import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.ui.Resource
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryRemoteDataSource: CategoryRemoteDataSource) {
    suspend fun getCategoriesList():
            Resource<List<Category>> {
        return categoryRemoteDataSource.getCategoriesList()
    }

    suspend fun getProductsOfSpecificCategory(categoryId: Int):
            Resource<List<Product>> {
        return categoryRemoteDataSource.getListOfCategoryProducts(categoryId).apply {
            this.data?.filter {
                it.id != NetworkParams.SPECIAL_OFFERS
            }
        }
    }

    suspend fun getCurrentCategory(categoryId: Int): Resource<Category> {
        return categoryRemoteDataSource.getCategoryById(categoryId)
    }
}