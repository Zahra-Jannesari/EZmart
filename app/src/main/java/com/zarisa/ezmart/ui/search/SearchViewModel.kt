package com.zarisa.ezmart.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.search.SearchRepository
import com.zarisa.ezmart.model.Attribute
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {
    val listOfAllAttributes = MutableLiveData<Attribute>()
    private val listOfSearchMatchProducts = MutableLiveData<List<Product>>()
    var searchText = ""
    val attributeFilter = mutableListOf<Attribute>()
    var categoryId = 0
    var categoryName: String = "همه محصولات"
    var searchListOrder: SearchOrder = SearchOrder.NEWEST
    fun listOfFilteredSearchProducts() =
        Transformations.map(listOfSearchMatchProducts) { it ->
            if (attributeFilter.isNullOrEmpty())
                it
            else {
                it.filter { product ->
                    product.attributes.containsAll(attributeFilter)
                }
            }
        }


    fun getSearchResults() {
        viewModelScope.launch {
            if (searchText.isNotBlank())
                listOfSearchMatchProducts.value =
                    searchRepository.getListOfSearchMatches(
                        categoryId,
                        searchListOrder,
                        searchText
                    ).data?.filter { product ->
                        product.name.contains(searchText)
                    }
        }
    }
}