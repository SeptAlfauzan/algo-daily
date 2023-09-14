package com.septalfauzan.algotrack.helper

import android.util.Log
import com.septalfauzan.algotrack.domain.model.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkCall<T>{
    lateinit var call: Call<T>
    private val _result: MutableStateFlow<UiState<T>> = MutableStateFlow<UiState<T>>(UiState.Loading)
    val result: MutableStateFlow<UiState<T>> = _result

    fun makeCall(call: Call<T>) : StateFlow<UiState<T>>{
        this.call = call

        val result = MutableStateFlow<UiState<T>>(UiState.Loading)

        val callback = object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                result.value = when {
                    response.isSuccessful -> response.body()?.let {
                        UiState.Success(it)
                    } ?: UiState.Error("response is null!")
                    else -> UiState.Error("Request failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                result.value = UiState.Error("Network call failed: ${t.message}")
            }
        }

        Log.d("TAG", "makeCall: ${result.value}")
        call.enqueue(callback)
        return result
//        val callback = makeCallback(this._result)
//        call.enqueue(callback)
//        return this.result
    }

    private fun makeCallback(result: MutableStateFlow<UiState<T>>) = object : Callback<T>{
        override fun onResponse(call: Call<T>, response: Response<T>) {
            result.value = when{
                response.isSuccessful -> response.body()?.let {
                    UiState.Success(it)
                } ?: UiState.Error("response is null!")
                else -> UiState.Error("Request failed: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            result.value = UiState.Error("Network call failed: ${t.message}")
        }
    }
//    class ApiCallback<T> : Callback<T> {
//        private val _result = MutableStateFlow<UiState<T>>(UiState.Loading)
//        val result: MutableStateFlow<UiState<T>> = _result
//
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//            _result.value = when{
//                response.isSuccessful -> response.body()?.let {
//                    UiState.Success(it)
//                } ?: UiState.Error("response is null!")
//                else -> UiState.Error("Request failed: ${response.code()}")
//            }
//
//            Log.d("TAG", "onResponse: ${_result.value} ${result.value}")
//        }
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//            _result.value = UiState.Error("Network call failed: ${t.message}")
//        }
//    }
}
