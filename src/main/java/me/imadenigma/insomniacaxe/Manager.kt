package me.imadenigma.insomniacaxe

import com.google.gson.JsonArray
import me.imadenigma.insomniacaxe.axe.Axe
import me.imadenigma.insomniacaxe.holder.AxeHolder
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.imadenigma.insomniacaxe.enchant.enchants.DoubleCoins
import me.lucko.helper.Services
import me.lucko.helper.gson.GsonProvider
import me.lucko.helper.utils.Log
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.system.measureTimeMillis

class Manager {

    companion object {
        var coins: Long = 0
    }
    init {
        coins = Services.load(Configuration::class.java).configNode.getNode("coins-to-add").getLong(100)
    }

    fun loadUsers() {
        Log.info("&aLoading Users...".colorize())
        val ms = measureTimeMillis {
            val file = File(InsomniacAxe.singleton.dataFolder, "users.json")
            file.makeReady()
            for (element in GsonProvider.parser().parse(FileReader(file)).asJsonArray) {
                AxeHolder.deserialize(element)
            }
        }
        Log.info("&aLoading Users took &4$ms ms".colorize())
    }

    fun saveUsers() {
        Log.info("&aSaving Users...".colorize())
        val file = File(InsomniacAxe.singleton.dataFolder, "users.json")
        file.makeReady()
        val ms = measureTimeMillis {
            val array = JsonArray()
            for (user in AxeHolder.users) {
                array.add(user.serialize())
            }
            val writer = FileWriter(file)
            GsonProvider.writeElementPretty(writer,array)
            writer.close()
        }
        Log.info("&aSaving took &4$ms ms".colorize())
    }

    fun loadAxes() {
        Log.info("&aLoading axes...".colorize())
        val ms = measureTimeMillis {
            val file = File(InsomniacAxe.singleton.dataFolder, "axes.json")
            file.makeReady()
            for (element in GsonProvider.parser().parse(FileReader(file)).asJsonArray) {
                Axe.deserialize(element)
            }
        }
        Log.info("&aLoading of axes took &4$ms ms".colorize())
    }

    fun saveAxes() {
        Log.info("&3Saving axes...".colorize())
        val file = File(InsomniacAxe.singleton.dataFolder, "axes.json")
        file.makeReady()
        val ms = measureTimeMillis {
            val array = JsonArray()
            for (axe in Axe.axes) {
                array.add(axe.value.serialize())
            }
            val writer = FileWriter(file)
            GsonProvider.writeElementPretty(writer,array)
            writer.close()
        }
        Log.info("&aSaving took &4$ms ms".colorize())
    }

    fun loadEnchantsFactory() {
        EnchantsFactory()
    }


}