package com.sbg.rpg.cli

import com.beust.jcommander.Parameter
import java.util.ArrayList
import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import java.nio.file.Paths
import java.nio.file.Files

data class CommandLineArguments(
    @Parameter(description = "Sprite sheets to (un)pack",
               required= true)
    val spriteSheetPaths: ArrayList<String> = ArrayList<String>(),

    @Parameter(names = arrayOf("-metadata-output-format", "-mof"),
               description = "Structure of the metadata file. Any of {json, yaml, txt}",
               validateWith = MetadataOutputFormatValidator::class)
    val metadataOutputFormat: String = "yaml",

    @Parameter(names = arrayOf("-verbose", "-v"),
               description = "Turn on debug statements, but without logging to file")
    val verbose: Boolean = false,

    @Parameter(names = arrayOf("-debug", "-d"),
               description = "Turn on debug statements with logging to file")
    val debugMode: Boolean = false,

    @Parameter(names = arrayOf("-export-folder", "-e"),
               description = "Where to export the new files.",
               required = true,
               validateWith = FolderExistsValidator::class)
    val exportFolder: String = "",

    @Parameter(names = arrayOf("-help", "-h"),
               description = "This help message.",
               help = true)
    val help: Boolean = false
)

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

class FolderExistsValidator: IParameterValidator {
    override fun validate(name: String?, value: String?) {
        if (value == null || value.isEmpty())
            throw ParameterException("export-folder value may not be empty")

        val path = Paths.get(value)!!.toAbsolutePath()!!
        if (!Files.exists(path))
            throw ParameterException("Path $path does not exist.")
    }
}