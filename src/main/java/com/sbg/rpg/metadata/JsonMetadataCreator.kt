package com.sbg.rpg.metadata

import com.google.gson.Gson
import com.sbg.rpg.packer.SpriteBounds
import org.apache.logging.log4j.LogManager

/**
 * Converts a list of SpriteBounds, essentially a pair of Frame Index and the location and area of the Rectangle,
 * into a json string representation. The string is guaranteed to have proper line breaks for each OS, but not
 * necessarily a human-readable format,
 *
 * @param spriteBoundsList The SpriteBounds to convert
 * @return A json representation of the SpriteBounds or
 *         en empty ("") string if empty
 */
class JsonMetadataCreator: MetadataCreator {
    private val logger = LogManager.getLogger(JsonMetadataCreator::class.simpleName)

    override fun create(spriteBoundsList: List<SpriteBounds>): String {
        if (spriteBoundsList.isEmpty())
            return ""

        return Gson().toJson(spriteBoundsList)
    }
}