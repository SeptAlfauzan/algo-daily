package com.septalfauzan.algotrack.data.source.remote.apiResponse

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("data")
	val data: AuthResponseData
)

data class AuthResponseData(

	@field:SerializedName("token")
	val token: String
)

data class RegisterResponse(

	@field:SerializedName("data")
	val data: RegisterData
)

data class RegisterData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)