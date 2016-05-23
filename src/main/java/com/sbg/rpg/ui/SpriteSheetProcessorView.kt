package com.sbg.rpg.ui

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Button
import org.apache.logging.log4j.LogManager
import tornadofx.View

class SpriteSheetProcessorView: View() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorView::class.simpleName)

    override val root: Parent = FXMLLoader.load(this.javaClass.classLoader.getResource("views/SpriteSheetView.fxml"))

    val controller: SpriteSheetProcessorController by inject()

    val combineButton: Button by fxid()
    val separateButton: Button by fxid()
    val packButton: Button by fxid()
    val tutorialButton: Button by fxid()


}