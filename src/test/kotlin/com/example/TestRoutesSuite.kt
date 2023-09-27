package com.example

import com.example.repository.AdminInterfaceImplTest
import com.example.repository.UserInterfaceImplTest
import com.example.routes.AdminRoutesTest
import com.example.service.AdminServicesTest
import com.example.service.UserServicesTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite class that includes all the route tests for the application.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
//    AdminRoutesTest::class,
    AdminInterfaceImplTest::class,
    UserInterfaceImplTest::class,
    UserServicesTest::class,
    AdminServicesTest::class

//    RecentWatchlistRoutesTest::class,
//    WatchlistRoutesTest::class
)
class TestRoutesSuite
