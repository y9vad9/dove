package com.dove.server.features

import com.dove.mailer.LocalMailer
import com.dove.server.local.Environment
import org.junit.jupiter.api.BeforeAll

abstract class FeaturesTest {

    private val mailer = LocalMailer()

    @BeforeAll
    open fun initialize() {
        Environment.mailer = mailer
    }
}