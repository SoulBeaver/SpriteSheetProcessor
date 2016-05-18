package com.sbg.rpg.metadata

import org.jetbrains.spek.api.*
import java.nio.file.Paths
import kotlin.test.assertEquals

class ConverterSpec: Spek() { init {
    /*
    given("A .txt -> .yaml converter") {
        on("loading a non-existent file") {
            it("throws an IllegalArgumentException") {
                failsWith(javaClass<IllegalArgumentException>()) {
                    convertToYaml(Paths.get("Missing.txt")!!)
                }
            }
        }

        on("loading an empty file") {
            val emptyFileUrl = javaClass<ConverterSpec>().getClassLoader()!!.getResource("converter/Empty.txt")!!

            it("produces empty yaml") {
                val output = convertToYaml(Paths.get(emptyFileUrl.toURI())!!)

                assertTrue(output.isEmpty(),
                           "YAML output not empty, but:  $output")
            }
        }

        on("loading file with on frame entry") {
            val frameFileUrl = javaClass<ConverterSpec>().getClassLoader()!!.getResource("converter/Frame.txt")!!

            it("produces corresponding yaml for frame") {
                val expected = "Frames:\n" +
                               "  - Index: 0\n" +
                               "    Bounds: [157, 184, 123, 189]\n"

                val output = convertToYaml(Paths.get(frameFileUrl.toURI())!!)

                assertEquals(expected, output,
                             "The YAML did not conform to the expected output.")
            }
        }

        on("loading file with multiple frame entries") {
            val framesFileUrl = javaClass<ConverterSpec>().getClassLoader()!!.getResource("converter/Frames.txt")!!

            it("produces corresponding yaml for all frames") {
                val expected = "Frames:\n" +
                               "  - Index: 0\n" +
                               "    Bounds: [157, 184, 123, 189]\n" +
                               "  - Index: 1\n" +
                               "    Bounds: [187, 0, 177, 183]\n" +
                               "  - Index: 2\n" +
                               "    Bounds: [402, 181, 105, 189]\n"

                val output = convertToYaml(Paths.get(framesFileUrl.toURI())!!)

                assertEquals(expected, output,
                             "The YAML did not conform to the expected output.")
            }
        }

        on("loading file with frames and animations") {
            val frameAnimationFileUrl = javaClass<ConverterSpec>().getClassLoader()!!.getResource("converter/FrameAnimation.txt")!!

            it("produces corresponding yaml incorporating an animation name and number of frames") {
                val expected = "Frames:\n" +
                               "  - Index: 0\n" +
                               "    Bounds: [157, 184, 123, 189]\n" +
                               "  - Index: 1\n" +
                               "    Bounds: [187, 0, 177, 183]\n" +
                               "  - Index: 2\n" +
                               "    Bounds: [402, 181, 105, 189]\n" +
                               "Animations:\n" +
                               "  - Name: Walking\n" +
                               "    Frames: [0, 1, 2]\n"

                val output = convertToYaml(Paths.get(frameAnimationFileUrl.toURI())!!)

                assertEquals(expected, output,
                             "The YAML did not conform to the expected output.\nPerhaps the Animations section is malformed?")
            }
        }

        on("loading file with no frames but animations") {
            val onlyAnimationFileUrl = ConverterSpec::class.java.classLoader.getResource("converter/OnlyAnimation.txt")!!

            it("throws a TextParseException") {
                failsWith(TextParseException::class.java) {
                    convertToYaml(Paths.get(onlyAnimationFileUrl.toURI())!!)
                }
            }
        }

        on("loading file with trailing newlines") {
            val trailingNewlines = ConverterSpec::class.java.classLoader.getResource("converter/TrailingNewlines.txt")!!

            it("loads normally and ignores trailing newlines") {
                val expected = "Frames:\n" +
                               "  - Index: 0\n" +
                               "    Bounds: [157, 184, 123, 189]\n" +
                               "  - Index: 1\n" +
                               "    Bounds: [187, 0, 177, 183]\n" +
                               "  - Index: 2\n" +
                               "    Bounds: [402, 181, 105, 189]\n" +
                               "Animations:\n" +
                               "  - Name: Walking\n" +
                               "    Frames: [0, 1, 2]\n"

                val output = convertToYaml(Paths.get(trailingNewlines.toURI())!!)

                assertEquals(expected, output)
            }
        }

        on("Loading a file with unnatural ordering") {
            val unnaturalOrdering = ConverterSpec::class.java.classLoader.getResource("converter/UnsortedFrames.txt")!!

            it("loads and sorts frames into natural order") {
                val expected = "Frames:\n" +
                               "  - Index: 0\n" +
                               "    Bounds: [157, 184, 123, 189]\n" +
                               "  - Index: 1\n" +
                               "    Bounds: [187, 0, 177, 183]\n" +
                               "  - Index: 2\n" +
                               "    Bounds: [402, 181, 105, 189]\n" +
                               "  - Index: 10\n" +
                               "    Bounds: [187, 0, 177, 183]\n"

                val output = convertToYaml(Paths.get(unnaturalOrdering.toURI())!!)

                assertEquals(expected, output)
            }
        }
    }
    */
}}