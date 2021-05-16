package me.imadenigma.insomniacaxe.listeners

import me.imadenigma.insomniacaxe.*
import me.imadenigma.insomniacaxe.axe.Axe
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
import kotlin.NoSuchElementException

class EnchantListeners : Listener {

    init {
        InsomniacAxe.singleton.registerListener(this)
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val user = AxeHolder.getHolder(e.player)
        if (!user.isHoldingInsoAxe()) return
        val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
        val axe = user.getAxeByUUID(nbtVal) ?: return
        user.give(Manager.coins);user.drops.addAll(e.block.drops);axe.enchants.ordered().forEach { it.function(e) }
        if (e.block.isInsoBlock()) {
            if (user.drops.isNotEmpty()) {
                e.isDropItems = false
                if (!user.zeus) {
                    user.drops.forEach {
                        e.block.world.dropItem(e.block.location, it)
                    }
                }

            }
            if (AxeLevel.isFrenzy) user.give(Manager.coins)
        }

        if (AxeLevel.getBlocksToNextLevel(axe.level) <= axe.brokenBlocks) {
            if (axe.level != AxeLevel.blocksToNextLevel.size) axe.level++
        }

        for (drop in user.drops ) {
            for (i in 0 until drop.amount) {
                val bool = drop.type == Material.PUMPKIN
                axe.addBlock(bool)
                user.countBreakingBlocks(bool)
            }
        }
        user.drops.clear()

    }

    @EventHandler
    fun onBlockIgnite(e: BlockIgniteEvent) {
        if (e.cause == BlockIgniteEvent.IgniteCause.LIGHTNING && e.block.isInsoBlock()) e.isCancelled = true
    }

}






