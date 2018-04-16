package com.ravielcd.mediapicker

import android.app.Application

class MyApplication : Application() {
    private var appComponent: AppComponent? = null

    fun getAppComponent(): AppComponent? {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}