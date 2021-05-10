package me.imadenigma.insomniacaxe.listeners

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.isInsoBlock
import me.imadenigma.insomniacaxe.ordered
import me.lucko.helper.Schedulers
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import java.util.*

class EnchantListeners : Listener {

    init {
        InsomniacAxe.singleton.registerListener(this)
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {

        val user = AxeHolder.getHolder(e.player)
        if (!user.isValid()) return
        if (!user.isHoldingInsoAxe()) return
        val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
        val uuid = run {
            try {
                UUID.fromString(nbtVal)
            }catch (e: IllegalArgumentException) {
                return
            }
        }
        e.isDropItems = false
        user.drops.addAll(e.block.drops)
        val axe = user.getAxeByUUID(uuid) ?: return
        axe.enchants.ordered().forEach { it.function(e) }
        if (e.block.isInsoBlock()) {
            axe.brokenBlocks++
            e.block.drops.clear()
            user.drops.forEach {
                e.block.world.dropItem(e.block.location, it)
            }
        }
        user.drops.clear()

    }

    @EventHandler
    fun onAxeHold(e: PlayerItemHeldEvent) {
        Schedulers.sync().runLater({
            val user = AxeHolder.getHolder(e.player)
            if (!user.isValid()) return@runLater
            if (!user.isHoldingInsoAxe()) {
                user.axes.forEach { axe ->
                    axe.enchants.forEach {
                        it.function(e)
                    }
                }
                return@runLater
            }
            val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
            val uuid = UUID.fromString(nbtVal)
            val axe = user.getAxeByUUID(uuid) ?: return@runLater
            for (ench in axe.enchants.ordered()) {
                ench.function(e)
            }
        },3L)
    }
}






