package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.scene.text.Text
import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrModel
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Model
import ru.avem.posvanna.communication.model.devices.parma.ParmaModel
import ru.avem.posvanna.utils.LogTag
import ru.avem.posvanna.utils.Toast
import ru.avem.posvanna.utils.sleep
import ru.avem.posvanna.view.MainView
import tornadofx.add
import tornadofx.runLater
import tornadofx.style
import java.text.SimpleDateFormat
import kotlin.experimental.and

class Test1Controller : TestController() {
    val controller: MainViewController by inject()
    val mainView: MainView by inject()

    private var logBuffer: String? = null

    @Volatile
    var isExperimentRunning: Boolean = true

    @Volatile
    var isExperimentEnded: Boolean = true

    //region переменные для значений с приборов

    @Volatile
    private var measuringU11: Double = 0.0

    @Volatile
    private var measuringI11: Double = 0.0

    @Volatile
    private var measuringt11: Double = 0.0

    @Volatile
    private var measuringU12: Double = 0.0

    @Volatile
    private var measuringI12: Double = 0.0

    @Volatile
    private var measuringt12: Double = 0.0

    @Volatile
    private var measuringU13: Double = 0.0

    @Volatile
    private var measuringI13: Double = 0.0

    @Volatile
    private var measuringt13: Double = 0.0

    @Volatile
    private var measuringU14: Double = 0.0

    @Volatile
    private var measuringI14: Double = 0.0

    @Volatile
    private var measuringt14: Double = 0.0

    @Volatile
    private var measuringU15: Double = 0.0

    @Volatile
    private var measuringI15: Double = 0.0

    @Volatile
    private var measuringt15: Double = 0.0

    @Volatile
    private var measuringU16: Double = 0.0

    @Volatile
    private var measuringI16: Double = 0.0

    @Volatile
    private var measuringt16: Double = 0.0

    @Volatile
    private var measuringU21: Double = 0.0

    @Volatile
    private var measuringI21: Double = 0.0

    @Volatile
    private var measuringt21: Double = 0.0

    @Volatile
    private var measuringU22: Double = 0.0

    @Volatile
    private var measuringI22: Double = 0.0

    @Volatile
    private var measuringt22: Double = 0.0

    @Volatile
    private var measuringU23: Double = 0.0

    @Volatile
    private var measuringI23: Double = 0.0

    @Volatile
    private var measuringt23: Double = 0.0

    @Volatile
    private var measuringU24: Double = 0.0

    @Volatile
    private var measuringI24: Double = 0.0

    @Volatile
    private var measuringt24: Double = 0.0

    @Volatile
    private var measuringU25: Double = 0.0

    @Volatile
    private var measuringI25: Double = 0.0

    @Volatile
    private var measuringt25: Double = 0.0

    @Volatile
    private var measuringU26: Double = 0.0

    @Volatile
    private var measuringI26: Double = 0.0

    @Volatile
    private var measuringt26: Double = 0.0

    @Volatile
    private var measuringU31: Double = 0.0

    @Volatile
    private var measuringI31: Double = 0.0

    @Volatile
    private var measuringt31: Double = 0.0

    @Volatile
    private var measuringU32: Double = 0.0

    @Volatile
    private var measuringI32: Double = 0.0

    @Volatile
    private var measuringt32: Double = 0.0

    @Volatile
    private var measuringU33: Double = 0.0

    @Volatile
    private var measuringI33: Double = 0.0

    @Volatile
    private var measuringt33: Double = 0.0

    @Volatile
    private var measuringU34: Double = 0.0

    @Volatile
    private var measuringI34: Double = 0.0

    @Volatile
    private var measuringt34: Double = 0.0

    @Volatile
    private var measuringU35: Double = 0.0

    @Volatile
    private var measuringI35: Double = 0.0

    @Volatile
    private var measuringt35: Double = 0.0

    @Volatile
    private var measuringU36: Double = 0.0

    @Volatile
    private var measuringI36: Double = 0.0

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

    var cause: String = ""
        set(value) {
            if (value != "") {
                isExperimentRunning = false
            }
            field = value
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
                cause = "Открыта дверь шкафа"
            }
            if (doorZone1) {
                cause = "Открыта дверь зоны 1"
            }
            if (doorZone2) {
                cause = "Открыта дверь зоны 2 "
            }
            if (doorZone3) {
                cause = "Открыта дверь зоны 3 "
            }
            if (stopButton) {
                cause = "Нажали кнопку СТОП"
            }
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.DD2, OwenPrModel.FIXED_STATES_REGISTER_2) { value ->
            currentI1 = value.toShort() and 1 > 0
            currentI2 = value.toShort() and 2 > 0
            currentI3 = value.toShort() and 4 > 0
            if (currentI1) {
                cause = "Токовая защита лопасти 1"
            }
            if (currentI2) {
                cause = "Токовая защита лопасти 2"
            }
            if (currentI3) {
                cause = "Токовая защита лопасти 3"
            }
        }
        //endregion
        //region parma poll
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IA) { value ->
            measuringI11 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IB) { value ->
            measuringI12 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IC) { value ->
            measuringI13 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.UA) { value ->
            measuringU11 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.UB) { value ->
            measuringU12 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.UC) { value ->
            measuringU13 = value.toShort() / 100.0
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.IA) { value ->
            measuringI21 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.IB) { value ->
            measuringI22 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.IC) { value ->
            measuringI23 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.UA) { value ->
            measuringU21 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.UB) { value ->
            measuringU22 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.UC) { value ->
            measuringU23 = value.toShort() / 100.0
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.IA) { value ->
            measuringI31 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.IB) { value ->
            measuringI32 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.IC) { value ->
            measuringI33 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.UA) { value ->
            measuringU31 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.UB) { value ->
            measuringU32 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.UC) { value ->
            measuringU33 = value.toShort() / 100.0
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IA) { value ->
            measuringI14 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IB) { value ->
            measuringI15 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.IC) { value ->
            measuringI16 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.UA) { value ->
            measuringU14 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.UB) { value ->
            measuringU15 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA1, ParmaModel.UC) { value ->
            measuringU16 = value.toShort() / 100.0
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.IA) { value ->
            measuringI24 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.IB) { value ->
            measuringI25 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.IC) { value ->
            measuringI26 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.UA) { value ->
            measuringU24 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.UB) { value ->
            measuringU25 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA2, ParmaModel.UC) { value ->
            measuringU26 = value.toShort() / 100.0
        }

        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.IA) { value ->
            measuringI34 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.IB) { value ->
            measuringI35 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.IC) { value ->
            measuringI36 = value.toShort() / 5000.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.UA) { value ->
            measuringU34 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.UB) { value ->
            measuringU35 = value.toShort() / 100.0
        }
        CommunicationModel.startPoll(CommunicationModel.DeviceID.PARMA3, ParmaModel.UC) { value ->
            measuringU36 = value.toShort() / 100.0
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

    fun startTest() {
        cause = ""

        isExperimentEnded = false

        if (isExperimentRunning) {
            appendMessageToLog(LogTag.DEBUG, "Инициализация устройств")
        }

        while (isExperimentRunning) {
            sleep(1000)
        }

        while (!controller.isDevicesResponding() && isExperimentRunning) {
            CommunicationModel.checkDevices()
            sleep(100)
        }

        if (isExperimentRunning && controller.isDevicesResponding()) {
            CommunicationModel.addWritingRegister(
                CommunicationModel.DeviceID.DD2,
                OwenPrModel.RESET_DOG,
                1.toShort()
            )
            owenPR.initOwenPR()
            startPollDevices()
            sleep(1000)
        }

        if (!startButton && isExperimentRunning && controller.isDevicesResponding()) {
            runLater {
                Toast.makeText("Нажмите кнопку ПУСК").show(Toast.ToastType.WARNING)
            }
        }
        var timeToStart = 300
        while (!startButton && isExperimentRunning && controller.isDevicesResponding() && timeToStart-- > 0) {
            appendOneMessageToLog(LogTag.DEBUG, "Нажмите кнопку ПУСК")
            sleep(100)
        }

        if (!startButton) {
            cause = "Не нажата кнопка ПУСК"
        }

        if (isExperimentRunning && controller.isDevicesResponding()) {
            appendMessageToLog(LogTag.DEBUG, "Подготовка стенда")
            appendMessageToLog(LogTag.DEBUG, "Сбор схемы")
        }

        setResult()

        finalizeExperiment()
    }

    private fun sleepWhile(timeSecond: Int) {
        var timer = timeSecond * 10
        while (isExperimentRunning && timer-- > 0) {
            sleep(100)
        }
    }

    private fun setResult() {
        if (!controller.isDevicesResponding()) {
            appendMessageToLog(LogTag.ERROR, "Испытание прервано по причине: \nпотеряна связь с устройствами")
        } else if (cause.isNotEmpty()) {
            appendMessageToLog(LogTag.ERROR, "Испытание прервано по причине: ${cause}")
        } else {
            appendMessageToLog(LogTag.MESSAGE, "Испытание завершено успешно")
        }
    }

    private fun finalizeExperiment() {
        isExperimentEnded = true
        owenPR.offAllKMs()
        CommunicationModel.clearPollingRegisters()

    }
}