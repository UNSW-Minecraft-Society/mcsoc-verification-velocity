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


internal data class MessageConfigData(
    val form_link: String,
    val discord_link: String
) : LoadedFileData {
    companion object {
        fun getDefault(): MessageConfigData {
            return MessageConfigData("forms.gle.fake123", "discord.gg/yunkudas")
        }
        
        const val FORM_LINK_JSON_KEY = "form_url"
        const val DISCORD_LINK_JSON_KEY = "discord_invite"
        
        fun fromJson(json: JsonElement?): MessageConfigData {
            val default = MessageConfigData.getDefault()
            return json?.asJsonObject?.let{json_object ->
                val form_link = json_object.get(FORM_LINK_JSON_KEY)?.asString ?: default.form_link
                val discord_link = json_object.get(DISCORD_LINK_JSON_KEY)?.asString ?: default.discord_link
                MessageConfigData(form_link, discord_link)
            } ?: default
        }
    }
    
    override fun toJson(): JsonObject {
        return JsonObject().apply{
            addProperty(FORM_LINK_JSON_KEY, form_link)
            addProperty(DISCORD_LINK_JSON_KEY, discord_link)
        }
    }
    
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): MessageConfigData {
        return MessageConfigData.fromJson(json)
    }
}

internal interface MessageConfigFileLoader : JsonFileLoader<MessageConfigData> {
    companion object {
        fun registerGsonTypes(builder: GsonBuilder): GsonBuilder {
            builder.registerTypeAdapter(MessageConfigData::class.java, MessageConfigData.getDefault())
            return builder
        }
    }
}
