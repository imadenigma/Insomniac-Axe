package me.imadenigma.insomniacaxe.enchant.priority

@Target(AnnotationTarget.CLASS)
@Retention
@Repeatable
annotation class EnchPriority(val priority: Priority)