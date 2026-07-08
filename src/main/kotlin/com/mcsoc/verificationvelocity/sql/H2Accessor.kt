package com.mcsoc.verificationvelocity.sql

import org.jetbrains.exposed.v1.jdbc.Database

class H2Accessor {

    fun main() {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
    }
}