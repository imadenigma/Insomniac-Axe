package me.imadenigma.insomniacaxe.commands

import me.imadenigma.insomniacaxe.gui.EnchantGui
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Upgrade : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) EnchantGui.openGui(sender)
        return true
    }
}