package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import net.brcdev.shopgui.ShopGuiPlusApi
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

@EnchPriority(Priority.HIGHEST)
class Autosell(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore) {

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        sellBlock(e.getUser()!!)
    }
    companion object {
        private var prices: Map<Material, Double>? = null
        fun sellBlock(user: AxeHolder) {
            val drops = user.drops
            if (prices == null) {
                prices = mapOf(
                    Material.PUMPKIN to ShopGuiPlusApi.getItemStackPriceSell(ItemStack(Material.PUMPKIN)),
                    Material.MELON to ShopGuiPlusApi.getItemStackPriceSell(ItemStack(Material.MELON))
                )
            }
            for (drop in drops) {
                val price = prices!![drop.type]!! * (1 + AxeLevel.boosters[user.getAxeInMainHand()!!.level]!!) * drop.amount
                InsomniacAxe.singleton.economy!!.depositPlayer(user.offlinePlayer,price.toDouble())
            }
            user.drops.clear()
        }
    }
}