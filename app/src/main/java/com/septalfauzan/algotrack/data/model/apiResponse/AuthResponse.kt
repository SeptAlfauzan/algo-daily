package com.septalfauzan.algotrack.data.model.apiResponse

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("data")
	val data: Data
)

data class Data(

	@field:SerializedName("token")
	val token: String
)
