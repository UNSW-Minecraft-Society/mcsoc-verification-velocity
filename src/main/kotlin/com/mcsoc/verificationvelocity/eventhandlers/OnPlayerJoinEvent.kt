package com.mcsoc.verificationvelocity.eventhandlers

import com.mcsoc.verificationvelocity.firebase.FirebaseReader
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.Logger


private val DISCONNECT_MESSAGE = Component.text("You are not whitelisted!!!\n")
    .color(NamedTextColor.AQUA)
    .append(
        Component.text("Please fill out the form: https://forms.gle/QLhUwVHrwnXRkff86 and join the discord <discord link>.")
        .color(NamedTextColor.GRAY)
    )

class OnPlayerJoinEvent(val logger: Logger) {
    @Subscribe
    fun event(ctx: ServerPreConnectEvent) {
        val joiner = ctx.player
        
        if (FirebaseReader.checkIfPlayerIsWhitelisted(joiner, logger)) {
            joiner.disconnect(Component.text("failed successfully"))
            return
        } else {
            ctx.result = ServerPreConnectEvent.ServerResult.denied()
            joiner.disconnect(DISCONNECT_MESSAGE)
        }
    }
}
