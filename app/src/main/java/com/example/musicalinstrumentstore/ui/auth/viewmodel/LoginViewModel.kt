package com.example.musicalinstrumentstore.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicalinstrumentstore.data.model.User
import com.example.musicalinstrumentstore.data.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    //LiveData == lifecycle aware (follows observer pattern, a container that follows data change)
    private val _loginResult = MutableLiveData<Result<User>>() //read only(cannot be changed outside viewmodel)
    val loginResult: LiveData<Result<User>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {

            try {
                val user = userRepository.loginUser(email, password)
                if (user != null) {
                    _loginResult.value = Result.success(user)
                } else {
                    _loginResult.value = Result.failure(Exception("Invalid Credentials"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }
}