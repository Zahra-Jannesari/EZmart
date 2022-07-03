package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //product
    @GET("products/")
    suspend fun getListOfProducts(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("include") include: String = "",
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<Product>

    @GET("products/{id}")
    suspend fun getCartProductById(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<CartProduct>

    @GET("products/reviews/")
    suspend fun getProductReviews(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("product") productId: List<Int>,
        @Query("per_page") perPage: Int = 100
    ): Response<List<Review>>

    //category
    @GET("products/categories")
    suspend fun getListOfAllCategories(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<Category>>

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
        @Query("category") categoryId: String,
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
        @Query("attribute") attribute: String,
        @Query("attribute_term") attribute_term: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>

    @GET("products/")
    suspend fun getListOfSearchMatchProducts(
        @Query("search") searchText: String,
        @Query("category") categoryId: String,
        @Query("orderby") orderby: String,
        @Query("order") order: String,
        @Query("attribute") attribute: String,
        @Query("attribute_term") attribute_term: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<Product>>

    @GET("products/attributes/")
    suspend fun getListOfAttributes(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
    ): Response<List<AttrProps>>

    @GET("products/attributes/{id}/terms")
    suspend fun getListOfAttributeTerms(
        @Path("id") attributeId: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Query("per_page") perPage: Int = 100
    ): Response<List<AttrProps>>


    //shopping
    @POST("orders/")
    suspend fun createOrder(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Body order: Order
    ): Response<Order>

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Body order: Order
    ): Response<Order>

    @GET("orders/{id}")
    suspend fun retrieveOrder(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<Order>

    @GET("orders/")
    suspend fun getCustomerOrders(
        @Query("customer") customerId: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<List<Order>>

    //customer
    @POST("customers/")
    suspend fun createCustomer(
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions(),
        @Body customer: Customer
    ): Response<Customer>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Int,
        @QueryMap options: Map<String, String> = NetworkParams.getBaseOptions()
    ): Response<Customer>
}