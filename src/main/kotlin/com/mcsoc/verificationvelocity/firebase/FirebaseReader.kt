package com.mcsoc.verificationvelocity.firebase

import com.velocitypowered.api.proxy.Player


object FirebaseReader {
    fun checkIfPlayerIsWhitelisted(player: Player): Boolean {
        val uuid = player.gameProfile.id
        
        return false
    }
}
