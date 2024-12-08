package com.example.countryquiz.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquiz.Event
import com.example.countryquiz.data.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    val authResult = MutableLiveData<Event<Result<String>>>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val response = firebaseRepository.signIn(email, password)
            authResult.value = Event(response)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseRepository.getCurrentUser()
    }
}
