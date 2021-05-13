package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.Manager
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.isInsoBlock
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent

@EnchPriority(Priority.HIGH)
class Eclipse(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore) {
    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        if (e.block.world.isDayTime) return
        if (!e.block.isInsoBlock()) return
        e.getUser()!!.give((0.015 * Manager.coins).toLong())
    }
}