
package me.imadenigma.insomniacaxe

import me.lucko.helper.Helper
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
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
