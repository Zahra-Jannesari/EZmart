package com.zarisa.ezmart.ui.specific_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zarisa.ezmart.data.category.CategoryRepository
import com.zarisa.ezmart.data.network.NetworkParams
import com.zarisa.ezmart.model.Status
import com.zarisa.ezmart.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {
    val statusLiveData = MutableLiveData<Status>()
    val listOfProductsByCategory = MutableLiveData<List<Product>?>()
    var statusMessage = ""
    fun initCategory(categoryId: Int) {
        statusLiveData.value = Status.LOADING
        viewModelScope.launch {
            categoryRepository.getProductsOfSpecificCategory(categoryId).let {
                statusLiveData.value = it.status
                statusMessage = it.message
                if (it.status == Status.SUCCESSFUL)
                    listOfProductsByCategory.value = it.data?.filter { it.id!= NetworkParams.SPECIAL_OFFERS }
            }
        }
    }
}
