/*
 *  Copyright 2016 Christian Broomfield
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sbg.rpg.image

import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO

fun readImage(path: Path): BufferedImage {
    try {
        /*
         * An image is not guaranteed to contain an alpha channel.
         * Therefore we explicitly convert any loaded image to type ARGB for further processing.
         */
        return ImageIO.read(path.toFile()).toBufferedImage(BufferedImage.TYPE_INT_ARGB)
    } catch (e: Exception) {
        throw ImageReadException("Could not convert file to an image! Is this really an image?", e)
    }
}