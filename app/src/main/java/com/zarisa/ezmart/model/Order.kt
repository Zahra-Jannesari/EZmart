package com.zarisa.ezmart.model

data class Order(
    val id: Int = 0,
    val total:String="",
    var customer_id: Int = 0,
    var line_items: List<OrderItem> = listOf()
)