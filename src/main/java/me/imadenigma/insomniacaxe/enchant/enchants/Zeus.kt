package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.isInsoBlock
import org.apache.commons.lang.math.RandomUtils
import org.bukkit.Material
import org.bukkit.entity.LightningStrike
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent

@EnchPriority(Priority.LOWEST)
class Zeus(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, private val percentage: Float, override val isGlowing: Boolean,
    override val price: Double, override val lore: List<String>
) : Enchant(name, isEnabled, slot, material, isGlowing, price, lore) {

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        val random = RandomUtils.nextInt(100)
        if (random > percentage) return
        val user = e.getUser()!!
        var blocksSize = 0
        val axe = user.getAxeInMainHand()!!
        for (i in -50..50) {
            for (j in -50..50) {
                val block = e.block.world.getHighestBlockAt(e.block.location.add(i.toDouble(),0.0,j.toDouble()))
                if (block.isInsoBlock()) {
                    e.player.world.spawn(block.location, LightningStrike::class.java)
                    user.drops.forEach { it.amount += 1 }
                    block.drops.clear()
                    block.breakNaturally()
                    blocksSize++
                }
            }
        }
        axe.brokenBlocks += blocksSize
    }

}
