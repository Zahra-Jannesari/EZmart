package com.zarisa.ezmart.data.search

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.domain.NetworkCall
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.AttrProps
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import retrofit2.Response
import javax.inject.Inject


class SearchRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    //search with category
    suspend fun searchMatches(
        searchText: String,
        order: SearchOrder,
        category: String
    ): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getListOfSearchMatchProducts(
                    searchText,
                    category,
                    order.orderby,
                    order.order
                )
            }
        }.fetch()
    }

    //search without category
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

    //filtered search with category
    suspend fun searchMatches(
        searchText: String,
        order: SearchOrder,
        attr: String,
        attrTerm: Int,
        category: String
    ): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getListOfSearchMatchProducts(
                    searchText,
                    category,
                    order.orderby,
                    order.order, attr, attrTerm
                )
            }
        }.fetch()
    }

    //filtered search without category
    suspend fun searchMatches(
        searchText: String, order: SearchOrder, attr: String,
        attrTerm: Int,
    ): Resource<List<Product>> {
        return object : NetworkCall<List<Product>>() {
            override suspend fun createCall(): Response<List<Product>> {
                return apiService.getListOfSearchMatchProducts(
                    searchText,
                    order.orderby,
                    order.order, attr, attrTerm
                )
            }
        }.fetch()
    }


    suspend fun getListOfAttributes(
    ): Resource<List<AttrProps>> {
        return object : NetworkCall<List<AttrProps>>() {
            override suspend fun createCall(): Response<List<AttrProps>> {
                return apiService.getListOfAttributes()
            }
        }.fetch()
    }

    suspend fun getListOfAttributeTerms(
        attrId: Int
    ): Resource<List<AttrProps>> {
        return object : NetworkCall<List<AttrProps>>() {
            override suspend fun createCall(): Response<List<AttrProps>> {
                return apiService.getListOfAttributeTerms(
                    attributeId = attrId
                )
            }
        }.fetch()
    }
}