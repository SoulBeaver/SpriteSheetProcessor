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
package com.sbg.rpg.ui

import com.sbg.rpg.image.SpriteCutter
import com.sbg.rpg.image.SpriteDrawer
import com.sbg.rpg.ui.model.AnnotatedSpriteSheet
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import com.sbg.rpg.util.filenameWithoutExtension
import com.sbg.rpg.util.pmap
import com.sbg.rpg.util.readImage
import org.apache.logging.log4j.LogManager
import tornadofx.Controller
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

class SpriteSheetProcessorController : Controller() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorController::class.simpleName)

    private val view: SpriteSheetProcessorView by inject()

    private val spriteSheetUnpacker: SpriteSheetUnpacker = SpriteSheetUnpacker(SpriteCutter(SpriteDrawer()))
    private var spriteSheetPaths: List<Path> = emptyList()

    fun unpackSpriteSheets(spriteSheetFiles: List<File>): List<AnnotatedSpriteSheet> {
        logger.debug("Loading files $spriteSheetFiles")
        this.spriteSheetPaths = spriteSheetFiles.map { Paths.get(it.absolutePath) }

        val annotatedSpriteSheets = spriteSheetPaths.pmap { spriteSheetPath ->
            logger.debug("Unpacking ${spriteSheetPath.fileName}")

            val spriteSheet = spriteSheetPath.readImage()
            val spriteBoundsList = spriteSheetUnpacker.calculateSpriteBounds(spriteSheet)

            AnnotatedSpriteSheet(
                    spriteSheet,
                    spriteBoundsList
            )
        }

        return annotatedSpriteSheets
    }

    fun saveSprites(directory: File) {
        val spritesPerFile = spriteSheetPaths.pmap { spriteSheetPath ->
            spriteSheetPath.fileName to spriteSheetUnpacker.unpack(spriteSheetPath.readImage())
        }

        logger.debug("Writing individual sprites to file.")

        for ((fileName, sprites) in spritesPerFile) {
            val fileNameWithoutExtension = fileName.filenameWithoutExtension()

            sprites.forEachIndexed { idx, sprite ->
                ImageIO.write(
                        sprite,
                        "png",
                        Paths.get(directory.absolutePath, "${fileNameWithoutExtension}_$idx.png").toFile())
            }
        }

        logger.debug("Finished writing sprites to file.")
    }
}
