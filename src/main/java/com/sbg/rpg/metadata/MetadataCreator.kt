package com.sbg.rpg.metadata

import java.awt.Image
import java.awt.Rectangle
import com.google.gson.Gson
import org.yaml.snakeyaml.Yaml
import com.sbg.rpg.packer.SpriteBounds
import com.google.common.base.Joiner

fun createJsonMetadata(spriteBoundsList: List<SpriteBounds>): String {
    if (spriteBoundsList.isEmpty())
        return ""

    return Gson().toJson(spriteBoundsList)!!
}

fun createYamlMetadata(spriteBoundsList: List<SpriteBounds>): String {
    if (spriteBoundsList.isEmpty())
        return ""

    val yamlBuilder = StringBuilder()
    yamlBuilder.append("Frames:${System.lineSeparator()}")
    spriteBoundsList.forEach {
        yamlBuilder.append("  - Index: ${it.frame}${System.lineSeparator()}")
        yamlBuilder.append("    Bounds: [${it.bounds.x}, ${it.bounds.y}, ${it.bounds.width}, ${it.bounds.height}]${System.lineSeparator()}")
    }

    return yamlBuilder.toString()
}

fun createTextMetadata(spriteBoundsList: List<SpriteBounds>): String {
    if (spriteBoundsList.isEmpty())
        return ""

    val entries = spriteBoundsList.map {
        "${it.frame}=${it.bounds.x} ${it.bounds.y} ${it.bounds.width} ${it.bounds.height}"
    }

    return Joiner.on(System.lineSeparator())!!.join(entries)!!
}