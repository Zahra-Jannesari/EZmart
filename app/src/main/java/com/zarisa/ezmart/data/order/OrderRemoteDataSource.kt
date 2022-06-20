package com.zarisa.ezmart.data.order

import com.zarisa.ezmart.data.network.ApiService
import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.domain.NetworkCall
import com.zarisa.ezmart.domain.Resource
import retrofit2.Response
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(private val apiService: ApiService) {
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
}