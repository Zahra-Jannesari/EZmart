package com.zarisa.ezmart.data.order

import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.ui.Resource
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderRemoteDataSource: OrderRemoteDataSource) {
    suspend fun createOrder(order: Order): Resource<Order> {
        return orderRemoteDataSource.createOrder(order)
    }

    suspend fun updateOrder(order: Order, orderId: Int): Resource<Order> {
        return orderRemoteDataSource.updateOrder(order, orderId)
    }

    suspend fun retrieveOrder(orderId: Int): Resource<Order> {
        return orderRemoteDataSource.retrieveOrder(orderId)
    }
}