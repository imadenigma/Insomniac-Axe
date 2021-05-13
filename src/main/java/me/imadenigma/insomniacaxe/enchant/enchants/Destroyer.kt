package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.isInsoBlock
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent

@EnchPriority(Priority.LOWEST)
class Destroyer(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore) {

    private val faces =
        listOf(BlockFace.DOWN, BlockFace.NORTH, BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH)

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        if (!e.block.isInsoBlock()) return
        destroyBlocks(e.block, e.getUser()!!)
    }

    private fun destroyBlocks(block: Block, user: AxeHolder) {
        for (face in faces) {
            val target = block.getRelative(face)
            if (target.isInsoBlock()) {
                user.drops.forEach {
                    it.amount += target.drops.first().amount
                }
                user.countBreakingBlocks()
                target.drops.clear()
                target.type = Material.AIR
                target.state.update()
                return
            }
        }
    }
}
/*
user.drops.addAll(block.drops)
        var target = block
        for (face in faces) {
            i@ for (i in 0..100) {
                target = block.getRelative(face)
                if (!target.isInsoBlock()) break@i
                user.drops.addAll(target.drops)
                target.drops.clear()
                target.breakNaturally()
            }
        }
 */