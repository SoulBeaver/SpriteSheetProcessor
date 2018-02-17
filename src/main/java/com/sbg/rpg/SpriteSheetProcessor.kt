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
import com.sbg.rpg.image.Sprite
import com.sbg.rpg.image.readImage
import com.sbg.rpg.packer.SpriteSheetPacker
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import com.sbg.rpg.util.filenameWithoutExtension
import org.apache.logging.log4j.LogManager
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Controller class that processes sprites or spritesheets as necessary.
 */
class SpriteSheetProcessor(private val spriteSheetUnpacker: SpriteSheetUnpacker,
                           private val spriteSheetPacker: SpriteSheetPacker?) {

    private val logger = LogManager.getLogger(SpriteSheetProcessor::class.simpleName)

    /**
     * Reads and processes a list of sprite sheets.
     *
     * @param cla input as to how and what to process. Determines which file will be processed
     *  as well as the output type of the resultant Metadata or Atlas file.
     */
    fun processSpriteSheets(cla: CommandLineArguments) {
        for (rawSpriteSheetPath in cla.spriteSheetPaths) {
            logger.info("Working on $rawSpriteSheetPath.")
            val spriteSheetPath = Paths.get(rawSpriteSheetPath)!!.toAbsolutePath()!!
            val spriteSheetName = spriteSheetPath.filenameWithoutExtension()

            logger.info("Unpacking sprites.")
            val sprites = spriteSheetUnpacker.unpack(readImage(spriteSheetPath))

            if (spriteSheetPacker != null) {
                logger.info("Creating a packed sprite sheet")

                packSpriteSheet(cla, spriteSheetName, sprites)
            } else {
                logger.info("Writing individual sprites to file.")

                keepUnpacked(cla, spriteSheetName, sprites)
            }
        }

        logger.info("Finished processing ${cla.spriteSheetPaths.size} sprite sheets.")
    }

    private fun packSpriteSheet(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>) {
        val packedSpriteSheet = spriteSheetPacker!!.pack(sprites)

        ImageIO.write(
                packedSpriteSheet.canvas,
                "png",
                Paths.get(cla.exportFolder, "${spriteSheetName}_sheet.png").toFile())
        Files.write(
                Paths.get(cla.exportFolder, "${spriteSheetName}_sheet.${cla.packSpriteSheets}"),
                packedSpriteSheet.metadata.toByteArray(Charsets.UTF_8))
    }

    private fun keepUnpacked(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>) {
        sprites.forEachIndexed { idx, sprite ->
            ImageIO.write(
                    sprite,
                    "png",
                    Paths.get(cla.exportFolder, "${spriteSheetName}_$idx.png").toFile())
        }
    }
}