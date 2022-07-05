package com.zarisa.ezmart.model

data class Review(
    val id: Int = 0,
    val reviewer: String,
    var review: String,
    var rating: Int,
    val reviewer_email: String = "",
    val product_id: Int
)

data class ReviewDeleted(
    val deleted: Boolean
)
