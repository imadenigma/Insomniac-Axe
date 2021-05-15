package me.imadenigma.insomniacaxe.axe

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import me.imadenigma.insomniacaxe.enchant.Enchant
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.lucko.helper.Services
import me.lucko.helper.gson.GsonSerializable
import me.lucko.helper.gson.JsonBuilder
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.lang.IllegalArgumentException
import java.util.*

class Axe(var level: Int, val material: Material, val enchants: MutableList<Enchant>, val uuid: UUID, var brokenBlocks: Long) : GsonSerializable {

    init {
        axes[this.uuid.toString()] = this
    }

    var brokenPumpkins: Long = 0L
    override fun serialize(): JsonElement {
        val jsonArray = JsonArray()
        for (name in enchants.stream().map { it.name })
            jsonArray.add(ChatColor.stripColor(name))
        return JsonBuilder.`object`()
            .add("level",level)
            .add("material",material.name)
            .add("uuid", this.uuid.toString())
            .add("blocks",this.brokenBlocks)
            .add("pumpkin",this.brokenPumpkins)
            .add("enchants", jsonArray)
            .build()
    }

    companion object {
        val axes = mutableMapOf<String, Axe>()
        fun deserialize(element: JsonElement) : Axe {
            val objet = element.asJsonObject
            val level = objet.get("level").asInt
            val material = Material.matchMaterial(objet.get("material").asString)
            val uuid = UUID.fromString(objet.get("uuid").asString)
            val array = objet.get("enchants").asJsonArray.map { it.asString }.toList()
            val brokenBlocks = objet.get("blocks").asLong
            println(array)
            val enchants = mutableListOf<Enchant>()
            val names = Services.load(EnchantsFactory::class.java).enchants.values
            for (i in array) {
                println(i)
                val ench = names.stream().filter { ChatColor.stripColor(it.name).equals(i,true) }.findAny().get()
                enchants.add(ench)
            }
            return Axe(level,material!!,enchants, uuid, brokenBlocks)
        }

        fun isAxe(item: ItemStack): Boolean {
            @Suppress("SENSELESS_COMPARISON")
            if (item == null) return false
            if (item.type.name.contains("axe",true)) {
                try {
                    UUID.fromString(ItemNBT.getNBTTag(item, "uuid"))
                }catch (e: IllegalArgumentException) { return false }
                return true
            }
            return false
        }

    }

}