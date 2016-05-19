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