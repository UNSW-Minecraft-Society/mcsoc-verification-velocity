package com.mcsoc.verificationvelocity.dataloader.configfileloaders

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

import com.mcsoc.verificationvelocity.dataloader.JsonFileLoader
import com.mcsoc.verificationvelocity.dataloader.LoadedFileData


internal data class WhitelistConfigData(
    val api_key: String,
    val server_names: List<String>
) : LoadedFileData {
    companion object {
        fun getDefault(): WhitelistConfigData {
            return WhitelistConfigData("fakekey123", listOf("server1", "server2"))
        }
        
        const val API_KEY_JSON_KEY = "api_key"
        const val SERVER_NAMES_JSON_KEY = "server_names"
        
        fun fromJson(json: JsonElement?): WhitelistConfigData {
            val default = WhitelistConfigData.getDefault()
            return json?.asJsonObject?.let{json_object ->
                val api_key = json_object.get(API_KEY_JSON_KEY)?.asString ?: default.api_key
                val names_list = json_object.get(SERVER_NAMES_JSON_KEY)?.asJsonArray?.map{it.asString} ?: default.server_names
                WhitelistConfigData(api_key, names_list)
            } ?: default
        }
    }
    
    override fun toJson(): JsonObject {
        return JsonObject().apply{
            addProperty(API_KEY_JSON_KEY, api_key)
            add(SERVER_NAMES_JSON_KEY, JsonArray().apply{
                server_names.forEach(::add)
            })
        }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): WhitelistConfigData? {
        return WhitelistConfigData.fromJson(json)
    }
}

internal interface WhitelistConfigFileLoader : JsonFileLoader<WhitelistConfigData> {
    companion object {
        fun registerGsonTypes(builder: GsonBuilder): GsonBuilder {
            builder.registerTypeAdapter(WhitelistConfigData::class.java, WhitelistConfigData.getDefault())
            return builder
        }
    }
}
