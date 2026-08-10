package com.mcsoc.verificationvelocity

import com.google.inject.Inject
import com.mcsoc.verificationvelocity.database.VerifiedPlayersCacheLoader
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer

import java.nio.file.Path

import org.slf4j.Logger

import com.mcsoc.verificationvelocity.configloader.PluginConfigLoader;
import com.mcsoc.verificationvelocity.eventhandlers.CommandRegistration;
import com.mcsoc.verificationvelocity.eventhandlers.OnPlayerJoinEvent;


@Plugin(
    id = VerificationPlugin.MODID, 
    name = "MCSoc Verification Plugin - Velocity", 
    version = "0.1.2",
    url = "https://mc.unswminecraft.com", 
    description = "A velocity plugin to interface with UNSW Minecraft Society's Firebase verification system.", 
    authors = ["veveddo"]
)
class VerificationPlugin {
    companion object {
        const val MODID = "mcsoc-verification"
    }

    private val logger: Logger
    private val server: ProxyServer
    private val data_directory: Path
    
    @Inject
    constructor(server: ProxyServer, logger: Logger, @DataDirectory data_directory: Path) {
        this.server = server
        this.logger = logger
        this.data_directory = data_directory
    }

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        PluginConfigLoader.initialise(data_directory, logger)
        PluginConfigLoader.loadDataFromFiles()
        
        VerifiedPlayersCacheLoader.initialise(data_directory)

        server.eventManager.register(this, OnPlayerJoinEvent(server, logger))
        CommandRegistration.registerCommands(this, server)
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        PluginConfigLoader.saveDataToFiles()
    }
}