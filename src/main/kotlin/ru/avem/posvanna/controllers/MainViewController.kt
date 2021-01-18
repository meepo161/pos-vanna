package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.Text
import ru.avem.posvanna.app.Pos.Companion.isAppRunning
import ru.avem.posvanna.entities.TableValuesTest1
import ru.avem.posvanna.entities.TableValuesTest2
import ru.avem.posvanna.entities.TableValuesTest3
import ru.avem.posvanna.utils.*
import ru.avem.posvanna.view.MainView
import tornadofx.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread
import kotlin.time.ExperimentalTime


class MainViewController : TestController() {
    val view: MainView by inject()
    var position1 = ""

    var tableValuesTest1 = observableListOf(
        TableValuesTest1(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest1(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        )
    )

    var tableValuesTest2 = observableListOf(
        TableValuesTest2(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest2(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        )
    )
    var tableValuesTest3 = observableListOf(
        TableValuesTest3(
            SimpleStringProperty("1 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("2 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("3 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("4 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("5 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        ),
        TableValuesTest3(
            SimpleStringProperty("6 секция"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0"),
            SimpleStringProperty("0.0")
        )
    )

    init {
        thread(isDaemon = true) {
            while (isAppRunning) {
                if (owenPR.isResponding) {
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
        if (view.textFieldTimeStart.text.isEmpty() || !view.textFieldTimeStart.text.isDouble()) {
            runLater {
                Toast.makeText("Введите время нагрева").show(Toast.ToastType.ERROR)
            }
        } else if (view.textFieldTimePause.text.isEmpty() || !view.textFieldTimeStart.text.isDouble()) {
            runLater {
                Toast.makeText("Введите время паузы").show(Toast.ToastType.ERROR)
            }
        } else if (view.textFieldTimeCycle.text.isEmpty() || !view.textFieldTimeStart.text.isDouble()) {
            runLater {
                Toast.makeText("Введите количество циклов").show(Toast.ToastType.ERROR)
            }
        } else if (!isAtLeastOneIsSelected()) {
            runLater {
                Toast.makeText("Выберите хотя бы одно испытание из списка").show(Toast.ToastType.ERROR)
            }
        } else {
            thread(isDaemon = true) {
                runLater {
                    view.mainMenubar.isDisable = true
                    view.buttonStart.text = "Остановить"
                }
                clearTable()

                val allTime =
                    ((view.textFieldTimeStart.text.toDouble() * 60 + view.textFieldTimePause.text.toDouble() * 60) * view.textFieldTimeCycle.text.toDouble()).toInt()
                val cb = CallbackTimer(
                    tickPeriod = 1.seconds, tickTimes = allTime,
                    tickJob = {
                        runLater {
                            view.labelTimeRemaining.text =
                                "Осталось: " + toHHmmss((allTime - it.getCurrentTicks()) * 1000L)
                        }
                    },
                    onFinishJob = {
                    })
                var i = 0
                while (i++ < 1000) {
                    sleep(10)
                }
                cb.stop()

//                appendMessageToLog(LogTag.DEBUG, "Начало испытания")
//                Test1Controller().startTest()
//                appendMessageToLog(LogTag.MESSAGE, "Испытание завершено")

                runLater {
                    view.buttonStart.text = "Запустить"
                    view.mainMenubar.isDisable = false
                    view.checkBoxTest1.isDisable = false
                    view.checkBoxTest2.isDisable = false
                    view.checkBoxTest3.isDisable = false
                }
            }
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

    fun clearTable() {
        runLater {
            tableValuesTest2[1].section21I.value = ""
        }
    }

    fun showAboutUs() {
        Toast.makeText("Версия ПО: 1.0.0\nВерсия БСУ: 1.0.0\nДата: 30.04.2020").show(Toast.ToastType.INFORMATION)
    }

}
