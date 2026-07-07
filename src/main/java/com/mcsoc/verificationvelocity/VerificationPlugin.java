package com.mcsoc.verificationvelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPreShutdownEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;
import org.slf4j.Logger;

import com.mcsoc.verificationvelocity.dataloader.PluginDataLoader;
import com.mcsoc.verificationvelocity.eventhandlers.CommandRegistration;
import com.mcsoc.verificationvelocity.eventhandlers.OnPlayerJoinEvent;


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

    private final Logger logger;
    private final ProxyServer server;
    private final Path data_directory;

    @Inject
    public VerificationPlugin(ProxyServer server, Logger logger, @DataDirectory Path data_directory) {
        this.server = server;
        this.logger = logger;
        this.data_directory = data_directory;

        logger.info("Hello there! I made my first plugin with Velocity.");
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        PluginDataLoader.setDataDirectory(data_directory);
        PluginDataLoader.loadDataFromFiles();
        
        server.getEventManager().register(this, new OnPlayerJoinEvent(logger));
        CommandRegistration.registerCommands(this, server);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        PluginDataLoader.saveDataToFiles();
    }
}