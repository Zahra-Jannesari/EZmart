package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.OrderByEnum

class NetworkParams {
    companion object {
        const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
        private const val CONSUMER_KEY = "ck_cfa862db1553b15dc59f6b27b14f343fe5f74e0e"
        private const val CONSUMER_SECRET = "cs_5de453d21f1ff46c90f7412f9e441882574d9af2"
        private const val ORDER_BY = "orderby"

        fun getBaseOptions(): Map<String, String> {
            val baseOptionsHashMap = HashMap<String, String>()
            baseOptionsHashMap["consumer_key"] = CONSUMER_KEY
            baseOptionsHashMap["consumer_secret"] = CONSUMER_SECRET
            return baseOptionsHashMap
        }

        fun getDateOption(): Map<String, String> {
            val timeOptionsHashMap = HashMap<String, String>()
            timeOptionsHashMap.putAll(getBaseOptions())
            timeOptionsHashMap[ORDER_BY] = OrderByEnum.DATE.toString().lowercase()
            return timeOptionsHashMap
        }

        fun getRateOption(): Map<String, String> {
            val timeOptionsHashMap = HashMap<String, String>()
            timeOptionsHashMap.putAll(getBaseOptions())
            timeOptionsHashMap[ORDER_BY] = OrderByEnum.RATING.toString().lowercase()
            return timeOptionsHashMap
        }

        fun getPopularityOption(): Map<String, String> {
            val timeOptionsHashMap = HashMap<String, String>()
            timeOptionsHashMap.putAll(getBaseOptions())
            timeOptionsHashMap[ORDER_BY] = OrderByEnum.POPULARITY.toString().lowercase()
            return timeOptionsHashMap
        }
    }
}