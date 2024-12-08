package com.example.countryquiz.network

import com.example.countryquiz.model.Country
import retrofit2.http.GET

interface CountryAPI {
    //https://restcountries.com/v3.1/all
    @GET("/v3.1/all")
    suspend fun getCountry() : List<Country>
}