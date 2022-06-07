package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.Product
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("products/")
    suspend fun getListOfProducts(
        @QueryMap options: Map<String, String>
    ): List<Product>

}