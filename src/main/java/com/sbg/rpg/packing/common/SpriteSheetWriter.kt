package com.sbg.rpg.packing.common

import java.awt.image.RenderedImage
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

interface ISpriteSheetWriter {

    fun write(source: RenderedImage, extension: String, path: Path)

    fun writeMetadata(metadata: String, path: Path)
}

class SpriteSheetWriter: ISpriteSheetWriter {
    override fun write(source: RenderedImage, extension: String, path: Path) {
        ImageIO.write(source, extension, path.toFile())
    }

    override fun writeMetadata(metadata: String, path: Path) {
        Files.write(path, metadata.toByteArray(Charsets.UTF_8))
    }
}