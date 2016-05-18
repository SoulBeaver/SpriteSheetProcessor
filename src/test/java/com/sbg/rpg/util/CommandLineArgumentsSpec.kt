package com.sbg.rpg.util

import com.sbg.rpg.cli.CommandLineArguments
import com.beust.jcommander.JCommander
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.beust.jcommander.ParameterException
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFailsWith

class CommandLineArgumentsSpec: Spek() { init {
    given("Command line arguments") {
        val cla = CommandLineArguments()

        on("setting parameters using the verbose names") {
            val args: Array<String?> = arrayOf("-verbose",
                                             "-metadata-output-format", "json",
                                             "-export-folder", "target",
                                             "unpack/SingleSprite.png")

            it("the parameters are correctly set") {
                JCommander(cla, *args)

                assertTrue(cla.verbose)
                assertFalse(cla.debugMode)
                assertEquals("json", cla.metadataOutputFormat)
            }
        }

        on("setting parameters using the short names") {
            val args: Array<String?> = arrayOf("-v",
                                             "-mof", "json",
                                             "-e", "target",
                                             "unpack/SingleSprite.png")

            it("the parameters are correctly set") {
                JCommander(cla, *args)

                assertTrue(cla.verbose)
                assertFalse(cla.debugMode)
                assertEquals("json", cla.metadataOutputFormat)
            }
        }

        on("using an empty metadata output format value") {
            val args: Array<String?> = arrayOf("-mof", "",
                                             "-e", "target",
                                             "unpack/SingleSprite.png")

            it("a ParameterException is thrown") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("using an invalid metadata output format") {
            val args: Array<String?> = arrayOf("-mof", "python",
                                             "-e", "target",
                                             "unpack/SingleSprite.png")

            it("a ParameterException is thrown") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("using a valid, uppercased output format") {
            val args: Array<String?> = arrayOf("-mof", "YAML",
                                             "-e", "target",
                                             "unpack/SingleSprite.png")

            it("ignores casing and parses argument") {
                JCommander(cla, *args)
            }
        }

        on("forgetting the export folder") {
            val args: Array<String?> = arrayOf("unpack/SingleSprite.png")

            it("throws a ParameterException") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("not including any sprite sheets") {
            val args: Array<String?> = arrayOf("-e", "target")

            it("throws a ParameterException") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }

        on("including a non-existent export folder") {
            val args: Array<String?> = arrayOf("-e", "C:/Rawr/Pssshhhoooooooommmasdf")

            it("throws ParameterException") {
                assertFailsWith<ParameterException> {
                    JCommander(cla, *args)
                }
            }
        }
    }
}}