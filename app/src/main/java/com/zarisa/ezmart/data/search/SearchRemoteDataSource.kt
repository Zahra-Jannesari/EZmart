package com.zarisa.ezmart.data.search

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun searchMatches(
        searchText: String,
        order: SearchOrder,
        category: String
    ): List<Product> {
        return apiService.getListOfSearchMatchProducts(
            searchText,
            order.orderby,
            order.order,
            category
        )
    }

    suspend fun searchMatches(searchText: String, order: SearchOrder): List<Product> {
        return apiService.getListOfSearchMatchProducts(searchText, order.orderby, order.order)
    }
}