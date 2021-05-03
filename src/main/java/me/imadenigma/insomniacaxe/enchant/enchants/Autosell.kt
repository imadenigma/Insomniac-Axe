package me.imadenigma.insomniacaxe.enchant.enchants

import me.imadenigma.insomniacaxe.InsomniacAxe
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.mattstudios.mfgui.gui.components.ItemNBT
import net.brcdev.shopgui.ShopGuiPlusApi
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class Autosell(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material
) :
    Enchant(name, isEnabled, slot, material) {

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        val user = AxeHolder.getHolder(e.player)
        val axe = user.getAxeByUUID(UUID.fromString(ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid")))
        if (e.block.type == Material.PUMPKIN || e.block.type == Material.MELON) {
            InsomniacAxe.singleton.economy!!.depositPlayer(e.player, ShopGuiPlusApi.getItemStackPriceSell(
                ItemStack(e.block.type)
            ) * AxeLevel.getBoosterVal(axe?.level!!))
            e.block.drops.clear()
        }
    }
}