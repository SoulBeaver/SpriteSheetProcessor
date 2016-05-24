package com.sbg.rpg.util

fun <T> List<T>.collate(batchSize: Int): List<List<T>> {
    if (batchSize <= 0)
        return emptyList()

    var remainder = this
    val collatedList = mutableListOf<List<T>>()

    while (remainder.isNotEmpty()) {
        val partition = remainder.take(batchSize)

        collatedList.add(partition)

        remainder = remainder.drop(batchSize)
    }

    return collatedList
}