package com.example.countryquiz.data

import com.example.countryquiz.model.Country
import com.example.countryquiz.network.CountryAPI
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val countryAPI: CountryAPI) : Repository {

    override suspend fun getCounrty(): Result<List<Country>> {
        return try {
            val country = countryAPI.getCountry()
            Result.success(country)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}