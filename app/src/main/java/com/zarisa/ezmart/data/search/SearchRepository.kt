package com.zarisa.ezmart.data.search

import com.zarisa.ezmart.domain.Resource
import com.zarisa.ezmart.model.AttrProps
import com.zarisa.ezmart.model.Product
import com.zarisa.ezmart.model.SearchOrder
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchRemoteDataSource: SearchRemoteDataSource) {
    suspend fun getListOfSearchMatches(
        categoryId: Int,
        searchOrder: SearchOrder,
        searchText: String
    ): Resource<List<Product>> {
        return if (categoryId == 0) searchRemoteDataSource.searchMatches(searchText, searchOrder)
        else searchRemoteDataSource.searchMatches(searchText, searchOrder, categoryId.toString())
    }

    suspend fun getListOfSearchMatches(
        categoryId: Int,
        searchOrder: SearchOrder,
        searchText: String, selectedAttr: AttrProps, selectedAttrTerm: AttrProps
    ): Resource<List<Product>> {
        return if (categoryId == 0) searchRemoteDataSource.searchMatches(
            searchText,
            searchOrder,
            "pa_${if (selectedAttr.name == "رنگ") "color" else "size"}",
            selectedAttrTerm.id
        )
        else searchRemoteDataSource.searchMatches(
            searchText,
            searchOrder,
            "pa_${if (selectedAttr.name == "رنگ") "color" else "size"}",
            selectedAttrTerm.id,
            categoryId.toString()
        )
    }

    suspend fun getListOfAttributes(
    ): Resource<List<AttrProps>> {
        return searchRemoteDataSource.getListOfAttributes()
    }

    suspend fun getListOfAttributeTerms(
        attrId: Int
    ): Resource<List<AttrProps>> {
        return searchRemoteDataSource.getListOfAttributeTerms(attrId)
    }
}