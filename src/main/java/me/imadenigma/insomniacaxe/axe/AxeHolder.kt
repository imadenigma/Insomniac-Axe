package me.imadenigma.insomniacaxe.axe

import com.google.common.base.Suppliers
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import me.imadenigma.insomniacaxe.Configuration
import me.imadenigma.insomniacaxe.colorize
import me.imadenigma.insomniacaxe.getDisplayName
import me.imadenigma.insomniacaxe.setDisplayName
import me.lucko.helper.Services
import me.lucko.helper.config.ConfigurationNode
import me.lucko.helper.gson.GsonSerializable
import me.lucko.helper.gson.JsonBuilder
import me.lucko.helper.utils.Log
import me.mattstudios.mfgui.gui.components.ItemNBT
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import java.util.*

class AxeHolder(val offlinePlayer: OfflinePlayer, val axes: MutableList<Axe>) : GsonSerializable {
    private val configuration : ConfigurationNode
    val drops = mutableSetOf<ItemStack>()
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
        val name = this.configuration.getNode("axe-name").string!!
        return this.offlinePlayer.player?.inventory?.itemInMainHand?.getDisplayName().equals(name) && this.axes.isNotEmpty()
    }

    fun countBreakingBlocks() {
        if (!offlinePlayer.isOnline) return
        if (!this.isHoldingInsoAxe()) return
        val axe = getAxeByUUID(UUID.fromString(ItemNBT.getNBTTag(offlinePlayer.player?.inventory?.itemInMainHand, "uuid"))) ?: return
        axe.brokenBlocks++
    }

    fun getAxeInMainHand() : Axe? {
        if (!offlinePlayer.isOnline) return null
        if (!this.isHoldingInsoAxe()) return null
        val axe = getAxeByUUID(UUID.fromString(ItemNBT.getNBTTag(offlinePlayer.player?.inventory?.itemInMainHand, "uuid")))
        return axe
    }

    override fun serialize(): JsonElement {
        val jsonArray = JsonArray()
        axes.forEach {
            jsonArray.add(it.serialize())
        }
        return JsonBuilder.`object`()
            .add("uuid", offlinePlayer.uniqueId.toString())
            .add("axes", jsonArray)
            .build()
    }

    fun giveAxe(axe: Axe) : Boolean {
        if (!this.offlinePlayer.isOnline) return false
        val player = this.offlinePlayer.player
        val configuration = Services.load(Configuration::class.java)
        var ax = ItemStack(axe.material)
        ax.setDisplayName(configuration.configNode.getNode("axe-name").getString("null"))
        ax = ItemNBT.setNBTTag(ax, "uuid", axe.uuid.toString())
        if (player?.inventory?.firstEmpty() == -1) {
            player.world.dropItem(player.location,ax )
        }else player?.inventory?.addItem(ax)
        this.axes.add(axe)
        println("donated")
        return true
    }

    fun getAxeByUUID(uuid: UUID) : Axe? {
        return this.axes.stream().filter { it.uuid == uuid }.findAny().orElse(null)
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

            val optional =  users.stream().filter { it.offlinePlayer.uniqueId == offlinePlayer.uniqueId }.findAny()
            if (optional.isPresent) {
                return optional.get()
            }
            return AxeHolder(offlinePlayer, mutableListOf())
        }

        fun deserialize(element: JsonElement): AxeHolder {
            val objet = element.asJsonObject
            val uuid = UUID.fromString(objet.get("uuid").asString)
            val axeList = mutableListOf<Axe>()
            objet.get("axes").asJsonArray.map {
                axeList.add(Axe.deserialize(it))
            }
            return AxeHolder(Bukkit.getOfflinePlayer(uuid),axeList)
        }
    }
}
