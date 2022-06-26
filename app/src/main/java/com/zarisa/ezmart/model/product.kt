package com.zarisa.ezmart.model

data class Product(
    val id: Int,
    val images: List<Image>,
    val name: String,
    val categories: List<Category>?,
    val description: String,
    val price: String,
    val short_description: String,
    val total_sales: Int,
    val on_sale: Boolean,
    val rating_count: Int?,
    val sale_price: String,
    val average_rating: String?,
    val tags: List<Tag>,
    val date_created: String,
    val date_created_gmt: String,
    val related_ids: List<Int>,
)