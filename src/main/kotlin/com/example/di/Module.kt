package com.example.di


import com.example.dao.AdminInterface
import com.example.dao.UserInterface
import com.example.redis.RedisClient
import com.example.redis.RedisHelper
import com.example.redis.RedisSessionStorage
import com.example.repositories.AdminInterfaceImpl
import com.example.repositories.UserInterfaceImpl
import com.example.service.UserServices
import com.example.service.AdminServices

import com.example.utils.helperfunctions.HelperMethods
import com.example.utils.helperfunctions.Mapping

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind

import org.koin.dsl.module


val myModule = module {
    singleOf(::AdminInterfaceImpl) {bind<AdminInterface>()}
    singleOf(::UserInterfaceImpl) {bind<UserInterface>()}
    singleOf(::AdminServices)
    singleOf(::UserServices)
    singleOf(::HelperMethods)
    singleOf(::Mapping)
    single<RedisHelper> { RedisHelper(RedisClient.jedis) }
    single<RedisSessionStorage> { RedisSessionStorage(RedisClient.jedis) }





}
