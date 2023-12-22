package com.bangkit.huggingpet.dataclass

import com.google.gson.annotations.SerializedName
import com.bangkit.huggingpet.database.ListPetDetail

data class RegisterDataAccount(
    var name: String,
    var email: String,
    var password: String
)

data class LoginDataAccount(
    var email: String,
    var password: String
)

data class ResponseDetail(
    var error: Boolean,
    var message: String
)

data class ResponseLogin(
    var error: Boolean,
    var message: String,
    var loginResult: LoginResult
)

data class LoginResult(
    var userId: String,
    var name: String,
    var token: String
)

data class ResponsePagingPet(
    @field:SerializedName("error")
    var error: String,

    @field:SerializedName("message")
    var message: String,

    @field:SerializedName("listPet")
    var listBucket: List<ListPetDetail>
)

data class ResponseDisease(
    var prediction: String
)