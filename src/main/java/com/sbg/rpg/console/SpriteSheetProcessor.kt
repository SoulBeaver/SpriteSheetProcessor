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
package com.sbg.rpg.console

import com.sbg.rpg.packing.common.ImageReadException
import com.sbg.rpg.packing.common.Sprite
import com.sbg.rpg.packing.common.SpriteSheetWriter
import com.sbg.rpg.packing.packer.SpriteSheetPacker
import com.sbg.rpg.packing.unpacker.SpriteSheetUnpacker
import com.sbg.rpg.packing.common.extensions.filenameWithoutExtension
import com.sbg.rpg.packing.common.extensions.readImage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Controller class that processes sprites or spritesheets as necessary.
 */
abstract class SpriteSheetProcessor(
        protected val spriteSheetUnpacker: SpriteSheetUnpacker,
        protected val spriteSheetWriter: SpriteSheetWriter) {

    protected val logger: Logger = LogManager.getLogger(SpriteSheetProcessor::class.simpleName)

    /**
     * Reads and processes a list of sprite sheets.
     *
     * @param cla input as to how and what to process. Determines which file will be processed
     *  as well as the output type of the resultant Metadata or Atlas file.
     */
    fun processSpriteSheets(cla: CommandLineArguments) {
        if (cla.failFast) {
            verifySpriteSheets(cla.spriteSheetPaths)
        }

        for (rawSpriteSheetPath in cla.spriteSheetPaths) {
            logger.info("Working on $rawSpriteSheetPath.")

            val spriteSheetPath = Paths.get(rawSpriteSheetPath)!!.toAbsolutePath()!!
            val spriteSheetName = spriteSheetPath.filenameWithoutExtension()

            logger.debug("Attempting to unpack sprites.")

            tryUnpack(spriteSheetPath)?.let { sprites ->
                if (sprites.isNotEmpty()) {
                    export(cla, spriteSheetName, sprites)
                } else {
                    logger.warn("Unable to detect any sprites for sprite sheet.")
                }
            }
        }

        logger.info("Finished processing ${cla.spriteSheetPaths.size} sprite sheets.")
    }

    private fun verifySpriteSheets(spriteSheetPaths: List<String>) {
        spriteSheetPaths.forEach { spriteSheetPath ->
            val absolutePath = Paths.get(spriteSheetPath)!!.toAbsolutePath()!!

            if (!Files.exists(absolutePath) || !Files.isRegularFile(absolutePath)) {
                throw IllegalArgumentException("Unable to find file $spriteSheetPath or it doesn't appear to be an common.")
            }
        }
    }

    private fun tryUnpack(spriteSheetPath: Path): List<Sprite>? {
        return try {
            spriteSheetUnpacker.unpack(spriteSheetPath.readImage())
        } catch (e: ImageReadException) {
            logger.error("Skipping sprite sheet; unable to read common.", e)
            null
        } catch (e: Exception) {
            logger.error("Skipping sprite sheet; an unidentified error occurred.", e)
            null
        }
    }

    protected abstract fun export(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>)
}

class SpriteSheetPackingProcessor(
        spriteSheetUnpacker: SpriteSheetUnpacker,
        spriteSheetWriter: SpriteSheetWriter,
        private val spriteSheetPacker: SpriteSheetPacker) : SpriteSheetProcessor(spriteSheetUnpacker, spriteSheetWriter) {

    override fun export(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>) {
        logger.debug("Creating a packed sprite sheet")

        val packedSpriteSheet = spriteSheetPacker.pack(sprites)

        spriteSheetWriter.write(
                packedSpriteSheet.canvas,
                "png",
                Paths.get(cla.exportFolder, "${spriteSheetName}_sheet.png"))
        spriteSheetWriter.writeMetadata(
                packedSpriteSheet.metadata,
                Paths.get(cla.exportFolder, "${spriteSheetName}_sheet.${cla.packSpriteSheets}"))
    }
}

class SpriteSheetUnpackingProcessor(
        spriteSheetUnpacker: SpriteSheetUnpacker,
        spriteSheetWriter: SpriteSheetWriter) : SpriteSheetProcessor(spriteSheetUnpacker, spriteSheetWriter) {

    override fun export(cla: CommandLineArguments, spriteSheetName: String, sprites: List<Sprite>) {
        logger.debug("Writing individual sprites to file.")

        sprites.forEachIndexed { idx, sprite ->
            spriteSheetWriter.write(
                    sprite,
                    "png",
                    Paths.get(cla.exportFolder, "${spriteSheetName}_$idx.png"))
        }
    }
}