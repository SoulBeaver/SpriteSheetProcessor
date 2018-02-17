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
package com.sbg.rpg.console

import com.beust.jcommander.Parameter
import java.util.ArrayList

data class CommandLineArguments(
        @Parameter(description = "Sprite sheets to (un)pack",
                required = true)
        val spriteSheetPaths: List<String> = ArrayList(),

        @Parameter(names = ["-pack", "-p"],
                description = "Create a sprite sheet with metadata file, any of {yaml, json, txt}",
                validateWith = MetadataOutputFormatValidator::class)
        val packSpriteSheets: String = "none",

        @Parameter(names = ["-verbose", "-v"],
                description = "Turn on debug statements, but without logging to file")
        val verbose: Boolean = false,

        @Parameter(names = ["-debug", "-d"],
                description = "Turn on debug statements with logging to file")
        val debugMode: Boolean = false,

        @Parameter(names = ["-export-folder", "-e"],
                description = "Where to export the new files.",
                required = true,
                validateWith = FolderExistsValidator::class)
        val exportFolder: String = "",

        @Parameter(names = ["-fail-fast", "-ff"],
                description = "Terminate immediately if any of the sprite sheets could not be loaded.")
        val failFast: Boolean = false,

        @Parameter(names = ["-help", "-h"],
                description = "This help message.",
                help = true)
        val help: Boolean = false
)