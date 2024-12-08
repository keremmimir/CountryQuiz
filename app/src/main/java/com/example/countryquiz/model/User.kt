package com.example.countryquiz.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
)
