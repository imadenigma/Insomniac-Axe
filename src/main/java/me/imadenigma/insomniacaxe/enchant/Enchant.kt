package me.imadenigma.insomniacaxe.enchant

import org.bukkit.Material
import org.bukkit.event.Event


abstract class Enchant(open val name: String, open val isEnabled: Boolean, open val slot: Int, open val material: Material){

    abstract fun function(e: Event)
}