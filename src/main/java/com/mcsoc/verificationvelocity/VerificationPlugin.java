package com.mcsoc.verificationvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import com.mcsoc.verificationvelocity.CommandRegistration;


@Plugin(
    id = VerificationPlugin.MODID, 
    name = "MCSoc Verification Plugin - Velocity", 
    version = "0.0.1",
    url = "https://mc.unswminecraft.com", 
    description = "A velocity plugin to interface with UNSW Minecraft Society's Firebase verification system.", 
    authors = {"veveddo"}
)
public class VerificationPlugin {
    
    public static final String MODID = "mcsoc-verification";

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VerificationPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        logger.info("Hello there! I made my first plugin with Velocity.");
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new OnPlayerJoinEvent());
        CommandRegistration.registerCommands(this, server);
    }
}