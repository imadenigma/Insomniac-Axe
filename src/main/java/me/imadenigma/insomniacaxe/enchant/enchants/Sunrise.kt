package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.Manager
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.holder.AxeHolder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.jetbrains.annotations.NotNull

@EnchPriority(Priority.HIGHEST)
class Sunrise(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material,override val isGlowing: Boolean,
    override val price: Double, override val lore: List<String>
) : Enchant(name, isEnabled, slot, material, isGlowing, price, lore)  {

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        if (!e.player.world.isDayTime) return
        increaseCoins(e.getUser()!!)
        increaseMoney(e.player)
    }

    private fun increaseMoney(player: Player) {
        val economy = InsomniacAxe.singleton.economy!!
        economy.depositPlayer(player,0.01 * economy.getBalance(player))
    }

    private fun increaseCoins(axeHolder: AxeHolder) {
        axeHolder.give((0.01 * Manager.coins).toLong())
    }

}