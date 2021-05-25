package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.giveItem
import me.imadenigma.insomniacaxe.isInsoBlock
import me.lucko.helper.Schedulers
import org.apache.commons.lang.math.RandomUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import java.util.concurrent.TimeUnit


@EnchPriority(Priority.MEDIUM)
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
        val axe = user.getAxeInMainHand()!!
        println("here")
        getNearbyBlocks(e.block.location, 30)
            .filter { it.isInsoBlock() }
            .filter { it.type == e.block.type }
            .forEach {
                user.drops.forEach { drop -> drop.amount += 1 }
                if (!user.isLowConf)
                    e.player.world.strikeLightningEffect(it.location)
                it.type = Material.AIR
                it.state.update()
            }
        user.drops.forEach { drop -> drop.amount -= 1 }
        user.zeus = true
        if (!(axe.enchants.any { it is Autosell })) {
            if (axe.enchants.any { it is DoubleDrops }) {
                user.drops.forEach {
                    it.amount *= 2
                }
            }
            user.drops.forEach {
                e.player.giveItem(it)
                for (i in 0 until it.amount) {
                    axe.addBlock(true)
                    user.countBreakingBlocks(true)
                }
            }
            user.drops.clear()
        } else {
            Autosell.sellBlock(user)
        }

    }

    private fun getNearbyBlocks(location: Location, radius: Int): Set<Block> {
        val blocks = mutableSetOf<Block>()
        for (x in location.blockX - radius..location.blockX + radius) {
            for (y in location.blockY..location.blockY + radius) {
                for (z in location.blockZ - radius..location.blockZ + radius) {
                    blocks.add(location.world.getBlockAt(x, y, z))
                }
            }
        }
        return blocks.filter { it.isInsoBlock() }.toSet()
    }
}
