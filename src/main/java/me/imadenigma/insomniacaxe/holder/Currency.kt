package me.imadenigma.insomniacaxe.holder

interface Currency {

    fun give(amount: Long)

    fun take(amount: Long)

    fun set(amount: Long)

}