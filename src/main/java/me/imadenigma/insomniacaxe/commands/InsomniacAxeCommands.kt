package me.imadenigma.insomniacaxe.commands

import com.google.common.reflect.TypeToken
import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.gui.EnchantGui
import me.lucko.helper.Helper
import me.lucko.helper.Schedulers
import me.lucko.helper.Services
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class InsomniacAxeCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // /insomniac give <player> <material> <level>
        if (args.isEmpty()) return false
        val language = Services.get(Configuration::class.java).get().languageNode
        if (args.size >= 4) {
            if (args[1].equals("gems", true)) {
                if (args[0].equals("set", true)) {
                    val player = Bukkit.getPlayer(args[2]) ?: return false
                    val user = AxeHolder.getHolder(player)
                    var amount = user.balance
                    try {
                        amount = args[3].toLong()
                    } catch (e: NumberFormatException) {
                    }
                    user.set(amount)
                    val receiverMsg =
                        language.getNode("receiver-set-balance").getString("null").replace("{0}", amount.toString())
                    val senderMsg =
                        language.getNode("sender-set-balance").getString("null").replace("{0}", player.displayName)
                            .replace("{1}", amount.toString())
                    player.sendMessage(receiverMsg.colorize())
                    sender.sendMessage(senderMsg.colorize())
                }
                if (args[0].equals("remove",true)) {
                    val player = Bukkit.getPlayer(args[2]) ?: return false
                    val user = AxeHolder.getHolder(player)
                    var amount = 0L
                    try {
                        amount = args[3].toLong()
                    } catch (e: NumberFormatException) {
                    }
                    user.take(amount)
                    val receiverMsg =
                        language.getNode("receiver-remove-balance").getString("null").replace("{0}", player.displayName)
                            .replace("{1}", amount.toString())
                    val senderMsg =
                        language.getNode("sender-remove-balance").getString("null").replace("{0}", player.displayName)
                            .replace("{1}", amount.toString())
                    player.sendMessage(receiverMsg.colorize())
                    sender.sendMessage(senderMsg.colorize())
                }
            }
        }
        if (args[0].equals("give", true)) {
            if (args[1].equals("gems",true)) {
                val player = Bukkit.getPlayer(args[2]) ?: return false
                val user = AxeHolder.getHolder(player)
                var amount = 0L
                try {
                    amount = args[3].toLong()
                } catch (e: NumberFormatException) {
                }
                user.give(amount)
                val receiverMsg =
                    language.getNode("receiver-give-balance").getString("null").replace("{0}", player.displayName)
                        .replace("{1}", amount.toString())
                val senderMsg =
                    language.getNode("sender-give-balance").getString("null").replace("{0}", player.displayName)
                        .replace("{1}", amount.toString())
                player.sendMessage(receiverMsg.colorize())
                sender.sendMessage(senderMsg.colorize())
            }
            if (!sender.hasPermission("insomniac.give")) {
                sender.sendMessage("&4You must have permission to execute this command".colorize())
                return false
            }
            if (args.size < 4) {
                sender.sendMessage("&e/insomniac give <player> <material> <level>".colorize())
                return false
            }
            val player = Bukkit.getPlayer(args[1]) ?: run {
                sender.sendMessage("&4Player not found".colorize())
                return false
            }
            val material = Material.matchMaterial(args[2]) ?: run {
                sender.sendMessage("&4Item not found".colorize())
                return false
            }
            var level = 1
            try {
                level = args[3].toInt()
                if (level > Services.load(Configuration::class.java).configNode.getNode("levels").childrenMap.size || level == 0) {
                    sender.sendMessage("&4Level is not available".colorize())
                    return false
                }
            } catch (e: NumberFormatException) {
            }
            val uuid = UUID.randomUUID()
            val axe = Axe(level, material, mutableListOf(), uuid, 0L)
            val holder = AxeHolder.getHolder(player)
            holder.giveAxe(axe)
            sender.sendMessage("&3You gave ${player.displayName} &3Axe successfully".colorize())
        }
        if (args[0].equals("shop", true) && sender is Player) {
            return true
        }
        if (args[0].equals("reload", true) && sender.hasPermission("insomniac.reload")) {
            Schedulers.sync().runLater(
                {
                    Helper.plugins().enablePlugin(InsomniacAxe.singleton)
                }, 10L
            )
            Helper.plugins().disablePlugin(InsomniacAxe.singleton)
        }
        if (args[0].equals("help",true)) {
            val list = language.getNode("insomniac-help").getList(TypeToken.of(String::class.java))
            list.forEach {
                sender.sendMessage(it.colorize())
            }
        }

        return false
    }
}