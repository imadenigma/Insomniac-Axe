package me.imadenigma.insomniacaxe

import me.imadenigma.insomniacaxe.commands.GemsCommands
import me.imadenigma.insomniacaxe.commands.InsomniacAxeCommands
import me.imadenigma.insomniacaxe.commands.PumpkinTop
import me.imadenigma.insomniacaxe.commands.Upgrade
import me.imadenigma.insomniacaxe.listeners.EnchantListeners
import me.imadenigma.insomniacaxe.listeners.PlayerListeners
import me.lucko.helper.Helper
import me.lucko.helper.plugin.ExtendedJavaPlugin
import me.lucko.helper.plugin.ap.Plugin
import net.milkbowl.vault.economy.Economy
import kotlin.system.measureTimeMillis


@Plugin(
    name = "InsomniacAxe",
    apiVersion = "1.15"
)
class InsomniacAxe : ExtendedJavaPlugin() {

    private var manager: Manager? = null
    var economy: Economy? = null
        private set

    override fun enable() {
        // Plugin startup logic
        singleton = this
        Configuration()
        registerCommand(InsomniacAxeCommands(), "insomniacaxe")
        registerCommand(GemsCommands(), "nightmare")
        registerCommand(PumpkinTop(), "pumpkintop")
        registerCommand(Upgrade(), "upgrade")
        manager = Manager()
        println("&7loading of enchantments took ${measureTimeMillis { manager!!.loadEnchantsFactory() }} ms ".colorize())
        manager!!.loadAxes()
        manager!!.loadUsers()
        registerListeners()
        setupEconomy()
    }

    private fun registerListeners() {
        EnchantListeners()
        PlayerListeners()
    }

    override fun disable() {
        // Plugin shutdown logic
        manager!!.saveUsers()
        manager!!.saveAxes()
    }

    private fun setupEconomy() {
        if (Helper.plugins().isPluginEnabled("Vault")) {
            val rsp = Helper.services().getRegistration(Economy::class.java) ?: return
            this.economy = rsp.provider
        }
    }

    companion object {
        lateinit var singleton: InsomniacAxe
            private set
    }
}