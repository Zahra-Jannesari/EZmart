package com.zarisa.ezmart.ui

import com.bumptech.glide.load.HttpException
import com.zarisa.ezmart.model.Status
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class Resource<T>(var status: Status, var data: T?, var message: String? = null)

const val REQUEST_NOT_FOUND = "نتیجه یافت نشد."
fun handleServerRequestCode(code: Int): String {
    return when (code) {
        200 -> "درخواست موفقیت آمیز بود."
        404 -> REQUEST_NOT_FOUND
        in 201..299 -> "درخواست با موفقیت ارسال شد."
        in 300..399 -> "صفحه مورد نظر یافت نشد."
        in 400..499 -> "مشکلی از سمت کاربر وجود دارد. لطفا مشکل را به ما گزارش دهید."
        in 500..599 -> "مشکلی از سمت سرور وجود دارد. لطفا مشکل را به ما گزارش دهید."
        else -> "مشکلی رخ داده است."
    }
}

abstract class NetworkCall<ResultType> {
    suspend fun fetch(): Resource<ResultType> {
        return try {
            val response = createCall()
            val massage = handleServerRequestCode(response.code())
            when {
                response.isSuccessful -> Resource(Status.SUCCESSFUL, response.body(), massage)
                massage == REQUEST_NOT_FOUND -> Resource(Status.NOT_FOUND, null, massage)
                else -> Resource(Status.SERVER_ERROR, null, massage)
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource(Status.NETWORK_ERROR, null, e.message)

        } catch (e: ConnectException) {
            e.printStackTrace()
            Resource(Status.NETWORK_ERROR, null, e.message)

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Resource(Status.NETWORK_ERROR, null, e.message)

        } catch (e: UnknownHostException) {
            e.printStackTrace()
            Resource(Status.NETWORK_ERROR, null, e.message)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource(Status.NETWORK_ERROR, null, e.message)
        }
    }

    abstract suspend fun createCall(): Response<ResultType>
}