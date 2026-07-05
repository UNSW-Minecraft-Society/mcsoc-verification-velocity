package com.mcsoc.verificationvelocity

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor


object CommandRegistration {
    @JvmStatic
    fun registerCommands(proxy: ProxyServer, plugin: VerificationPlugin) {
        val manager = proxy.commandManager
        val debug_command = this.registerDebugCommand(proxy)
        val meta = manager.metaBuilder(debug_command)
            // .aliases()
            .plugin(plugin)
            .build()
            
        manager.register(meta, debug_command)
    }
    
    private fun registerDebugCommand(proxy: ProxyServer): BrigadierCommand {
        val rootNode: LiteralArgumentBuilder<CommandSource> = BrigadierCommand.literalArgumentBuilder(VerificationPlugin.MODID)
        .then(BrigadierCommand.literalArgumentBuilder("debug")
        .executes{ctx ->
            val source = ctx.source
            source.sendMessage{
                Component.text("Hello World", NamedTextColor.AQUA)
            }
            return@executes Command.SINGLE_SUCCESS
        })
        return BrigadierCommand(rootNode)
    }
}