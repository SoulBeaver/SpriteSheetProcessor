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
package com.sbg.rpg.javafx

import javafx.application.Application
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import tornadofx.App
import tornadofx.View

/**
 * Launches a user-friendly GUI for further sprite sheet processing.
 */
class SpriteSheetProcessorApp : App() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorApp::class.simpleName)

    override val primaryView = SpriteSheetProcessorView::class

    init {

    }
}

fun main(args: Array<String>) {
    Application.launch(SpriteSheetProcessorApp::class.java, *args)
}