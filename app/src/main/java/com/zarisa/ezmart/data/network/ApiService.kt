package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.Review
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("products/")
    suspend fun getListOfProducts(
        @QueryMap options: Map<String, String>,
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>

    @GET("products/categories")
    suspend fun getListOfAllCategories(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<Category>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<Product>

    @GET("products/categories/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<Category>

    @GET("products/")
    suspend fun getProductsByCategory(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("category") categoryId: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>

    @GET("products/reviews/")
    suspend fun getProductReviews(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("product") productId: List<Int>,
        @Query("per_page") perPage: Int = 100
    ): Response<List<Review>>


    //search and filter
    @GET("products/")
    suspend fun getListOfSearchMatchProducts(
        @Query("search") searchText: String,
        @Query("orderby") orderby: String,
        @Query("order") order: String,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>

    @GET("products/")
    suspend fun getListOfSearchMatchProducts(
        @Query("search") searchText: String,
        @Query("orderby") orderby: String,
        @Query("order") order: String,
        @Query("category") categoryId: String,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>
}