package me.imadenigma.insomniacaxe.enchant

import org.bukkit.Material
import org.bukkit.event.Event


abstract class Enchant(
    open val name: String,
    open val isEnabled: Boolean,
    open val slot: Int,
    open val material: Material,
    open val isGlowing: Boolean,
    open val price: Double,
    open val lore: List<String>
) {

    abstract fun function(e: Event)
}