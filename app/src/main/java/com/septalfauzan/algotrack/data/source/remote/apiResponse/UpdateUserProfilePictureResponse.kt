package com.septalfauzan.algotrack.data.source.remote.apiResponse

import com.google.gson.annotations.SerializedName

data class UpdateUserProfilePictureResponse(

	@field:SerializedName("data")
	val data: UpdateUserProfilePicData? = null
)

data class UpdateUserProfilePicData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
