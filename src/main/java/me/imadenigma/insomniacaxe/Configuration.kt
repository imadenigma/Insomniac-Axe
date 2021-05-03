package me.imadenigma.insomniacaxe

import me.imadenigma.insomniacaxe.axe.AxeLevel
import me.lucko.helper.Services
import me.lucko.helper.config.ConfigurationNode
import me.lucko.helper.config.yaml.YAMLConfigurationLoader
import me.lucko.helper.utils.Log
import org.bukkit.Bukkit
import java.io.File
import kotlin.system.measureTimeMillis

class Configuration {
    var configNode: ConfigurationNode
    init {
        val ms = measureTimeMillis {
            val file = File(InsomniacAxe.singleton.dataFolder, "config.yml")
            file.makeReady()
            val loader = YAMLConfigurationLoader.builder().setFile(file).build()

            if (!loader.canLoad()) {
                Log.warn("Could not load config file!!!")
                Bukkit.getPluginManager().disablePlugin(InsomniacAxe.singleton)
            }

            configNode = loader.load()
            val boost = mutableMapOf<Int, Float>()
            val blocks = mutableMapOf<Int, Int>()

            for (entry in configNode.getNode("levels").childrenMap) {
                boost[entry.key as Int] = entry.value.getNode("booster").getFloat(0F)
                blocks[entry.key as Int] = entry.value.getNode("blocksToNextLevel").getInt(1)
            }

            AxeLevel.boosters = boost
            AxeLevel.blocksToNextLevel = blocks
            Services.provide(Configuration::class.java, this)
        }
        Log.info("&4Loading config file took &a$ms ms".colorize())
    }
}
