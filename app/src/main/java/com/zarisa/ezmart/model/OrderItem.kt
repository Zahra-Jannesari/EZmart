package com.zarisa.ezmart.model

import com.squareup.moshi.Json

data class OrderItem(
    val id: Int,
    @Json(name = "name") val productName: String,
    val product_id: Int,
    val quantity: Int,
    val subtotal: String,
    val total: String,
    val price: String
)
