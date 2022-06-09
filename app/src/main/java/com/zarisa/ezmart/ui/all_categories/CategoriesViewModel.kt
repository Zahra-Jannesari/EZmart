package com.zarisa.ezmart.ui.all_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.category.CategoryRepository
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    val categoryList = MutableLiveData<List<Category>>()
    val networkStatusLiveData = MutableLiveData<NetworkStatus>()
    fun getCategoriesList() {
        networkStatusLiveData.value = NetworkStatus.LOADING
        viewModelScope.launch {
            try {
                categoryList.value = categoryRepository.getCategoriesList()
                networkStatusLiveData.value = NetworkStatus.SUCCESSFUL
            } catch (e: Exception) {
                networkStatusLiveData.value = NetworkStatus.ERROR
            }
        }
    }
}