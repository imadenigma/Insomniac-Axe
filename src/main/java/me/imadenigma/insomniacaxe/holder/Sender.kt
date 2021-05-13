package me.imadenigma.insomniacaxe.holder

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.colorize
import me.lucko.helper.Services

object Sender {
    fun cMsg(path: String): String {
        return Services.load(Configuration::class.java).languageNode.getNode(path.split(".")).getString("").colorize()
    }
}