package com.sbg.rpg.metadata

import java.nio.file.Path
import java.nio.file.Files
import com.google.common.base.Preconditions
import java.nio.charset.StandardCharsets
import com.google.common.base.Splitter

data class TextParseException(message: String = "", cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * <h2>Converts a Metadata .txt file into a .yaml file.</h2>
 *
 * <b>Format of a metadata text file:</b>
 * <pre>
 *      <index>: <x> <y> <width> <height>
 *      ...
 *
 *      <name>: <frame1> <frame2> ... <frameN>
 *      ...
 * </pre>
 *
 * <p>Be aware that a blank line (between ... and <name>) is the separator between
 * frame and animation entries. Trailing newspaces are ok, but any others will break
 * the parsing.</p>
 *
 * <b>Output format:</b>
 * <pre>
 *      Frames:
 *        - Index: <index>
 *          Bounds: [<x>, <y>, <width>, <height>]
 *        ...
 *      Animations:
 *        - Name: <name<
 *          Frames: [<frame1>, <frame2>, ..., <frameN>
 * </pre>
 *
 * @param metadata path to metadata file for parsing
 * @returns formatted .yaml string of metadata contents
 * @throws TextParseException if an error occurs parsing a frame or animation entry
 * @throws IllegalArgumentException if the metadata file does not exist
 * @throws IOException if the file could not be accessed or read
 */
fun convertToYaml(metadata: Path): String {
    Preconditions.checkArgument(Files.exists(metadata),
            "The file ${metadata.getFileName()} does not exist")

    val lines = Files.readAllLines(metadata, StandardCharsets.UTF_8).map { it.trim() }
    if (lines.all { it.isEmpty() })
        return ""

    val yamlFrameEntries = gatherTextFrameEntries(lines)
            .map(::split)
            .map(::toFrameEntry)
            .sortBy { it.index }
            .map { toYaml(it) }

    val yamlAnimationEntries = gatherTextAnimationEntries(lines)
            .map(::split)
            .map(::toAnimationEntry)
            .map { toYaml(it) }

    return buildCompleteYaml(yamlFrameEntries, yamlAnimationEntries)
}

private fun gatherTextFrameEntries(lines: List<String>): List<String> {
    return lines.takeWhile { it.isNotEmpty() }
}

private fun gatherTextAnimationEntries(lines: List<String>): List<String> {
    return lines.dropWhile { it.isNotEmpty() }.drop(1).filter { it.isNotEmpty() }
}

private fun split(line: String): List<String> {
    val normalizedEntry = line.replace("=", " ")
    return Splitter.on(' ')!!
            .trimResults()!!
            .omitEmptyStrings()!!
            .split(normalizedEntry)!!
            .toList()
}

private data class FrameEntry(val index: Int, val bounds: List<Int>)
private data class AnimationEntry(val name: String, val frames: List<Int>)

private fun toFrameEntry(entryPieces: List<String>): FrameEntry {
    try {
        val frameIndex = entryPieces.first!!
        val frameBounds = entryPieces.drop(1).map { it.toInt() }

        return FrameEntry(frameIndex.toInt(), frameBounds)
    } catch (e: Exception) {
        throw TextParseException("Could not parse a Frame entry.", e)
    }
}

private fun toAnimationEntry(entryPieces: List<String>): AnimationEntry {
    try {
        val animationName = entryPieces.first!!
        val animationFrames = entryPieces.drop(1).map { it.toInt() }

        return AnimationEntry(animationName, animationFrames)
    } catch (e: Exception) {
        throw TextParseException("Could not parse an Animation entry.", e)
    }
}

private fun toYaml(frameEntry: FrameEntry): String {

    return with(StringBuilder()) {
        append("  - Index: ${frameEntry.index}\n")
        append("    Bounds: [")
        append("${frameEntry.bounds.makeString(", ")}]\n")
        toString()
    }
}

private fun toYaml(animationEntry: AnimationEntry): String {

    return with(StringBuilder()) {
        append("  - Name: ${animationEntry.name}\n")
        append("    Frames: [")
        append("${animationEntry.frames.makeString(", ")}]\n")
        toString()
    }
}

private fun buildCompleteYaml(yamlFrameEntries: List<String>,
                              yamlAnimationEntries: List<String>): String {

    fun appendYamlEntries(builder: StringBuilder,
                          sectionName: String,
                          entries: List<String>) {
        builder.append("${sectionName}\n")
        entries.forEach { builder.append(it) }
    }

    val completeYamlBuilder = StringBuilder()

    appendYamlEntries(completeYamlBuilder, "Frames:", yamlFrameEntries)
    if (yamlAnimationEntries.isNotEmpty())
        appendYamlEntries(completeYamlBuilder, "Animations:", yamlAnimationEntries)

    return completeYamlBuilder.toString()
}

