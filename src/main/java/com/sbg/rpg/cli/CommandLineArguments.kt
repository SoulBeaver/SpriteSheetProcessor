package com.sbg.rpg.cli

import com.beust.jcommander.Parameter
import java.util.ArrayList
import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import java.util.Arrays
import kotlin.properties.Delegates

data class CommandLineArguments {
    Parameter(description = "Sprite sheets to (un)pack",
              required= true)
    val spriteSheetPaths = ArrayList<String>()

    Parameter(names = array("-metadata-output-format", "-mof"),
              description = "Structure of the metadata file. Any of {json, yaml, txt}",
              validateWith = javaClass<MetadataOutputFormatValidator>())
    val metadataOutputFormat = "yaml"

    Parameter(names = array("-verbose", "-v"),
              description = "Turn on debug statements, but without logging to file")
    val verbose = false

    Parameter(names = array("-debug", "-d"),
              description = "Turn on debug statements with logging to file")
    val debugMode = false

    Parameter(names = array("-export-folder", "-e"),
              description = "Where to export the new files.",
              required = true)
    val exportFolder: String = ""
}

class MetadataOutputFormatValidator: IParameterValidator {
    val acceptedOutputFormats = listOf("json", "yaml", "txt")

    override fun validate(name: String?, value: String?) {
        if (name == null || value == null)
            throw ParameterException("Metadata output format may not be null.")

        val trimmedValue = value.trim().toLowerCase()
        if (!acceptedOutputFormats.contains(trimmedValue))
            throw ParameterException("Accepted metadata output formats:  {json, yaml, txt}, received:  $value")
    }
}