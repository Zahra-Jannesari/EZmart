package com.zarisa.ezmart.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.search.SearchRepository
import com.zarisa.ezmart.model.AttrProps
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {
    val listOfSearchMatchProducts = MutableLiveData<List<Product>?>()
    var searchText = ""
    var categoryId = 0
    var categoryName: String = "همه محصولات"
    var statusMessage = ""
    val statusLiveData = MutableLiveData<Status?>()
    var searchListOrder = SearchOrder.NEWEST
    val listOfAttributes = MutableLiveData<List<AttrProps>?>()
    val listOfAttributeTerms = MutableLiveData<List<AttrProps>?>()
    var selectedAttr: AttrProps? = null
    var selectedAttrTerm = MutableLiveData<AttrProps?>(null)
    val attrTermStatus = MutableLiveData<Status?>()

    init {
        viewModelScope.launch {
            searchRepository.getListOfAttributes().let {
                if (it.status == Status.SUCCESSFUL) {
                    listOfAttributes.postValue(it.data)
                    selectedAttr = it.data?.get(0)
                    getAttrTerms()
                }
            }
        }
    }

    fun getAttrTerms() {
        viewModelScope.launch {
            attrTermStatus.postValue(Status.LOADING)
            if (selectedAttr?.id != 0 && selectedAttr?.id != null)
                searchRepository.getListOfAttributeTerms(selectedAttr!!.id).let {
                    attrTermStatus.postValue(it.status)
                    if (it.status == Status.SUCCESSFUL)
                        listOfAttributeTerms.postValue(it.data)
                }
        }
    }

    fun getSearchResults() {
        viewModelScope.launch {
            if (searchText.isNotBlank()) {
                statusLiveData.postValue(Status.LOADING)
                val resource =
                    if (selectedAttr != null && selectedAttrTerm.value != null) searchRepository.getListOfSearchMatches(
                        categoryId,
                        searchListOrder,
                        searchText, selectedAttr!!, selectedAttrTerm.value!!
                    ) else searchRepository.getListOfSearchMatches(
                        categoryId,
                        searchListOrder,
                        searchText
                    )

                resource.let {
                    statusMessage = it.serverMessage?.message ?: it.message
                    statusLiveData.value = it.status
                    if (it.status == Status.SUCCESSFUL)
                        if (it.data.isNullOrEmpty()) {
                            statusMessage = "نتیجه یافت نشد."
                            statusLiveData.postValue(Status.NOT_FOUND)
                        } else
                            listOfSearchMatchProducts.value = it.data?.filter { product ->
                                product.name.contains(searchText)
                            }
                }
            }
        }
    }

    fun resetFilter() {
        selectedAttr = listOfAttributes.value?.get(0)
        getAttrTerms()
        selectedAttrTerm.postValue(null)
    }
}