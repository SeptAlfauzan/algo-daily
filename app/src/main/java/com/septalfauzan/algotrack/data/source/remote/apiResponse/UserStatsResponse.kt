package com.septalfauzan.algotrack.data.source.remote.apiResponse

import com.google.gson.annotations.SerializedName

data class UserStatsResponse(

	@field:SerializedName("data")
	val data: UserStatsData? = null
)

data class UserStatsData(

	@field:SerializedName("onTimeCountDay")
	val onTimeCountDay: Int? = null,

	@field:SerializedName("onTimePercentageWeek")
	val onTimePercentageWeek: Int? = null,

	@field:SerializedName("lateCountDay")
	val lateCountDay: Int? = null
)
