package com.zarisa.ezmart.model

import com.squareup.moshi.Json

data class OrderItem(
    val id: Int = 0,
    @Json(name = "name") val productName: String = "",
    val product_id: Int,
    val total: String = "0",
    val price: String = "0"
) {
    var quantity: Int = 1
}
