package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ThemeUseCase @Inject constructor(private val themeRepository: IThemeRepository) :
    IThemeUseCase {
    override suspend fun setTheme(value: Boolean) {
        themeRepository.setDarkThemeValue(value)
    }

    override suspend fun getTheme(): Boolean {
        try {
            return themeRepository.getDarkThemeValueFlow().first()
        } catch (e: Exception) {
            throw e
        }
    }
}