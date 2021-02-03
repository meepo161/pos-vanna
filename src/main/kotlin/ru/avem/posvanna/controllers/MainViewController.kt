package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.Text
import ru.avem.posvanna.app.Pos.Companion.isAppRunning
import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.entities.*
import ru.avem.posvanna.utils.LogTag
import ru.avem.posvanna.utils.State
import ru.avem.posvanna.utils.Toast
import ru.avem.posvanna.utils.sleep
import ru.avem.posvanna.view.MainView
import tornadofx.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread
import kotlin.time.ExperimentalTime


class MainViewController : Controller() {
    val view: MainView by inject()
    var position1 = ""

    @Volatile
    var isExperimentRunning: Boolean = true

    var cause: String = ""
        set(value) {
            if (value != "") {
                isExperimentRunning = false
            }
            field = value
        }

    var tableValuesTest21 = observableListOf(
        TableValuesTest21(
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        )
    )
    var tableValuesTest22 = observableListOf(
        TableValuesTest22(
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        )
    )
    var tableValuesTest23 = observableListOf(
        TableValuesTest23(
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        )
    )

    var tableValuesTest1 = observableListOf(
        TableValuesTest1(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("0.0")
        )
    )

    var tableValuesTest2 = observableListOf(
        TableValuesTest2(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("0.0")
        )
    )
    var tableValuesTest3 = observableListOf(
        TableValuesTest3(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("0.0")
        )
    )


    var tableValuesTestTime = observableList(
        TableValuesTestTime(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("1.0"),
            SimpleStringProperty("2.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("1.0"),
            SimpleStringProperty("2.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("1.0"),
            SimpleStringProperty("2.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("1.0"),
            SimpleStringProperty("2.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("1.0"),
            SimpleStringProperty("2.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("1.0"),
            SimpleStringProperty("2.0")
        )
    )

    init {
        thread(isDaemon = true) {
            runLater {
                view.buttonStop.isDisable = true
            }
            while (isAppRunning) {
                if (CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).isResponding) {
                    runLater {
                        view.comIndicate.fill = State.OK.c
                    }
                } else {
                    runLater {
                        view.comIndicate.fill = State.BAD.c
                    }
                }
                sleep(1000)
            }
        }
    }

    var isDevicesResponding: () -> Boolean = {
        true
    }

    @UseExperimental(ExperimentalTime::class)
    fun handleStartTest() {
//        if (view.textFieldTimeStart1.text.isEmpty() || !view.textFieldTimeStart1.text.isDouble()) {
//            runLater {
//                Toast.makeText("Введите время нагрева").show(Toast.ToastType.ERROR)
//            }
//        } else if (view.textFieldTimePause1.text.isEmpty() || !view.textFieldTimePause1.text.isDouble()) {
//            runLater {
//                Toast.makeText("Введите время паузы").show(Toast.ToastType.ERROR)
//            }
//        } else if (view.textFieldTimeCycle.text.isEmpty() || !view.textFieldTimeCycle.text.isDouble()) {
//            runLater {
//                Toast.makeText("Введите количество циклов").show(Toast.ToastType.ERROR)
//            }
//        } else if (!isAtLeastOneIsSelected()) {
//            runLater {
//                Toast.makeText("Выберите хотя бы одно испытание из списка").show(Toast.ToastType.ERROR)
//            }
//        } else {
        thread(isDaemon = true) {
            runLater {
                view.buttonStart.isDisable = true
                view.buttonStop.isDisable = false
                view.mainMenubar.isDisable = true
                view.checkBoxTest1.isDisable = true
                view.checkBoxTest2.isDisable = true
                view.checkBoxTest3.isDisable = true
            }

            isExperimentRunning = true
            clearTable()

            appendMessageToLog(LogTag.DEBUG, "Начало испытания")

            Test1Controller().startTest()

            appendMessageToLog(LogTag.MESSAGE, "Испытание завершено")

            isExperimentRunning = false
            runLater {
                view.buttonStart.isDisable = false
                view.buttonStop.isDisable = true
                view.mainMenubar.isDisable = false
                view.checkBoxTest1.isDisable = false
                view.checkBoxTest2.isDisable = false
                view.checkBoxTest3.isDisable = false
            }
        }
//        }
    }

    fun handleStopTest() {
        cause = "Отменено оператором"
    }

    private fun appendMessageToLog(tag: LogTag, _msg: String) {
        val msg = Text("${SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())} | $_msg")
        msg.style {
            fill = when (tag) {
                LogTag.MESSAGE -> tag.c
                LogTag.ERROR -> tag.c
                LogTag.DEBUG -> tag.c
            }
        }

        Platform.runLater {
            view.vBoxLog.add(msg)
        }
    }

    private fun isAtLeastOneIsSelected(): Boolean {
        return view.checkBoxTest1.isSelected ||
                view.checkBoxTest2.isSelected ||
                view.checkBoxTest3.isSelected
    }

    fun clearTable() {
        runLater {
            tableValuesTest2[1].section21t.value = ""
        }
    }

    fun showAboutUs() {
        Toast.makeText("Версия ПО: 1.0.0\nВерсия БСУ: 1.0.0\nДата: 30.04.2020").show(Toast.ToastType.INFORMATION)
    }


}
