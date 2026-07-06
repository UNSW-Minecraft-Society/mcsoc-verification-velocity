package com.mcsoc.verificationvelocity.dataloader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.file.Path

internal data class ConfigData(val api_key: String) {
    companion object {
        fun getDefault(): ConfigData {
            return ConfigData("fake123")
        }
        
        const val API_KEY_JSON_KEY = "api_key"
        
        fun fromJson(json: JsonElement?): ConfigData {
            val json_object = json?.asJsonObject ?: return ConfigData.getDefault() 
            val api_key = json_object.get(API_KEY_JSON_KEY).asString ?: return ConfigData.getDefault()
            return ConfigData(api_key)
        }
    }
    
    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty(API_KEY_JSON_KEY, this.api_key)
        return json
    }
    
    class JsonSerialiser : JsonSerializer<ConfigData>, JsonDeserializer<ConfigData> {
            override fun serialize(src: ConfigData?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
                return src?.toJson()
            }
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ConfigData {
                return ConfigData.fromJson(json)
            }
        }
}

internal interface ConfigFileLoader : JsonFileLoader<ConfigData> {
    companion object {
        fun registerGsonTypes(builder: GsonBuilder): GsonBuilder {
            builder.registerTypeAdapter(ConfigData::class.java, ConfigData.JsonSerialiser())
            return builder
        }
        val config_data_type: Type = object: TypeToken<ConfigData>(){}.type
    }
    
    fun saveConfigData(file_path: Path, config_data: ConfigData) {
        this.writeToFileFromData(file_path, config_data, config_data_type)
    }
    
    fun loadConfigData(file_path: Path): ConfigData {
        return this.readFromFileToData(file_path, config_data_type) ?: ConfigData.getDefault()
    }
}
