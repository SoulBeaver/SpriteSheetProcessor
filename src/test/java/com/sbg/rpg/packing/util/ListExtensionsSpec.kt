package com.sbg.rpg.packing.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object ListExtensionsSpec: Spek({
    given("a list with no elements") {
        val empty = emptyList<Int>()

        on("trying to collate it") {
            val actual = empty.collate(1)

            it("returns an empty list") {
                val expected: List<List<Int>> = emptyList()
                assertEquals(expected, actual)
            }
        }
    }

    given("a list with one element") {
        val single = listOf(1)

        on("trying to collate it by one-element increments") {
            val actual = single.collate(1)

            it("returns [ [ 1 ] ]") {
                val expected: List<List<Int>> = listOf(listOf(1))
                assertEquals(expected, actual)
            }
        }

        on("trying to collate it by two-element increments") {
            val actual = single.collate(2)

            it("returns [ [ 1 ] ]") {
                val expected: List<List<Int>> = listOf(listOf(1))
                assertEquals(expected, actual)
            }
        }
    }

    given("a list with ten elements") {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

        on("trying to collate it by zero-element increments") {
            val actual = list.collate(0)

            it("returns an empty list") {
                val expected: List<List<Int>> = emptyList()
                assertEquals(expected, actual)
            }
        }

        on("trying to collate it by one-element increments") {
            val actual = list.collate(1)

            it("returns [ [1], [2], [3], [4], [5], [6], [7], [8], [9], [0] ]") {
                val expected = listOf(
                        listOf(1),
                        listOf(2),
                        listOf(3),
                        listOf(4),
                        listOf(5),
                        listOf(6),
                        listOf(7),
                        listOf(8),
                        listOf(9),
                        listOf(0)
                )
                assertEquals(expected, actual)
            }
        }

        on("trying to collate it by two-element increments") {
            val actual = list.collate(2)

            it("returns [ [1, 2], [3, 4], [5, 6], [7, 8], [9, 0] ]") {
                val expected = listOf(
                        listOf(1, 2),
                        listOf(3, 4),
                        listOf(5, 6),
                        listOf(7, 8),
                        listOf(9, 0)
                )
                assertEquals(expected, actual)
            }
        }

        on("trying to collate it by three element increments") {
            val actual = list.collate(3)

            it("returns [ [1, 2, 3], [4, 5, 6], [7, 8, 9], [0] ] ") {
                val expected = listOf(
                        listOf(1, 2, 3),
                        listOf(4, 5, 6),
                        listOf(7, 8, 9),
                        listOf(0)
                )
                assertEquals(expected, actual)
            }
        }

        on("trying to collate it by ten element increments") {
            val actual = list.collate(10)

            it("returns [ [1, 2, 3, 4, 5, 6, 7, 8, 9, 0] ]") {
                val expected = listOf(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))
                assertEquals(expected, actual)
            }
        }
    }
})