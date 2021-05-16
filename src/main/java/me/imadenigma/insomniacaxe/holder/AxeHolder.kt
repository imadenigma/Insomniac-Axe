package me.imadenigma.insomniacaxe.holder

import com.google.gson.JsonElement
import me.imadenigma.insomniacaxe.*
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.lucko.helper.Services
import me.lucko.helper.config.ConfigurationNode
import me.lucko.helper.gson.GsonSerializable
import me.lucko.helper.gson.JsonBuilder
import me.lucko.helper.utils.Log
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.NoSuchElementException

class AxeHolder(val offlinePlayer: OfflinePlayer, var balance: Long = 0L, var brokenBlocks: Long = 0L, var brokenPumps: Long = 0L) : GsonSerializable, Currency {
    private val configuration : ConfigurationNode
    val drops = mutableSetOf<ItemStack>()
    var zeus = false
    init {
        users.add(this)
        this.configuration = Services.load(Configuration::class.java).configNode
        Log.info("Handling join of ${offlinePlayer.name}, his/her uuid: ${offlinePlayer.uniqueId}")
    }

    fun isValid() : Boolean {
        if (!offlinePlayer.isOnline) return false
        val name = this.configuration.getNode("axe-name").string!!
        return Arrays.stream(this.offlinePlayer.player?.inventory?.contents).filter { Objects.nonNull(it) }.anyMatch { it.getDisplayName() == name.colorize() }
    }

    fun isHoldingInsoAxe() : Boolean {
        if (!offlinePlayer.isOnline) return false
        if (!this.isHoldingAxe()) return false
        return Axe.axes.containsKey(ItemNBT.getNBTTag(offlinePlayer.player?.inventory?.itemInMainHand, "uuid"))
    }

    fun countBreakingBlocks(isInso: Boolean = false) {
        if (!offlinePlayer.isOnline) return
        if (!this.isHoldingInsoAxe()) return
        if (isInso) this.brokenPumps++
        increaseBlocks()
    }

    fun getAxeInMainHand(): Axe? {
        return getAxeByUUID(ItemNBT.getNBTTag(offlinePlayer.player?.inventory?.itemInMainHand ?: ItemStack(Material.AIR), "uuid"))
    }

    override fun serialize(): JsonElement {
        return JsonBuilder.`object`()
            .add("uuid", offlinePlayer.uniqueId.toString())
            .add("balance",this.balance)
            .add("blocks", this.brokenBlocks)
            .add("pumpkins", this.brokenPumps)
            .build()
    }

    fun giveAxe(axe: Axe) : Boolean {
        val player = this.offlinePlayer.player ?: let {
            println("player is null , line 71, AxeHolder.kt")
            return false
        }
        val configuration = Services.load(Configuration::class.java)
        var ax = ItemStack(axe.material)
        ax.setDisplayName(configuration.configNode.getNode("axe-name").getString("null"))
        val meta = ax.itemMeta
        val lore = ax.itemMeta.lore ?: mutableListOf()
        lore.addAll(
            axeLore(axe.level, axe.brokenBlocks, AxeLevel.blocksToNextLevel[axe.level]!!.toLong())
        )
        meta.lore = lore

        meta.addEnchant(Enchantment.DURABILITY,10,true)
        meta.addEnchant(Enchantment.DIG_SPEED,10,true)
        meta.addEnchant(Enchantment.SWEEPING_EDGE,5,true)
        ax.itemMeta = meta
        ax = ItemNBT.setNBTTag(ax, "uuid", axe.uuid.toString())
        if (player.inventory.firstEmpty() == -1) {
            player.world.dropItem(player.location,ax )
        }else player.inventory.addItem(ax)
        Axe.axes[axe.uuid.toString()] = axe
        println("donated")
        return true
    }

    fun getAxeByUUID(uuid: String) : Axe? {
        return Axe.axes[uuid]
    }

    private fun isHoldingAxe() : Boolean {
        if (!this.offlinePlayer.isOnline) return false
        val player = this.offlinePlayer.player!!
        val item = player.inventory.itemInMainHand
        if (item.type.name.contains("_AXE")) return true
        return false
    }

    companion object {
        val users = mutableSetOf<AxeHolder>()

        fun getHolder(offlinePlayer: OfflinePlayer): AxeHolder {

            val optional =  users.stream().filter {
                val bool = it.offlinePlayer.uniqueId == offlinePlayer.uniqueId
                println("optional$bool")
                return@filter bool
            }.findAny()
            if (optional.isPresent) {
                return optional.get()
            }
            return AxeHolder(offlinePlayer)
        }

        fun deserialize(element: JsonElement) {
            val objet = element.asJsonObject
            val uuid = UUID.fromString(objet.get("uuid").asString)
            val balance = objet.get("balance").asLong
            val brokenBlocks = objet.get("blocks").asLong
            val brokenPumps = objet.get("pumpkins").asLong
            val axeHolder = AxeHolder(Bukkit.getOfflinePlayer(uuid), balance = balance, brokenBlocks = brokenBlocks, brokenPumps = brokenPumps)
            if (users.any { it.offlinePlayer.uniqueId == uuid }) {
                val holder = users.stream().filter { it.offlinePlayer.uniqueId.equals(uuid) }.findAny().get()
                if (axeHolder.balance > holder.balance || axeHolder.brokenBlocks > holder.brokenBlocks || axeHolder.brokenPumps > holder.brokenPumps) {
                    users.remove(axeHolder)
                    users.remove(holder)
                    users.add(axeHolder)
                    return
                }
            }
        }
    }

    override fun give(amount: Long) {
        this.balance += amount
    }

    override fun take(amount: Long) {
        this.balance -= balance
    }

    override fun set(amount: Long) {
        this.balance = amount
    }

    fun increaseBlocks() {
        this.brokenBlocks++
        val item = this.offlinePlayer.player!!.inventory.itemInMainHand
        val meta = item.itemMeta
        val lore = meta.lore ?: mutableListOf()
        if (lore.isNotEmpty()) {
            if (lore[lore.size - 1].contains("--------")) {
                for (i in 0 until 4) {
                    try {
                        lore.removeLast()
                    } catch (e: NoSuchElementException) {}
                }
            }
        }
        val axe = getAxeInMainHand()!!
        lore.addAll(
            axeLore(axe.level, axe.brokenBlocks, AxeLevel.blocksToNextLevel[axe.level]!!.toLong())
        )
        meta.lore = lore
        item.itemMeta = meta
        println("updating lore")
    }


}
