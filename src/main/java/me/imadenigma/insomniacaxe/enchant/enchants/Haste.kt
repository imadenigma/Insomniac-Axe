package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getDisplayName
import me.imadenigma.insomniacaxe.getUser
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@EnchPriority(Priority.HIGH)
class Haste(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore)  {

    override fun function(e: Event) {
        if (e !is PlayerItemHeldEvent) return

        if (e.getUser()!!.isHoldingInsoAxe()) {
            if (e.getUser()!!.getAxeInMainHand()!!.enchants.contains(this)) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, Int.MAX_VALUE, 2))
                hasteUsers.add(e.player)
            }else
                e.player.removePotionEffect(PotionEffectType.FAST_DIGGING)
        } else
            if (hasteUsers.contains(e.player)) {
                e.player.removePotionEffect(PotionEffectType.FAST_DIGGING)
            }
    }

    fun firstFun(player: Player) {
        if (player.inventory.itemInMainHand.getDisplayName().equals("Insomniac Axe", true)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, Int.MAX_VALUE, 2))
            hasteUsers.add(player)
        } else
            if (hasteUsers.contains(player)) {
                player.removePotionEffect(PotionEffectType.FAST_DIGGING)
            }
    }

    companion object {
        val hasteUsers = mutableListOf<Player>()
    }

}