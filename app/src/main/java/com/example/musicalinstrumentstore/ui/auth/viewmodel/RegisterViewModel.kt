
package com.example.musicalinstrumentstore.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.repository.UserRepository
import kotlinx.coroutines.launch


// ViewModel to handle registration logic in the UI layer
class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<User>>()
    val registerResult: LiveData<Result<User>> = _registerResult

    // validate input fields before attempting registration
    private fun isValidInput(user: User): Boolean {
        return user.name.isNotBlank() && user.email.isNotBlank() && user.passWord.isNotBlank()
                && user.surname.isNotBlank()
    }

    fun register(user: User) {
        if (!isValidInput(user)) {
            _registerResult.value = Result.failure(Exception("Please fill all the fields"))
            return
        }

        // Launch a coroutine in the ViewModel's scope to handle registration asynchronously
        viewModelScope.launch {
            try {
                val result = userRepository.registerUser(user) // Call repository function
                _registerResult.value = result // Post the result to LiveData
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}