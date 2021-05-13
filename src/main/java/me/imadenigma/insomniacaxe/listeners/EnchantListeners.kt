package me.imadenigma.insomniacaxe.listeners

import me.imadenigma.insomniacaxe.*
import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.lucko.helper.Schedulers
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import java.util.*

class EnchantListeners : Listener {

    init {
        InsomniacAxe.singleton.registerListener(this)
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val user = AxeHolder.getHolder(e.player)
        if (!user.isHoldingInsoAxe()) return
        val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
        val uuid = run {
            try {
                UUID.fromString(nbtVal)
            } catch (e: IllegalArgumentException) {
                return
            }
        }
        val axe = user.getAxeByUUID(uuid) ?: return
        user.give(Manager.coins);e.isDropItems = false;user.drops.addAll(e.block.drops);axe.enchants.ordered().forEach { it.function(e) }
        if (e.block.isInsoBlock()) {
            if (AxeLevel.isFrenzy) user.give(Manager.coins)
            e.block.drops.clear()
            axe.brokenBlocks++
            if (e.block.type == Material.PUMPKIN) axe.brokenPumpkins++
            if (!user.zeus) {
                user.drops.forEach {
                    e.block.world.dropItem(e.block.location, it)
                }

            }
            if (AxeLevel.getBlocksToNextLevel(axe.level) <= axe.brokenBlocks) {
                if (axe.level != AxeLevel.blocksToNextLevel.size) axe.level++
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
        }, 3L)
    }

    @EventHandler
    fun onBlockIgnite(e: BlockIgniteEvent) {
        if (e.cause == BlockIgniteEvent.IgniteCause.LIGHTNING && e.block.isInsoBlock()) e.isCancelled = true
    }

}






