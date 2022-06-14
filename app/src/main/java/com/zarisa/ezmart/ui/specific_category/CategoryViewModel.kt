package com.zarisa.ezmart.ui.specific_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.category.CategoryRepository
import com.zarisa.ezmart.model.Category
import com.zarisa.ezmart.model.NetworkStatus
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    val networkStatusLiveData = MutableLiveData<NetworkStatus>()
    val currentCategory = MutableLiveData<Category>()
    val listOfProductsByCategory = MutableLiveData<List<Product>>()
    fun initCategory(categoryId: Int) {
        networkStatusLiveData.value = NetworkStatus.LOADING
        viewModelScope.launch {
            try {
                getProductsOfCategory(categoryId)
                getCurrentCategoryName(categoryId)
                networkStatusLiveData.value = NetworkStatus.SUCCESSFUL
            } catch (e: Exception) {
                networkStatusLiveData.value = NetworkStatus.ERROR
            }
        }
    }

    private suspend fun getCurrentCategoryName(categoryId: Int) {

        currentCategory.value = categoryRepository.getCurrentCategory(categoryId)
    }

    private suspend fun getProductsOfCategory(categoryId: Int) {

        listOfProductsByCategory.value =
            categoryRepository.getProductsOfSpecificCategory(categoryId)

    }
}
