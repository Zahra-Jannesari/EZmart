package com.zarisa.ezmart.data.order

import com.zarisa.ezmart.model.Order
import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.Customer
import javax.inject.Inject

class CustomerRepository @Inject constructor(private val customerRemoteDataSource: CustomerRemoteDataSource) {
    suspend fun createOrder(order: Order): Resource<Order> {
        return customerRemoteDataSource.createOrder(order)
    }

    suspend fun updateOrder(order: Order, orderId: Int): Resource<Order> {
        return customerRemoteDataSource.updateOrder(order, orderId)
    }

    suspend fun retrieveOrder(orderId: Int): Resource<Order> {
        return customerRemoteDataSource.retrieveOrder(orderId)
    }

    suspend fun getCustomerOrders(customerId: Int): Resource<List<Order>> {
        return customerRemoteDataSource.getCustomerOrders(customerId)
    }

    suspend fun createCustomer(customer: Customer): Resource<Customer> {
        return customerRemoteDataSource.createCustomer(customer)
    }

    suspend fun getCustomer(customerId: Int): Resource<Customer> {
        return customerRemoteDataSource.getCustomer(customerId)
    }
}