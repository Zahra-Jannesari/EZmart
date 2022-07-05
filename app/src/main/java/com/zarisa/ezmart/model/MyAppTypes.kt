package com.zarisa.ezmart.model

enum class Status {
    LOADING, SUCCESSFUL, NETWORK_ERROR, SERVER_ERROR, NOT_FOUND, EMPTY_CART
}

enum class OrderByEnum(val orderName: String) {
    DATE("date"), POPULARITY("popularity"), RATING("rating")
}

enum class SearchOrder(val orderby: String, val order: String) {
    BEST_SELLERS("popularity", "desc"),
    HIGH_PRICE("price", "desc"),
    LOW_PRICE("price", "asc"),
    NEWEST("date", "desc")
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
typealias OnEditCartItem = (Int, Int) -> Unit
typealias OnDeleteReview=(Int)->Unit
typealias OnEditReview=(Review)->Unit

const val ITEM_ID = "id"
const val SEARCH_ORIGIN = "search_origin"
const val SEARCH_IN_ALL = "all"
const val CATEGORY_ITEM = "category"
const val CUSTOMER = "Customer"
const val CUSTOMER_ID = "Customer Id"
const val ORDER_ID = "order id"
const val USER_EMAIL="user name"
const val USER_NAME="name"