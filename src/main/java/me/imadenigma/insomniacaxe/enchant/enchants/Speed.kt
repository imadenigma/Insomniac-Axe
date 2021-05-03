package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.getDisplayName
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Speed(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore) {

    override fun function(e: Event) {
        if (e !is PlayerItemHeldEvent) return
        if (e.player.inventory.itemInMainHand.getDisplayName().equals("Insomniac Axe", true)) {
            e.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 2))
            speedUsers.add(e.player)
        } else
            if (speedUsers.contains(e.player)) {
                e.player.removePotionEffect(PotionEffectType.SPEED)
            }
    }

    companion object {
        val speedUsers = mutableListOf<Player>()
    }
}