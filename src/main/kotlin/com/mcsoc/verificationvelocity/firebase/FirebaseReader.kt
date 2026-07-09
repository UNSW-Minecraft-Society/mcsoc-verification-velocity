package com.mcsoc.verificationvelocity.firebase

import com.google.gson.JsonParser
import com.mcsoc.verificationvelocity.configloader.PluginConfigLoader
import com.velocitypowered.api.proxy.Player
import org.slf4j.Logger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


object FirebaseReader {
    fun checkIfPlayerIsWhitelisted(player: Player, logger: Logger): Boolean {
        val username = player.gameProfile.name
        val find_user_res = getFindUserResponse(username)
        if (find_user_res.body() == "Unauthorized") {
            logger.error("Unable to authorise with Firebase! Check that the runner url and API Key in config.toml are valid.")
            return false
        }
        val res_json = JsonParser.parseString(find_user_res.body()).asJsonObject
        for (entry in res_json.get("results").asJsonArray) {
            return entry?.asJsonObject?.get("is_verified")?.asBoolean ?: continue
        }
        
        return false
    }
    
    fun getFindUserResponse(name: String): HttpResponse<String> {
        val runner_url = URI(PluginConfigLoader.findUser_url)
        val api_key = PluginConfigLoader.api_key
        val http_client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(runner_url)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $api_key")
            .POST(HttpRequest.BodyPublishers.ofString("{\"minecraft_username\":\"$name\"}"))
            .build()
            
        val response = http_client.send(request, HttpResponse.BodyHandlers.ofString())
        return response
    }
}
