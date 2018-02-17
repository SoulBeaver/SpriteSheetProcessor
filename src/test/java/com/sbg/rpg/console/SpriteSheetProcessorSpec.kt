package com.sbg.rpg.console

import com.sbg.rpg.packing.image.SpriteSheetWriter
import com.sbg.rpg.packing.unpacker.SpriteSheetUnpacker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.Paths
import kotlin.test.assertFailsWith

object SpriteSheetProcessorSpec: Spek({
    given("an unpacking processor") {
        val unpacker = mockk<SpriteSheetUnpacker>()
        val writer = mockk<SpriteSheetWriter>()

        val unpackingProcessor = SpriteSheetUnpackingProcessor(unpacker, writer)

        on("trying to process a non-existing sprite sheet") {
            val cla = CommandLineArguments(
                    spriteSheetPaths = listOf("doesNotExist"),
                    exportFolder = "target",
                    failFast = true
            )

            it ("should fail with an IllegalArgumentException") {
                assertFailsWith(IllegalArgumentException::class) {
                    unpackingProcessor.processSpriteSheets(cla)
                }
            }
        }

        on("trying to process multiple sprite sheets without the -ff flag") {
            val spriteSheetPath = Paths.get(this.javaClass.classLoader.getResource("console/ManySprites.png").toURI())

            val cla = CommandLineArguments(
                    spriteSheetPaths = listOf("doesNotExist1", "doesNotExist2", spriteSheetPath.toAbsolutePath().toString()),
                    exportFolder = "target",
                    failFast = false
            )

            it ("should ignore all missing files") {
                every { unpacker.unpack(any()) } returns emptyList()

                unpackingProcessor.processSpriteSheets(cla)

                verify(exactly = 1) { unpacker.unpack(any()) }
            }
        }

        on("trying to process multiple sprite sheets with the -ff flag") {
            val cla = CommandLineArguments(
                    spriteSheetPaths = listOf("doesNotExist1", "doesNotExist2", "doesNotExist3"),
                    exportFolder = "target",
                    failFast = true
            )

            it ("should fail the entire batch without processing a single sheet") {
                assertFailsWith(IllegalArgumentException::class) {
                    unpackingProcessor.processSpriteSheets(cla)
                }
            }
        }
    }
})