package com.mcsoc.verificationvelocity.eventhandlers

import com.mcsoc.verificationvelocity.database.VerifiedPlayersCacheLoader
import com.mcsoc.verificationvelocity.dataloader.PluginDataLoader
import com.mcsoc.verificationvelocity.firebase.FirebaseReader
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.Logger
import kotlin.jvm.optionals.getOrNull


private val DISCONNECT_MESSAGE = {form_url: String, discord_invite: String -> 
    Component.text("You are not whitelisted!!!\n\n")
    .color(NamedTextColor.AQUA)
    .append(
        Component.text("Please fill out the form: ")
        .color(NamedTextColor.GRAY)
        .append(
            Component.text(form_url)
            .color(NamedTextColor.LIGHT_PURPLE)
            .append(
                Component.text("\nand join the discord ")
                .color(NamedTextColor.GRAY)
                .append(
                    Component.text(discord_invite)
                    .color(NamedTextColor.LIGHT_PURPLE)
                )
                .append(
                    Component.text(".")
                    .color(NamedTextColor.GRAY)
                )
            )
        )
    )
}

class OnPlayerJoinEvent(val logger: Logger) {
    @Subscribe(priority = 10)
    fun checkIfWhitelisted(ctx: ServerPreConnectEvent) {
        val joiner = ctx.player
        
        val server_joining_name = ctx.result.server.getOrNull()?.serverInfo?.name
        if (PluginDataLoader.whitelisted_servers.none{it == server_joining_name} ||
            VerifiedPlayersCacheLoader.checkIfPlayerPresent(joiner.gameProfile)
        ) return joiner.disconnect(Component.text("no check necessary"))
        
        if (FirebaseReader.checkIfPlayerIsWhitelisted(joiner, logger)) {
            VerifiedPlayersCacheLoader.cachePlayer(joiner.gameProfile)
            return joiner.disconnect(Component.text("passed whitelist"))
        } else {
            val form_url = PluginDataLoader.form_url
            val discord_url = PluginDataLoader.discord_url
            ctx.result = ServerPreConnectEvent.ServerResult.denied()
            joiner.disconnect(DISCONNECT_MESSAGE(form_url, discord_url))
        }
    }
}
