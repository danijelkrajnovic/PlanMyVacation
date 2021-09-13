package com.example.planmyvacation

import android.app.Application
import com.example.planmyvacation.koin.AppModule.myModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlanMyVacationApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlanMyVacationApp)
            modules(myModule)
        }
    }

}
