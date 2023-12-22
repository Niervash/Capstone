package com.bangkit.huggingpet.api

import com.bangkit.huggingpet.*
import com.bangkit.huggingpet.dataclass.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registUser(@Body requestRegister: RegisterDataAccount): Call<ResponseDetail>

    @POST("login")
    fun loginUser(@Body requestLogin: LoginDataAccount): Call<ResponseLogin>
}
    interface ApiServiceUpload {
        @Multipart
        @POST("predict_cat")
        fun uploadImage(
            @Part Kucing: MultipartBody.Part
        ): Call<ResponseDisease>
    }