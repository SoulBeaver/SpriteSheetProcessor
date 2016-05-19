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
package com.sbg.rpg

import com.sbg.rpg.cli.CommandLineArguments
import com.sbg.rpg.metadata.JsonMetadataCreator
import com.sbg.rpg.metadata.MetadataCreator
import com.sbg.rpg.metadata.TextMetadataCreator
import com.sbg.rpg.metadata.YamlMetadataCreator
import com.sbg.rpg.packer.SpriteSheetPacker
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths
import java.util.*
import kotlin.properties.Delegates

/**
 * TODO: Write me
 */
class SpriteSheetProcessor() {
    private val logger = LogManager.getLogger(SpriteSheetProcessor::class.simpleName)

    private var metadataCreator: MetadataCreator by Delegates.notNull()
    private val spriteSheetUnpacker: SpriteSheetUnpacker
    private val spriteSheetPacker: SpriteSheetPacker

    init {
        spriteSheetUnpacker = SpriteSheetUnpacker()
        spriteSheetPacker = SpriteSheetPacker()
    }

    /**
     * TODO: Write me
     */
    fun processSpriteSheets(commandLineArguments: CommandLineArguments) {
        val spriteSheetsWithMetadata = ArrayList<ProcessedSpriteSheet>()

        for (rawSpriteSheetPath in commandLineArguments.spriteSheetPaths) {
            logger.debug("Working on $rawSpriteSheetPath")
            val spriteSheetPath = Paths.get(rawSpriteSheetPath)!!.toAbsolutePath()!!

            logger.trace("Unpacking sprites")
            val sprites = spriteSheetUnpacker.unpack(spriteSheetPath)

            logger.trace("Packing sprites")
            val (packedSpriteSheet, spriteBoundsList) = spriteSheetPacker.packSprites(sprites)

            logger.debug("Creating ${commandLineArguments.metadataOutputFormat} metadata")
            metadataCreator = when(commandLineArguments.metadataOutputFormat) {
                "json"                    -> JsonMetadataCreator()
                in arrayOf("yaml", "yml") -> YamlMetadataCreator()
                "txt"                     -> TextMetadataCreator()
                else                      -> throw IllegalArgumentException("Metadata Output Format must be one of {yaml, json, txt}")
            }

            val metadata = metadataCreator.create(spriteBoundsList)
            spriteSheetsWithMetadata.add(ProcessedSpriteSheet(packedSpriteSheet, metadata))
        }
    }
}