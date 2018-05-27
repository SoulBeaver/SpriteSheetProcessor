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

import com.sbg.rpg.packing.packer.model.IndexedSpriteBounds
import org.apache.logging.log4j.LogManager

/**
 * Converts a list of IndexedSpriteBounds, essentially a pair of Frame Index and the location and area of the Rectangle,
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
 * @param indexedSpriteBoundsList The IndexedSpriteBounds to convert
 * @return A yaml representation of the IndexedSpriteBounds or
 *         en empty ("") string if empty
 */
class YamlMetadataCreator: MetadataCreator {
    private val logger = LogManager.getLogger(YamlMetadataCreator::class.simpleName)

    override fun create(indexedSpriteBoundsList: List<IndexedSpriteBounds>): String {
        if (indexedSpriteBoundsList.isEmpty()) {
            logger.warn("No spriteBounds in list, returning empty string.")
            return ""
        }

        val yamlBuilder = StringBuilder()
        yamlBuilder.append("Frames:${System.lineSeparator()}")
        indexedSpriteBoundsList.forEach {
            yamlBuilder.append("  - Index: ${it.frame}${System.lineSeparator()}")
            yamlBuilder.append("    Bounds: [${it.bounds.x}, ${it.bounds.y}, ${it.bounds.width}, ${it.bounds.height}]${System.lineSeparator()}")
        }

        return yamlBuilder.toString()
    }
}