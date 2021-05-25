package me.imadenigma.insomniacaxe.listeners

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.getDisplayName
import me.imadenigma.insomniacaxe.gui.EnchantGui
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.ordered
import me.lucko.helper.Schedulers
import me.mattstudios.mfgui.gui.components.ItemNBT
import net.milkbowl.vault.chat.Chat
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ItemDespawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*
import java.util.stream.Collectors


class PlayerListeners : Listener {

    init {
        InsomniacAxe.singleton.registerListener(this)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val item = e.player.inventory.itemInMainHand ?: return
        if (!Axe.isAxe(item)) return
        val axe = AxeHolder.getHolder(e.player).getAxeInMainHand() ?: return
        if (axe.enchants.any { !it.isEnabled }) {
            val disabledEnchs = axe.enchants.stream().filter { !it.isEnabled }.collect(Collectors.toSet())
            val meta = e.player.inventory.itemInMainHand.itemMeta ?: Bukkit.getItemFactory()
                .getItemMeta(e.player.inventory.itemInMainHand.type)
            val lore = meta.lore!!
            val li = mutableSetOf<String>()
            lore.stream()
                .filter { disabledEnchs.any { ench -> ChatColor.stripColor(ench.name) == ChatColor.stripColor(it) } }
                .forEach {
                    li.add(it)
                }
            for (str in li) {
                lore.remove(str)
                lore.add(0, ChatColor.getLastColors(str) + "&m".colorize() + ChatColor.stripColor(str))

            }
            meta.lore = lore
            e.player.inventory.itemInMainHand.itemMeta = meta
        }
        if (axe.enchants.any { it.isEnabled }) {
            val disabledEnchs = axe.enchants.stream().filter { it.isEnabled }.collect(Collectors.toSet())
            val meta = e.player.inventory.itemInMainHand.itemMeta ?: Bukkit.getItemFactory()
                .getItemMeta(e.player.inventory.itemInMainHand.type)
            val lore = meta.lore!!
            val li = mutableSetOf<String>()
            lore.stream()
                .filter { disabledEnchs.any { ench -> ChatColor.stripColor(ench.name) == ChatColor.stripColor(it) } }
                .forEach {
                    li.add(it)
                }
            for (str in li) {
                lore.remove(str)
                lore.add(0, StringUtils.remove(str, "&m".colorize()))

            }
            meta.lore = lore
            e.player.inventory.itemInMainHand.itemMeta = meta
        }
    }

    @EventHandler
    fun onItemDamage(e: ItemDespawnEvent) {
        val nbtVal = ItemNBT.getNBTTag(e.entity.itemStack, "uuid")
        if (Axe.axes.containsKey(nbtVal))
            ("Deleting the ${Axe.axes.remove(nbtVal)?.material?.name}, uuid: $nbtVal")
    }

    @EventHandler
    fun onAxeHold(e: PlayerItemHeldEvent) {
        Schedulers.sync().runLater({
            val user = AxeHolder.getHolder(e.player)
            if (!user.isHoldingInsoAxe()) {
                for (item in e.player.inventory.contents
                    .filter { Objects.nonNull(it) }
                    .filter { it.type.name.contains("_AXE", true) }) {
                    val axe = user.getAxeByUUID(ItemNBT.getNBTTag(item, "uuid")) ?: continue
                    axe.enchants.forEach { it.function(e) }
                }
                return@runLater
            }
            val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
            val axe = user.getAxeByUUID(nbtVal) ?: return@runLater
            if (axe.enchants.any { !it.isEnabled }) {
                val disabledEnchs = axe.enchants.stream().filter { !it.isEnabled }.collect(Collectors.toSet())
                val meta = e.player.inventory.itemInMainHand.itemMeta ?: Bukkit.getItemFactory()
                    .getItemMeta(e.player.inventory.itemInMainHand.type)
                val lore = meta.lore!!
                val li = mutableSetOf<String>()
                lore.stream()
                    .filter { disabledEnchs.any { ench -> ChatColor.stripColor(ench.name) == ChatColor.stripColor(it) } }
                    .forEach {
                        li.add(it)
                    }
                for (str in li) {
                    lore.remove(str)
                    lore.add(0, ChatColor.getLastColors(str) + "&m".colorize() + ChatColor.stripColor(str))

                }
                meta.lore = lore
                e.player.inventory.itemInMainHand.itemMeta = meta
            }
            if (axe.enchants.any { it.isEnabled }) {
                val disabledEnchs = axe.enchants.stream().filter { it.isEnabled }.collect(Collectors.toSet())
                val meta = e.player.inventory.itemInMainHand.itemMeta ?: Bukkit.getItemFactory()
                    .getItemMeta(e.player.inventory.itemInMainHand.type)
                val lore = meta.lore!!
                val li = mutableSetOf<String>()
                lore.stream()
                    .filter { disabledEnchs.any { ench -> ChatColor.stripColor(ench.name) == ChatColor.stripColor(it) } }
                    .forEach {
                        li.add(it)
                    }
                for (str in li) {
                    lore.remove(str)
                    lore.add(0, StringUtils.remove(str, "&m".colorize()))

                }
                meta.lore = lore
                e.player.inventory.itemInMainHand.itemMeta = meta
            }

            for (ench in axe.enchants.ordered()) {
                ench.function(e)
            }
        }, 3L)
    }

    @EventHandler
    fun onAxeClick(e: PlayerInteractEvent) {
        if (!e.hasItem()) return
        if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_AIR) {
            val user = AxeHolder.getHolder(e.player)
            val axe = user.getAxeInMainHand() ?: return
            EnchantGui.openGui(e.player)
        }
    }

}