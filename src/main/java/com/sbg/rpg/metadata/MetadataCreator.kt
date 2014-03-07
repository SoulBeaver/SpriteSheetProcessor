package com.sbg.rpg.metadata

import java.awt.Image
import java.awt.Rectangle
import com.google.gson.Gson
import org.yaml.snakeyaml.Yaml
import com.sbg.rpg.packer.SpriteBounds
import com.google.common.base.Joiner
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger("MetadataCreator")!!

/**
 * Converts a list of SpriteBounds, essentially a pair of Frame Index and the location and area of the Rectangle,
 * into a json string representation. The string is guaranteed to have proper line breaks for each OS, but not
 * necessarily a human-readable format,
 *
 * @param spriteBoundsList The SpriteBounds to convert
 * @return A json representation of the SpriteBounds or
 *         en empty ("") string if empty
 */
fun createJsonMetadata(spriteBoundsList: List<SpriteBounds>): String {
    if (spriteBoundsList.isEmpty())
        return ""

    return Gson().toJson(spriteBoundsList)!!
}

/**
 * Converts a list of SpriteBounds, essentially a pair of Frame Index and the location and area of the Rectangle,
 * into a yaml string representation. The string is guaranteed to have proper line breaks for each OS and a
 * readable format.
 *
 * <pre>
 *     Frames:
 *       - Index: 0
 *         Bounds: 0 0 50 50
 *       - Index: 1
 *         Bounds: 51 51 50 50
 *       ...
 * </pre>
 *
 * @param spriteBoundsList The SpriteBounds to convert
 * @return A yaml representation of the SpriteBounds or
 *         en empty ("") string if empty
 */
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

/**
 * Converts a list of SpriteBounds, essentially a pair of Frame Index and the location and area of the Rectangle,
 * into a text string representation. The string is guaranteed to have proper line breaks for each OS and a
 * somewhat readable format.
 *
 * Schema:
 * <pre>
 *     <Index>=<X> <Y> <Width> <Height>
 * </pre>
 *
 * Example:
 * <pre>
 *     0=0 0 50 50
 *     1=51 51 50 50
 *     ...
 * </pre>
 *
 * @param spriteBoundsList The SpriteBounds to convert
 * @return A plain text representation of the SpriteBounds or
 *         en empty ("") string if empty
 */
fun createTextMetadata(spriteBoundsList: List<SpriteBounds>): String {
    if (spriteBoundsList.isEmpty())
        return ""

    val entries = spriteBoundsList.map {
        "${it.frame}=${it.bounds.x} ${it.bounds.y} ${it.bounds.width} ${it.bounds.height}"
    }

    return Joiner.on(System.lineSeparator())!!.join(entries)!!
}