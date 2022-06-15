package com.zarisa.ezmart.model

enum class NetworkStatus {
    LOADING, SUCCESSFUL, ERROR
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

typealias OnItemClick = (Int) -> Unit
typealias OnCategoryClick = (Category) -> Unit

const val ITEM_ID = "id"
const val SEARCH_ORIGIN = "search_origin"
const val SEARCH_IN_ALL = "all"
const val CATEGORY_ITEM = "category"