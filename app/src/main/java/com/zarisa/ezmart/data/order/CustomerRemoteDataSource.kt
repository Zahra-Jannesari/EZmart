package com.zarisa.ezmart.data.order

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.domain.NetworkCall
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.Coupon
import com.zarisa.ezmart.model.Customer
import com.zarisa.ezmart.model.Order
import retrofit2.Response
import javax.inject.Inject

class CustomerRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun createOrder(order: Order): Resource<Order> {
        return object : NetworkCall<Order>() {
            override suspend fun createCall(): Response<Order> {
                return apiService.createOrder(order = order)
            }
        }.fetch()
    }

    suspend fun updateOrder(order: Order, orderId: Int): Resource<Order> {
        return object : NetworkCall<Order>() {
            override suspend fun createCall(): Response<Order> {
                return apiService.updateOrder(order = order, id = orderId)
            }
        }.fetch()
    }

    suspend fun retrieveOrder(orderId: Int): Resource<Order> {
        return object : NetworkCall<Order>() {
            override suspend fun createCall(): Response<Order> {
                return apiService.retrieveOrder(id = orderId)
            }
        }.fetch()
    }

    suspend fun createCustomer(customer: Customer): Resource<Customer> {
        return object : NetworkCall<Customer>() {
            override suspend fun createCall(): Response<Customer> {
                return apiService.createCustomer(customer = customer)
            }
        }.fetch()
    }

    suspend fun getCustomer(customerId: Int): Resource<Customer> {
        return object : NetworkCall<Customer>() {
            override suspend fun createCall(): Response<Customer> {
                return apiService.getCustomer(id = customerId)
            }
        }.fetch()
    }

    suspend fun retrieveCoupon(coupon: String): Resource<List<Coupon>> {
        return object : NetworkCall<List<Coupon>>() {
            override suspend fun createCall(): Response<List<Coupon>> {
                return apiService.retrieveCoupon(coupon)
            }
        }.fetch()
    }
}