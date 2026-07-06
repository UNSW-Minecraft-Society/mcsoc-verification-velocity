package com.mcsoc.verificationvelocity.eventhandlers

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer

import com.mcsoc.verificationvelocity.VerificationPlugin
import com.mcsoc.verificationvelocity.dataloader.PluginDataLoader


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
            PluginDataLoader.loadDataFromFiles()
            return@executes Command.SINGLE_SUCCESS
        })
        return BrigadierCommand(rootNode)
    }
    
    @JvmStatic
    fun registerCommands(plugin: VerificationPlugin, proxy: ProxyServer) {
        val manager = proxy.commandManager
        
        val debug_command = this.constructDebugCommand(proxy)
        val debug_meta = manager.metaBuilder(debug_command)
            // .aliases()
            .plugin(plugin)
            .build()
        manager.register(debug_meta, debug_command)
    }
}