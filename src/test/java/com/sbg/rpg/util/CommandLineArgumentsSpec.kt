package com.sbg.rpg.util

import org.spek.Spek
import com.sbg.rpg.cli.CommandLineArguments
import com.beust.jcommander.JCommander
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.beust.jcommander.ParameterException
import kotlin.test.failsWith

class CommandLineArgumentsSpec: Spek() {{
    given("Command line arguments") {
        val cla = CommandLineArguments()

        on("setting parameters using the verbose names") {
            val args: Array<String?> = array("-verbose", "-metadata-output-format", "json")

            it("the parameters are correctly set") {
                JCommander(cla, *args)

                assertTrue(cla.verbose)
                assertFalse(cla.debugMode)
                assertEquals("json", cla.metadataOutputFormat)
            }
        }

        on("setting parameters using the short names") {
            val args: Array<String?> = array("-v", "-mof", "json")

            it("the parameters are correctly set") {
                JCommander(cla, *args)

                assertTrue(cla.verbose)
                assertFalse(cla.debugMode)
                assertEquals("json", cla.metadataOutputFormat)
            }
        }

        on("using an empty metadata output format value") {
            val args: Array<String?> = array("-mof", "")

            it("a ParameterException is thrown") {
                failsWith(javaClass<ParameterException>()) {
                    JCommander(cla, *args)
                }
            }
        }

        on("using an invalid metadata output format") {
            val args: Array<String?> = array("-mof", "python")

            it("a ParameterException is thrown") {
                failsWith(javaClass<ParameterException>()) {
                    JCommander(cla, *args)
                }
            }
        }

        on("using a valid, uppercased output format") {
            val args: Array<String?> = array("-mof", "YAML")

            it("ignores casing and parses argument") {
                JCommander(cla, *args)
            }
        }
    }
}}