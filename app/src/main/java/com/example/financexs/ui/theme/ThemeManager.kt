package com.example.financexs.ui.theme

import com.example.financexs.data.local.enums.TemaApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeManager {
    private val _currentTheme = MutableStateFlow(TemaApp.SISTEMA)
    val currentTheme: StateFlow<TemaApp> = _currentTheme.asStateFlow()

    fun updateTheme(tema: TemaApp) {
        _currentTheme.value = tema
    }
}
