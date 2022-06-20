package com.zarisa.ezmart.model

import com.squareup.moshi.Json

data class Order(
    val id: Int = 0,
    val total: String = "0",
    var customer_id:Int=0
) {
    @Json(name = "line_items")
    var lineItems: List<OrderItem> = mutableListOf()
}