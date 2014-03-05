package com.sbg.rpg

import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.io.ByteArrayInputStream
import com.sbg.rpg.metadata.convertToYaml
import com.beust.jcommander.Parameter
import java.util.ArrayList
import com.beust.jcommander.JCommander
import com.sbg.rpg.cli.CommandLineArguments
import org.apache.logging.log4j.LogManager

fun main(args: Array<String?>) {
    val logger = LogManager.getLogger("main")!!

    logger.debug("Received arguments:  $it")

    val commandLineArguments = CommandLineArguments()
    JCommander(commandLineArguments).parse(*args)
}