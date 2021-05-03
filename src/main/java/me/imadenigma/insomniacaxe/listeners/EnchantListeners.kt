package me.imadenigma.insomniacaxe.listeners

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.lucko.helper.Schedulers
import me.mattstudios.mfgui.gui.components.ItemNBT
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
        val axe = user.getAxeByUUID(uuid) ?: return
        axe.enchants.forEach { it.function(e) }

    }

    @EventHandler
    fun onAxeHold(e: PlayerItemHeldEvent) {
        println("previous: ${e.previousSlot} and next: ${e.newSlot}")
        Schedulers.sync().runLater({
            val user = AxeHolder.getHolder(e.player)
            if (!user.isValid()) return@runLater
            if (!user.isHoldingInsoAxe()) {
                user.axes.forEach { axe ->
                    axe.enchants.forEach { it.function(e) }
                }
                return@runLater
            }
            val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
            val uuid = UUID.fromString(nbtVal)
            val axe = user.getAxeByUUID(uuid) ?: return@runLater
            axe.enchants.forEach { it.function(e) }
        },3L)
    }
}




