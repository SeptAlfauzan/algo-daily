package com.septalfauzan.algotrack.data.source.remote.apiResponse

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(

	@field:SerializedName("data")
	val data: Data
)

data class Data(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
