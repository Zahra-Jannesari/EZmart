package com.zarisa.ezmart.data.search

import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.domain.Resource
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchRemoteDataSource: SearchRemoteDataSource) {
    suspend fun getListOfSearchMatches(
        categoryId: Int,
        searchText: String
    ): Resource<List<Product>> {
        return if (categoryId == 0) searchRemoteDataSource.searchMatches(searchText)
        else searchRemoteDataSource.searchMatches(searchText, categoryId.toString())
    }

}