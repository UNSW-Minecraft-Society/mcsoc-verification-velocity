package com.mcsoc.verificationvelocity.dataloader;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/*
    someBooleanProperty = true
    # inline tables in gradle 'libs.versions.toml' notation
    gradle-libs-like-property = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }

    [table1]
    # it can be null or nil, but don't forget to mark it with '?' in the codes
    # keep in mind, that null is prohibited by TOML spec, but it is very important in Kotlin
    property1 = null
    property2 = 6
    # check property3 in Table1 below. As it has the default value, it is not required and can be not provided 
    
    [table2]
    someNumber = 5
    [table2."akuleshov7.com"]
        name = 'this is a "literal" string'
        # empty lists are also supported
        configurationList = ["a",  "b",  "c", null]

    # such redeclaration of table2
    # is prohibited in toml specification;
    # but ktoml is allowing it in non-strict mode: 
    [table2]       
    otherNumber = 5.56
*/


/*
    debug = true

    [whitelist]
    api_key = <>
    whitelisted_servers = [<>, <>, <>]

    [message]
    form_url = <>
    discord_url = <>
*/

object TomlFileLoader {
    @Serializable
    data class MyClass(
        val someBooleanProperty: Boolean,
        val table1: Table1,
        val table2: Table2,
    @SerialName("gradle-libs-like-property")
    val kotlinJvm: GradlePlugin
    )

    @Serializable
    data class Table1(
        // nullable values, from toml you can pass null/nil/empty value to this kind of a field
        val property1: Long?,
        // please note, that according to the specification of toml integer values should be represented with Long
        val property2: Long,
        // no need to pass this value as it has the default value and is NOT REQUIRED
        val property3: Long = 5
    )

    @Serializable
    data class Table2(
        val someNumber: Long,
        @SerialName("akuleshov7.com")
        val inlineTable: InlineTable,
        val otherNumber: Double
    )

    @Serializable
    data class GradlePlugin(val id: String, val version: Version)

    @Serializable
    data class Version(val ref: String)


}
