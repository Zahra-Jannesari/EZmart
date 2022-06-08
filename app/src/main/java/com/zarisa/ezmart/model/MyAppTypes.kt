package com.zarisa.ezmart.model

enum class NetworkStatus {
    LOADING, SUCCESSFUL, ERROR
}
typealias OnItemClick = (Int) -> Unit

enum class OrderByEnum {
    DATE, POPULARITY, RATING
}

const val ITEM_ID = "id"