package com.sbg.rpg.cli

import com.beust.jcommander.Parameter
import java.util.ArrayList

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