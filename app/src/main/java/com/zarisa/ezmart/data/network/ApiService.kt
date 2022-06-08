package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product
import retrofit2.http.*

interface ApiService {

    @GET("products/")
    suspend fun getListOfProducts(
        @QueryMap options: Map<String, String>
    ): List<Product>

    @GET("products/categories")
    suspend fun getListOfAllCategories(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): List<Category>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Product

    @GET("products/categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Category

    @GET("products/")
    suspend fun getProductsByCategory(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("category") categoryId: String,
        @Query("page") page: Int = 1
    ): List<Product>
}