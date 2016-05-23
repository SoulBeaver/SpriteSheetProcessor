package com.sbg.rpg.ui

import javafx.event.ActionEvent
import javafx.fxml.FXML
import org.apache.logging.log4j.LogManager
import tornadofx.Controller

class SpriteSheetProcessorController: Controller() {
    private val logger = LogManager.getLogger(SpriteSheetProcessorController::class.simpleName)

    init {

    }

    /**
     * OnClick event that's triggered when the user wants to combine one or more sprites into one.
     */
    @FXML
    fun onCombineSelected(e: ActionEvent) {
        logger.debug("onCombineSelected")
    }

    /**
     * OnClick event that's triggered when the user wants to separate one or more sprites into one.
     */
    @FXML
    fun onSeparateSelected(e: ActionEvent) {
        logger.debug("onSeparateSelected")
    }

    /**
     * OnClick event that's triggered when the user wishes to hide the tutorial panel.
     */
    @FXML
    fun onTutorialCompleted(e: ActionEvent) {
        logger.debug("onTutorialCompleted")
    }

    /**
     * OnClick event that's triggered when the user wants to pack the sprites into a sprite sheet.
     */
    @FXML
    fun onPackSpriteSheet(e: ActionEvent) {
        logger.debug("onPackSpriteSheet")
    }
}
