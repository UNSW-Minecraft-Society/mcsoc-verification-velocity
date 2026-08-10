package com.mcsoc.verificationvelocity.configloader

import com.akuleshov7.ktoml.TomlInputConfig
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
internal data class ConfigData(
    @SerialName("whitelist")
    val whitelist_data: WhitelistConfigData = WhitelistConfigData.getDefault(), 
    @SerialName("disconnect")
    val disconnect_message_data: DisconnectMessageConfigData = DisconnectMessageConfigData.getDefault(),
    @SerialName("debug")
    val debug_data: DebugConfigData = DebugConfigData.getDefault()
) {
    companion object {
        fun getDefault(): ConfigData {
            return ConfigData()
        }
    }
}

@Serializable
internal data class DebugConfigData(
    val early_disconnect: Boolean = false,
    val autofail: Boolean = false,
) {
    companion object {
        fun getDefault(): DebugConfigData {
            return DebugConfigData()
        }
    }
}

@Serializable
internal data class WhitelistConfigData(
    val findUser_url: String = "https://finduser-blahblah.run.app",
    val api_key: String = "fakekey123",
    val whitelisted_servers: List<String> = listOf("server1", "server2")
) {
    companion object {
        fun getDefault(): WhitelistConfigData {
            return WhitelistConfigData()
        }
    }
}

@Serializable
internal data class DisconnectMessageConfigData(
    val form_url: String = "forms.google.com/formurl",
    val discord_url: String = "discord.gg/yourinvite"
) {
    companion object {
        fun getDefault(): DisconnectMessageConfigData {
            return DisconnectMessageConfigData()
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
    private val debug_config get() = config.debug_data
    val debug_earlydisconnect get() = debug_config.early_disconnect
    val debug_autofail get() = debug_config.autofail

    private val whitelist_config get() = config.whitelist_data
    val findUser_url get() = whitelist_config.findUser_url
    val api_key get() = whitelist_config.api_key
    val whitelisted_servers get() = whitelist_config.whitelisted_servers
    
    private val disconnect_message_config get() = config.disconnect_message_data
    val form_url get() = disconnect_message_config.form_url
    val discord_url get() = disconnect_message_config.discord_url
    
    
    private fun loadConfigData() {
        config = try {
            config_path.inputStream().use {
                TomlFileReader(
                    TomlInputConfig.compliant(ignoreUnknownNames = true, allowEmptyToml = true)
                ).decodeFromStream(ConfigData.serializer(), it)
            }
        } catch (e: Exception) {
            when (e) {
                is SerializationException -> logger.error("Error while deserialising ConfigData: ", e)
                is IllegalArgumentException -> logger.error("Invalid ConfigData: ", e)
                is IOException -> logger.error("Cannot read config file: ", e)
                else -> throw e
            }
            ConfigData.getDefault()
        }
        
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
