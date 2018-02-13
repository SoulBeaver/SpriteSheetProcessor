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

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.sbg.rpg.cli.CommandLineArguments
import com.sbg.rpg.image.SpriteCutter
import com.sbg.rpg.metadata.JsonMetadataCreator
import com.sbg.rpg.metadata.TextMetadataCreator
import com.sbg.rpg.metadata.YamlMetadataCreator
import com.sbg.rpg.unpacker.SpriteSheetUnpacker
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.Level

fun main(args: Array<String>) {
    try {
        val commandLineArguments = CommandLineArguments()
        JCommander(commandLineArguments).parse(*args)

        if (commandLineArguments.verbose || commandLineArguments.debugMode)
            enableVerboseOutput()
        if (!commandLineArguments.debugMode)
            disableLoggingToFile()

        createProcessor(commandLineArguments).processSpriteSheets(commandLineArguments)
    } catch (pe: ParameterException) {
        println("Unable to start because of invalid input:\n\t${pe.message}")
        JCommander(CommandLineArguments()).usage()
    } catch (e: Exception) {
        println("Unable to recover from an exception, terminating program; exception=$e")
    }
}

private fun enableVerboseOutput() {
    val loggerContext = LogManager.getContext(false) as LoggerContext
    val configuration = loggerContext.configuration
    configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).level = Level.DEBUG

    loggerContext.updateLoggers(configuration)
}

private fun disableLoggingToFile() {
    val loggerContext = LogManager.getContext(false) as LoggerContext
    val configuration = loggerContext.configuration
    configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)!!.removeAppender("LogFile")
}

private fun createProcessor(cla: CommandLineArguments): SpriteSheetProcessor {
    val metadataCreator = when (cla.metadataOutputFormat) {
        "json" -> JsonMetadataCreator()
        "yaml" -> YamlMetadataCreator()
        "txt"  -> TextMetadataCreator()
        else   -> throw IllegalArgumentException("Expected one of {json, yaml, txt} as metadata output, but got ${cla.metadataOutputFormat}")
    }

    return SpriteSheetProcessor(metadataCreator, SpriteSheetUnpacker(SpriteCutter()))
}