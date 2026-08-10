package com.mcsoc.verificationvelocity.eventhandlers

import kotlin.jvm.optionals.getOrNull
import kotlin.time.Duration.Companion.milliseconds
import org.slf4j.Logger

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration

import com.mcsoc.verificationvelocity.database.VerifiedPlayersCacheLoader
import com.mcsoc.verificationvelocity.configloader.PluginConfigLoader
import com.mcsoc.verificationvelocity.firebase.FirebaseReader


private val DISCONNECT_MESSAGE = {form_url: String, discord_invite: String -> 
    Component.text("You are not whitelisted!!!\n\n")
        .color(NamedTextColor.AQUA)
    .append(
        Component.text("Please fill out the form: ")
        .color(NamedTextColor.GRAY)
    ).append(
        Component.text(form_url)
        .color(NamedTextColor.LIGHT_PURPLE)
        .clickEvent(ClickEvent.openUrl(form_url))
        .decorate(TextDecoration.UNDERLINED)
    ).append(
        Component.text("\nand join the discord: ")
        .color(NamedTextColor.GRAY)
    ).append(
        Component.text(discord_invite)
        .color(NamedTextColor.LIGHT_PURPLE)
        .clickEvent(ClickEvent.openUrl(discord_invite))
        .decorate(TextDecoration.UNDERLINED)
    ).append(
        Component.text(".")
        .color(NamedTextColor.GRAY)
    )
}

private fun handleUnverifiedPlayer(ctx: ServerPreConnectEvent, logger: Logger) {
    val form_url = PluginConfigLoader.form_url
    val discord_url = PluginConfigLoader.discord_url
    val joiner = ctx.player
    ctx.result = ServerPreConnectEvent.ServerResult.denied()
    joiner.disconnect(DISCONNECT_MESSAGE(form_url, discord_url))
}

class OnPlayerJoinEvent(val logger: Logger) {
    @Subscribe(priority = 10)
    fun checkIfWhitelisted(ctx: ServerPreConnectEvent) {
        val joiner = ctx.player
        
        if (PluginConfigLoader.debug_autofail) {
            handleUnverifiedPlayer(ctx, logger)
            return
        }
        
        val server_joining_name = ctx.result.server.getOrNull()?.serverInfo?.name
        if (PluginConfigLoader.whitelisted_servers.none{it == server_joining_name} ||
            VerifiedPlayersCacheLoader.checkIfPlayerPresent(joiner.gameProfile)
        ) {
            if (PluginConfigLoader.debug_earlydisconnect) {
                joiner.disconnect(Component.text("no check necessary"))
                return
            }
        }
        
        if (FirebaseReader.checkIfPlayerIsWhitelisted(joiner, logger)) {
            VerifiedPlayersCacheLoader.cachePlayer(joiner.gameProfile)
            if (PluginConfigLoader.debug_earlydisconnect) {
                joiner.disconnect(Component.text("passed whitelist"))
                return
            }
        } else {
            handleUnverifiedPlayer(ctx, logger)
            return
        }
    }
}