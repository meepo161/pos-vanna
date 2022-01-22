package ru.avem.posvanna.app

import javafx.scene.image.Image
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import ru.avem.posvanna.database.validateDB
import ru.avem.posvanna.view.AuthorizationView
import ru.avem.posvanna.view.Styles
import tornadofx.App
import tornadofx.FX
import kotlin.system.exitProcess

class Pos : App(AuthorizationView::class, Styles::class) {

    companion object {
        var isAppRunning = true
    }

    override fun init() {
        validateDB()
    }

    override fun start(stage: Stage) {
        stage.isFullScreen = true
        stage.isResizable = false
//        stage.initStyle(StageStyle.TRANSPARENT)
        stage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        super.start(stage)
        FX.primaryStage.icons += Image("icon.png")
    }


    override fun stop() {
        isAppRunning = false
        exitProcess(0)
    }
}
