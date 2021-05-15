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

class EnchantListeners : Listener {

    init {
        InsomniacAxe.singleton.registerListener(this)
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val user = AxeHolder.getHolder(e.player)
        if (!user.isHoldingInsoAxe()) return
        val nbtVal = ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")
        val axe = user.getAxeByUUID(nbtVal) ?: kotlin.run {
            println("hna lprblm")
            return
        }
        user.give(Manager.coins);user.drops.addAll(e.block.drops);axe.enchants.ordered().forEach { it.function(e) }
        for(i in 1..user.drops.first().amount) {
            user.increaseBlocks()
        }
        if (e.block.isInsoBlock()) {
            if (e.block.type == Material.PUMPKIN) user.brokenPumps++
            if (user.drops.isNotEmpty()) {
                axe.brokenBlocks += user.drops.first().amount
                e.isDropItems = false
                if (e.block.type == Material.PUMPKIN) axe.brokenPumpkins += user.drops.first { it.type == Material.PUMPKIN }.amount
                if (!user.zeus) {
                    user.drops.forEach {
                        e.block.world.dropItem(e.block.location, it)
                    }
                }

            }
            if (AxeLevel.isFrenzy) user.give(Manager.coins)

            if (AxeLevel.getBlocksToNextLevel(axe.level) <= axe.brokenBlocks) {
                if (axe.level != AxeLevel.blocksToNextLevel.size) axe.level++
            }

        }else user.increaseBlocks()
        user.drops.clear()

    }

    @EventHandler
    fun onBlockIgnite(e: BlockIgniteEvent) {
        if (e.cause == BlockIgniteEvent.IgniteCause.LIGHTNING && e.block.isInsoBlock()) e.isCancelled = true
    }

}






