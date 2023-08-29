package com.septalfauzan.algotrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.data.repository.MainRepository
import com.septalfauzan.algotrack.data.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val repository: ThemeRepository) : ViewModel() {
    private val _isDarkTheme: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            repository.getDarkThemeValueFlow().collect {
                _isDarkTheme.value = it
            }
        }
    }

    fun toggleDarkTheme() {
        viewModelScope.launch {
            try {
                repository.setDarkThemeValue(!isDarkTheme.value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}