package com.zarisa.ezmart.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.category.CategoryRepository
import com.zarisa.ezmart.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    val categoryList = MutableLiveData<List<Category>>()

    fun getCategoriesList() {
        viewModelScope.launch {
            categoryList.value = categoryRepository.getCategoriesList()
        }
    }
}