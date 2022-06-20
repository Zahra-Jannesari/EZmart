package com.zarisa.ezmart.model

data class Order(
    val id: Int = 0,
    val total: String = "0",
    var customer_id: Int = 0,
    var line_items: List<OrderItem> = listOf()
)