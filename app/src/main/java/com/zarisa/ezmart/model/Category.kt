package com.zarisa.ezmart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Category(
    val id: Int,
    val name: String,
    val image: @RawValue Image?
) : Parcelable