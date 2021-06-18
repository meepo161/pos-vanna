package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.text.Text
import org.jetbrains.exposed.sql.transactions.transaction
import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrModel
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Model
import ru.avem.posvanna.communication.model.devices.parma.ParmaModel
import ru.avem.posvanna.database.entities.Protocol
import ru.avem.posvanna.database.entities.ProtocolRotorBlade
import ru.avem.posvanna.database.entities.ProtocolVars
import ru.avem.posvanna.protocol.saveProtocolAsWorkbook
import ru.avem.posvanna.utils.*
import ru.avem.posvanna.view.MainView
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.text.SimpleDateFormat
import kotlin.concurrent.thread
import kotlin.experimental.and
import kotlin.time.ExperimentalTime

class Test1Controller : TestController() {
    val controller: MainViewController by inject()
    val mainView: MainView by inject()

    private var logBuffer: String? = null

    @Volatile
    var isExperimentEnded: Boolean = true

    //region переменные для значений с приборов
    @Volatile
    private var measuringUA: Double = 0.0

    @Volatile
    private var measuringUB: Double = 0.0

    @Volatile
    private var measuringUC: Double = 0.0

    @Volatile
    private var measuringIA: Double = 0.0

    @Volatile
    private var measuringIB: Double = 0.0

    @Volatile
    private var measuringIC: Double = 0.0

    @Volatile
    private var measuringt11: Double = 0.0

    @Volatile
    private var measuringt12: Double = 0.0

    @Volatile
    private var measuringt13: Double = 0.0

    @Volatile
    private var measuringt14: Double = 0.0

    @Volatile
    private var measuringt15: Double = 0.0

    @Volatile
    private var measuringt16: Double = 0.0

    @Volatile
    private var measuringt17: Double = 0.0

    @Volatile
    private var measuringt21: Double = 0.0

    @Volatile
    private var measuringt22: Double = 0.0

    @Volatile
    private var measuringt23: Double = 0.0

    @Volatile
    private var measuringt24: Double = 0.0

    @Volatile
    private var measuringt25: Double = 0.0

    @Volatile
    private var measuringt26: Double = 0.0

    @Volatile
    private var measuringt31: Double = 0.0

    @Volatile
    private var measuringt32: Double = 0.0

    @Volatile
    private var measuringt33: Double = 0.0

    @Volatile
    private var measuringt34: Double = 0.0

    @Volatile
    private var measuringt35: Double = 0.0

    @Volatile
    private var measuringt36: Double = 0.0
    //endregion

    //region переменные для защит ПР
    @Volatile
    private var doorZone1: Boolean = false

    @Volatile
    private var startButton: Boolean = false

    @Volatile
    private var stopButton: Boolean = false

    @Volatile
    private var currentI1: Boolean = false

    @Volatile
    private var currentI2: Boolean = false

    @Volatile
    private var currentI3: Boolean = false

    @Volatile
    private var trmStatus11: Boolean = false

    @Volatile
    private var trmStatus12: Boolean = false

    @Volatile
    private var trmStatus13: Boolean = false

    @Volatile
    private var trmStatus14: Boolean = false

    @Volatile
    private var trmStatus15: Boolean = false

    @Volatile
    private var trmStatus16: Boolean = false

    @Volatile
    private var trmStatus17: Boolean = false

    @Volatile
    private var trmStatus21: Boolean = false

    @Volatile
    private var trmStatus22: Boolean = false

    @Volatile
    private var trmStatus23: Boolean = false

    @Volatile
    private var trmStatus24: Boolean = false

    @Volatile
    private var trmStatus25: Boolean = false

    @Volatile
    private var trmStatus26: Boolean = false

    @Volatile
    private var trmStatus31: Boolean = false

    @Volatile
    private var trmStatus32: Boolean = false

    @Volatile
    private var trmStatus33: Boolean = false

    @Volatile
    private var trmStatus34: Boolean = false

    @Volatile
    private var trmStatus35: Boolean = false

    @Volatile
    private var trmStatus36: Boolean = false
    //endregion

    private var cycles: Int = 0

    private var maxTemp: Double = 0.0

    //region листы для БД
    private var listOfValues11 = mutableListOf<String>()
    private var listOfValues12 = mutableListOf<String>()
    private var listOfValues13 = mutableListOf<String>()
    private var listOfValues14 = mutableListOf<String>()
    private var listOfValues15 = mutableListOf<String>()
    private var listOfValues16 = mutableListOf<String>()
    private var listOfValues17 = mutableListOf<String>()
    private var listOfValues21 = mutableListOf<String>()
    private var listOfValues22 = mutableListOf<String>()
    private var listOfValues23 = mutableListOf<String>()
    private var listOfValues24 = mutableListOf<String>()
    private var listOfValues25 = mutableListOf<String>()
    private var listOfValues26 = mutableListOf<String>()
    private var listOfValues31 = mutableListOf<String>()
    private var listOfValues32 = mutableListOf<String>()
    private var listOfValues33 = mutableListOf<String>()
    private var listOfValues34 = mutableListOf<String>()
    private var listOfValues35 = mutableListOf<String>()
    private var listOfValues36 = mutableListOf<String>()
    //endregion

    var isClicked = false

    var unixTimeStart = 0L

    private fun appendOneMessageToLog(tag: LogTag, message: String) {
        if (logBuffer == null || logBuffer != message) {
            logBuffer = message
            appendMessageToLog(tag, message)
        }
    }

    fun appendMessageToLog(tag: LogTag, _msg: String) {
        val msg = Text("${SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())} | $_msg")
        msg.style {
            fill = when (tag) {
                LogTag.MESSAGE -> tag.c
                LogTag.ERROR -> tag.c
                LogTag.DEBUG -> tag.c
            }
        }

        Platform.runLater {
            mainView.vBoxLog.add(msg)
        }
    }

    fun isDevicesResponding(): Boolean {
        return CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).isResponding
    }

    private fun startPollDevices() {
        //region pr pool
        CommunicationModel.startPoll(CommunicationModel.DeviceID.DD2, OwenPrModel.FIXED_STATES_REGISTER_1) { value ->
            stopButton = value.toShort() and 32 > 0
            startButton = value.toShort() and 64 > 0
            if (doorZone1) {
                controller.cause = "Открыта дверь зоны"
            }
            if (stopButton) {
                controller.cause = "Нажата кнопка Стоп"
            }
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.DD2, OwenPrModel.INSTANT_STATES_REGISTER_1) { value ->
            doorZone1 = value.toShort() and 2 > 0
            if (doorZone1) {
                controller.cause = "Открыта дверь зоны"
            }
        }
        //endregion

        //region parma poll
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IA) { value ->
            measuringIA = value.toDouble() * 10
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IB) { value ->
            measuringIB = value.toDouble() * 10
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IC) { value ->
            measuringIC = value.toDouble() * 10
        }

        if (measuringIA > 29 || measuringIB > 29 || measuringIC > 29) {
            controller.cause = "Ток превысил 29А"
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.U_AB) { value ->
            measuringUA = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.U_BC) { value ->
            measuringUB = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.U_CA) { value ->
            measuringUC = value.toDouble()
        }
        //endregion

        //region trm poll
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_1) { value ->
            measuringt11 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_2) { value ->
            measuringt12 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_3) { value ->
            measuringt13 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_4) { value ->
            measuringt14 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_5) { value ->
            measuringt15 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_6) { value ->
            measuringt16 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_7) { value ->
            measuringt17 = value.toDouble()
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_1) { value ->
            measuringt21 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_2) { value ->
            measuringt22 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_3) { value ->
            measuringt23 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_4) { value ->
            measuringt24 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_5) { value ->
            measuringt25 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_6) { value ->
            measuringt26 = value.toDouble()
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_1) { value ->
            measuringt31 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_2) { value ->
            measuringt32 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_3) { value ->
            measuringt33 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_4) { value ->
            measuringt34 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_5) { value ->
            measuringt35 = value.toDouble()
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_6) { value ->
            measuringt36 = value.toDouble()
        }
        //endregion
    }

    @ExperimentalTime
    fun startTest() {
        thread(isDaemon = true) {
            runLater {
                mainView.buttonStop.isDisable = false
                mainView.buttonStart.isDisable = true
            }
            controller.cause = ""
            controller.isExperimentRunning = true
            isExperimentEnded = false
            isClicked = false
            appendMessageToLog(LogTag.DEBUG, "Начало испытания")
            unixTimeStart = System.currentTimeMillis()
            sleep(1000)

            runLater {
                mainView.labelTestStatus.text = ""
                mainView.labelTestStatusEnd1.text = ""
                mainView.labelTimeRemaining.text = ""
                mainView.textFieldTimeCycle.isDisable = true
                mainView.tableViewTestTime.isDisable = true
                mainView.buttonStop.isDisable = false
                mainView.buttonStart.isDisable = true
                mainView.checkBoxTest1.isDisable = true
                mainView.checkBoxTest2.isDisable = true
                mainView.checkBoxTest3.isDisable = true
                mainView.textFieldMaxTemp.isDisable = true
            }
            maxTemp = mainView.textFieldMaxTemp.text.replace(",", ".").toDouble()

            if (controller.isExperimentRunning) {
                startPollDevices()
                appendMessageToLog(LogTag.DEBUG, "Инициализация устройств")
                sleep(1000)
            }

            var timeToPrepare = 300
            while (!controller.isDevicesResponding() && controller.isExperimentRunning && timeToPrepare-- > 0) {
                sleep(100)
            }

            if (!controller.isDevicesResponding()) {
                var cause = ""
                cause += "Не отвечают приборы: "
                if (!CommunicationModel.getDeviceById(CommunicationModel.DeviceID.DD2).isResponding) {
                    cause += "ПР "
                }
                if (!CommunicationModel.getDeviceById(CommunicationModel.DeviceID.TRM1).isResponding) {
                    cause += "ТРМ1 "
                }
                if (!CommunicationModel.getDeviceById(CommunicationModel.DeviceID.TRM2).isResponding) {
                    cause += "ТРМ2 "
                }
                if (!CommunicationModel.getDeviceById(CommunicationModel.DeviceID.TRM3).isResponding) {
                    cause += "ТРМ3 "
                }
                controller.cause = cause
            }

            if (controller.isExperimentRunning && controller.isDevicesResponding()) {
                CommunicationModel.addWritingRegister(
                    CommunicationModel.DeviceID.DD2,
                    OwenPrModel.RESET_DOG,
                    1.toShort()
                )
                owenPR.initOwenPR()
                startPollDevices()
                sleep(1000)
            }


            if (!startButton && controller.isExperimentRunning && controller.isDevicesResponding()) {
                runLater {
                    Toast.makeText("Нажмите кнопку ПУСК").show(Toast.ToastType.WARNING)
                }
            }

            var timeToStart = 300
            while (!startButton && controller.isExperimentRunning && controller.isDevicesResponding() && timeToStart-- > 0) {
                appendOneMessageToLog(LogTag.DEBUG, "Нажмите кнопку ПУСК")
                sleep(100)
            }

            if (!startButton && controller.isExperimentRunning && controller.isDevicesResponding()) {
                controller.cause = "Не нажата кнопка ПУСК"
            }

            if (controller.isExperimentRunning && controller.isDevicesResponding()) {
                soundWarning(1, 1000)
                appendMessageToLog(LogTag.DEBUG, "Подготовка стенда")
                startValues()
                sleep(2000)
                appendMessageToLog(LogTag.DEBUG, "Ожидайте завершения...")
            }

            cycles = mainView.textFieldTimeCycle.text.toInt()

            val allTime =
                (((controller.tableValuesTestTime[0].start.value.replace(",", ".").toDouble())
                        + (controller.tableValuesTestTime[1].start.value.replace(",", ".").toDouble())
                        + (controller.tableValuesTestTime[2].start.value.replace(",", ".").toDouble())
                        + (controller.tableValuesTestTime[3].start.value.replace(",", ".").toDouble())
                        + (controller.tableValuesTestTime[4].start.value.replace(",", ".").toDouble())
                        + (controller.tableValuesTestTime[5].start.value.replace(",", ".").toDouble())
                        + (controller.tableValuesTestTimePause[0].pause.value.replace(",", ".").toDouble()))
                        * mainView.textFieldTimeCycle.text.replace(",", ".").toDouble()).toInt()
            CallbackTimer(
                tickPeriod = 1.seconds, tickTimes = allTime,
                tickJob = {
                    if (!controller.isExperimentRunning) it.stop()
                    runLater {
                        mainView.labelTimeRemaining.text =
                            "                   Осталось всего: " + toHHmmss((allTime - it.getCurrentTicks()) * 1000L)
                        controller.tableValuesTest21[0].voltage.value = formatRealNumber(measuringUA).toString()
                        controller.tableValuesTest21[0].ampere.value = formatRealNumber(measuringIA).toString()
                        controller.tableValuesTest22[0].voltage.value = formatRealNumber(measuringUB).toString()
                        controller.tableValuesTest22[0].ampere.value = formatRealNumber(measuringIB).toString()
                        controller.tableValuesTest23[0].voltage.value = formatRealNumber(measuringUC).toString()
                        controller.tableValuesTest23[0].ampere.value = formatRealNumber(measuringIC).toString()
                        if (measuringt11 < -50 || measuringt11 > 100 || !trmStatus11) {
                            controller.tableValuesTest1[0].section1t.value = "-.--"
                        } else {
                            controller.tableValuesTest1[0].section1t.value = String.format("%.2f", measuringt11)
                        }
                        if (measuringt12 < -50 || measuringt12 > 100 || !trmStatus12) {
                            controller.tableValuesTest1[1].section1t.value = "-.--"
                        } else {
                            controller.tableValuesTest1[1].section1t.value = String.format("%.2f", measuringt12)
                        }
                        if (measuringt13 < -50 || measuringt13 > 100 || !trmStatus13) {
                            controller.tableValuesTest1[2].section1t.value = "-.--"
                        } else {
                            controller.tableValuesTest1[2].section1t.value = String.format("%.2f", measuringt13)
                        }
                        if (measuringt14 < -50 || measuringt14 > 100 || !trmStatus14) {
                            controller.tableValuesTest1[3].section1t.value = "-.--"
                        } else {
                            controller.tableValuesTest1[3].section1t.value = String.format("%.2f", measuringt14)
                        }
                        if (measuringt15 < -50 || measuringt15 > 100 || !trmStatus15) {
                            controller.tableValuesTest1[4].section1t.value = "-.--"
                        } else {
                            controller.tableValuesTest1[4].section1t.value = String.format("%.2f", measuringt15)
                        }
                        if (measuringt16 < -50 || measuringt16 > 100 || !trmStatus16) {
                            controller.tableValuesTest1[5].section1t.value = "-.--"
                        } else {
                            controller.tableValuesTest1[5].section1t.value = String.format("%.2f", measuringt16)
                        }
                        if (measuringt21 < -50 || measuringt21 > 100 || !trmStatus21) {
                            controller.tableValuesTest2[0].section21t.value = "-.--"
                        } else {
                            controller.tableValuesTest2[0].section21t.value = String.format("%.2f", measuringt21)
                        }
                        if (measuringt22 < -50 || measuringt22 > 100 || !trmStatus22) {
                            controller.tableValuesTest2[1].section21t.value = "-.--"
                        } else {
                            controller.tableValuesTest2[1].section21t.value = String.format("%.2f", measuringt22)
                        }
                        if (measuringt23 < -50 || measuringt23 > 100 || !trmStatus23) {
                            controller.tableValuesTest2[2].section21t.value = "-.--"
                        } else {
                            controller.tableValuesTest2[2].section21t.value = String.format("%.2f", measuringt23)
                        }
                        if (measuringt24 < -50 || measuringt24 > 100 || !trmStatus24) {
                            controller.tableValuesTest2[3].section21t.value = "-.--"
                        } else {
                            controller.tableValuesTest2[3].section21t.value = String.format("%.2f", measuringt24)
                        }
                        if (measuringt25 < -50 || measuringt25 > 100 || !trmStatus25) {
                            controller.tableValuesTest2[4].section21t.value = "-.--"
                        } else {
                            controller.tableValuesTest2[4].section21t.value = String.format("%.2f", measuringt25)
                        }
                        if (measuringt26 < -50 || measuringt26 > 100 || !trmStatus26) {
                            controller.tableValuesTest2[5].section21t.value = "-.--"
                        } else {
                            controller.tableValuesTest2[5].section21t.value = String.format("%.2f", measuringt26)
                        }
                        if (measuringt31 < -50 || measuringt31 > 100 || !trmStatus31) {
                            controller.tableValuesTest3[0].section31t.value = "-.--"
                        } else {
                            controller.tableValuesTest3[0].section31t.value = String.format("%.2f", measuringt31)
                        }
                        if (measuringt32 < -50 || measuringt32 > 100 || !trmStatus32) {
                            controller.tableValuesTest3[1].section31t.value = "-.--"
                        } else {
                            controller.tableValuesTest3[1].section31t.value = String.format("%.2f", measuringt32)
                        }
                        if (measuringt33 < -50 || measuringt33 > 100 || !trmStatus33) {
                            controller.tableValuesTest3[2].section31t.value = "-.--"
                        } else {
                            controller.tableValuesTest3[2].section31t.value = String.format("%.2f", measuringt33)
                        }
                        if (measuringt34 < -50 || measuringt34 > 100 || !trmStatus34) {
                            controller.tableValuesTest3[3].section31t.value = "-.--"
                        } else {
                            controller.tableValuesTest3[3].section31t.value = String.format("%.2f", measuringt34)
                        }
                        if (measuringt35 < -50 || measuringt35 > 100 || !trmStatus35) {
                            controller.tableValuesTest3[4].section31t.value = "-.--"
                        } else {
                            controller.tableValuesTest3[4].section31t.value = String.format("%.2f", measuringt35)
                        }
                        if (measuringt36 < -50 || measuringt36 > 100 || !trmStatus36) {
                            controller.tableValuesTest3[5].section31t.value = "-.--"
                        } else {
                            controller.tableValuesTest3[5].section31t.value = String.format("%.2f", measuringt36)
                        }
                        if (measuringt17 < -40 && measuringt17 > 400 || !trmStatus17) {
                            controller.tableValuesWaterTemp[0].waterTemp.value = "-.--"
                        } else {
                            controller.tableValuesWaterTemp[0].waterTemp.value = String.format("%.2f", measuringt17)
                        }
                    }

                    if (measuringt11 > maxTemp) {
                        controller.cause = "Температура 1 лопасти 1 секции больше $maxTemp°С"
                    }
                    if (measuringt12 > maxTemp) {
                        controller.cause = "Температура 1 лопасти 2 секции больше $maxTemp°С"
                    }
                    if (measuringt13 > maxTemp) {
                        controller.cause = "Температура 1 лопасти 3 секции больше $maxTemp°С"
                    }
                    if (measuringt14 > maxTemp) {
                        controller.cause = "Температура 1 лопасти 4 секции больше $maxTemp°С"
                    }
                    if (measuringt15 > maxTemp) {
                        controller.cause = "Температура 1 лопасти 5 секции больше $maxTemp°С"
                    }
                    if (measuringt16 > maxTemp) {
                        controller.cause = "Температура 1 лопасти 6 секции больше $maxTemp°С"
                    }

                    if (measuringt17 > maxTemp) {
                        controller.cause = "Температура воды больше $maxTemp°С"
                    }

                    if (measuringt21 > maxTemp) {
                        controller.cause = "Температура 2 лопасти 1 секции больше $maxTemp°С"
                    }
                    if (measuringt22 > maxTemp) {
                        controller.cause = "Температура 2 лопасти 2 секции больше $maxTemp°С"
                    }
                    if (measuringt23 > maxTemp) {
                        controller.cause = "Температура 2 лопасти 3 секции больше $maxTemp°С"
                    }
                    if (measuringt24 > maxTemp) {
                        controller.cause = "Температура 2 лопасти 4 секции больше $maxTemp°С"
                    }
                    if (measuringt25 > maxTemp) {
                        controller.cause = "Температура 2 лопасти 5 секции больше $maxTemp°С"
                    }
                    if (measuringt26 > maxTemp) {
                        controller.cause = "Температура 2 лопасти 6 секции больше $maxTemp°С"
                    }

                    if (measuringt31 > maxTemp) {
                        controller.cause = "Температура 3 лопасти 1 секции больше $maxTemp°С"
                    }
                    if (measuringt32 > maxTemp) {
                        controller.cause = "Температура 3 лопасти 2 секции больше $maxTemp°С"
                    }
                    if (measuringt33 > maxTemp) {
                        controller.cause = "Температура 3 лопасти 3 секции больше $maxTemp°С"
                    }
                    if (measuringt34 > maxTemp) {
                        controller.cause = "Температура 3 лопасти 4 секции больше $maxTemp°С"
                    }
                    if (measuringt35 > maxTemp) {
                        controller.cause = "Температура 3 лопасти 5 секции больше $maxTemp°С"
                    }
                    if (measuringt36 > maxTemp) {
                        controller.cause = "Температура 3 лопасти 6 секции больше $maxTemp°С"
                    }

                    if (measuringt11 < -50 || measuringt11 > 100 || !trmStatus11) {
                        listOfValues11.add("-99.9")
                    } else {
                        listOfValues11.add(String.format("%.1f", measuringt11))
                    }
                    if (measuringt12 < -50 || measuringt12 > 100 || !trmStatus12) {
                        listOfValues12.add("-99.9")
                    } else {
                        listOfValues12.add(String.format("%.1f", measuringt12))
                    }
                    if (measuringt13 < -50 || measuringt13 > 100 || !trmStatus13) {
                        listOfValues13.add("-99.9")
                    } else {
                        listOfValues13.add(String.format("%.1f", measuringt13))
                    }
                    if (measuringt14 < -50 || measuringt14 > 100 || !trmStatus14) {
                        listOfValues14.add("-99.9")
                    } else {
                        listOfValues14.add(String.format("%.1f", measuringt14))
                    }
                    if (measuringt15 < -50 || measuringt15 > 100 || !trmStatus15) {
                        listOfValues15.add("-99.9")
                    } else {
                        listOfValues15.add(String.format("%.1f", measuringt15))
                    }
                    if (measuringt16 < -50 || measuringt16 > 100 || !trmStatus16) {
                        listOfValues16.add("-99.9")
                    } else {
                        listOfValues16.add(String.format("%.1f", measuringt16))
                    }
                    if (measuringt21 < -50 || measuringt21 > 100 || !trmStatus21) {
                        listOfValues21.add("-99.9")
                    } else {
                        listOfValues21.add(String.format("%.1f", measuringt21))
                    }
                    if (measuringt22 < -50 || measuringt22 > 100 || !trmStatus22) {
                        listOfValues22.add("-99.9")
                    } else {
                        listOfValues22.add(String.format("%.1f", measuringt22))
                    }
                    if (measuringt23 < -50 || measuringt23 > 100 || !trmStatus23) {
                        listOfValues23.add("-99.9")
                    } else {
                        listOfValues23.add(String.format("%.1f", measuringt23))
                    }
                    if (measuringt24 < -50 || measuringt24 > 100 || !trmStatus24) {
                        listOfValues24.add("-99.9")
                    } else {
                        listOfValues24.add(String.format("%.1f", measuringt24))
                    }
                    if (measuringt25 < -50 || measuringt25 > 100 || !trmStatus25) {
                        listOfValues25.add("-99.9")
                    } else {
                        listOfValues25.add(String.format("%.1f", measuringt25))
                    }
                    if (measuringt26 < -50 || measuringt26 > 100 || !trmStatus26) {
                        listOfValues26.add("-99.9")
                    } else {
                        listOfValues26.add(String.format("%.1f", measuringt26))
                    }
                    if (measuringt31 < -50 || measuringt31 > 100 || !trmStatus31) {
                        listOfValues31.add("-99.9")
                    } else {
                        listOfValues31.add(String.format("%.1f", measuringt31))
                    }
                    if (measuringt32 < -50 || measuringt32 > 100 || !trmStatus32) {
                        listOfValues32.add("-99.9")
                    } else {
                        listOfValues32.add(String.format("%.1f", measuringt32))
                    }
                    if (measuringt33 < -50 || measuringt33 > 100 || !trmStatus33) {
                        listOfValues33.add("-99.9")
                    } else {
                        listOfValues33.add(String.format("%.1f", measuringt33))
                    }
                    if (measuringt34 < -50 || measuringt34 > 100 || !trmStatus34) {
                        listOfValues34.add("-99.9")
                    } else {
                        listOfValues34.add(String.format("%.1f", measuringt34))
                    }
                    if (measuringt35 < -50 || measuringt35 > 100 || !trmStatus35) {
                        listOfValues35.add("-99.9")
                    } else {
                        listOfValues35.add(String.format("%.1f", measuringt35))
                    }
                    if (measuringt36 < -50 || measuringt36 > 100 || !trmStatus36) {
                        listOfValues36.add("-99.9")
                    } else {
                        listOfValues36.add(String.format("%.1f", measuringt36))
                    }
                    if (measuringt17 < -40 || measuringt17 > 400 || !trmStatus17) {
                        listOfValues17.add("-99.9")
                    } else {
                        listOfValues17.add(String.format("%.1f", measuringt17))
                    }
                },
                onFinishJob = {
                })

            var currentCycle = 0
            while (controller.isExperimentRunning && controller.isDevicesResponding() && cycles-- > 0) {
                appendOneMessageToLog(LogTag.MESSAGE, "Цикл ${++currentCycle}")

                if (controller.tableValuesTestTime[0].start.value.replace(",", ".").toDouble() != 0.0) {


                    mainView.tableViewTestTimePause.selectionModel.clearSelection()
                    mainView.tableViewTestTime.selectionModel.select(0)
                    mainView.tableViewTest1.selectionModel.select(0)
                    mainView.tableViewTest2.selectionModel.select(0)
                    mainView.tableViewTest3.selectionModel.select(0)

                    val timeStart1 =
                        (controller.tableValuesTestTime[0].start.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerStart1 = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timeStart1,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: нагрев 1 cекции"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    if (mainView.checkBoxTest1.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on11()
                    }
                    if (mainView.checkBoxTest2.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on21()
                    }
                    if (mainView.checkBoxTest3.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on31()
                    }

                    while (controller.isExperimentRunning && callbackTimerStart1.isRunning && controller.isDevicesResponding()) {
                        sleep(100)
                    }

                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }

                if (controller.tableValuesTestTime[1].start.value.replace(",", ".").toDouble() != 0.0) {

                    mainView.tableViewTestTime.selectionModel.select(1)
                    mainView.tableViewTest1.selectionModel.select(1)
                    mainView.tableViewTest2.selectionModel.select(1)
                    mainView.tableViewTest3.selectionModel.select(1)

                    val timeStart2 =
                        (controller.tableValuesTestTime[1].start.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerStart2 = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timeStart2,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: нагрев 2 cекции"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    if (mainView.checkBoxTest1.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on12()
                    }
                    if (mainView.checkBoxTest2.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on22()
                    }
                    if (mainView.checkBoxTest3.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on32()
                    }

                    while (controller.isExperimentRunning && callbackTimerStart2.isRunning && controller.isDevicesResponding()) {
                        sleep(100)
                    }

                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }

                if (controller.tableValuesTestTime[2].start.value.replace(",", ".").toDouble() != 0.0) {

                    mainView.tableViewTestTime.selectionModel.select(2)
                    mainView.tableViewTest1.selectionModel.select(2)
                    mainView.tableViewTest2.selectionModel.select(2)
                    mainView.tableViewTest3.selectionModel.select(2)

                    val timeStart3 =
                        (controller.tableValuesTestTime[2].start.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerStart3 = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timeStart3,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: нагрев 3 cекции"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    if (mainView.checkBoxTest1.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on13()
                    }
                    if (mainView.checkBoxTest2.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on23()
                    }
                    if (mainView.checkBoxTest3.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on33()
                    }

                    while (controller.isExperimentRunning && callbackTimerStart3.isRunning && controller.isDevicesResponding()) {
                        sleep(100)
                    }

                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }

                if (controller.tableValuesTestTime[3].start.value.replace(",", ".").toDouble() != 0.0) {

                    mainView.tableViewTestTime.selectionModel.select(3)
                    mainView.tableViewTest1.selectionModel.select(3)
                    mainView.tableViewTest2.selectionModel.select(3)
                    mainView.tableViewTest3.selectionModel.select(3)

                    val timeStart4 =
                        (controller.tableValuesTestTime[3].start.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerStart4 = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timeStart4,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: нагрев 4 cекции"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    if (mainView.checkBoxTest1.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on14()
                    }
                    if (mainView.checkBoxTest2.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on24()
                    }
                    if (mainView.checkBoxTest3.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on34()
                    }

                    while (controller.isExperimentRunning && callbackTimerStart4.isRunning && controller.isDevicesResponding()) {
                        sleep(100)
                    }

                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }

                if (controller.tableValuesTestTime[4].start.value.replace(",", ".").toDouble() != 0.0) {

                    mainView.tableViewTestTime.selectionModel.select(4)
                    mainView.tableViewTest1.selectionModel.select(4)
                    mainView.tableViewTest2.selectionModel.select(4)
                    mainView.tableViewTest3.selectionModel.select(4)

                    val timeStart5 =
                        (controller.tableValuesTestTime[4].start.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerStart5 = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timeStart5,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: нагрев 5 cекции"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    if (mainView.checkBoxTest1.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on15()
                    }
                    if (mainView.checkBoxTest2.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on25()
                    }
                    if (mainView.checkBoxTest3.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on35()
                    }

                    while (controller.isExperimentRunning && callbackTimerStart5.isRunning && controller.isDevicesResponding()) {
                        sleep(100)
                    }

                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }

                if (controller.tableValuesTestTime[5].start.value.replace(",", ".").toDouble() != 0.0) {

                    mainView.tableViewTestTime.selectionModel.select(5)
                    mainView.tableViewTest1.selectionModel.select(5)
                    mainView.tableViewTest2.selectionModel.select(5)
                    mainView.tableViewTest3.selectionModel.select(5)

                    val timeStart6 =
                        (controller.tableValuesTestTime[5].start.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerStart6 = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timeStart6,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: нагрев 6 cекции"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    if (mainView.checkBoxTest1.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on16()
                    }
                    if (mainView.checkBoxTest2.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on26()
                    }
                    if (mainView.checkBoxTest3.isSelected && controller.isExperimentRunning && controller.isDevicesResponding()) {
                        owenPR.on36()
                    }

                    while (controller.isExperimentRunning && callbackTimerStart6.isRunning && controller.isDevicesResponding()) {
                        sleep(100)
                    }

                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }

                if (controller.tableValuesTestTimePause[0].pause.value.replace(",", ".").toDouble() != 0.0) {

                    mainView.tableViewTestTimePause.selectionModel.select(0)
                    mainView.tableViewTestTime.selectionModel.clearSelection()
                    mainView.tableViewTest1.selectionModel.clearSelection()
                    mainView.tableViewTest2.selectionModel.clearSelection()
                    mainView.tableViewTest3.selectionModel.clearSelection()

                    val timePause =
                        (controller.tableValuesTestTimePause[0].pause.value.replace(",", ".").toDouble()).toInt()
                    val callbackTimerPause = CallbackTimer(
                        tickPeriod = 1.seconds, tickTimes = timePause,
                        tickJob = {
                            if (!controller.isExperimentRunning) {
                                it.stop()
                            } else {
                                runLater {
                                    mainView.labelTestStatus.text = "Статус: пауза"
                                }
                            }
                        },
                        onFinishJob = {
                        })

                    while (controller.isExperimentRunning && callbackTimerPause.isRunning) {
                        sleep(100)
                    }
                    if (controller.isExperimentRunning) {
                        owenPR.offAllKMs()
                    }
                }
            }

            owenPR.offAllKMs()

            appendMessageToLog(LogTag.MESSAGE, "Испытание завершено")
            setResult()

            soundWarning(2, 1000)

            finalizeExperiment()

            if (listOfValues11.isNotEmpty()) {
                saveProtocolToDB()
                Singleton.currentProtocol = transaction {
                    Protocol.all().toList().asObservable()
                }.last()
                runLater {
                    confirm(
                        "Печать протокола",
                        "Испытание завершено. Вы хотите напечатать протокол?",
                        ButtonType.YES, ButtonType.NO,
                        owner = mainView.currentWindow,
                        title = "Печать"
                    ) {
                        if (mainView.checkBoxTest1.isSelected && mainView.checkBoxTest2.isSelected && mainView.checkBoxTest3.isSelected) {
                            saveProtocolAsWorkbook(Singleton.currentProtocol)
                            Desktop.getDesktop().print(File("protocol.xlsx"))
                        } else {
                            var protocol = Singleton.currentProtocol
                            if (mainView.checkBoxTest1.isSelected) {
                                val protocolRotorBlade = transaction {
                                    ProtocolRotorBlade.new {
                                        date = protocol.date
                                        time = protocol.time
                                        dateEnd = protocol.dateEnd
                                        timeEnd = protocol.timeEnd
                                        cipher = protocol.cipher1
                                        productName = protocol.productName1
                                        operator = protocol.operator
                                        temp1 = protocol.temp11
                                        temp2 = protocol.temp12
                                        temp3 = protocol.temp13
                                        temp4 = protocol.temp14
                                        temp5 = protocol.temp15
                                        temp6 = protocol.temp16
                                        NUMBER_DATE_ATTESTATION = protocol.NUMBER_DATE_ATTESTATION
                                        NAME_OF_OPERATION = protocol.NAME_OF_OPERATION
                                        NUMBER_CONTROLLER = protocol.NUMBER_CONTROLLER
                                        T1 = protocol.T1
                                        T2 = protocol.T2
                                        T3 = protocol.T3
                                        T4 = protocol.T4
                                        T5 = protocol.T5
                                        T6 = protocol.T6
                                    }
                                }
                                saveProtocolAsWorkbook(protocolRotorBlade)
                                Desktop.getDesktop().print(File("protocol1RotorBlade.xlsx"))
                            }
                            if (mainView.checkBoxTest2.isSelected) {
                                protocol = Singleton.currentProtocol
                                val protocolRotorBlade = transaction {
                                    ProtocolRotorBlade.new {
                                        date = protocol.date
                                        time = protocol.time
                                        dateEnd = protocol.dateEnd
                                        timeEnd = protocol.timeEnd
                                        cipher = protocol.cipher2
                                        productName = protocol.productName2
                                        operator = protocol.operator
                                        temp1 = protocol.temp21
                                        temp2 = protocol.temp22
                                        temp3 = protocol.temp23
                                        temp4 = protocol.temp24
                                        temp5 = protocol.temp25
                                        temp6 = protocol.temp26
                                        NUMBER_DATE_ATTESTATION = protocol.NUMBER_DATE_ATTESTATION
                                        NAME_OF_OPERATION = protocol.NAME_OF_OPERATION
                                        NUMBER_CONTROLLER = protocol.NUMBER_CONTROLLER
                                        T1 = protocol.T7
                                        T2 = protocol.T8
                                        T3 = protocol.T9
                                        T4 = protocol.T10
                                        T5 = protocol.T11
                                        T6 = protocol.T12
                                    }
                                }
                                saveProtocolAsWorkbook(protocolRotorBlade)
                                Desktop.getDesktop().print(File("protocol1RotorBlade.xlsx"))
                            }
                            if (mainView.checkBoxTest3.isSelected) {
                                protocol = Singleton.currentProtocol
                                val protocolRotorBlade = transaction {
                                    ProtocolRotorBlade.new {
                                        date = protocol.date
                                        time = protocol.time
                                        dateEnd = protocol.dateEnd
                                        timeEnd = protocol.timeEnd
                                        cipher = protocol.cipher3
                                        productName = protocol.productName3
                                        operator = protocol.operator
                                        temp1 = protocol.temp31
                                        temp2 = protocol.temp32
                                        temp3 = protocol.temp33
                                        temp4 = protocol.temp34
                                        temp5 = protocol.temp35
                                        temp6 = protocol.temp36
                                        NUMBER_DATE_ATTESTATION = protocol.NUMBER_DATE_ATTESTATION
                                        NAME_OF_OPERATION = protocol.NAME_OF_OPERATION
                                        NUMBER_CONTROLLER = protocol.NUMBER_CONTROLLER
                                        T1 = protocol.T13
                                        T2 = protocol.T14
                                        T3 = protocol.T15
                                        T4 = protocol.T16
                                        T5 = protocol.T17
                                        T6 = protocol.T18
                                    }
                                }
                                saveProtocolAsWorkbook(protocolRotorBlade)
                                Desktop.getDesktop().print(File("protocol1RotorBlade.xlsx"))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun soundWarning(times: Int, sleep: Long) {
        thread(isDaemon = true) {
            for (i in 0 until times) {
                owenPR.onSound()
                sleep(sleep)
                owenPR.offSound()
                sleep(sleep)
            }
        }
    }

    private fun saveProtocolToDB() {
        val dateFormatter = SimpleDateFormat("dd.MM.y")
        val timeFormatter = SimpleDateFormat("HH:mm:ss")
        val unixTimeEnd = System.currentTimeMillis()

        val protocolVars = transaction {
            ProtocolVars.all().toList().asObservable()
        }.first()

        transaction {
            Protocol.new {
                date = dateFormatter.format(unixTimeStart).toString()
                time = timeFormatter.format(unixTimeStart).toString()
                dateEnd = dateFormatter.format(unixTimeEnd).toString()
                timeEnd = timeFormatter.format(unixTimeEnd).toString()
                operator = controller.position1
                cipher1 = mainView.tfCipher1.text.toString()
                productName1 = mainView.tfProductNumber1.text.toString()
                cipher2 = mainView.tfCipher3.text.toString()
                productName2 = mainView.tfProductNumber3.text.toString()
                cipher3 = mainView.tfCipher3.text.toString()
                productName3 = mainView.tfProductNumber3.text.toString()
                temp11 = listOfValues11.toString()
                temp12 = listOfValues12.toString()
                temp13 = listOfValues13.toString()
                temp14 = listOfValues14.toString()
                temp15 = listOfValues15.toString()
                temp16 = listOfValues16.toString()
                temp21 = listOfValues21.toString()
                temp22 = listOfValues22.toString()
                temp23 = listOfValues23.toString()
                temp24 = listOfValues24.toString()
                temp25 = listOfValues25.toString()
                temp26 = listOfValues26.toString()
                temp31 = listOfValues31.toString()
                temp32 = listOfValues32.toString()
                temp33 = listOfValues33.toString()
                temp34 = listOfValues34.toString()
                temp35 = listOfValues35.toString()
                temp36 = listOfValues36.toString()
                NUMBER_DATE_ATTESTATION = protocolVars.NUMBER_DATE_ATTESTATION
                NAME_OF_OPERATION = protocolVars.NAME_OF_OPERATION
                NUMBER_CONTROLLER = protocolVars.NUMBER_CONTROLLER
                T1 = protocolVars.T1
                T2 = protocolVars.T2
                T3 = protocolVars.T3
                T4 = protocolVars.T4
                T5 = protocolVars.T5
                T6 = protocolVars.T6
                T7 = protocolVars.T7
                T8 = protocolVars.T8
                T9 = protocolVars.T9
                T10 = protocolVars.T10
                T11 = protocolVars.T11
                T12 = protocolVars.T12
                T13 = protocolVars.T13
                T14 = protocolVars.T14
                T15 = protocolVars.T15
                T16 = protocolVars.T16
                T17 = protocolVars.T17
                T18 = protocolVars.T18

            }
        }
    }

    private fun startValues() {
        thread(isDaemon = true) {
            while (controller.isExperimentRunning) {
                trmStatus11 = true //trm1.checkStatus(0) == 1
                trmStatus12 = true //trm1.checkStatus(1) == 1
                trmStatus13 = true //trm1.checkStatus(2) == 1
                trmStatus14 = true //trm1.checkStatus(3) == 1
                trmStatus15 = true //trm1.checkStatus(4) == 1
                trmStatus16 = true //trm1.checkStatus(5) == 1
                trmStatus17 = true //trm1.checkStatus(6) == 1
                trmStatus21 = true //trm2.checkStatus(0) == 1
                trmStatus22 = true //trm2.checkStatus(1) == 1
                trmStatus23 = true //trm2.checkStatus(2) == 1
                trmStatus24 = true //trm2.checkStatus(3) == 1
                trmStatus25 = true //trm2.checkStatus(4) == 1
                trmStatus26 = true //trm2.checkStatus(5) == 1
                trmStatus31 = true //trm3.checkStatus(0) == 1
                trmStatus32 = true //trm3.checkStatus(1) == 1
                trmStatus33 = true //trm3.checkStatus(2) == 1
                trmStatus34 = true //trm3.checkStatus(3) == 1
                trmStatus35 = true //trm3.checkStatus(4) == 1
                trmStatus36 = true //trm3.checkStatus(5) == 1
                sleep(100)
            }
        }
    }

    private fun setResult() {
        if (controller.cause.isNotEmpty()) {
            appendMessageToLog(LogTag.ERROR, "Испытание прервано по причине: ${controller.cause}")
            soundError()
        } else if (!controller.isDevicesResponding()) {
            appendMessageToLog(LogTag.ERROR, "Испытание прервано по причине: потеряна связь с устройствами")
            soundError()
        } else {
            appendMessageToLog(LogTag.MESSAGE, "Испытание завершено успешно")
        }
    }

    private fun finalizeExperiment() {
        isExperimentEnded = true
        controller.isExperimentRunning = false
//        owenPR.offAllKMs()
        CommunicationModel.clearPollingRegisters()
        runLater {
            mainView.labelTestStatus.text = ""
            mainView.tableViewTestTime.isDisable = false
            mainView.textFieldTimeCycle.isDisable = false
            mainView.buttonStart.isDisable = false
            mainView.buttonStop.isDisable = true
            mainView.mainMenubar.isDisable = false
            mainView.checkBoxTest1.isDisable = false
            mainView.checkBoxTest2.isDisable = false
            mainView.checkBoxTest3.isDisable = false
            mainView.textFieldMaxTemp.isDisable = false
        }
    }
}