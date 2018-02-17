package com.sbg.rpg.util

import com.sbg.rpg.cli.CommandLineArguments
import com.beust.jcommander.JCommander
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.beust.jcommander.ParameterException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFailsWith

class CommandLineArgumentsSpec: Spek({
    given("Command line arguments") {
        val cla = CommandLineArguments()

        on("setting parameters using the verbose names") {
            val args = arrayOf("-verbose",
                               "-pack", "json",
                               "-export-folder", "target",
                               "unpack/SingleSprite.png")

            it("the parameters are correctly set") {
                JCommander(cla, *args)

                assertTrue(cla.verbose)
                assertFalse(cla.debugMode)
                assertEquals("json", cla.packSpriteSheets)
            }
        }

        on("setting parameters using the short names") {
            val args = arrayOf("-v",
                               "-p", "json",
                               "-e", "target",
                               "unpack/SingleSprite.png")

            it("the parameters are correctly set") {
                JCommander(cla, *args)

                assertTrue(cla.verbose)
                assertFalse(cla.debugMode)
                assertEquals("json", cla.packSpriteSheets)
            }
        }

        on("using an empty metadata output format value") {
            val args = arrayOf("-p", "",
                               "-e", "target",
                               "unpack/SingleSprite.png")

            it("a ParameterException is thrown") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("using an invalid metadata output format") {
            val args = arrayOf("-p", "python",
                               "-e", "target",
                               "unpack/SingleSprite.png")

            it("a ParameterException is thrown") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("using a valid, uppercased output format") {
            val args = arrayOf("-p", "YAML",
                               "-e", "target",
                               "unpack/SingleSprite.png")

            it("ignores casing and parses argument") {
                JCommander(cla, *args)
            }
        }

        on("using the alternative yml name") {
            val args = arrayOf("-p", "yml",
                                "-e", "target",
                                "unpack/SingleSprite.png")

            it("correctly parses the command as yml") {
                JCommander(cla, *args)

                assertEquals(cla.packSpriteSheets, "yml")
            }
        }

        on("not wanting to pack sprites") {
            val args = arrayOf(
                    "-e", "target",
                    "unpack/SingleSprite.png")

            it("happily ignores packing") {
                JCommander(cla, *args)

                assertEquals(cla.exportFolder, "target")
                assertEquals(cla.spriteSheetPaths.first(), "unpack/SingleSprite.png")
            }
        }

        on("forgetting the export folder") {
            val args = arrayOf("unpack/SingleSprite.png")

            it("throws a ParameterException") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("not including any sprite sheets") {
            val args = arrayOf("-e", "target")

            it("throws a ParameterException") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("including a non-existent export folder") {
            val args = arrayOf("-e", "C:/Rawr/Pssshhhoooooooommmasdf")

            it("throws ParameterException") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("choosing the help option") {
            val args = arrayOf("-help")

            it("shows the help menu") {
                JCommander(cla, *args)
            }
        }
    }
})