package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.scene.text.Text
import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrModel
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Model
import ru.avem.posvanna.communication.model.devices.parma.ParmaModel
import ru.avem.posvanna.utils.*
import ru.avem.posvanna.view.MainView
import tornadofx.add
import tornadofx.runLater
import tornadofx.seconds
import tornadofx.style
import java.text.SimpleDateFormat
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
    private var isStartButton: Boolean = false

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
    private var doorShkaf: Boolean = false

    @Volatile
    private var doorZone1: Boolean = false

    @Volatile
    private var doorZone2: Boolean = false

    @Volatile
    private var doorZone3: Boolean = false

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
    //endregion

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

    private fun startPollDevices() {
        //region pr pool
        CommunicationModel.startPoll(CommunicationModel.DeviceID.DD2, OwenPrModel.FIXED_STATES_REGISTER_1) { value ->
            doorShkaf = value.toShort() and 1 > 0
            doorZone1 = value.toShort() and 2 > 0
            doorZone2 = value.toShort() and 4 > 0
            doorZone3 = value.toShort() and 8 > 0
            startButton = value.toShort() and 32 > 0
            stopButton = value.toShort() and 64 > 0
            if (doorShkaf) {
                controller.cause = "Открыта дверь шкафа"
            }
            if (doorZone1) {
                controller.cause = "Открыта дверь зоны 1"
            }
            if (doorZone2) {
                controller.cause = "Открыта дверь зоны 2 "
            }
            if (doorZone3) {
                controller.cause = "Открыта дверь зоны 3 "
            }
            if (stopButton) {
                controller.cause = "Нажали кнопку СТОП"
            }
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.DD2, OwenPrModel.FIXED_STATES_REGISTER_2) { value ->
            currentI1 = value.toShort() and 1 > 0
            currentI2 = value.toShort() and 2 > 0
            currentI3 = value.toShort() and 4 > 0
            if (currentI1) {
                controller.cause = "Токовая защита лопасти 1"
            }
            if (currentI2) {
                controller.cause = "Токовая защита лопасти 2"
            }
            if (currentI3) {
                controller.cause = "Токовая защита лопасти 3"
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
        runLater {
            controller.tableValuesTest1[0].section1t.value = formatRealNumber(measuringt11).toString()
        }
        if (measuringt11 > 45) {
            controller.cause = "Температура 1 лопасти 1 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_2) { value ->
            measuringt12 = value.toDouble()
        }
        if (measuringt12 > 45) {
            controller.cause = "Температура 1 лопасти 2 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_3) { value ->
            measuringt13 = value.toDouble()
        }
        if (measuringt13 > 45) {
            controller.cause = "Температура 1 лопасти 3 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_4) { value ->
            measuringt14 = value.toDouble()
        }
        if (measuringt14 > 45) {
            controller.cause = "Температура 1 лопасти 4 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_5) { value ->
            measuringt15 = value.toDouble()
        }
        if (measuringt15 > 45) {
            controller.cause = "Температура 1 лопасти 5 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_6) { value ->
            measuringt16 = value.toDouble()
        }
        if (measuringt16 > 45) {
            controller.cause = "Температура 1 лопасти 6 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM1, Trm136Model.TEMPERATURE_7) { value ->
            measuringt17 = value.toDouble()
        }
        if (measuringt17 > 45) {
            controller.cause = "Температура воды больше 45°С"
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_1) { value ->
            measuringt21 = value.toDouble()
        }
        if (measuringt21 > 45) {
            controller.cause = "Температура 2 лопасти 1 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_2) { value ->
            measuringt22 = value.toDouble()
        }
        if (measuringt22 > 45) {
            controller.cause = "Температура 2 лопасти 2 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_3) { value ->
            measuringt23 = value.toDouble()
        }
        if (measuringt23 > 45) {
            controller.cause = "Температура 2 лопасти 3 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_4) { value ->
            measuringt24 = value.toDouble()
        }
        if (measuringt24 > 45) {
            controller.cause = "Температура 2 лопасти 4 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_5) { value ->
            measuringt25 = value.toDouble()
        }
        if (measuringt25 > 45) {
            controller.cause = "Температура 2 лопасти 5 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM2, Trm136Model.TEMPERATURE_6) { value ->
            measuringt26 = value.toDouble()
        }
        if (measuringt26 > 45) {
            controller.cause = "Температура 2 лопасти 6 секции больше 45°С"
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_1) { value ->
            measuringt31 = value.toDouble()
        }
        if (measuringt31 > 45) {
            controller.cause = "Температура 3 лопасти 1 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_2) { value ->
            measuringt32 = value.toDouble()
        }
        if (measuringt32 > 45) {
            controller.cause = "Температура 3 лопасти 2 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_3) { value ->
            measuringt33 = value.toDouble()
        }
        if (measuringt33 > 45) {
            controller.cause = "Температура 3 лопасти 3 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_4) { value ->
            measuringt34 = value.toDouble()
        }
        if (measuringt34 > 45) {
            controller.cause = "Температура 3 лопасти 4 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_5) { value ->
            measuringt35 = value.toDouble()
        }
        if (measuringt35 > 45) {
            controller.cause = "Температура 3 лопасти 5 секции больше 45°С"
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.TRM3, Trm136Model.TEMPERATURE_6) { value ->
            measuringt36 = value.toDouble()
        }
        if (measuringt36 > 45) {
            controller.cause = "Температура 3 лопасти 6 секции больше 45°С"
        }
        //endregion
    }

    @ExperimentalTime
    fun startTest() {
        controller.cause = ""

        isExperimentEnded = false

        if (controller.isExperimentRunning) {
            appendMessageToLog(LogTag.DEBUG, "Инициализация устройств")
        }

        if (controller.isExperimentRunning && controller.isDevicesResponding()) {
            CommunicationModel.addWritingRegister(
                CommunicationModel.DeviceID.DD2,
                OwenPrModel.RESET_DOG,
                1.toShort()
            )
            owenPR.initOwenPR()
            sleep(1000)
            startPollDevices()
            sleep(1000)
        }


        while (controller.isExperimentRunning) {
            runLater {
                controller.tableValuesTest3[0].section31t.value = formatRealNumber(measuringt31).toString()
            }
            sleep(100)
        }
//        while (!controller.isDevicesResponding() && controller.isExperimentRunning) {
//            CommunicationModel.checkDevices()
//            sleep(100)
//        }
//

//
//        if (!startButton && controller.isExperimentRunning && controller.isDevicesResponding()) {
//            runLater {
//                Toast.makeText("Нажмите кнопку ПУСК").show(Toast.ToastType.WARNING)
//            }
//        }
//        var timeToStart = 300
//        while (!startButton && controller.isExperimentRunning && controller.isDevicesResponding() && timeToStart-- > 0) {
//            appendOneMessageToLog(LogTag.DEBUG, "Нажмите кнопку ПУСК")
//            sleep(100)
//        }
//
//        if (!startButton) {
//            cause = "Не нажата кнопка ПУСК"
//        }

        if (controller.isExperimentRunning && controller.isDevicesResponding()) {
            appendMessageToLog(LogTag.DEBUG, "Подготовка стенда")
            appendMessageToLog(LogTag.DEBUG, "Сбор схемы")
        }

//        owenPR.on11()
//        owenPR.on21()
//        owenPR.on31()
//        sleepWhile(5)
//        appendOneMessageToLog(LogTag.MESSAGE, "Ток IA = " + formatRealNumber(measuringIA).toString())
//        appendOneMessageToLog(LogTag.MESSAGE, "Напряжение UA = " + formatRealNumber(measuringUA).toString())
//        sleepWhile(5)
//        owenPR.off11()
//        owenPR.off21()
//        owenPR.off31()

        var cycles = mainView.textFieldTimeCycle.text.toInt()

        val allTime =
            ((controller.tableValuesTest4[0].start.value.toDouble() * 60) + (controller.tableValuesTest4[0].pause.value.toDouble() * 60) +
                    (controller.tableValuesTest4[1].start.value.toDouble() * 60) + (controller.tableValuesTest4[1].pause.value.toDouble() * 60) +
                    (controller.tableValuesTest4[2].start.value.toDouble() * 60) + (controller.tableValuesTest4[2].pause.value.toDouble() * 60) +
                    (controller.tableValuesTest4[3].start.value.toDouble() * 60) + (controller.tableValuesTest4[3].pause.value.toDouble() * 60) +
                    (controller.tableValuesTest4[4].start.value.toDouble() * 60) + (controller.tableValuesTest4[4].pause.value.toDouble() * 60) +
                    (controller.tableValuesTest4[5].start.value.toDouble() * 60) + (controller.tableValuesTest4[5].pause.value.toDouble() * 60)
                    * mainView.textFieldTimeCycle.text.toDouble()).toInt()
        val callbackTimer = CallbackTimer(
            tickPeriod = 1.seconds, tickTimes = allTime,
            tickJob = {
                if (!controller.isExperimentRunning) it.stop()
                runLater {
                    mainView.labelTimeRemaining.text =
                        "Осталось всего: " + toHHmmss((allTime - it.getCurrentTicks()) * 1000L)
                }
            },
            onFinishJob = {
            })

//        while (controller.isExperimentRunning && controller.isDevicesResponding() && cycles-- > 0) {
//            if (mainView.checkBoxTest1.isSelected) {
//                owenPR.on11()
//            }
//            if (mainView.checkBoxTest2.isSelected) {
//                owenPR.on21()
//            }
//            if (mainView.checkBoxTest3.isSelected) {
//                owenPR.on31()
//            }
//
//            val timeStart = (controller.tableValuesTest4[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 1 cекции. Осталось: " + toHHmmss((timeStart - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause = (controller.tableValuesTest4[0].pause.value.toDouble() * 60 * 60).toInt()
//            val textFieldTimePause = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 1 cекции. Осталось: " + toHHmmss((timePause - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && textFieldTimePause.isRunning) {
//                sleep(100)
//            }
//        }


        owenPR.offAllKMs()
        setResult()

        finalizeExperiment()
        runLater {
            mainView.labelTestStatus.text = "Статус: стоп"
        }
    }

    private fun sleepWhile(timeSecond: Int) {
        var timer = timeSecond * 10
        while (controller.isExperimentRunning && timer-- > 0 && controller.isDevicesResponding()) {
            sleep(100)
        }
    }

    private fun setResult() {
        if (!controller.isDevicesResponding()) {
            appendMessageToLog(LogTag.ERROR, "Испытание прервано по причине: \nпотеряна связь с устройствами")
        } else if (controller.cause.isNotEmpty()) {
            appendMessageToLog(LogTag.ERROR, "Испытание прервано по причине: ${controller.cause}")
        } else {
            appendMessageToLog(LogTag.MESSAGE, "Испытание завершено успешно")
        }
    }

    private fun finalizeExperiment() {
        isExperimentEnded = true
//        owenPR.offAllKMs()
        CommunicationModel.clearPollingRegisters()

    }
}