package com.zarisa.ezmart.data.search

import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchRemoteDataSource: SearchRemoteDataSource) {
    suspend fun getListOfSearchMatches(
        categoryId: Int,
        searchOrder: SearchOrder,
        searchText: String
    ): List<Product> {
        return if (categoryId == 0) searchRemoteDataSource.searchMatches(searchText, searchOrder)
        else searchRemoteDataSource.searchMatches(searchText, searchOrder, categoryId.toString())
    }

}