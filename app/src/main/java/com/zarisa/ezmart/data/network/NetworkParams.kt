package com.zarisa.ezmart.data.network

import com.zarisa.ezmart.model.OrderByEnum

class NetworkParams {
    companion object {
        const val BASE_URL = "https://woocommerce.maktabsharif.ir/wp-json/wc/v3/"
        private const val CONSUMER_KEY = "ck_63f4c52da932ddad1570283b31f3c96c4bd9fd6f"//ck_cfa862db1553b15dc59f6b27b14f343fe5f74e0e
        private const val CONSUMER_SECRET = "cs_294e7de35430398f323b43c21dd1b29f67b5370b"//cs_5de453d21f1ff46c90f7412f9e441882574d9af2
        private const val ORDER_BY = "orderby"
        const val SPECIAL_OFFERS = 608

        fun getBaseOptions(): Map<String, String> {
            val baseOptionsHashMap = HashMap<String, String>()
            baseOptionsHashMap["consumer_key"] = CONSUMER_KEY
            baseOptionsHashMap["consumer_secret"] = CONSUMER_SECRET
            return baseOptionsHashMap
        }

        fun getDateOption(): Map<String, String> {
            val timeOptionsHashMap = HashMap<String, String>()
            timeOptionsHashMap.putAll(getBaseOptions())
            timeOptionsHashMap[ORDER_BY] = OrderByEnum.DATE.orderName
            return timeOptionsHashMap
        }

        fun getRateOption(): Map<String, String> {
            val timeOptionsHashMap = HashMap<String, String>()
            timeOptionsHashMap.putAll(getBaseOptions())
            timeOptionsHashMap[ORDER_BY] = OrderByEnum.RATING.orderName
            return timeOptionsHashMap
        }

        fun getPopularityOption(): Map<String, String> {
            val timeOptionsHashMap = HashMap<String, String>()
            timeOptionsHashMap.putAll(getBaseOptions())
            timeOptionsHashMap[ORDER_BY] = OrderByEnum.POPULARITY.orderName
            return timeOptionsHashMap
        }
    }
}