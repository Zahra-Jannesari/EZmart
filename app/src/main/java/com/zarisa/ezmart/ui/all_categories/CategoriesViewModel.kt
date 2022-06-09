package com.zarisa.ezmart.ui.all_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.category.CategoryRepository
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.NetworkStatus
import com.zarisa.ezmart.ui.ParentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ParentViewModel() {
    val categoryList = MutableLiveData<List<Category>>()

    fun getCategoriesList() {
        setNetworkStatus(NetworkStatus.LOADING)
        viewModelScope.launch {
            try {
                categoryList.value = categoryRepository.getCategoriesList()
                setNetworkStatus(NetworkStatus.SUCCESSFUL)
            } catch (e: Exception) {
                setNetworkStatus(NetworkStatus.ERROR)
            }
        }
    }

    override fun setNetworkStatus(networkStatus: NetworkStatus) {
        super.setNetworkStatus(networkStatus)
    }
}