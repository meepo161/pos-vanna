package ru.avem.posvanna.app

import javafx.scene.image.Image
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import javafx.stage.StageStyle
import ru.avem.posvanna.database.validateDB
import ru.avem.posvanna.view.MainView
import ru.avem.posvanna.view.Styles
import tornadofx.App
import tornadofx.FX

class Pos : App(MainView::class, Styles::class) {

    companion object {
        var isAppRunning = true
    }

    override fun init() {
        validateDB()
    }

    override fun start(stage: Stage) {
        stage.isFullScreen = true
        stage.isResizable = true
//        stage.initStyle(StageStyle.TRANSPARENT)
        stage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        super.start(stage)
        FX.primaryStage.icons += Image("icon.png")
    }


    override fun stop() {
        isAppRunning = false
        super.stop()
    }
}
