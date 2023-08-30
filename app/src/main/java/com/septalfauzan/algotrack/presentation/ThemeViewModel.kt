package com.septalfauzan.algotrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septalfauzan.algotrack.domain.usecase.IThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val themeUseCase: IThemeUseCase) : ViewModel() {
    private val _isDarkTheme: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            _isDarkTheme.value = themeUseCase.getTheme()
        }
    }

    fun toggleDarkTheme() {
        viewModelScope.launch {
            try {
                themeUseCase.setTheme(!isDarkTheme.value)
                _isDarkTheme.value = themeUseCase.getTheme()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}