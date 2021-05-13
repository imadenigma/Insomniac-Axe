import junit.framework.TestCase
import me.imadenigma.insomniacaxe.enchant.priority.EnchPriority
import me.imadenigma.insomniacaxe.enchant.enchants.Autosell
import org.bukkit.Material

class Test : TestCase() {

    fun test1() {
        //val enchant = Autosell("smiya",true,1,Material.PUMPKIN,true,1.0, listOf())
        //println(enchant::class.java.getAnnotation(EnchPriority::class.java).priority)
        val list = mutableListOf<Int>(1)
        list.add(1)
        list.add(1)
        list.add(1)
        println(list)
    }

    fun test2() {
        var x: Int = 101
        x = (x * 1.5).toInt()
        println(x)
    }
}