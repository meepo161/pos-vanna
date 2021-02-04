package ru.avem.posvanna.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import javafx.stage.Modality
import ru.avem.posvanna.controllers.MainViewController
import ru.avem.posvanna.entities.*
import ru.avem.posvanna.utils.callKeyBoard
import ru.avem.posvanna.view.Styles.Companion.extraHard
import ru.avem.posvanna.view.Styles.Companion.megaHard
import ru.avem.posvanna.view.Styles.Companion.stopStart
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

    var textFieldTimeCycle: TextField by singleAssign()

    var labelTimeRemaining: Label by singleAssign()
    var labelTestStatus: Label by singleAssign()

    var buttonStart: Button by singleAssign()
    var buttonStop: Button by singleAssign()
    var checkBoxTest1: CheckBox by singleAssign()
    var checkBoxTest2: CheckBox by singleAssign()
    var checkBoxTest3: CheckBox by singleAssign()

    override fun onBeforeShow() {
    }

    override fun onDock() {
    }

    override val root = borderpane {
        maxWidth = 1920.0
        maxHeight = 1000.0
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
                    item("Протоколы") {
                        action {
                            find<ProtocolListWindow>().openModal(
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
                        vbox(spacing = 0.0) {
                            hbox(spacing = 16.0) {
                                alignmentProperty().set(Pos.CENTER)
                                label("Количество циклов:").addClass(extraHard)
                                textFieldTimeCycle = textfield {
                                    callKeyBoard()
                                    text = ""
                                    prefWidth = 100.0
                                    alignment = Pos.CENTER
                                }.addClass(extraHard)
                            }
                            tableview(controller.tableValuesTestTime) {

                                minHeight = 346.0
                                maxHeight = 346.0
                                minWidth = 500.0
                                prefWidth = 500.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

                                onEditStart {
                                    callKeyBoard()
                                }
                                column("", TableValuesTestTime::descriptor.getter)
                                column("Нагрев, мин", TableValuesTestTime::start.getter).makeEditable()
                                column("Пауза, мин", TableValuesTestTime::pause.getter).makeEditable()
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

                            checkBoxTest1 = checkbox("1-я лопасть") {
                            }.addClass(extraHard)
                            tableview(controller.tableValuesTest1) {
                                minHeight = 346.0
                                maxHeight = 346.0
                                minWidth = 400.0
                                prefWidth = 400.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                mouseTransparentProperty().set(true)
                                column("", TableValuesTest1::descriptor.getter)
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
                                minWidth = 400.0
                                prefWidth = 400.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                mouseTransparentProperty().set(true)
                                column("", TableValuesTest2::descriptor.getter)
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
                                minWidth = 400.0
                                prefWidth = 400.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                mouseTransparentProperty().set(true)
                                column("", TableValuesTest3::descriptor.getter)
                                column("t, °C", TableValuesTest3::section31t.getter)
                            }
                        }
                    }
                    hbox(spacing = 16.0) {
                        anchorpaneConstraints {
                            leftAnchor = 16.0
                            rightAnchor = 16.0
                            topAnchor = 16.0
                            bottomAnchor = 16.0
                        }
                        alignmentProperty().set(Pos.CENTER)
                        tableview(controller.tableValuesTest21) {
                            minHeight = 96.0
                            maxHeight = 96.0
                            minWidth = 500.0
                            prefWidth = 500.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("U, В", TableValuesTest21::voltage.getter)
                            column("I, А", TableValuesTest21::ampere.getter)
                        }
                        tableview(controller.tableValuesTest22) {
                            minHeight = 96.0
                            maxHeight = 96.0
                            minWidth = 500.0
                            prefWidth = 500.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("U, В", TableValuesTest22::voltage.getter)
                            column("I, А", TableValuesTest22::ampere.getter)
                        }
                        tableview(controller.tableValuesTest23) {
                            minHeight = 96.0
                            maxHeight = 96.0
                            minWidth = 500.0
                            prefWidth = 500.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("U, В", TableValuesTest23::voltage.getter)
                            column("I, А", TableValuesTest23::ampere.getter)
                        }
                    }
                    hbox {
                        alignmentProperty().set(Pos.CENTER)
                        anchorpane {
                            scrollpane {
//                            anchorpaneConstraints {
//                                leftAnchor = 0.0
//                                rightAnchor = 0.0
//                                topAnchor = 0.0
//                                bottomAnchor = 0.0
//                            }
                                minHeight = 200.0
                                maxHeight = 200.0
                                prefHeight = 200.0
                                minWidth = 1800.0
                                minWidth = 1800.0
                                prefWidth = 1800.0
                                vBoxLog = vbox {
                                }.addClass(megaHard)

                                vvalueProperty().bind(vBoxLog.heightProperty())
                            }
                        }
                    }

                    hbox(spacing = 16) {
                        alignment = Pos.CENTER
                        labelTestStatus = label("")
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
                        }.addClass(stopStart)
                        buttonStop = button("Остановить") {
                            prefWidth = 640.0
                            prefHeight = 128.0
                            action {
                                controller.handleStopTest()
                            }
                        }.addClass(stopStart)
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
