package me.imadenigma.insomniacaxe.commands

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.lucko.helper.Services
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PumpkinTop : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty()) {
            val config = Services.load(Configuration::class.java).configNode
            val format = config.getNode("pumpkin-top", "format").getString("null")
            val size = config.getNode("pumpkin-top", "size").getInt(0)
            val usersSorted = AxeHolder.users.sortedBy(AxeHolder::brokenPumps)
            usersSorted.stream().limit(size.toLong()).forEach {
                sender.sendMessage(
                    format.replace("{0}", it.offlinePlayer.name!!).replace("{1}", it.brokenPumps.toString()).colorize()
                )
            }
        }
        return true
    }
}