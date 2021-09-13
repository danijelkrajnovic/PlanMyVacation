package com.example.planmyvacation.koin

import com.example.planmyvacation.screens.login.LoginPresenter
import com.example.planmyvacation.screens.query.QueryPresenter
import org.koin.dsl.module

object AppModule {

    val myModule = module {
        factory { LoginPresenter() }
        factory { QueryPresenter() }
    }
}