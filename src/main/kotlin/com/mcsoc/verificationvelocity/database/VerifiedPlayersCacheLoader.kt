package com.mcsoc.verificationvelocity.database

import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.notExists
import kotlin.io.path.createFile

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

import com.velocitypowered.api.util.GameProfile
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.insert


const val DB_FILE_NAME = "verified_player_cache"

object VerifiedPlayersTable : Table() {
    // mc usernames are max 16 characters
    val uuid = varchar("uuid", 36)
    val username = varchar("username", 16)
    override val primaryKey get() = PrimaryKey(uuid)
}

object VerifiedPlayersCacheLoader {
    
    private lateinit var db_path: Path
    val db by lazy {
        Database.connect("jdbc:h2:./$db_path", driver = "org.h2.Driver")
    }
    
    @JvmStatic
    fun initialise(value: Path) {
        db_path = value.resolve(DB_FILE_NAME)
        value.createParentDirectories()
        value.takeIf{it.notExists()}?.createFile()
        
        transaction(db) {
            SchemaUtils.create(VerifiedPlayersTable)
        }
    }
    
    fun checkIfPlayerPresent(profile: GameProfile): Boolean {
        val player_uuid_string = profile.id.toString()
        return transaction(db) {
            VerifiedPlayersTable.select(VerifiedPlayersTable.uuid).where{
                VerifiedPlayersTable.uuid like player_uuid_string
            }.count()
        } > 0
    }
    
    fun cachePlayer(profile: GameProfile) {
        transaction(db) {
            VerifiedPlayersTable.insert{
                it[uuid] = profile.id.toString()
                it[username] = profile.name
            }
        }
    }
}