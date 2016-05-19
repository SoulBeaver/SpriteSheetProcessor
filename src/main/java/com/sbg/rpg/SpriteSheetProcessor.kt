package com.sbg.rpg

import com.sbg.rpg.cli.CommandLineArguments
import com.sbg.rpg.metadata.MetadataCreator
import com.sbg.rpg.packer.SpriteSheetPacker
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths
import java.util.*

class SpriteSheetProcessor {
    private val logger = LogManager.getLogger(SpriteSheetProcessor::class.simpleName)

    private val metadataCreator: MetadataCreator
    private val spriteSheetUnpacker: SpriteSheetUnpacker
    private val spriteSheetPacker: SpriteSheetPacker

    init {
        metadataCreator = MetadataCreator()
        spriteSheetUnpacker = SpriteSheetUnpacker()
        spriteSheetPacker = SpriteSheetPacker()
    }

    /**
     * TODO: Write me
     */
    fun processSpriteSheets(commandLineArguments: CommandLineArguments) {
        val spriteSheetsWithMetadata = ArrayList<ProcessedSpriteSheet>()

        for (rawSpriteSheetPath in commandLineArguments.spriteSheetPaths) {
            logger.info("Working on $rawSpriteSheetPath")
            val spriteSheetPath = Paths.get(rawSpriteSheetPath)!!.toAbsolutePath()!!

            logger.debug("Unpacking sprites")
            val sprites = spriteSheetUnpacker.unpack(spriteSheetPath)

            logger.debug("Packing sprites")
            val (packedSpriteSheet, spritesBounds) = spriteSheetPacker.packSprites(sprites)

            logger.debug("Creating ${commandLineArguments.metadataOutputFormat} metadata")
            val metadata = when(commandLineArguments.metadataOutputFormat) {
                "json"                    -> metadataCreator.createJsonMetadata(spritesBounds)
                in arrayOf("yaml", "yml") -> metadataCreator.createYamlMetadata(spritesBounds)
                "txt"                     -> metadataCreator.createTextMetadata(spritesBounds)
                else                      -> throw IllegalArgumentException("Metadata Output Format must be one of {yaml, json, txt}")
            }

            spriteSheetsWithMetadata.add(ProcessedSpriteSheet(packedSpriteSheet, metadata))
        }
    }
}