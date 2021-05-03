package me.imadenigma.insomniacaxe.command

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.lucko.helper.Services
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

class Command : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // /insomniac give <player> <material> <level>
        if (args[0] == "give") {
            if (!sender.hasPermission("insomniac.give")) {
                sender.sendMessage("&4You must have permission to execute this command".colorize())
                return false
            }
            if (args.size < 4) {
                sender.sendMessage("&e/insomniac give <player> <material> <level>")
                return false
            }
            val player = Bukkit.getPlayer(args[1]) ?: run {
                sender.sendMessage("&4Player not found")
                return false
            }
            val material = Material.matchMaterial(args[2]) ?: run {
                sender.sendMessage("&4Item not found")
                return false
            }
            var level = 1
            try {
                level = args[3].toInt()
                if (level > Services.load(Configuration::class.java).configNode.getNode("levels").childrenMap.size || level == 0) {
                    sender.sendMessage("&4Level is not available".colorize())
                    return false
                }
            } catch (e: NumberFormatException) {}
            val uuid = UUID.randomUUID()
            val axe = Axe(level, material, mutableListOf(),uuid)
            val enchant = Services.load(EnchantsFactory::class.java).enchants
            axe.enchants.add(enchant["speed"]!!)
            val holder = AxeHolder.getHolder(player)
            holder.giveAxe(axe)
            sender.sendMessage("&3You gave ${player.displayName} &3Axe successfully".colorize())
        }
        return true
    }
}