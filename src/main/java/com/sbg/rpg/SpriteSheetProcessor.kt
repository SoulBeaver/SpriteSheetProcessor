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
import com.sbg.rpg.packer.SpriteSheetPacker
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import com.sbg.rpg.util.filenameWithoutExtension
import com.sbg.rpg.util.readImage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Controller class that processes sprites or spritesheets as necessary.
 */
abstract class SpriteSheetProcessor(private val spriteSheetUnpacker: SpriteSheetUnpacker) {

    protected val logger: Logger = LogManager.getLogger(SpriteSheetProcessor::class.simpleName)

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

            logger.debug("Unpacking sprites.")
            val sprites = spriteSheetUnpacker.unpack(spriteSheetPath.readImage())

            export(cla, spriteSheetName, sprites)
        }

        logger.info("Finished processing ${cla.spriteSheetPaths.size} sprite sheets.")
    }


    protected abstract fun export(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>)
}

class SpriteSheetPackingProcessor(
        spriteSheetUnpacker: SpriteSheetUnpacker,
        private val spriteSheetPacker: SpriteSheetPacker): SpriteSheetProcessor(spriteSheetUnpacker) {

    override fun export(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>) {
        logger.debug("Creating a packed sprite sheet")

        val packedSpriteSheet = spriteSheetPacker.pack(sprites)

        ImageIO.write(
                packedSpriteSheet.canvas,
                "png",
                Paths.get(cla.exportFolder, "${spriteSheetName}_sheet.png").toFile())
        Files.write(
                Paths.get(cla.exportFolder, "${spriteSheetName}_sheet.${cla.packSpriteSheets}"),
                packedSpriteSheet.metadata.toByteArray(Charsets.UTF_8))
    }
}

class SpriteSheetUnpackingProcessor(spriteSheetUnpacker: SpriteSheetUnpacker): SpriteSheetProcessor(spriteSheetUnpacker) {
    override fun export(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>) {
        logger.debug("Writing individual sprites to file.")

        sprites.forEachIndexed { idx, sprite ->
            ImageIO.write(
                    sprite,
                    "png",
                    Paths.get(cla.exportFolder, "${spriteSheetName}_$idx.png").toFile())
        }
    }
}