package com.sbg.rpg.metadata

import com.sbg.rpg.packer.SpriteBounds

interface MetadataCreator {
    fun create(spriteBoundsList: List<SpriteBounds>): String
}