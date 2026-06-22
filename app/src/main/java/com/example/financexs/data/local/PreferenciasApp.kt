package com.example.financexs.data.local

import android.content.Context

class PreferenciasApp(context: Context) {

    companion object {
        private const val PREFS_NAME = "financexs_prefs"
        private const val KEY_ONBOARDING = "onboarding_completado"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var onboardingCompletado: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING, value).apply()
}
