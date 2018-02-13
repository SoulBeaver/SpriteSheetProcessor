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
import com.sbg.rpg.image.SpriteDrawer
import com.sbg.rpg.image.readImage
import com.sbg.rpg.metadata.MetadataCreator
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.properties.Delegates

/**
 * Controller class that processes sprites or spritesheets as necessary.
 */
class SpriteSheetProcessor(private val metadataCreator: MetadataCreator,
                           private val spriteSheetUnpacker: SpriteSheetUnpacker) {

    private val logger = LogManager.getLogger(SpriteSheetProcessor::class.simpleName)

    /**
     * Reads and processes a list of sprite sheets.
     *
     * @param commandLineArguments input as to how and what to process. Determines which file will be processed
     *  as well as the output type of the resultant Metadata or Atlas file.
     */
    fun processSpriteSheets(commandLineArguments: CommandLineArguments) {
        for (rawSpriteSheetPath in commandLineArguments.spriteSheetPaths) {
            logger.info("Working on $rawSpriteSheetPath.")
            val spriteSheetPath = Paths.get(rawSpriteSheetPath)!!.toAbsolutePath()!!

            logger.info("Unpacking sprites.")
            val sprites = spriteSheetUnpacker.unpack(readImage(spriteSheetPath))

            logger.info("Writing individual sprites to file.")
            sprites.forEachIndexed { idx, sprite ->
                ImageIO.write(sprite, "png", Paths.get(commandLineArguments.exportFolder, "${spriteSheetPath.fileName}_sprite_$idx.png").toFile())
            }
        }

        logger.info("Finished unpacking spritesheets.")
    }
}