package com.example.musicalinstrumentstore.ui.customer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicalinstrumentstore.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProductsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userName = MutableLiveData<Result<String>>()
    val userName: LiveData<Result<String>> = _userName

     fun getUserName(email: String) {

        viewModelScope.launch {
            val user = userRepository.fetchUser(email)
            if (user != null) {
                _userName.value = Result.success(user.name)
            } else {
                _userName.value =
                    Result.failure(Exception("Something went wrong trying to get user name"))
            }
        }


    }


}