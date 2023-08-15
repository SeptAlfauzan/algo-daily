package com.septalfauzan.algotrack.viewmodels

import androidx.lifecycle.ViewModel
import com.septalfauzan.algotrack.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
}