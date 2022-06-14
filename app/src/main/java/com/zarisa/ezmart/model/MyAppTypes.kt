package com.zarisa.ezmart.model

enum class NetworkStatus {
    LOADING, SUCCESSFUL, ERROR
}

enum class OrderByEnum {
    DATE, POPULARITY, RATING
}

typealias OnItemClick = (Int) -> Unit
typealias OnCategoryClick = (Category) -> Unit

const val ITEM_ID = "id"
const val SEARCH_ORIGIN = "search_origin"
const val SEARCH_IN_ALL = "all"
const val CATEGORY_ITEM = "category"