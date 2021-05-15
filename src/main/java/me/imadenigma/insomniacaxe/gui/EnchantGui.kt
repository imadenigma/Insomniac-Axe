package me.imadenigma.insomniacaxe.gui

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.addEnchant
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.imadenigma.insomniacaxe.enchant.enchants.Haste
import me.imadenigma.insomniacaxe.enchant.enchants.Speed
import me.lucko.helper.Services
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.components.ItemNBT
import me.mattstudios.mfgui.gui.guis.Gui
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import kotlin.streams.toList

class EnchantGui(player: Player, lines: Int, title: String) {

    init {
        val gui = Gui(lines, title.colorize())
        val enchants = Services.load(EnchantsFactory::class.java).enchants.values
        val user = AxeHolder.getHolder(player)
        for (enchant in enchants) {
            val lore = enchant.lore.stream().map { it.colorize() }.toList().toMutableList()
            val balanceCheck = InsomniacAxe.singleton.economy!!.getBalance(player) < enchant.price
            var owningCheck = false
            if (balanceCheck) {
                lore.add("&4You don't have balance to purchase it".colorize())
            }
            if (!enchant.isEnabled) {
                lore.add("&4disabled".colorize())
            }
            if (!user.isHoldingInsoAxe()) {
                lore.add("&4You aren't Holding an insomniac axe".colorize())
            } else {
                owningCheck = user.getAxeInMainHand()!!.enchants.contains(enchant)
                if (owningCheck) {
                    lore.add("&3You already own this enchant".colorize())
                }
            }
            val prefix = if (!enchant.isEnabled) "&m".colorize()
            else ""
            lore.forEach { it.colorize() }
            val item = ItemBuilder.from(enchant.material)
                .glow(enchant.isGlowing)
                .setLore(lore)
                .setName(ChatColor.getLastColors(enchant.name) + prefix.colorize() + ChatColor.stripColor(enchant.name))
                .asGuiItem {
                    it.isCancelled = true
                    if (balanceCheck) {
                        player.sendMessage("&4You don't have money enough".colorize())
                        return@asGuiItem
                    }
                    if (!user.isHoldingInsoAxe()) {
                        player.sendMessage("&4You aren't holding an axe".colorize())
                        return@asGuiItem
                    }
                    if (owningCheck) {
                        player.sendMessage("&4You already own this enchant".colorize())
                        return@asGuiItem
                    }
                    if (!enchant.isEnabled) {
                        player.sendMessage("&4this enchant is disabled".colorize())
                        return@asGuiItem
                    }
                    val axe =
                        user.getAxeByUUID(ItemNBT.getNBTTag(player.inventory.itemInMainHand, "uuid"))
                            ?: run {
                                player.sendMessage("&4Can't get the axe, try again".colorize())
                                return@asGuiItem
                            }
                    axe.enchants.add(enchant)
                    InsomniacAxe.singleton.economy!!.withdrawPlayer(player, enchant.price)
                    player.sendMessage("&eYou bought ${enchant.name} Successfully".colorize())
                    player.inventory.itemInMainHand.addEnchant(enchant)
                    player.closeInventory()
                    if (enchant is Speed) enchant.firstFun(player)
                    else if (enchant is Haste) enchant.firstFun(player)

                }
            gui.setItem(enchant.slot, item)
        }
        gui.open(player)
    }

    companion object {
        private var lines: Int = 0
        private var title: String = ""
        fun openGui(player: Player) {
            if (lines == 0) {
                val configuration = Services.load(Configuration::class.java).configNode
                lines = configuration.getNode("gui", "lines").getInt(0)
                title = configuration.getNode("gui", "title").getString("")
            }
            EnchantGui(player, lines, title)
        }
    }
}