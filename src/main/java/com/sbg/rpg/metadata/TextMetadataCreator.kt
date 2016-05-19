package com.sbg.rpg.metadata

import com.sbg.rpg.packer.SpriteBounds
import org.apache.logging.log4j.LogManager

/**
 * Converts a list of SpriteBounds (Int * Rectangle), essentially a pair of Frame Index and the location and area of the Rectangle,
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
class TextMetadataCreator: MetadataCreator {
    private val logger = LogManager.getLogger(TextMetadataCreator::class.simpleName)

    override fun create(spriteBoundsList: List<SpriteBounds>): String {
        if (spriteBoundsList.isEmpty()) {
            logger.warn("No spriteBounds in list, returning empty string.")
            return ""
        }

        val entries = spriteBoundsList.map {
            "${it.frame}=${it.bounds.x} ${it.bounds.y} ${it.bounds.width} ${it.bounds.height}"
        }

        return entries.joinToString(System.lineSeparator())
    }
}