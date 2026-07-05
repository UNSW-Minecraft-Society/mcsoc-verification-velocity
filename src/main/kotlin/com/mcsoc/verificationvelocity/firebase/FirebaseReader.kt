package com.mcsoc.verificationvelocity.firebase

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.velocitypowered.api.proxy.Player
import org.slf4j.Logger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


val findUser_endpoint_url = URI.create("https://finduser-l7edgf7twa-an.a.run.app")
// TODO MAKE THIS NOT BE IN THE CODE IF POSSIBLE??
const val api_key = "Bearer 8dKY69GCNQdQdwYHeYHc4g4PG6CDpZuAmS5rGmpTB666Rt7uFFbSV5wcwRQj"

object FirebaseReader {
    fun checkIfPlayerIsWhitelisted(player: Player, logger: Logger): Boolean {
        val username = player.gameProfile.name
        val firebase = getNondescriptResponse(username)
        val res = JsonParser.parseString(firebase.body()).asJsonObject
        for (entry in res.get("results").asJsonArray) {
            val is_verified = entry?.asJsonObject?.get("is_verified")?.asBoolean ?: continue
            if (is_verified) return true
        }
        
        return false
    }
    
    fun getNondescriptResponse(name: String): HttpResponse<String> {
        val http_client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(findUser_endpoint_url)
            .header("Content-Type", "application/json")
            .header("Authorization", api_key)
            .POST(HttpRequest.BodyPublishers.ofString("{\"minecraft_username\":\"$name\"}"))
            .build()
            
        val response = http_client.send(request, HttpResponse.BodyHandlers.ofString())
        return response
    }
}
