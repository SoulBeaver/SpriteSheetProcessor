/*
 *  Copyright 2016 Christian Broomfield
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sbg.rpg.util

import java.awt.Rectangle
import java.awt.Image
import java.awt.Point

fun spanRectangleFrom(points: List<Point>): Rectangle {
    if (points.isEmpty())
        throw IllegalArgumentException("No points to span Rectangle from.")

    val left = points.reduce {
        current, next -> if (current.x > next.x) next else current
    }
    val top = points.reduce {
        current, next -> if (current.y > next.y) next else current
    }
    val right = points.reduce {
        current, next -> if (current.x < next.x) next else current
    }
    val bottom = points.reduce {
        current, next -> if (current.y < next.y) next else current
    }

    return Rectangle(left.x, top.y,
                     right.x - left.x + 1,
                     bottom.y - top.y + 1)

}