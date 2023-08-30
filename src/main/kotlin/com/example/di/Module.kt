package com.example.di


import com.example.repositories.InterfaceAdminImpl
import com.example.repositories.InterfaceUserImpl

import org.koin.dsl.module


val myModule = module {
    single<InterfaceAdminImpl> {
        InterfaceAdminImpl()
    }
    single<InterfaceUserImpl> {
        InterfaceUserImpl()
    }
}
