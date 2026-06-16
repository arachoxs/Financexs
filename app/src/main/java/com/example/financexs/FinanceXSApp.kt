package com.example.financexs

import android.app.Application
import com.example.financexs.di.AppModule

class FinanceXSApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}
