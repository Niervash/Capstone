package com.bangkit.huggingpet.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.huggingpet.api.ApiConfig
import com.bangkit.huggingpet.api.ApiService
import com.bangkit.huggingpet.database.ListPetDetail
import com.bangkit.huggingpet.database.PetDatabase
import com.bangkit.huggingpet.dataclass.LoginDataAccount
import com.bangkit.huggingpet.dataclass.RegisterDataAccount
import com.bangkit.huggingpet.dataclass.ResponseDetail
import com.bangkit.huggingpet.dataclass.ResponseLogin
import com.bangkit.huggingpet.espresso.wrapEspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MainRepository(
    private val petDatabase: PetDatabase,
    private val apiService: ApiService,
) {

    private var _pets = MutableLiveData<List<ListPetDetail>>()
    var pets: LiveData<List<ListPetDetail>> = _pets

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userLogin = MutableLiveData<ResponseLogin>()
    var userlogin: LiveData<ResponseLogin> = _userLogin

    fun getResponseLogin(loginDataAccount: LoginDataAccount) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            val api = ApiConfig.getApiService().loginUser(loginDataAccount)
            api.enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()

                    if (response.isSuccessful) {
                        _userLogin.value = responseBody!!
                        _message.value = "Hello ${_userLogin.value!!.loginResult.name}!"

                    } else {
                        when (response.code()) {
                            401 -> _message.value =
                                "Email or password that you are input are wrong, please try again"
                            408 -> _message.value =
                                "Your connection is low, please try again"
                            else -> _message.value = "Error message: " + response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = "Error message: " + t.message.toString()
                }

            })
        }
    }

    fun getResponseRegister(registDataUser: RegisterDataAccount) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            val api = ApiConfig.getApiService().registUser(registDataUser)
            api.enqueue(object : Callback<ResponseDetail> {
                override fun onResponse(
                    call: Call<ResponseDetail>,
                    response: Response<ResponseDetail>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _message.value = "Account successfully created"
                    } else {
                        when (response.code()) {
                            400 -> _message.value =
                                "Email has already registered, please try again"
                            408 -> _message.value =
                                "Your connection is low, please try again"
                            else -> _message.value = "Error message: " + response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = "Error message: " + t.message.toString()
                }

            })
        }
    }
}