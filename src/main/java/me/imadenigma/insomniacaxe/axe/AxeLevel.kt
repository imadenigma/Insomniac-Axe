package me.imadenigma.insomniacaxe.axe


object AxeLevel {
    lateinit var boosters: Map<Int,Float>
    lateinit var blocksToNextLevel: Map<Int,Int>
    var isFrenzy: Boolean = false
    fun getBoosterVal(level: Int) : Float {
        if (isFrenzy) {
            return (boosters[level] ?: boosters[level - 1]!!) * 2
        }
        return boosters[level] ?: boosters[level - 1]!!
    }
    fun getBlocksToNextLevel(level: Int) : Int {
        return blocksToNextLevel[level] ?: blocksToNextLevel[level - 1]!!
    }
}