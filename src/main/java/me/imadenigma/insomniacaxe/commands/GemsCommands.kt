package me.imadenigma.insomniacaxe.commands

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.holder.Sender
import me.lucko.helper.Services
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.NumberFormatException

class GemsCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false
        if (!args[0].equals("gems",true)) return false
        if (args.size < 2) return false
        val language = Services.get(Configuration::class.java).get().languageNode
        if (args[1].equals("balance",true)) {
            if (sender !is Player) return false
            val holder = AxeHolder.getHolder(sender)
            val msg = language.getNode("nightmare-balance").getString("null").replace("{1}",holder.balance.toString())
            sender.sendMessage(msg.colorize())
        }
        if (args[1].equals("pay", true)) {
            val player = Bukkit.getPlayer(args[2]) ?: return true
            val user = AxeHolder.getHolder(player)
            var amount = 0
            try {
                amount = args[3].toInt()
            }catch (e: NumberFormatException) {}
            user.give(amount.toLong())
            if (sender is Player) AxeHolder.getHolder(sender).take(amount.toLong())
            val senderMsg = language.getNode("sender-pay").getString("null").replace("{0}",player.displayName).replace("{1}", amount.toString())
            val receiverMsg = language.getNode("receiver-pay").getString("null").replace("{0}",player.displayName).replace("{1}", amount.toString())
            sender.sendMessage(senderMsg.colorize())
            player.sendMessage(receiverMsg.colorize())
        }
        return true
    }
}