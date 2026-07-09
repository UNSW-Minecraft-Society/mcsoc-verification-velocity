package com.mcsoc.verificationvelocity.eventhandlers

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer

import com.mcsoc.verificationvelocity.VerificationPlugin
import com.mcsoc.verificationvelocity.configloader.PluginConfigLoader


object CommandRegistration {
    
    private fun constructDebugCommand(proxy: ProxyServer): BrigadierCommand {
        val rootNode: LiteralArgumentBuilder<CommandSource> = BrigadierCommand.literalArgumentBuilder(VerificationPlugin.MODID)
        .then(BrigadierCommand.literalArgumentBuilder("debug")
        .executes{ctx ->
            val source = ctx.source
            
            return@executes Command.SINGLE_SUCCESS
        })
        return BrigadierCommand(rootNode)
    }
    
    private fun constructReloadConfigCommand(proxy: ProxyServer): BrigadierCommand {
        val rootNode: LiteralArgumentBuilder<CommandSource> = BrigadierCommand.literalArgumentBuilder(VerificationPlugin.MODID)
        .then(BrigadierCommand.literalArgumentBuilder("reload")
        .executes{ctx ->
            val source = ctx.source
            PluginConfigLoader.loadDataFromFiles()
            return@executes Command.SINGLE_SUCCESS
        })
        return BrigadierCommand(rootNode)
    }
    
    
    private fun registerCommand(plugin: VerificationPlugin, proxy: ProxyServer, command: BrigadierCommand) {
        val manager = proxy.commandManager
        val meta = manager.metaBuilder(command)
            // .aliases()
            .plugin(plugin)
            .build()
        manager.register(meta, command)
    }
    
    @JvmStatic
    fun registerCommands(plugin: VerificationPlugin, proxy: ProxyServer) {
        val manager = proxy.commandManager
        
        // registerCommand(plugin, proxy, this.constructDebugCommand(proxy))
        registerCommand(plugin, proxy, this.constructReloadConfigCommand(proxy))
    }
}