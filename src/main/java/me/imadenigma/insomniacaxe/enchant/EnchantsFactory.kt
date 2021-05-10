package me.imadenigma.insomniacaxe.enchant

import com.google.common.reflect.TypeToken
import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.enchant.enchants.*
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.lucko.helper.Services
import me.lucko.helper.config.ConfigurationNode
import org.bukkit.Material
import java.util.*

@Suppress("JoinDeclarationAndAssignment")
class EnchantsFactory {

    private var config: ConfigurationNode
    val enchants: MutableMap<String, Enchant>
    val enchantsOrdered: LinkedList<Enchant>
    init {
        this.config = Services.load(Configuration::class.java).configNode.getNode("enchants")
        // speed
        this.config = this.config.getNode("speed")
        var name = this.config.getNode("name").getString("").colorize()
        var isEnabled = this.config.getNode("enabled").getBoolean(true)
        var slot = this.config.getNode("slot").getInt(1)
        var material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        var isGlowing = this.config.getNode("glow").getBoolean(false)
        var price = this.config.getNode("price").getDouble(1.0)
        var lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val speed = Speed(name, isEnabled, slot, material, isGlowing, price, lore)
        // haste
        this.config = this.config.parent?.getNode("haste")!!
        name = this.config.getNode("name").getString("").colorize()
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val haste = Haste(name, isEnabled, slot, material, isGlowing, price, lore)

        // autosell
        this.config = this.config.parent?.getNode("autosell")!!
        name = this.config.getNode("name").getString("").colorize()
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val autosell = Autosell(name, isEnabled, slot, material, isGlowing, price, lore)
        //Zeus
        this.config = this.config.parent?.getNode("zeus")!!
        name = this.config.getNode("name").getString("").colorize()
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        slot = this.config.getNode("slot").getInt(1)
        val percentage = this.config.getNode("percentage").getFloat(50F)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val zeus = Zeus(name, isEnabled, slot, material, percentage, isGlowing, price, lore)

        //Destroyer
        this.config = this.config.parent?.getNode("destroyer")!!
        name = this.config.getNode("name").getString("").colorize()
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val destroyer = Destroyer(name, isEnabled, slot, material, isGlowing, price, lore)

        //Double Drops
        this.config = this.config.parent?.getNode("doubledrops")!!
        name = this.config.getNode("name").getString("").colorize()
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val dDrops = DoubleDrops(name, isEnabled, slot, material, isGlowing, price, lore)

        //Implants
        this.config = this.config.parent?.getNode("implants")!!
        name = this.config.getNode("name").getString("").colorize()
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val implants = Implants(name, isEnabled, slot, material, isGlowing, price, lore)


        enchants = mutableMapOf(
            "speed" to speed,
            "haste" to haste,
            "zeus" to zeus,
            "autosell" to autosell,
            "destroyer" to destroyer,
            "doubledrops" to dDrops,
            "implants" to implants
        )
        enchantsOrdered = LinkedList<Enchant>(
            enchants.values.sortedBy { it.javaClass.getAnnotation(EnchPriority::class.java).priority }
        )

        Services.provide(EnchantsFactory::class.java, this)
    }
}