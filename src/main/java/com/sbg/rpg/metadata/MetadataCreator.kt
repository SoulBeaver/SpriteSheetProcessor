package com.sbg.rpg.metadata

import java.awt.Image
import java.awt.Rectangle
import com.google.gson.Gson
import org.yaml.snakeyaml.Yaml
import com.sbg.rpg.packer.SpriteBounds
import com.google.common.base.Joiner

fun createJsonMetadata(spritesBounds: List<SpriteBounds>): String {
    return Gson().toJson(spritesBounds)!!
}

fun createYamlMetadata(spritesBounds: List<SpriteBounds>): String {
    return Yaml().dump(spritesBounds)!!
}

fun createTextMetadata(spritesBounds: List<SpriteBounds>): String {
    val entries = spritesBounds.map {
        "${it.frame}=${it.bounds.x} ${it.bounds.y} ${it.bounds.width} ${it.bounds.height}"
    }

    return Joiner.on(System.lineSeparator())!!.join(entries)!!
}