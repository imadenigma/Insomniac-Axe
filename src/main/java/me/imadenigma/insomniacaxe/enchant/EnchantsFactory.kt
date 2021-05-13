package me.imadenigma.insomniacaxe.enchant

import com.google.common.reflect.TypeToken
import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.enchant.enchants.*
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.lucko.helper.Services
import me.lucko.helper.config.ConfigurationNode
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import java.util.*

@Suppress("JoinDeclarationAndAssignment")
class EnchantsFactory {

    private var config: ConfigurationNode
    val enchants: MutableMap<String, Enchant>
    private val enchantsOrdered: LinkedList<Enchant>

    init {
        this.config = Services.load(Configuration::class.java).configNode.getNode("enchants")
        var prefix = ""
        // speed
        this.config = this.config.getNode("speed")
        var isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        var name = (prefix +  this.config.getNode("name").getString("")).colorize()
        var slot = this.config.getNode("slot").getInt(1)
        var material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        var isGlowing = this.config.getNode("glow").getBoolean(false)
        var price = this.config.getNode("price").getDouble(1.0)
        var lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val speed = Speed(name, isEnabled, slot, material, isGlowing, price, lore)
        // haste
        this.config = this.config.parent?.getNode("haste")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val haste = Haste(name, isEnabled, slot, material, isGlowing, price, lore)

        // autosell
        this.config = this.config.parent?.getNode("autosell")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val autosell = Autosell(name, isEnabled, slot, material, isGlowing, price, lore)
        //Zeus
        this.config = this.config.parent?.getNode("zeus")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        var percentage = this.config.getNode("percentage").getFloat(50F)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val zeus = Zeus(name, isEnabled, slot, material, percentage, isGlowing, price, lore)

        //Destroyer
        this.config = this.config.parent?.getNode("destroyer")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java))
        val destroyer = Destroyer(name, isEnabled, slot, material, isGlowing, price, lore)

        //Double Drops
        this.config = this.config.parent?.getNode("doubledrops")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val dDrops = DoubleDrops(name, isEnabled, slot, material, isGlowing, price, lore)

        //Implants
        this.config = this.config.parent?.getNode("implants")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val implants = Implants(name, isEnabled, slot, material, isGlowing, price, lore)
        // Frenzy
        this.config = this.config.parent?.getNode("frenzy")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        percentage = this.config.getNode("percentage").getFloat(100F)
        val frenzy = Frenzy(name, isEnabled, slot, material, isGlowing, price, lore, percentage.toDouble())

        // Sunrise
        this.config = this.config.parent?.getNode("sunrise")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val sunrise = Sunrise(name, isEnabled, slot, material, isGlowing, price, lore)

        // Eclipse
        this.config = this.config.parent?.getNode("eclipse")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val eclipse = Eclipse(name, isEnabled, slot, material, isGlowing, price, lore)

        // dCoins
        this.config = this.config.parent?.getNode("double-coins")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        val doubleCoins = DoubleCoins(name, isEnabled, slot, material, isGlowing, price, lore)


        // rampage
        this.config = this.config.parent?.getNode("rampage")!!
        isEnabled = this.config.getNode("enabled").getBoolean(true)
        prefix = if (!isEnabled) "&m"
        else ""
        name = (prefix +  this.config.getNode("name").getString("")).colorize()
        slot = this.config.getNode("slot").getInt(1)
        material = this.config.getNode("item").getValue(TypeToken.of(Material::class.java)) ?: Material.BEACON
        isGlowing = this.config.getNode("glow").getBoolean(false)
        price = this.config.getNode("price").getDouble(1.0)
        lore = this.config.getNode("lore").getList(TypeToken.of(String::class.java)).map { it.colorize() }.toList()
        percentage = this.config.getNode("percentage").getFloat(100F)

        val rampage = Rampage(name, isEnabled, slot, material, isGlowing, price, lore, percentage.toDouble())



        enchants = mutableMapOf(
            "speed" to speed,
            "haste" to haste,
            "zeus" to zeus,
            "autosell" to autosell,
            "destroyer" to destroyer,
            "doubledrops" to dDrops,
            "implants" to implants,
            "frenzy" to frenzy,
            "sunrise" to sunrise,
            "eclipse" to eclipse,
            "doublecoins" to doubleCoins,
            "rampage" to rampage
        )
        enchantsOrdered = LinkedList<Enchant>(
            enchants.values.filter { it.isEnabled }
                .sortedBy { it.javaClass.getAnnotation(EnchPriority::class.java).priority }
        )
        Bukkit.getOnlinePlayers().forEach {
            val item = it.inventory.itemInMainHand ?: return@forEach
            if (!Axe.isAxe(item)) return@forEach
            val axe = AxeHolder.getHolder(it).getAxeInMainHand() ?: return@forEach
            val lore = item?.itemMeta?.lore ?: return@forEach
            axe.enchants.filter { ench -> !ench.isEnabled }.forEach { ench ->
                lore.stream().filter { s -> ChatColor.stripColor(s) == ench.name }.forEach { str ->
                    lore.remove(str)
                    lore.add(1,"&m".colorize() + str)
                }
            }
            item.itemMeta.lore = lore

        }

        Services.provide(EnchantsFactory::class.java, this)
    }
}