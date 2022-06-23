package com.zarisa.ezmart.ui.search

import androidx.lifecycle.*
import com.zarisa.ezmart.data.search.SearchRepository
import com.zarisa.ezmart.model.Attribute
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {
    val listOfAllAttributes = MutableLiveData<Attribute>()
    private val listOfSearchMatchProducts = MutableLiveData<List<Product>>()
    var searchText = ""
    private val attributeFilter = mutableListOf<Attribute>()
    var categoryId = 0
    var categoryName: String = "همه محصولات"
    var statusMessage = ""
    val statusLiveData = MutableLiveData<Status?>()
    private var searchListOrder = SearchOrder.NEWEST
    fun listOfFilteredSearchProducts(): LiveData<List<Product>> =
        Transformations.map(listOfSearchMatchProducts) { list ->
            list?.let {
                when (searchListOrder) {
                    SearchOrder.LOW_PRICE -> list.sortedBy { Integer.parseInt(it.price) }
                    SearchOrder.HIGH_PRICE -> list.sortedBy { Integer.parseInt(it.price) }
                        .reversed()
                    SearchOrder.BEST_SELLERS -> list.sortedBy { it.total_sales }.reversed()
                    else -> list.sortedBy { it.date_created }.reversed()
                }.filter { product ->
                    product.attributes.containsAll(attributeFilter)
                }
            }
        }

    //call by clicking on search icon in text field
    fun getSearchResults() {
        viewModelScope.launch {
            if (searchText.isNotBlank()) {
                statusLiveData.postValue(Status.LOADING)
                searchRepository.getListOfSearchMatches(categoryId, searchText).let {
                    statusLiveData.value = it.status
                    statusMessage = it.message
                    if (it.status == Status.SUCCESSFUL)
                        listOfSearchMatchProducts.value = it.data?.filter { product ->
                            product.name.contains(searchText)
                        }
                }
            }

        }
    }

    //call by clicking on each filter
    fun setFilter() {

    }

    //call by clicking on order
    fun setOrder(order: SearchOrder) {
        searchListOrder = order
        getSearchResults()
    }
}