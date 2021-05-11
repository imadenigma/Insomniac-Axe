package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.priority.Priority
import me.imadenigma.insomniacaxe.getUser
import me.imadenigma.insomniacaxe.isInsoBlock
import me.lucko.helper.Schedulers
import net.brcdev.shopgui.ShopGuiPlusApi
import org.apache.commons.lang.math.RandomUtils
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import java.util.concurrent.TimeUnit

@EnchPriority(Priority.HIGHEST)
class Frenzy(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, override val isGlowing: Boolean, override val price: Double,
    override val lore: List<String>, private val percentage: Double
) :
    Enchant(name, isEnabled, slot, material, isGlowing, price, lore)  {

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        if (!e.block.isInsoBlock()) return
        if (RandomUtils.nextInt(100) > percentage) return
        AxeLevel.isFrenzy = true
        val shopItem = ShopGuiPlusApi.getItemStackShopItem(e.player.inventory.itemInMainHand)
        val price = shopItem.sellPrice
        shopItem.sellPrice *= 2
        e.player.sendMessage("&cYou are lucky now".colorize())
        val booster = when (e.getUser()?.getAxeInMainHand()!!.level) {
            1 -> 15
            else -> 30
        }

        Schedulers.async().runLater(
            {
                shopItem.sellPrice = price
                AxeLevel.isFrenzy = false
            }
            ,booster.toLong(),TimeUnit.SECONDS)

    }
}