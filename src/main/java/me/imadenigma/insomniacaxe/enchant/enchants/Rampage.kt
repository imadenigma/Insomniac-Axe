package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.Manager
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.isInsoBlock
import me.lucko.helper.Schedulers
import org.apache.commons.lang.math.RandomUtils
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import java.util.concurrent.TimeUnit

@EnchPriority(Priority.HIGHEST)
class Rampage(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>, private val percentage: Double
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore)   {
    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        if (!e.block.isInsoBlock()) return
        if (RandomUtils.nextInt(100) > percentage) return
        val coins = Manager.coins
        Manager.coins *= 2
        var level = e.getUser()!!.getAxeInMainHand()!!.level
        if (level != 1) level = 2
        Schedulers.async().runLater({
                                    Manager.coins /= coins
        }, (15 * level).toLong(), TimeUnit.SECONDS)
        e.player.sendMessage("you are lucky")
    }
}