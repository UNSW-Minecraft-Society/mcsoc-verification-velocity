package com.mcsoc.verificationvelocity.dataloader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.nio.file.Path

import com.mcsoc.verificationvelocity.dataloader.configfileloaders.MessageConfigData
import com.mcsoc.verificationvelocity.dataloader.configfileloaders.MessageConfigFileLoader
import com.mcsoc.verificationvelocity.dataloader.configfileloaders.WhitelistConfigData
import com.mcsoc.verificationvelocity.dataloader.configfileloaders.WhitelistConfigFileLoader
import java.lang.reflect.Type
import kotlin.io.path.Path


private const val MESSAGE_CONFIG_FILE_PATH = "message.json"
private const val WHITELIST_CONFIG_FILE_PATH = "whitelist.json"

private object WhitelistDataLoader: WhitelistConfigFileLoader {
    override fun getGsonParser(): Gson {
        var gson_builder = GsonBuilder()
        gson_builder = WhitelistConfigFileLoader.registerGsonTypes(gson_builder)
        return gson_builder.setPrettyPrinting().create()
    }

    override fun getDefaultFileData(): WhitelistConfigData {
        return WhitelistConfigData.getDefault()
    }

    override fun getFileDataType(): Type {
        return object: TypeToken<WhitelistConfigData>(){}.type
    }
}

private object MessageDataLoader: MessageConfigFileLoader {
    override fun getGsonParser(): Gson {
        var gson_builder = GsonBuilder()
        gson_builder = WhitelistConfigFileLoader.registerGsonTypes(gson_builder)
        return gson_builder.setPrettyPrinting().create()
    }
    
    override fun getDefaultFileData(): MessageConfigData {
        return MessageConfigData.getDefault()
    }
    
    override fun getFileDataType(): Type {
        return object: TypeToken<MessageConfigData>(){}.type
    }
}

object PluginDataLoader {
    private lateinit var message_config_path: Path
    private lateinit var whitelist_config_path: Path
    
    @JvmStatic
    fun setDataDirectory(value: Path) {
        whitelist_config_path = value.resolve(WHITELIST_CONFIG_FILE_PATH)
        message_config_path = value.resolve(MESSAGE_CONFIG_FILE_PATH)
    }
    
    private lateinit var whitelist_config: WhitelistConfigData
    val api_key get() = whitelist_config.api_key
    val whitelisted_servers get() = whitelist_config.server_names
    
    private lateinit var message_config: MessageConfigData
    val form_url get() = message_config.form_link
    val discord_url get() = message_config.discord_link
    
    
    private fun loadConfigData() {
        whitelist_config = WhitelistDataLoader.loadConfigData(whitelist_config_path)
        message_config = MessageDataLoader.loadConfigData(message_config_path)
    }
    private fun saveConfigData() {
        WhitelistDataLoader.saveConfigData(whitelist_config_path, whitelist_config)
        MessageDataLoader.saveConfigData(message_config_path, message_config)
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
