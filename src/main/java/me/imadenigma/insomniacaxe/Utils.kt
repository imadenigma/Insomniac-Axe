
package me.imadenigma.insomniacaxe

import com.google.common.collect.Lists
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.lucko.helper.Helper
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.NotNull
import java.io.File

fun File.makeReady() {
    @Suppress("SENSELESS_COMPARISON")
    if (this == null) return
    if (this.exists()) return
    Helper.hostPlugin().getBundledFile(this.name)

}

fun String.colorize() : String {
    return ChatColor.translateAlternateColorCodes('&',this)
}

fun ItemStack.getDisplayName() : String {
    val meta = this.itemMeta ?: Bukkit.getItemFactory().getItemMeta(this.type)
    return meta?.displayName ?: this.type.name
}

fun ItemStack.setDisplayName(name: String) {
    val meta = this.itemMeta ?: Bukkit.getItemFactory().getItemMeta(this.type)
    meta?.setDisplayName(name.colorize())
    this.itemMeta = meta
}

fun Block.isInsoBlock() : Boolean {
    if (this.type == Material.PUMPKIN || this.type == Material.MELON) return true
    return false
}

fun MutableList<Enchant>.ordered(): List<Enchant> {
    return this.filter { it.isEnabled }.sortedBy { it::class.java.getAnnotation(EnchPriority::class.java).priority }
}

fun Block.addDrop(amount: Int,block: Block) {
    val optional = this.drops.stream().filter { it.type == block.type }.findAny()
    this.drops.clear()
    if (optional.isPresent)
        this.drops.add(ItemStack(optional.get().type,optional.get().amount + block.drops.size))
    else block.drops.addAll(block.drops)
}

fun ItemStack.addEnchant(enchant: Enchant) {
    val meta = this.itemMeta ?: Bukkit.getItemFactory().getItemMeta(this.type)
    val lore = meta.lore ?: mutableListOf()
    lore.add(0,enchant.name)
    meta.lore = lore
    this.itemMeta = meta
    ("lore changed!")
}

fun Player.giveItem(item: ItemStack) {
    if (this.inventory.firstEmpty() == -1) {
        this.world.dropItem(this.location,item )
    }else this.inventory.addItem(item)
}

fun Event.getUser() : AxeHolder? {
    if (this is BlockBreakEvent) return AxeHolder.getHolder(this.player)
    if (this is PlayerItemHeldEvent) return AxeHolder.getHolder(this.player)
    return null
}

fun axeLore(level: Int, blocks: Long, max: Long): List<String> {
    return Lists.asList(
        ChatColor.translateAlternateColorCodes('&', "&8&m----------------------------"), arrayOf(
            ChatColor.translateAlternateColorCodes('&', "&7Level: &b$level"),
            ChatColor.translateAlternateColorCodes('&', "&7Blocks: &b$blocks&7/&b$max"),
            ChatColor.translateAlternateColorCodes('&', "&8&m----------------------------")
        )
    )
}