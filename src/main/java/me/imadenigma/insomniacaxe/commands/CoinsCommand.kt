package me.imadenigma.insomniacaxe.commands

import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.holder.Sender
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CoinsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args[0].equals("balance",true)) {
            if (sender is Player) {
                val balance = AxeHolder.getHolder(sender).balance
                sender.sendMessage(Sender.cMsg("player-balance").replace("{0}", balance.toString()))
                return true
            }
        }
        return true
    }
}