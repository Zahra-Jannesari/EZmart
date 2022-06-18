package com.zarisa.ezmart.model

enum class Status {
    LOADING, SUCCESSFUL, NETWORK_ERROR, SERVER_ERROR, NOT_FOUND
}

enum class OrderByEnum(val orderName: String) {
    DATE("date"), POPULARITY("popularity"), RATING("rating")
}

enum class SearchOrder() {
    BEST_SELLERS, HIGH_PRICE, LOW_PRICE, NEWEST
}

typealias OnItemClick = (Int) -> Unit
typealias OnCategoryClick = (Category) -> Unit

const val ITEM_ID = "id"
const val SEARCH_ORIGIN = "search_origin"
const val SEARCH_IN_ALL = "all"
const val CATEGORY_ITEM = "category"