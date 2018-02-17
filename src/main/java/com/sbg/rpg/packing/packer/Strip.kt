package com.sbg.rpg.packing.packer

data class Strip(val x: Int, val y: Int, val width: Int, val height: Int) {
    fun growHorizontally(amount: Int) = Strip(x, y, width + amount, height)

    fun growVertically(amount: Int) = Strip(x, y, width, height + amount)

    fun cutLeft(amount: Int) = Strip(x + amount, y, width - amount, height)
}

fun List<Strip>.growHorizontally(amount: Int) = this.map { strip -> strip.growHorizontally(amount) }

fun List<Strip>.growVertically(amount: Int) = this.map { strip -> strip.growVertically(amount) }