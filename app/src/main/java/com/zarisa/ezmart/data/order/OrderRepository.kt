package com.zarisa.ezmart.data.order

import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.Customer
import retrofit2.Response
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

    suspend fun getCustomerOrders(customerId: Int): Resource<List<Order>> {
        return orderRemoteDataSource.getCustomerOrders(customerId)
    }

    suspend fun createCustomer(customer: Customer): Resource<Customer> {
        return orderRemoteDataSource.createCustomer(customer)
    }

    suspend fun getCustomer(customerId: Int): Resource<Customer> {
        return orderRemoteDataSource.getCustomer(customerId)
    }
}