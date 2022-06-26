package com.zarisa.ezmart.model

data class AttrProps(
    val id: Int = 0,
    val name: String = ""
)

data class SelectableAttrProps(
    val attrProps: AttrProps,
    val isSelected: Boolean
)