package com.zarisa.ezmart.model

import com.squareup.moshi.Json

data class OrderItem(
    val product_id: Int,
    var quantity: Int = 1,
    val id: Int = 0,
    @Json(name = "name") val productName: String = "",
    val total: String = "0"
)