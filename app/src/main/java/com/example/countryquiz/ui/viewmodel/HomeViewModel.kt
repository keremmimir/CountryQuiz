package com.example.countryquiz.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquiz.Event
import com.example.countryquiz.data.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    val authResult = MutableLiveData<Event<Result<String>>>()

    fun signOut() {
        viewModelScope.launch {
            val response = firebaseRepository.signOut()
            authResult.value = Event(response)
        }
    }
}