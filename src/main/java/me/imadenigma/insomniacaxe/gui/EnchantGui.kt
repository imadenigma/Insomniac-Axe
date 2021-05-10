package me.imadenigma.insomniacaxe.gui

import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.addEnchant
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.lucko.helper.Services
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.components.ItemNBT
import me.mattstudios.mfgui.gui.guis.Gui
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemHeldEvent
import java.util.*

class EnchantGui(player: Player, lines: Int, title: String) {

    init {
        val gui = Gui(lines, title.colorize())
        val enchants = Services.load(EnchantsFactory::class.java).enchants.values
        val user = AxeHolder.getHolder(player)
        for (enchant in enchants) {
            val lore = enchant.lore.toMutableList()
            val balanceCheck = InsomniacAxe.singleton.economy!!.getBalance(player) < enchant.price
            if (balanceCheck) {
                lore.add("&4You don't have balance to purchase it".colorize())
            }
            if (!user.isHoldingInsoAxe()) {
                lore.add("&4You aren't Holding an insomniac axe".colorize())
            }
            val item = ItemBuilder.from(enchant.material)
                .glow(enchant.isGlowing)
                .setLore(lore)
                .setName(enchant.name)
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
                val axe = user.getAxeByUUID(UUID.fromString(ItemNBT.getNBTTag(player.inventory.itemInMainHand, "uuid"))) ?: run {
                    player.sendMessage("&4Can't get the axe, try again".colorize())
                    return@asGuiItem
                }
                axe.enchants.add(enchant)
                InsomniacAxe.singleton.economy!!.withdrawPlayer(player, enchant.price)
                player.sendMessage("&eYou bought ${enchant.name} Successfully".colorize())
                player.inventory.itemInMainHand.addEnchant(enchant)
                player.closeInventory()

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
                lines = configuration.getNode("gui","lines").getInt(0)
                title = configuration.getNode("gui","title").getString("")
            }
            EnchantGui(player,lines, title)
        }
    }
}