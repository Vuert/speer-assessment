package com.speer.technologies.app

import android.app.Application
import com.speer.technologies.di.DI
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SpeerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initDI()
    }

    private fun initDI() {
        startKoin {
            androidContext(this@SpeerApplication)
            modules(DI.getModules())
        }
    }
}
