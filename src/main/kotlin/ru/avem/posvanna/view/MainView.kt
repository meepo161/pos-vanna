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
import ru.avem.posvanna.utils.*
import ru.avem.posvanna.view.Styles.Companion.extraHard
import ru.avem.posvanna.view.Styles.Companion.megaHard
import ru.avem.posvanna.view.Styles.Companion.stopStart
import tornadofx.*
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime


class MainView : View("Комплексный стенд для испытания ПОС лопасти несущего винта") {
    override val configPath: Path = Paths.get("./app.conf")

    private val controller: MainViewController by inject()

    var mainMenubar: MenuBar by singleAssign()
    var comIndicate: Circle by singleAssign()
    var vBoxLog: VBox by singleAssign()

    val checkBoxIntBind = SimpleIntegerProperty() //TODO переименовать нормально

    var tfOperator: TextField by singleAssign()

    var tfCipher1: TextField by singleAssign()
    var tfProductNumber1: TextField by singleAssign()

    var tfCipher2: TextField by singleAssign()
    var tfProductNumber2: TextField by singleAssign()

    var tfCipher3: TextField by singleAssign()
    var tfProductNumber3: TextField by singleAssign()

    var textFieldTimeCycle: TextField by singleAssign()
    var tableViewTestTime: TableView<TableValuesTestTime> by singleAssign()
    var tableViewTestTimePause: TableView<TableValuesTestTimePause> by singleAssign()
    var tableViewTest1: TableView<TableValuesTest1> by singleAssign()
    var tableViewTest2: TableView<TableValuesTest2> by singleAssign()
    var tableViewTest3: TableView<TableValuesTest3> by singleAssign()

    var labelTimeRemaining: Label by singleAssign()
    var labelTestStatus: Label by singleAssign()
    var labelTestStatusEnd1: Label by singleAssign()
    var labelTestStatusEnd2: Label by singleAssign()
    var labelTestStatusEnd3: Label by singleAssign()

    var buttonStart: Button by singleAssign()
    var buttonStop: Button by singleAssign()
    var checkBoxTest1: CheckBox by singleAssign()
    var checkBoxTest2: CheckBox by singleAssign()
    var checkBoxTest3: CheckBox by singleAssign()


    var textFieldMaxTemp: TextField by singleAssign()

    override fun onBeforeShow() {
        tableViewTest1.isDisable = true
        tableViewTest2.isDisable = true
        tableViewTest3.isDisable = true
        checkBoxTest1.isSelected = false
        checkBoxTest2.isSelected = false
        checkBoxTest3.isSelected = false
    }

    override fun onDock() {
    }

    @ExperimentalTime
    override val root = borderpane {
        maxWidth = 1920.0
        maxHeight = 1000.0
        top {
            mainMenubar = menubar {
                menu("Меню") {
                    item("Сменить пользователя") {
                        action {
                            replaceWith<AuthorizationView>()
                        }
                    }
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
                    item("Пользователи") {
                        action {
                            find<UserEditorWindow>().openModal(
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
                            soundError()
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
                                    action {
                                        showAllTime()
                                    }
                                }.addClass(extraHard)
                            }
                            tableViewTestTime = tableview(controller.tableValuesTestTime) {
                                minHeight = 410.0
                                maxHeight = 410.0
                                minWidth = 400.0
                                prefWidth = 400.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

                                onEditStart {
                                    callKeyBoard()
                                }
                                onEditCommit {
                                    showAllTime()
                                }
                                column("Секция", TableValuesTestTime::descriptor.getter)
                                column("Нагрев, сек", TableValuesTestTime::start.getter).makeEditable()
                            }
                        }
                        vbox(spacing = 21.0) {
                            label("")
                            tableViewTestTimePause = tableview(controller.tableValuesTestTimePause) {
                                minHeight = 126.0
                                maxHeight = 126.0
                                minWidth = 200.0
                                prefWidth = 200.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

                                onEditStart {
                                    callKeyBoard()
                                }
                                onEditCommit {
                                    showAllTime()
                                }
                                column("Пауза, сек", TableValuesTestTimePause::pause.getter).makeEditable()
                            }
                        }
                        vbox(spacing = 4.0) {
                            anchorpaneConstraints {
                                leftAnchor = 16.0
                                rightAnchor = 16.0
                                topAnchor = 16.0
                                bottomAnchor = 16.0
                            }
                            alignmentProperty().set(Pos.CENTER)

                            checkBoxTest1 = checkbox("1-я лопасть") {
                                action {
                                    style = if (isSelected) {
                                        "-fx-background-color: #991400;"
                                    } else {
                                        ""
                                    }
                                    tableViewTest1.isDisable = !isSelected
                                }
                            }.addClass(extraHard)
                            vbox(spacing = 4.0, alignment = Pos.CENTER) {
                                tfCipher1 = textfield {
                                    prefWidth = 200.0
                                    callKeyBoard()
                                    alignment = Pos.CENTER
                                    promptText = "Шифр изделия"
                                }
                                tfProductNumber1 = textfield {
                                    prefWidth = 200.0
                                    callKeyBoard()
                                    alignment = Pos.CENTER
                                    promptText = "Номер изделия"
                                }
                            }
                            tableViewTest1 = tableview(controller.tableValuesTest1) {
                                minHeight = 410.0
                                maxHeight = 410.0
                                minWidth = 300.0
                                prefWidth = 300.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                mouseTransparentProperty().set(true)
                                column("Секция", TableValuesTest1::descriptor.getter)
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
                                action {
                                    style = if (isSelected) {
                                        "-fx-background-color: #991400;"
                                    } else {
                                        ""
                                    }
                                    tableViewTest2.isDisable = !isSelected
                                }
                            }.addClass(extraHard)
                            vbox(spacing = 4.0, alignment = Pos.CENTER) {
                                tfCipher2 = textfield {
                                    prefWidth = 200.0
                                    callKeyBoard()
                                    alignment = Pos.CENTER
                                    promptText = "Шифр изделия"
                                }
                                tfProductNumber2 = textfield {
                                    prefWidth = 200.0
                                    callKeyBoard()
                                    alignment = Pos.CENTER
                                    promptText = "Номер изделия"
                                }
                            }
                            tableViewTest2 = tableview(controller.tableValuesTest2) {
                                minHeight = 410.0
                                maxHeight = 410.0
                                minWidth = 300.0
                                prefWidth = 300.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                mouseTransparentProperty().set(true)
                                column("Секция", TableValuesTest2::descriptor.getter)
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
                                action {
                                    style = if (isSelected) {
                                        "-fx-background-color: #991400;"
                                    } else {
                                        ""
                                    }
                                    tableViewTest3.isDisable = !isSelected
                                }
                            }.addClass(extraHard)
                            vbox(spacing = 4.0, alignment = Pos.CENTER) {
                                tfCipher3 = textfield {
                                    prefWidth = 200.0
                                    callKeyBoard()
                                    alignment = Pos.CENTER
                                    promptText = "Шифр изделия"
                                }
                                tfProductNumber3 = textfield {
                                    prefWidth = 200.0
                                    callKeyBoard()
                                    alignment = Pos.CENTER
                                    promptText = "Номер изделия"
                                }
                            }
                            tableViewTest3 = tableview(controller.tableValuesTest3) {
                                minHeight = 410.0
                                maxHeight = 410.0
                                minWidth = 300.0
                                prefWidth = 300.0
                                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                                mouseTransparentProperty().set(true)
                                column("Секция", TableValuesTest3::descriptor.getter)
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
                        vbox(spacing = 16.0) {
                            label("Макс. температура").addClass(Styles.maxTemp)
                            textFieldMaxTemp = textfield {
                                callKeyBoard()
                                prefWidth = 100.0
                                alignment = Pos.CENTER
                            }.addClass(Styles.maxTemp)
                        }
                        tableview(controller.tableValuesWaterTemp) {
                            minHeight = 120.0
                            maxHeight = 120.0
                            minWidth = 180.0
                            prefWidth = 180.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("t воды, °C", TableValuesWaterTemp::waterTemp.getter)
                        }
                        tableview(controller.tableValuesTest21) {
                            minHeight = 120.0
                            maxHeight = 120.0
                            minWidth = 400.0
                            prefWidth = 400.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("UA, В", TableValuesTest21::voltage.getter)
                            column("IA, А", TableValuesTest21::ampere.getter)
                        }
                        tableview(controller.tableValuesTest22) {
                            minHeight = 120.0
                            maxHeight = 120.0
                            minWidth = 400.0
                            prefWidth = 400.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("UB, В", TableValuesTest22::voltage.getter)
                            column("IB, А", TableValuesTest22::ampere.getter)
                        }
                        tableview(controller.tableValuesTest23) {
                            minHeight = 120.0
                            maxHeight = 120.0
                            minWidth = 400.0
                            prefWidth = 400.0
                            columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                            mouseTransparentProperty().set(true)
                            column("UC, В", TableValuesTest23::voltage.getter)
                            column("IC, А", TableValuesTest23::ampere.getter)
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
                                minHeight = 100.0
                                maxHeight = 100.0
                                prefHeight = 100.0
                                minWidth = 1200.0
                                minWidth = 1200.0
                                prefWidth = 1200.0
                                vBoxLog = vbox {
                                }.addClass(megaHard)

                                vvalueProperty().bind(vBoxLog.heightProperty())
                            }
                        }
                    }

                    hbox(spacing = 16) {
                        alignment = Pos.CENTER
                        labelTestStatusEnd1 = label("")
                        labelTestStatusEnd2 = label("")
                        labelTestStatusEnd3 = label("")
                        labelTestStatus = label("")
                        labelTimeRemaining = label("")
                    }.addClass(extraHard)
                    hbox(spacing = 16) {
                        alignment = Pos.CENTER
                        buttonStart = button("Запустить") {
                            prefWidth = 640.0
                            prefHeight = 64.0
                            action {
                                controller.handleStartTest()
                            }
                        }.addClass(stopStart)
                        buttonStop = button("Остановить") {
                            prefWidth = 640.0
                            prefHeight = 64.0
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
            label(" Связь со ПР") {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                    marginBottom = 8.0
                }
            }
        }
    }.addClass(Styles.blueTheme, megaHard)

    @ExperimentalTime
    private fun showAllTime() {
        try {
            labelTimeRemaining.text = toHHmmss(
                (((controller.tableValuesTestTime[0].start.value.replace(",", ".")
                    .toDouble())
                        + (controller.tableValuesTestTime[1].start.value.replace(",", ".")
                    .toDouble())
                        + (controller.tableValuesTestTime[2].start.value.replace(",", ".")
                    .toDouble())
                        + (controller.tableValuesTestTime[3].start.value.replace(",", ".")
                    .toDouble())
                        + (controller.tableValuesTestTime[4].start.value.replace(",", ".")
                    .toDouble())
                        + (controller.tableValuesTestTime[5].start.value.replace(",", ".")
                    .toDouble())
                        + (controller.tableValuesTestTimePause[0].pause.value.replace(",", ".")
                    .toDouble()))
                        * textFieldTimeCycle.text.replace(",", ".")
                    .toDouble()).toLong() * 1000
            )
        } catch (e: Exception) {
            Toast.makeText("Проверьте правильность задания времени нагрева, паузы и количество циклов")
                .show(Toast.ToastType.ERROR)
        }
    }
}
