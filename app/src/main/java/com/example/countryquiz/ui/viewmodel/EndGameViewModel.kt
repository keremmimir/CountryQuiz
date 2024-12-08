package com.example.countryquiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquiz.data.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    fun addScore(score: Int) {
        viewModelScope.launch {
            firebaseRepository.addScore(score)
        }
    }
}