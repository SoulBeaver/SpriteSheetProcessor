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
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.appender.FileAppender

fun main(args: Array<String?>) {
    val commandLineArguments = CommandLineArguments()
    JCommander(commandLineArguments).parse(*args)

    if (commandLineArguments.verbose || commandLineArguments.debugMode)
        enableVerboseOutput()
    if (commandLineArguments.debugMode)
        enableLoggingToFile()

    // 1) Unpack:  Path -> List<Image>
    // 2) Create metadata:  List<Image> -> String {yaml, json, txt}
    // 3) Pack:  List<Image> -> Image
}

private fun enableVerboseOutput() {
    val loggerContext = LogManager.getContext(false) as LoggerContext
    val configuration = loggerContext.getConfiguration()!!
    configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)!!.setLevel(Level.DEBUG)

    loggerContext.updateLoggers(configuration)
}

private fun enableLoggingToFile() {
    val loggerContext = LogManager.getContext(false) as LoggerContext
    val configuration = loggerContext.getConfiguration()!!
    configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)!!.setLevel(Level.DEBUG)

    // TODO: Add file appender
    // configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).addAppender(FileAppender())
}