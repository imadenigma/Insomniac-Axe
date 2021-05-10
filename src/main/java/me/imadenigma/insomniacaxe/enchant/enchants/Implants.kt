package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.isInsoBlock
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent

@EnchPriority(Priority.HIGH)
class Implants(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore)   {
    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        if (!e.block.isInsoBlock()) return
        if (e.player.foodLevel != 20)
            e.player.foodLevel += 1
        if (e.player.health != 20.0) {
            try {
                e.player.health += 1
            } catch (err: IllegalArgumentException) {
                e.player.health = e.player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
            }
        }
    }
}