package com.zarisa.ezmart.model

enum class Status {
    LOADING, SUCCESSFUL, NETWORK_ERROR, SERVER_ERROR, NOT_FOUND
}

enum class OrderByEnum(val orderName: String) {
    DATE("date"), POPULARITY("popularity"), RATING("rating")
}

enum class SearchOrder {
    BEST_SELLERS, HIGH_PRICE, LOW_PRICE, NEWEST
}

enum class OrderingStatus(val message: String) {
    LOADING_ORDER("در حال افزودن آیتم به سبد خرید"),
    ALREADY_ADDED("آیتم در سبد خرید شماست."),
    ITEM_ADDED("آیتم با موفقیت به سبد خرید افزوده شد."),
    ORDER_ERROR_SERVER("خطایی رخ داده است."),
    ORDER_ERROR_INTERNET("اتصال اینترنت خود را چک کنید.")
}

typealias OnItemClick = (Int) -> Unit
typealias OnCategoryClick = (Category) -> Unit

const val ITEM_ID = "id"
const val SEARCH_ORIGIN = "search_origin"
const val SEARCH_IN_ALL = "all"
const val CATEGORY_ITEM = "category"
const val CUSTOMER = "Customer"
const val CUSTOMER_ID = "Customer Id"
const val ORDER_ID = "order id"