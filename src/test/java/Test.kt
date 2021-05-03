import junit.framework.TestCase
import me.imadenigma.insomniacaxe.colorize
import org.bukkit.ChatColor
import org.bukkit.Material

class Test : TestCase() {

    fun test1() {
        val name = "&3imad"
        println(ChatColor.stripColor(name.colorize()))
    }
}