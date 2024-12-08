package com.example.countryquiz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(val name: Name, val flags: Flags):Parcelable
