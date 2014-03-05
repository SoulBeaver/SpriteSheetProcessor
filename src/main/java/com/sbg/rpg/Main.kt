package com.sbg.rpg

import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.io.ByteArrayInputStream
import com.sbg.rpg.metadata.convertToYaml

fun main(args: Array<String>) {
    // TODO: Usage example
    // TODO: Error handling
    for (arg in args) {
        val metadata = Paths.get(arg)!!
        val yaml = convertToYaml(metadata)

        write(yaml, withYamlExtension(metadata))
    }
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