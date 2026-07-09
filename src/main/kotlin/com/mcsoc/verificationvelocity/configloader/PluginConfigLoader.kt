package com.mcsoc.verificationvelocity.configloader

import com.akuleshov7.ktoml.file.TomlFileReader
import com.akuleshov7.ktoml.file.TomlFileWriter
import com.akuleshov7.ktoml.source.decodeFromStream
import java.nio.file.Path

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlin.io.path.absolutePathString
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.inputStream
import kotlin.io.path.notExists

import org.slf4j.Logger
import java.io.IOException


private const val CONFIG_FILE_PATH = "config.toml"

@Serializable
data class ConfigData(
    val debug: Boolean,
    @SerialName("whitelist")
    val whitelist_data: WhitelistConfigData,
    @SerialName("disconnect")
    val disconnect_message_data: DisconnectMessageConfigData
) {
    companion object {
        fun getDefault(): ConfigData {
            return ConfigData(false, WhitelistConfigData.getDefault(), DisconnectMessageConfigData.getDefault())
        }
    }
}

@Serializable
data class WhitelistConfigData(
    val api_key: String,
    val whitelisted_servers: List<String>
) {
    companion object {
        fun getDefault(): WhitelistConfigData {
            return WhitelistConfigData("fakekey123", listOf("server1", "server2"))
        }
    }
}

@Serializable
data class DisconnectMessageConfigData(
    val form_url: String,
    val discord_url: String
) {
    companion object {
        fun getDefault(): DisconnectMessageConfigData {
            return DisconnectMessageConfigData("forms.google.com/formurl", "discord.gg/yourinvite")
        }
    }
}

object PluginConfigLoader {
    private lateinit var config_path: Path
    private lateinit var logger: Logger
    
    @JvmStatic
    fun initialise(value: Path, logger: Logger) {
        config_path = value.resolve(CONFIG_FILE_PATH)
        config_path.createParentDirectories()
        config_path.takeIf{it.notExists()}?.createFile()
        this.logger = logger
    }
    
    private lateinit var config: ConfigData

    private val whitelist_config get() = config.whitelist_data
    val api_key get() = whitelist_config.api_key
    val whitelisted_servers get() = whitelist_config.whitelisted_servers
    
    private val disconnect_message_config get() = config.disconnect_message_data
    val form_url get() = disconnect_message_config.form_url
    val discord_url get() = disconnect_message_config.discord_url
    
    
    private fun loadConfigData() {
        try {
            TomlFileReader.decodeFromStream(ConfigData.serializer(), config_path.inputStream())
        } catch (e: SerializationException) {
            logger.error("SerializationException: Cannot deserialize ConfigData.")
        } catch (e: IllegalArgumentException) {
            logger.error("IllegalArgumentException: Invalid ConfigData.")
        } catch (e: IOException) {
            logger.error("IOException: Cannot read from stream.")
        }
        config = config_path.inputStream().runCatching{
            TomlFileReader.decodeFromStream(ConfigData.serializer(),this)
        }.getOrNull() ?: ConfigData.getDefault()

        saveConfigData()
    }
    private fun saveConfigData() {
        TomlFileWriter().apply{
            this.encodeToFile(ConfigData.serializer(), config, config_path.absolutePathString())
        }
    }

    @JvmStatic
    fun loadDataFromFiles() {
        this.loadConfigData()
        
        this.saveDataToFiles()
    }
    
    @JvmStatic
    fun saveDataToFiles() {
        this.saveConfigData()
    }
}
