package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product
import retrofit2.http.*

interface ApiService {

    @GET("products/")
    suspend fun getListOfProducts(
        @QueryMap options: Map<String, String>,
        @Query("per_page") perPage: Int = 100
    ): List<Product>

    @GET("products/categories")
    suspend fun getListOfAllCategories(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
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
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100
    ): List<Product>


    //search and filter
    @GET("products/")
    suspend fun getListOfSearchMatchProducts(
        @Query("search") searchText: String,
        @Query("orderby") orderby: String,
        @Query("order") order: String,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): List<Product>

    @GET("products/")
    suspend fun getListOfSearchMatchProducts(
        @Query("search") searchText: String,
        @Query("orderby") orderby: String,
        @Query("order") order: String,
        @Query("category") categoryId: String,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): List<Product>

}