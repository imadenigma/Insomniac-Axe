package me.imadenigma.insomniacaxe

import com.google.gson.JsonArray
import me.imadenigma.insomniacaxe.axe.AxeHolder
import me.imadenigma.insomniacaxe.enchant.EnchantsFactory
import me.lucko.helper.gson.GsonProvider
import me.lucko.helper.utils.Log
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.system.measureTimeMillis

class Manager {

    fun loadUsers() {
        val ms = measureTimeMillis {
            val file = File(InsomniacAxe.singleton.dataFolder, "users.json")
            file.makeReady()
            for (element in GsonProvider.parser().parse(FileReader(file)).asJsonArray) {
                AxeHolder.deserialize(element)
            }
        }
        Log.info("&aLoading took &4$ms ms".colorize())


    }

    fun saveUsers() {
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
    fun loadEnchantsFactory() {
        EnchantsFactory()
    }

}