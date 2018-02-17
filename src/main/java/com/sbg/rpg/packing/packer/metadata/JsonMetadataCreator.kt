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
package com.sbg.rpg.packing.packer.metadata

import com.google.gson.Gson
import com.sbg.rpg.packing.packer.model.SpriteBounds
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
        if (spriteBoundsList.isEmpty()) {
            logger.warn("No spriteBounds in list, returning empty string.")
            return ""
        }

        return Gson().toJson(spriteBoundsList)
    }
}