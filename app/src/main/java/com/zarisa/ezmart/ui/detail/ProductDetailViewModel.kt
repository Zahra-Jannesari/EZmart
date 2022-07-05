package com.zarisa.ezmart.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.order.CustomerRepository
import com.zarisa.ezmart.data.product.ProductRepository
import com.zarisa.ezmart.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val REVIEWS = 10
const val RELATED_PRODUCTS = 20

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {
    val orderingStatus = MutableLiveData<OrderingStatus?>(null)
    val statusLiveData = MutableLiveData<Status>()
    val currentProduct = MutableLiveData<Product?>()
    val reviewsList = MutableLiveData<List<Review>?>()
    val relatedProductsList = MutableLiveData<List<Product>?>()
    var statusMessage = ""
    val selectedPartToShow = MutableLiveData(REVIEWS)
    val sideOptionsStatus = MutableLiveData<Status>()
    var customerName: String? = ""
    var customerEmail: String? = ""
    val reviewStatus = MutableLiveData<Status?>(null)

    fun initialProduct(id: Int) {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            productRepository.getProductById(id).let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL) {
                    currentProduct.value = it.data
                    getSideOptions()
                }
            }
        }
    }

    //order part
    suspend fun createOrder(order: Order): Int {
        orderingStatus.postValue(OrderingStatus.LOADING_ORDER)
        customerRepository.createOrder(order).let {
            when (it.status) {
                Status.SUCCESSFUL -> {
                    orderingStatus.postValue(OrderingStatus.ITEM_ADDED)
                    return it.data!!.id
                }
                Status.NETWORK_ERROR -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_INTERNET)
                else -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_SERVER)
            }
            return 0
        }
    }

    fun updateOrder(orderId: Int, orderItem: OrderItem) {
        orderingStatus.postValue(OrderingStatus.LOADING_ORDER)
        viewModelScope.launch {
            val order = getOrder(orderId)
            if (order != null) {
                if (isItemAlreadyExist(order.line_items, orderItem.product_id)) {
                    orderingStatus.postValue(OrderingStatus.ALREADY_ADDED)
                } else {
                    val newList = mutableListOf<OrderItem>()
                    newList.addAll(order.line_items)
                    newList.add(orderItem)
                    order.line_items = newList
                    customerRepository.updateOrder(order, orderId).let {
                        when (it.status) {
                            Status.SUCCESSFUL -> orderingStatus.postValue(OrderingStatus.ITEM_ADDED)
                            Status.NETWORK_ERROR -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_INTERNET)
                            else -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_SERVER)
                        }
                    }
                }
            }
        }
    }

    private fun isItemAlreadyExist(itemList: List<OrderItem>, newItemId: Int): Boolean {
        var itemAlreadyExist = false
        for (i in itemList) {
            if (i.product_id == newItemId)
                itemAlreadyExist = true
        }
        return itemAlreadyExist
    }

    private suspend fun getOrder(orderId: Int): Order? {
        customerRepository.retrieveOrder(orderId).let {
            when (it.status) {
                Status.SUCCESSFUL -> return it.data
                Status.NETWORK_ERROR -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_INTERNET)
                else -> orderingStatus.postValue(OrderingStatus.ORDER_ERROR_SERVER)
            }
            return null
        }
    }

    //side options(review and related product)
    private suspend fun getSideOptions() {
        sideOptionsStatus.postValue(Status.LOADING)
        var listsCompleted = getReviews()
        productRepository.getListOfProducts(include = currentProduct.value?.related_ids!!).let {
            if (it.status == Status.SUCCESSFUL)
                relatedProductsList.value = it.data
            else
                listsCompleted = false
        }
        if (listsCompleted)
            sideOptionsStatus.postValue(Status.SUCCESSFUL)
    }

    private suspend fun getReviews(): Boolean {
        currentProduct.value?.id?.let { id ->
            productRepository.getProductReviews(id).let {
                if (it.status == Status.SUCCESSFUL) {
                    reviewsList.value = it.data
                    return true
                }
            }
        }
        return false
    }

    fun createReview(rating: Int, review: String) {
        viewModelScope.launch {
            reviewStatus.postValue(Status.LOADING)
            val reviewBody = Review(
                0,
                customerName!!,
                review,
                rating,
                customerEmail!!,
                currentProduct.value!!.id
            )
            productRepository.createReview(reviewBody).let {
                reviewStatus.postValue(it.status)
//                if (it.status == Status.SUCCESSFUL)
//                    getReviews()
            }
        }
    }

    fun deleteReview(reviewId: Int) {
        reviewStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            productRepository.deleteReview(reviewId).let {
                reviewStatus.postValue(it.status)
//                if(it.data?.deleted == true)
//                    getReviews()
            }
        }
    }

    fun updateReview(review: Review) {
        reviewStatus.postValue(Status.LOADING)
        viewModelScope.launch {
            productRepository.updateReview(review).let {
                reviewStatus.postValue(it.status)
//                if (it.status == Status.SUCCESSFUL)
//                    getReviews()
            }
        }
    }
}