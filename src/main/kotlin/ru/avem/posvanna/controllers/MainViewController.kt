package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.Text
import ru.avem.posvanna.app.Pos.Companion.isAppRunning
import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.IDeviceController
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrModel
import ru.avem.posvanna.entities.*
import ru.avem.posvanna.utils.*
import ru.avem.posvanna.view.MainView
import tornadofx.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread
import kotlin.experimental.and
import kotlin.time.ExperimentalTime


class MainViewController : Controller() {
    val view: MainView by inject()
    var position1 = ""

    @Volatile
    var isExperimentRunning: Boolean = false

    @Volatile
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
            SimpleStringProperty("1"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("2"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("3"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("4"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("5"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("6"),
            SimpleStringProperty("0.0")
        )
    )

    var tableValuesTest2 = observableListOf(
        TableValuesTest2(
            SimpleStringProperty("1"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("2"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("3"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("4"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("5"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("6"),
            SimpleStringProperty("0.0")
        )
    )
    var tableValuesTest3 = observableListOf(
        TableValuesTest3(
            SimpleStringProperty("1"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("2"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("3"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("4"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("5"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("6"),
            SimpleStringProperty("0.0")
        )
    )

    var tableValuesTestTime = observableList(
        TableValuesTestTime(
            SimpleStringProperty("1"),
            SimpleStringProperty("4.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("2"),
            SimpleStringProperty("4.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("3"),
            SimpleStringProperty("4.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("4"),
            SimpleStringProperty("4.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("5"),
            SimpleStringProperty("4.0")
        ),
        TableValuesTestTime(
            SimpleStringProperty("6"),
            SimpleStringProperty("4.0")
        )
    )

    var tableValuesTestTimePause = observableList(
        TableValuesTestTimePause(
            SimpleStringProperty("150.0")
        )
    )

    var tableValuesWaterTemp = observableList(
        TableValuesWaterTemp(
            SimpleStringProperty("0.0")
        )
    )

    init {
//        thread(isDaemon = true) {
//            runLater {
//                view.buttonStop.isDisable = true
//            }
//            while (isAppRunning) {
//                var register = CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2)
//                    .getRegisterById(OwenPrModel.INSTANT_STATES_REGISTER_1)
//                CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).readRegister(register)
//                var doorZone1 = register.value.toShort() and 2 > 0
//
//                if (CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).isResponding) {
//                    runLater {
//                        view.comIndicate.fill = State.OK.c
//                    }
//                    if (doorZone1) {
//                        runLater {
//                            view.labelTestStatusEnd1.text = "Дверь открыта"
//                        }
//                    } else {
//                        runLater {
//                            view.labelTestStatusEnd1.text = ""
//                        }
//                    }
//                    if (!isExperimentRunning && CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).isResponding && !doorZone1) {
//                        runLater {
//                            view.buttonStart.isDisable = false
//                        }
//                    } else if (!isExperimentRunning && (!CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).isResponding || doorZone1)) {
//                        runLater {
//                            view.buttonStart.isDisable = true
//                        }
//
//                    }
//                } else {
//                    runLater {
//                        cause = "Нет связи"
//                        view.comIndicate.fill = State.BAD.c
//                        view.labelTestStatusEnd1.text = "Нет связи со стендом. Проверьте подключение."
//                        view.buttonStart.isDisable = true
//                        view.buttonStop.isDisable = true
//                    }
//                }
//            }
//        }
//        sleep(1000)
    }

    @UseExperimental(ExperimentalTime::class)
    fun handleStartTest() {
        if (view.textFieldTimeCycle.text.isEmpty() || !view.textFieldTimeCycle.text.isDouble()) {
            runLater {
                Toast.makeText("Введите количество циклов").show(Toast.ToastType.ERROR)
            }
        } else if (!isAtLeastOneIsSelected()) {
            runLater {
                Toast.makeText("Выберите хотя бы один объект испытания").show(Toast.ToastType.ERROR)
            }
        } else {
            if (view.textFieldMaxTemp.text.replace(",", ".").isDouble()) {
                Test1Controller().startTest()
            } else {
                Toast.makeText("Неверное значение максимальной температуры").show(Toast.ToastType.ERROR)
            }
        }
    }

    fun handleStopTest() {
        view.currentWindow?.let {
            showTwoWayDialog(
                "Отмена",
                "Вы действительно хотите отменить испытание?",
                "Нет",
                "Да",
                {  },
                { cause = "Отменено оператором" },
                currentWindow = it
            )
        }
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

    fun showAboutUs() {
        Toast.makeText("Версия ПО: 2.0.4\nВерсия БСУ: 1.0.0\nДата: 01.06.2021").show(Toast.ToastType.INFORMATION)
    }


}
