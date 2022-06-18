package com.zarisa.ezmart.ui.all_categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.category.CategoryRepository
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    val categoryList = MutableLiveData<List<Category>?>()
    val statusLiveData = MutableLiveData<Status>()
    var statusMessage = ""
    fun getCategoriesList() {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            categoryRepository.getCategoriesList().let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL)
                    categoryList.value = it.data
            }
        }
    }
}