package com.sbg.rpg

import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.io.ByteArrayInputStream
import com.beust.jcommander.Parameter
import java.util.ArrayList
import com.beust.jcommander.JCommander
import com.sbg.rpg.cli.CommandLineArguments
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.appender.FileAppender
import com.sbg.rpg.unpacker.unpack
import com.sbg.rpg.metadata.createJsonMetadata
import com.sbg.rpg.metadata.createYamlMetadata
import com.sbg.rpg.metadata.createTextMetadata
import com.sbg.rpg.packer.packSprites
import java.awt.Image
import java.util.UUID
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

private val logger = LogManager.getLogger("Main")!!

fun main(args: Array<String?>) {
    val commandLineArguments = CommandLineArguments()
    JCommander(commandLineArguments).parse(*args)

    if (commandLineArguments.verbose || commandLineArguments.debugMode)
        enableVerboseOutput()
    if (!commandLineArguments.debugMode)
        disableLoggingToFile()

    processSpriteSheets(commandLineArguments)
}

private fun enableVerboseOutput() {
    val loggerContext = LogManager.getContext(false) as LoggerContext
    val configuration = loggerContext.getConfiguration()!!
    configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)!!.setLevel(Level.DEBUG)

    loggerContext.updateLoggers(configuration)
}

private fun disableLoggingToFile() {
    val loggerContext = LogManager.getContext(false) as LoggerContext
    val configuration = loggerContext.getConfiguration()!!
    configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)!!.removeAppender("LogFile")
}

data class SpriteSheetWithMetadata(val spriteSheet: BufferedImage, val metadata: String)

private fun processSpriteSheets(commandLineArguments: CommandLineArguments) {
    val spriteSheetsWithMetadata = ArrayList<SpriteSheetWithMetadata>()

    for (rawSpriteSheetPath in commandLineArguments.spriteSheetPaths) {
        logger.info("Working on $rawSpriteSheetPath")
        val spriteSheetPath = Paths.get(rawSpriteSheetPath)!!.toAbsolutePath()!!

        logger.debug("Unpacking sprites")
        val sprites = unpack(spriteSheetPath)

        logger.debug("Packing sprites")
        val (packedSpriteSheet, spritesBounds) = packSprites(sprites)

        logger.debug("Creating ${commandLineArguments.metadataOutputFormat} metadata")
        val metadata = when(commandLineArguments.metadataOutputFormat) {
            "json" -> createJsonMetadata(spritesBounds)
            "yaml" -> createYamlMetadata(spritesBounds)
            "txt"  -> createTextMetadata(spritesBounds)
            else   -> throw IllegalArgumentException("Metadata Output Format must be one of {yaml, json, txt}")
        }

        spriteSheetsWithMetadata.add(SpriteSheetWithMetadata(packedSpriteSheet, metadata))
    }
}