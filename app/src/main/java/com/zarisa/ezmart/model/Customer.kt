package com.zarisa.ezmart.model

data class Customer(
    val id: Int = 0,
    val email: String = "",
    val first_name: String = "",
    val avatar_url: String = "",
    val shipping: Shipping = Shipping(),
)