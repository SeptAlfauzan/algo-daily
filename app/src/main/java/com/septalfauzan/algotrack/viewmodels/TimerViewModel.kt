package com.septalfauzan.algotrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val totalTimer = 600000L
    private var _timerState = MutableStateFlow(totalTimer)
    val timerState: StateFlow<Long> = _timerState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initTimer(totalTimer).onCompletion { _timerState.emit(totalTimer) }.collect {
                _timerState.value = it
                Log.d("TAG", ": ${timerState.value} ")
            }
        }
    }

    private fun initTimer(totalMillis: Long): Flow<Long> =
        (totalMillis - 1000 downTo 0 step 1000).asFlow() // Emit total - 1 because the first was emitted onStart
            .onEach { delay(1000) } // Each second later emit a number
            .onStart { emit(totalMillis) } // Emit total seconds immediately
            .onEmpty { emit(totalTimer) }
            .conflate() // In case the creating of State takes some time, conflate keeps the time ticking separately
            .transform { remainingMilis: Long ->
                emit(remainingMilis)
            }
}