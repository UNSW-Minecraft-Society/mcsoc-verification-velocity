package com.mcsoc.verificationvelocity.dataloader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.nio.file.Path


private const val CONFIG_DATA_FILE_PATH = "config.json"

object PluginDataLoader : ConfigFileLoader {
    override fun getGsonParser(): Gson {
        var gson_builder = GsonBuilder()
        gson_builder = ConfigFileLoader.registerGsonTypes(gson_builder)
        
        return gson_builder.setPrettyPrinting().create()
    }
    
    private lateinit var data_directory: Path
    @JvmStatic
    fun setDataDirectory(data_directory: Path) {
        this.data_directory = data_directory
    }
    private lateinit var config: ConfigData
    fun getApiKey(): String {
        return config.api_key
    }
    
    @JvmStatic
    fun loadDataFromFiles() {
        val config_path = data_directory.resolve(CONFIG_DATA_FILE_PATH)
        config = this.loadConfigData(config_path)
    }
    
    @JvmStatic
    fun saveDataToFiles() {
        val config_path = data_directory.resolve(CONFIG_DATA_FILE_PATH)
        this.saveConfigData(config_path, config)
    }
}
