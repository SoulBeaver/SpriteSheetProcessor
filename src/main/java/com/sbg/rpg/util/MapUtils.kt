package com.sbg.rpg.util

fun <K, V : Comparable<V>> Map<K, V>.max(): Pair<K, V>? {
    if (isEmpty()) return null

    val max = entrySet().reduce {
        current, next ->
        if (current.getValue() < next.getValue()) next
        else current
    }

    return max.getKey() to  max.getValue()
}