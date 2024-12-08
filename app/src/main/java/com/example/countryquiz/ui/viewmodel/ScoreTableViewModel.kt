package com.example.countryquiz.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquiz.data.FirebaseRepository
import com.example.countryquiz.model.ScoreTable
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreTableViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {
    val allScores = MutableLiveData<List<ScoreTable>>()
    val loading = MutableLiveData<Boolean>()

    init {
        getAllScore()
    }

    private fun getAllScore(){
        loading.value = true
        viewModelScope.launch {
            val getData = firebaseRepository.getAllScore()
            allScores.value = getData
            loading.value = false
        }
    }

    fun getCurrentUser() : FirebaseUser?{
        return firebaseRepository.getCurrentUser()
    }
}