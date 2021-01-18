package ru.avem.posvanna.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import javafx.stage.Modality
import ru.avem.posvanna.controllers.MainViewController
import ru.avem.posvanna.entities.TableValuesTest1
import ru.avem.posvanna.entities.TableValuesTest2
import ru.avem.posvanna.entities.TableValuesTest3
import ru.avem.posvanna.view.Styles.Companion.extraHard
import ru.avem.posvanna.view.Styles.Companion.megaHard
import tornadofx.*
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess


class MainView : View("Комплексный стенд для испытания ПОС лопасти несущего винта") {
    override val configPath: Path = Paths.get("./app.conf")

    private val controller: MainViewController by inject()

    var mainMenubar: MenuBar by singleAssign()
    var comIndicate: Circle by singleAssign()
    var vBoxLog: VBox by singleAssign()

    val checkBoxIntBind = SimpleIntegerProperty() //TODO переименовать нормально

    var textFieldTimeStart: TextField by singleAssign()
    var textFieldTimePause: TextField by singleAssign()
    var textFieldTimeCycle: TextField by singleAssign()

    var labelTimeRemaining: Label by singleAssign()


    var buttonStart: Button by singleAssign()
    var checkBoxTest1: CheckBox by singleAssign()
    var checkBoxTest2: CheckBox by singleAssign()
    var checkBoxTest3: CheckBox by singleAssign()

    override fun onBeforeShow() {
    }

    override fun onDock() {
    }

    override val root = borderpane {
        maxWidth = 1280.0
        maxHeight = 800.0
        top {
            mainMenubar = menubar {
                menu("Меню") {
                    item("Выход") {
                        action {
                            exitProcess(0)
                        }
                    }
                }
                menu("База данных") {
                    item("Объекты испытания") {
                        action {
                            find<ObjectTypeEditorWindow>().openModal(
                                    modality = Modality.WINDOW_MODAL,
                                    escapeClosesWindow = true,
                                    resizable = false,
                                    owner = this@MainView.currentWindow
                            )
                        }
                    }
                }
                menu("Информация") {
                    item("Версия ПО") {
                        action {
                            controller.showAboutUs()
                        }
                    }
                }
            }.addClass(megaHard)
        }
        center {
            anchorpane {
                vbox(spacing = 32.0) {
                    anchorpaneConstraints {
                        leftAnchor = 16.0
                        rightAnchor = 16.0
                        topAnchor = 16.0
                        bottomAnchor = 16.0
                    }
                    alignmentProperty().set(Pos.CENTER)

                    hbox(spacing = 16.0) {
                        alignmentProperty().set(Pos.CENTER)
                        label("Время нагрева:").addClass(extraHard)
                        textFieldTimeStart = textfield {
                            text = ""
                            prefWidth = 100.0
                            alignment = Pos.CENTER
                        }.addClass(extraHard)
                        label("мин     ").addClass(extraHard)
                        label("Время паузы:").addClass(extraHard)
                        textFieldTimePause = textfield {
                            text = ""
                            prefWidth = 100.0
                            alignment = Pos.CENTER
                        }.addClass(extraHard)
                        label("мин     ").addClass(extraHard)
                        label("Кол-во циклов:").addClass(extraHard)
                        textFieldTimeCycle = textfield {
                            text = ""
                            prefWidth = 100.0
                            alignment = Pos.CENTER
                        }.addClass(extraHard)
                    }.addClass(extraHard)
                    vbox(spacing = 16.0) {
                        anchorpaneConstraints {
                            leftAnchor = 16.0
                            rightAnchor = 16.0
                            topAnchor = 16.0
                            bottomAnchor = 16.0
                        }
                        alignmentProperty().set(Pos.CENTER)
                        vbox(spacing = 16.0) {
                            anchorpaneConstraints {
                                leftAnchor = 16.0
                                rightAnchor = 16.0
                                topAnchor = 16.0
                                bottomAnchor = 16.0
                            }
                            alignmentProperty().set(Pos.CENTER)

                            hbox(spacing = 16.0) {
                                alignmentProperty().set(Pos.CENTER)
                                vbox(spacing = 16.0) {
                                    anchorpaneConstraints {
                                        leftAnchor = 16.0
                                        rightAnchor = 16.0
                                        topAnchor = 16.0
                                        bottomAnchor = 16.0
                                    }
                                    alignmentProperty().set(Pos.CENTER)

                                    checkBoxTest1 = checkbox("1-я лопасть") {
                                    }.addClass(extraHard)
                                    tableview(controller.tableValuesTest1) {
                                        minHeight = 346.0
                                        maxHeight = 346.0
                                        minWidth = 600.0
                                        prefWidth = 600.0
                                        columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                        mouseTransparentProperty().set(true)
                                        column("", TableValuesTest1::descriptor.getter)
                                        column("U, В", TableValuesTest1::section1U.getter)
                                        column("I, А", TableValuesTest1::section1I.getter)
                                        column("t, °C", TableValuesTest1::section1t.getter)
                                    }
                                }
                                vbox(spacing = 16.0) {
                                    anchorpaneConstraints {
                                        leftAnchor = 16.0
                                        rightAnchor = 16.0
                                        topAnchor = 16.0
                                        bottomAnchor = 16.0
                                    }
                                    alignmentProperty().set(Pos.CENTER)
                                    checkBoxTest2 = checkbox("2-я лопасть") {
                                    }.addClass(extraHard)
                                    tableview(controller.tableValuesTest2) {
                                        minHeight = 346.0
                                        maxHeight = 346.0
                                        minWidth = 600.0
                                        prefWidth = 600.0
                                        columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                        mouseTransparentProperty().set(true)
                                        column("", TableValuesTest2::descriptor.getter)
                                        column("U, В", TableValuesTest2::section21U.getter)
                                        column("I, А", TableValuesTest2::section21I.getter)
                                        column("t, °C", TableValuesTest2::section21t.getter)
                                    }
                                }
                                vbox(spacing = 16.0) {
                                    anchorpaneConstraints {
                                        leftAnchor = 16.0
                                        rightAnchor = 16.0
                                        topAnchor = 16.0
                                        bottomAnchor = 16.0
                                    }
                                    alignmentProperty().set(Pos.CENTER)
                                    checkBoxTest3 = checkbox("3-я лопасть") {
                                    }.addClass(extraHard)
                                    tableview(controller.tableValuesTest3) {
                                        minHeight = 346.0
                                        maxHeight = 346.0
                                        minWidth = 600.0
                                        prefWidth = 600.0
                                        columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                        mouseTransparentProperty().set(true)
                                        column("", TableValuesTest3::descriptor.getter)
                                        column("U, В", TableValuesTest3::section31U.getter)
                                        column("I, А", TableValuesTest3::section31I.getter)
                                        column("t, °C", TableValuesTest3::section31t.getter)
                                    }
                                }
                            }
                            anchorpane {
                                scrollpane {
                                    anchorpaneConstraints {
                                        leftAnchor = 0.0
                                        rightAnchor = 0.0
                                        topAnchor = 0.0
                                        bottomAnchor = 0.0
                                    }
                                    minHeight = 209.0
                                    maxHeight = 209.0
                                    prefHeight = 209.0
                                    minWidth = 900.0
                                    minWidth = 900.0
                                    prefWidth = 900.0
                                    vBoxLog = vbox {
                                    }.addClass(megaHard)

                                    vvalueProperty().bind(vBoxLog.heightProperty())
                                }
                            }
                        }
                    }
                    hbox(spacing = 16) {
                        alignment = Pos.CENTER
                        labelTimeRemaining = label("")
                    }.addClass(extraHard)
                    hbox(spacing = 16) {
                        alignment = Pos.CENTER
                        buttonStart = button("Запустить") {
                            prefWidth = 640.0
                            prefHeight = 128.0
                            action {
                                controller.handleStartTest()
                            }
                        }.addClass(extraHard)
                    }
                }
            }
        }
        bottom = hbox(spacing = 32) {
            alignment = Pos.CENTER_LEFT
            comIndicate = circle(radius = 18) {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                    marginLeft = 8.0
                    marginBottom = 8.0
                }
                fill = c("cyan")
                stroke = c("black")
                isSmooth = true
            }
            label(" Связь со стендом") {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                    marginBottom = 8.0
                }
            }
        }
    }.addClass(Styles.blueTheme, megaHard)
}
