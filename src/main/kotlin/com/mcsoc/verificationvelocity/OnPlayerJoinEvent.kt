package com.mcsoc.verificationvelocity

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import org.slf4j.Logger

class OnPlayerJoinEvent(val logger: Logger) {

    @Subscribe
    fun event(ctx: ServerPreConnectEvent) {
        logger.info("p: {}", ctx.player)
    }
}
