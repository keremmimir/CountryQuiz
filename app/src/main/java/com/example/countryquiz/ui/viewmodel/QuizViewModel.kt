package com.example.countryquiz.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquiz.data.RepositoryImpl
import com.example.countryquiz.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    val correctCountry = MutableLiveData<Pair<Country, List<Country>>>()
    private val usedCountries = mutableListOf<Country>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val score = MutableLiveData<Int>()

    init {
        score.value = 0
        createQuiz()
    }

    fun createQuiz() {
        loading.value = true
        viewModelScope.launch {
            repositoryImpl.getCounrty().onSuccess { data ->
                val unusedCountries = data.filter { it !in usedCountries }
                val correctQuiz = unusedCountries.random()
                usedCountries.add(correctQuiz)
                val wrongQuiz = data.filter { it != correctQuiz }.shuffled().take(3)
                val options = (wrongQuiz + correctQuiz).shuffled()
                correctCountry.value = Pair(correctQuiz, options)
            }.onFailure { exception ->
                error.value = "API ERROR: ${exception.message}"
                Log.e("API ERROR", exception.message.toString())
            }
            loading.value = false
        }
    }

    fun checkAnswer(selectedCountry: Country): Boolean {
        val correctCountry = correctCountry.value?.first
        if (selectedCountry == correctCountry) {
            score.value = score.value?.plus(1)
            return true
        } else {
            return false
        }
    }
}