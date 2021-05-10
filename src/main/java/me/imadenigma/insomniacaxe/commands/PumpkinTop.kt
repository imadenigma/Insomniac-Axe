package me.imadenigma.insomniacaxe.commands

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.gui.EnchantGui
import me.lucko.helper.Services
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PumpkinTop : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val config = Services.load(Configuration::class.java).configNode
        val format = config.getNode("pumpkin-top", "format").getString("null")
        val size = config.getNode("pumpkin-top", "size").getInt(0)
        val usersSorted = AxeHolder.users.sortedBy { run {
            var i = 0
            it.axes.forEach { axe -> i += axe.brokenBlocks.toInt() }
            i
        } }
        for (i in 1..size) {
            sender.sendMessage(format.replace("{0}",usersSorted[i - 1].offlinePlayer.name!!))
        }
        return true
    }
}