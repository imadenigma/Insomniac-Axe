package me.imadenigma.insomniacaxe

import me.imadenigma.insomniacaxe.commands.CoinsCommand
import me.imadenigma.insomniacaxe.commands.GiveCommand
import me.imadenigma.insomniacaxe.commands.PumpkinTop
import me.imadenigma.insomniacaxe.listeners.EnchantListeners
import me.imadenigma.insomniacaxe.listeners.PlayerListeners
import me.lucko.helper.Helper
import me.lucko.helper.plugin.ExtendedJavaPlugin
import me.lucko.helper.plugin.ap.Plugin
import me.lucko.helper.plugin.ap.PluginDependency
import net.milkbowl.vault.economy.Economy


@Plugin(
    name = "InsomniacAxe",
    apiVersion = "1.15",
    depends = [
        PluginDependency("Vault"),
        PluginDependency("ShopGUIPlus")
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
        registerCommand(CoinsCommand(), "ngems")
        registerCommand(PumpkinTop(), "pumpkintop")
        manager = Manager()
        manager!!.loadEnchantsFactory()
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