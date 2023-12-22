package com.bangkit.huggingpet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.huggingpet.database.ListPetDetail
import com.bangkit.huggingpet.dataclass.LoginDataAccount
import com.bangkit.huggingpet.dataclass.RegisterDataAccount
import com.bangkit.huggingpet.dataclass.ResponseLogin
import com.bangkit.huggingpet.repository.MainRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel (private val mainRepository: MainRepository) : ViewModel() {

    val pets: LiveData<List<ListPetDetail>> = mainRepository.pets

    val message: LiveData<String> = mainRepository.message

    val isLoading: LiveData<Boolean> = mainRepository.isLoading

    val userlogin: LiveData<ResponseLogin> = mainRepository.userlogin

    fun login(loginDataAccount: LoginDataAccount) {
        mainRepository.getResponseLogin(loginDataAccount)
    }

    fun register(registDataUser: RegisterDataAccount) {
        mainRepository.getResponseRegister(registDataUser)
    }

}
