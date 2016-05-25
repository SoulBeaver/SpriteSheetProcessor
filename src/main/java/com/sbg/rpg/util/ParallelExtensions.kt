package com.sbg.rpg.util

import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Parallelizes the execution of map.
 *
 * Code taken from https://stackoverflow.com/questions/34697828/parallel-operations-on-kotlin-collections
 */
fun <T, R> Iterable<T>.pmap(
        numThreads: Int = Runtime.getRuntime().availableProcessors() - 2,
        exec: ExecutorService = Executors.newFixedThreadPool(numThreads),
        transform: (T) -> R): List<R> {

    // default size is just an inlined version of kotlin.collections.collectionSizeOrDefault
    val defaultSize = if (this is Collection<*>) this.size else 10
    val destination = Collections.synchronizedList(ArrayList<R>(defaultSize))

    for (item in this) {
        exec.submit { destination.add(transform(item)) }
    }

    exec.shutdown()
    exec.awaitTermination(1, TimeUnit.DAYS)

    return ArrayList<R>(destination)
}