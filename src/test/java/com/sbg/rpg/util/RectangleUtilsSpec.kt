package com.sbg.rpg.util

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.awt.Point
import java.awt.Rectangle
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RectangleUtilsSpec: Spek({
    given("a plot with no points") {
        val points = listOf<Point>()

        on("trying to span a Rectangle") {
            it("throws an IllegalArgumentException") {
                assertFailsWith<IllegalArgumentException> {
                    spanRectangleFrom(points)
                }
            }
        }
    }

    given("a plot with a single point") {
        val points = listOf(Point(0, 0))

        on("trying to span a Rectangle") {
            it("returns a Rectangle of size (0, 0, 1, 1)") {
                assertEquals(Rectangle(0, 0, 1, 1), spanRectangleFrom(points))
            }
        }
    }

    given ("a plot with two points") {
        val points = listOf(Point(0, 0), Point(1, 1))

        on("trying to span a Rectangle") {
            it("returns a Rectangle of size (0, 0, 2, 2)") {
                assertEquals(Rectangle(0, 0, 2, 2), spanRectangleFrom(points))
            }
        }
    }

    given ("a plot with three points") {
        val points = listOf(
                Point(0, 0),
                Point(1, 0),
                Point(1, 1)
        )

        on("trying to span a Rectangle") {
            it("returns a Rectangle of size (0, 0, 2, 2)") {
                assertEquals(Rectangle(0, 0, 2, 2), spanRectangleFrom(points))
            }
        }
    }

    given ("a plot with four points") {
        val points = listOf(
                Point(0, 0),
                Point(1, 0),
                Point(0, 1),
                Point(1, 1)
        )

        on("trying to span a Rectangle") {
            it("returns a Rectangle of size (0, 0, 2, 2)") {
                assertEquals(Rectangle(0, 0, 2, 2), spanRectangleFrom(points))
            }
        }
    }

    given("a plot with four identical points") {
        val points = listOf(
                Point(0, 0),
                Point(0, 0),
                Point(0, 0),
                Point(0, 0)
        )

        on("trying to span a Rectangle") {
            it("returns a Rectangle of size (0, 0, 1, 1)") {
                assertEquals(Rectangle(0, 0, 1, 1), spanRectangleFrom(points))
            }
        }
    }

    given ("a plot with two identical points and two different points") {
        val points = listOf(
                Point(0, 0),
                Point(0, 0),
                Point(1, 0),
                Point(1, 1)
        )

        on("trying to span a Rectangle") {
            it("returns a Rectangle of size (0, 0, 2, 2)") {
                assertEquals(Rectangle(0, 0, 2, 2), spanRectangleFrom(points))
            }
        }
    }
})