package com.example.countryquiz.data

import com.example.countryquiz.model.Country

interface Repository {
    suspend fun getCounrty():Result<List<Country>>
}