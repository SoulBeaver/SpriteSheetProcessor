package com.sbg.rpg.util

fun <K, V : Comparable<V>> Map<K, V>.max(): Pair<K, V>? {
    if (isEmpty()) return null

    val max = entries.reduce {
        current, next -> if (current.value < next.value) next
                         else current
    }

    return Pair(max.key, max.value)
}