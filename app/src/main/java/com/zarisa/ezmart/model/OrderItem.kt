package com.zarisa.ezmart.model

data class OrderItem(
    val product_id: Int,
    var quantity: Int = 1
)