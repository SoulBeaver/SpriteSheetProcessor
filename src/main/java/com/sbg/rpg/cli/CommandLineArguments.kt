package com.sbg.rpg.cli

import com.beust.jcommander.Parameter
import java.util.ArrayList
import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import java.util.Arrays

data class CommandLineArguments {
    Parameter
    val spriteSheetPaths = ArrayList<String>()

    Parameter(names = array("-metadata-output-format", "-mof"),
              description = "Structure of the metadata file. Any of {json, yaml, txt}",
              validateWith = javaClass<MetadataOutputFormatValidator>())
    val metadataOutputFormat = "yaml"

    Parameter(names = array("-verbose", "-v"),
              description = "Enable debug statements without logging to file")
    val verbose = false

    Parameter(names = array("-debug", "-d"),
              description = "Enable debug statements with logging to file")
    val debugMode = false
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