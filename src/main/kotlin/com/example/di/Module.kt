package com.example.di


import com.example.data.repositories.InterfaceAdminImpl
import com.example.data.repositories.InterfaceUserImpl

import org.koin.dsl.module


val myModule = module {
    single<InterfaceAdminImpl> {
        InterfaceAdminImpl()
    }
    single<InterfaceUserImpl> {
        InterfaceUserImpl()
    }
}
