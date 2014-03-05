package com.sbg.rpg.util

import org.spek.Spek
import java.util.HashMap
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class MapUtilsSpec: Spek() {{
    given("A map") {
        val map = HashMap<Int, Int>()

        on("with zero insertions") {
            it("returns null for max()") {
                val max = map.max()

                assertTrue(max == null,
                           "There should be no max value, but was $max")
            }
        }

        on("with one insertion") {
            it("returns the single entry as max") {
                map.put(1, 1)

                val max = map.max()

                assertEquals(Pair(1, 1), max,
                             "Expected (1, 1) as maximum, but was $max")
            }
        }

        on("with multiple insertions") {
            it("returns entry with largest value") {
                map.put(1, 1)
                map.put(2, 2)
                map.put(3, 4)

                val max = map.max()

                assertEquals(Pair(3, 4), max,
                             "Expected (3, 4) as maximum, but was $max")
            }
        }

        on("with multiple insertions containing equally large values") {
            it("returns first entry containing largest value") {
                map.put(1, 1)
                map.put(2, 2)
                map.put(3, 2)
                map.put(4, 2)

                val max = map.max()

                assertEquals(Pair(2, 2), max,
                             "Expected(2, 2) ax maximum, but was $max")
            }
        }
    }
}}