package com.zarisa.ezmart.domain

import com.google.gson.Gson
import com.zarisa.ezmart.model.ErrorResponse
import com.zarisa.ezmart.model.Status
import okhttp3.ResponseBody
import retrofit2.Response

const val REQUEST_NOT_FOUND = "نتیجه یافت نشد."
const val NETWORK_EXCEPTION = "لطفا اتصال اینترنت خود را چک کنید."

data class Resource<T>(
    var status: Status,
    var data: T?,
    var message: String,
    var serverMessage: ErrorResponse?
)

fun handleServerRequestCode(code: Int): String {
    return when (code) {
        200 -> "درخواست موفقیت آمیز بود."
        201 -> "منبع درخواستی با موفقیت بر روی سرور ایجاد شد."
        404 -> REQUEST_NOT_FOUND
        in 202..299 -> "درخواست با موفقیت ارسال شد."
        in 300..399 -> "صفحه مورد نظر یافت نشد."
        in 400..499 -> "مشکلی از سمت کاربر وجود دارد."
        in 500..599 -> "مشکلی از سمت سرور وجود دارد."
        else -> "مشکلی رخ داده است."
    }
}

abstract class NetworkCall<ResultType> {
    suspend fun fetch(): Resource<ResultType> {
        return try {
            val response = createCall()
            val massage = handleServerRequestCode(response.code())
            val serverMessage = convertErrorBody(response.errorBody())
            when {
                response.isSuccessful -> Resource(Status.SUCCESSFUL, response.body(), massage, null)
                massage == REQUEST_NOT_FOUND -> Resource(
                    Status.NOT_FOUND,
                    null,
                    massage,
                    serverMessage
                )
                else -> Resource(Status.SERVER_ERROR, null, massage, serverMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource(Status.NETWORK_ERROR, null, NETWORK_EXCEPTION, null)
        }
    }

    abstract suspend fun createCall(): Response<ResultType>
}

private fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
    return try {
        errorBody?.source()?.let {
            Gson().fromJson(it.readUtf8(), ErrorResponse::class.java)
        }
    } catch (exception: Exception) {
        null
    }
}