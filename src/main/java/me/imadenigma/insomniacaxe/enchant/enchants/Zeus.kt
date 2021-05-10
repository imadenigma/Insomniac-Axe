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
        val chunk1 = e.player.location.chunk
        val chunk2 = e.player.location.add(17.0, 0.0, 17.0).chunk
        var blocksSize = 0
        for (i in 0..15) {
            for (j in 0..15) {
                val block = chunk1.getBlock(i, e.block.y , j)
                val block2 = chunk2.getBlock(i, e.block.y, j)
                if (block.isInsoBlock()) {
                    val lightning = e.player.world.spawn(block.location, LightningStrike::class.java)
                    user.drops.addAll(block.drops)
                    block.drops.clear()
                    block.breakNaturally()
                    blocksSize++

                }
                if (block2.isInsoBlock()) {
                    val lightning = e.player.world.spawn(block.location, LightningStrike::class.java)
                    user.drops.addAll(block2.drops)
                    block2.drops.clear()
                    block2.breakNaturally()
                    blocksSize++
                }
            }
        }


    }

}