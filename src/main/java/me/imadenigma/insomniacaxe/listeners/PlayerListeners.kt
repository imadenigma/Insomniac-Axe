package me.imadenigma.insomniacaxe.listeners

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.lucko.helper.Helper
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent


class PlayerListeners : Listener {

    init {
        InsomniacAxe.singleton.registerListener(this)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val item = e.player.inventory.itemInMainHand ?: return
        if (!Axe.isAxe(item)) return
        val axe = AxeHolder.getHolder(e.player).getAxeInMainHand() ?: return
        val lore = item?.itemMeta?.lore ?: return
        axe.enchants.filter { ench -> !ench.isEnabled }.forEach { ench ->
            lore.stream().filter { ChatColor.stripColor(it) == ench.name }.forEach { str ->
                lore.remove(str)
                lore.add("&m".colorize() + str)
            }
        }
        item.itemMeta.lore = lore
    }

}