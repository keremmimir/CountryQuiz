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
class SignUpViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    val authResult = MutableLiveData<Event<Result<String>>>()

    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            val response = firebaseRepository.signUp(email, username, password)
            authResult.value = Event(response)
        }
    }
}