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

fun main(args: Array<String?>) {
    val commandLineArguments = CommandLineArguments()
    JCommander(commandLineArguments).parse(*args)
}

private fun withYamlExtension(path: Path): Path {
    val fileName = path.getFileName().toString()
    val fileNameWithoutExtension = fileName.substring(0, fileName.indexOf('.'))

    return path.resolveSibling("${fileNameWithoutExtension}.yaml")!!
}

private fun write(yaml: String, destination: Path) {
    ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)).use {
        Files.copy(it, destination)
    }
}