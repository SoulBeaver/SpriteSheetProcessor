package com.sbg.rpg.util

import java.nio.file.Path

fun Path.filenameWithoutExtension(): String {
    val filename = this.fileName.toString()

    val extensionStart = filename.lastIndexOf(".")
    return if (extensionStart == -1) {
        filename
    } else {
        filename.substring(0, extensionStart)
    }
}