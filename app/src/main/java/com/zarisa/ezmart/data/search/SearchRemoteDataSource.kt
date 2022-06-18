package com.zarisa.ezmart.data.search

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import com.zarisa.ezmart.ui.NetworkCall
import com.zarisa.ezmart.ui.Resource
import retrofit2.Response
import javax.inject.Inject


class SearchRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun searchMatches(
        searchText: String,
        order: SearchOrder,
        category: String
    ): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getListOfSearchMatchProducts(
                    searchText,
                    order.orderby,
                    order.order,
                    category
                )
            }
        }.fetch()
    }

    suspend fun searchMatches(searchText: String, order: SearchOrder): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getListOfSearchMatchProducts(
                    searchText,
                    order.orderby,
                    order.order
                )
            }
        }.fetch()

    }
}