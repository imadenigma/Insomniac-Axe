package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.isInsoBlock
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
        if (e.block.isInsoBlock())
            sellBlock(e.getUser()!!)
    }
    companion object {
        var prices: Map<Material, Double>? = null

        fun sellBlock(user: AxeHolder) {
            val drops = user.drops
            for (drop in drops) {
                for (i in 0 until drop.amount) {
                    val bool = drop.type == Material.PUMPKIN
                    user.getAxeInMainHand()?.addBlock(bool)
                    user.countBreakingBlocks(bool)
                }
                val uniquePrice =  getSellPrice(drop.type)
                val price = uniquePrice * (1 + AxeLevel.boosters[user.getAxeInMainHand()!!.level]!!) * drop.amount
                InsomniacAxe.singleton.economy!!.depositPlayer(user.offlinePlayer, price)
            }
            user.drops.clear()
        }

        private fun getSellPrice(material: Material): Double {
            return (prices?.get(material) ?: let {
                ShopGuiPlusApi.getItemStackShopItem(ItemStack(material)).sellPrice
            })
        }
    }
}