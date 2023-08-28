package com.septalfauzan.algotrack.helper

import android.util.Log
import com.septalfauzan.algotrack.data.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class NetworkCall<T>{
    lateinit var call: Call<T>

    fun makeCall(call: Call<T>) : StateFlow<UiState<T>>{
        this.call = call
        val callback = ApiCallback<T>()

        this.call.clone().enqueue(callback)
        Log.d("TAG", "makeCall: ")
        return callback.result
    }
}
class ApiCallback<T> : Callback<T> {
    private val _result = MutableStateFlow<UiState<T>>(UiState.Loading)
    val result: MutableStateFlow<UiState<T>> = _result

    override fun onResponse(call: Call<T>, response: Response<T>) {
        _result.value = when{
            response.isSuccessful -> response.body()?.let {
                UiState.Success(it)
            } ?: UiState.Error("response is null!")
            else -> UiState.Error("Request failed: ${response.code()}")
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        _result.value = UiState.Error("Network call failed: ${t.message}")
    }
}