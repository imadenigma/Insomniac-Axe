package me.imadenigma.insomniacaxe

import me.imadenigma.insomniacaxe.commands.GiveCommand
import me.imadenigma.insomniacaxe.listeners.EnchantListeners
import me.lucko.helper.Helper
import me.lucko.helper.plugin.ExtendedJavaPlugin
import me.lucko.helper.plugin.ap.Plugin
import me.lucko.helper.plugin.ap.PluginDependency
import net.milkbowl.vault.economy.Economy

@Plugin(
    name = "InsomniacAxe",
    version = "1.15",
    authors = ["Johan Liebert"],
    depends = [
        PluginDependency("Vault"),
    ]
    )
class InsomniacAxe : ExtendedJavaPlugin() {

    private var manager: Manager? = null
    var economy: Economy? = null
        private set

    override fun enable() {
        // Plugin startup logic
        singleton = this
        Configuration()
        registerCommand(GiveCommand(), "insomniac")
        manager = Manager()
        manager!!.loadEnchantsFactory()
        manager!!.loadUsers()
        registerListeners()

    }

    private fun registerListeners() {
        EnchantListeners()
    }

    override fun disable() {
        // Plugin shutdown logic
        manager!!.saveUsers()
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