package me.imadenigma.insomniacaxe.enchant.enchants

import com.google.common.net.PercentEscaper
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.lucko.helper.Services
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.apache.commons.lang.math.RandomUtils
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.entity.LightningStrike
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import java.util.*

class Zeus(
    override val name: String, override val isEnabled: Boolean, override val slot: Int,
    override val material: Material, private val percentage: Float, override val isGlowing: Boolean,
    override val price: Double, override val lore: List<String>
) : Enchant(name, isEnabled, slot, material, isGlowing, price, lore) {

    override fun function(e: Event) {
        if (e !is BlockBreakEvent) return
        val random = RandomUtils.nextInt(100)
        if (random > percentage) return
        val chunk1 = e.player.location.chunk
        val chunk2 = e.player.location.add(16.0, 0.0, 0.0).chunk
        var blocksSize = 0
        for (i in 0..15) {
            for (j in 0..15) {
                val block = chunk1.getBlock(i, e.player.location.y.toInt(), j)
                val block2 = chunk2.getBlock(i, e.player.location.y.toInt(), j)
                if (block.type == Material.PUMPKIN || block.type == Material.MELON || block2.type == Material.PUMPKIN || block2.type == Material.MELON
                ) {
                    val lightning = e.player.world.spawn(block.location, LightningStrike::class.java)
                    block.drops.clear()
                    block.breakNaturally()
                    blocksSize++
                }
            }
        }
        val axe = AxeHolder.getHolder(e.player)
            .getAxeByUUID(UUID.fromString(ItemNBT.getNBTTag(e.player.inventory.itemInMainHand, "uuid"))) ?: return
        axe.brokenBlocks += blocksSize
        val factory = Services.load(EnchantsFactory::class.java)
        if (axe.enchants.stream().anyMatch { it.name.equals(factory.enchants["autosell"]) }) {
            for (i in 0..blocksSize) {
                factory.enchants["autosell"]?.function(e)
            }
        }
    }

}