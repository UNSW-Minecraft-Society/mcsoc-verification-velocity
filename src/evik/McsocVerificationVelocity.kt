package com.mcsoc.verificationvelocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger


@Plugin(
    id = "mcsoc-verification-velocity", 
    name = "MCSoc Verification Plugin - Velocity", 
    version = "0.0.1",
    url = "https://mc.unswminecraft.com", 
    description = "A velocity plugin to interface with UNSW Minecraft Society's Firebase verification system.", 
    authors = ["veveddo"]
)
class VelocityTest {

    private val server: ProxyServer
    private val logger: Logger

    @Inject
    constructor(server: ProxyServer, logger: Logger) {
        this.server = server
        this.logger = logger

        logger.info("Hello there! I made my first plugin with Velocity.")
    }
}