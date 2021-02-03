package ru.avem.posvanna.controllers

import javafx.application.Platform
import javafx.scene.text.Text
import org.jetbrains.exposed.sql.transactions.transaction
import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrModel
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Model
import ru.avem.posvanna.communication.model.devices.parma.ParmaModel
import ru.avem.posvanna.database.entities.Protocol
import ru.avem.posvanna.utils.*
import ru.avem.posvanna.view.MainView
import tornadofx.add
import tornadofx.runLater
import tornadofx.seconds
import tornadofx.style
import java.text.SimpleDateFormat
import kotlin.concurrent.thread
import kotlin.experimental.and
import kotlin.random.Random
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

    private var cycles: Int = 0

    //region листы для БД
    private var listOfValues11 = mutableListOf<String>()
    private var listOfValues12 = mutableListOf<String>()
    private var listOfValues13 = mutableListOf<String>()
    private var listOfValues14 = mutableListOf<String>()
    private var listOfValues15 = mutableListOf<String>()
    private var listOfValues16 = mutableListOf<String>()
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

        for (i in 0..10000) {
            listOfValues11.add(String.format("%.1f", Random.nextDouble()))
            listOfValues12.add(String.format("%.1f", Random.nextDouble()))
            listOfValues13.add(String.format("%.1f", Random.nextDouble()))
            listOfValues14.add(String.format("%.1f", Random.nextDouble()))
            listOfValues15.add(String.format("%.1f", Random.nextDouble()))
            listOfValues16.add(String.format("%.1f", Random.nextDouble()))
            listOfValues21.add(String.format("%.1f", Random.nextDouble()))
            listOfValues22.add(String.format("%.1f", Random.nextDouble()))
            listOfValues23.add(String.format("%.1f", Random.nextDouble()))
            listOfValues24.add(String.format("%.1f", Random.nextDouble()))
            listOfValues25.add(String.format("%.1f", Random.nextDouble()))
            listOfValues26.add(String.format("%.1f", Random.nextDouble()))
            listOfValues31.add(String.format("%.1f", Random.nextDouble()))
            listOfValues32.add(String.format("%.1f", Random.nextDouble()))
            listOfValues33.add(String.format("%.1f", Random.nextDouble()))
            listOfValues34.add(String.format("%.1f", Random.nextDouble()))
            listOfValues35.add(String.format("%.1f", Random.nextDouble()))
            listOfValues36.add(String.format("%.1f", Random.nextDouble()))
        }
        saveProtocolToDB()
//
//        controller.cause = ""
//
//        isExperimentEnded = false
//
//        if (controller.isExperimentRunning) {
//            appendMessageToLog(LogTag.DEBUG, "Инициализация устройств")
//        }
//
//        if (controller.isExperimentRunning && controller.isDevicesResponding()) {
//            CommunicationModel.addWritingRegister(
//                CommunicationModel.DeviceID.DD2,
//                OwenPrModel.RESET_DOG,
//                1.toShort()
//            )
//            owenPR.initOwenPR()
//            sleep(1000)
//            startPollDevices()
//            sleep(1000)
//        }
//
//        if (controller.isExperimentRunning) {
//            getValuesInTable()
//        }
//
//        while (controller.isExperimentRunning) {
//            runLater {
//                controller.tableValuesTest3[0].section31t.value = formatRealNumber(measuringt31).toString()
//            }
//            sleep(100)
//        }
//        while (!controller.isDevicesResponding() && controller.isExperimentRunning) {
//            CommunicationModel.checkDevices()
//            sleep(100)
//        }
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
//            controller.cause = "Не нажата кнопка ПУСК"
//        }
//
//        if (controller.isExperimentRunning && controller.isDevicesResponding()) {
//            appendMessageToLog(LogTag.DEBUG, "Подготовка стенда")
//            appendMessageToLog(LogTag.DEBUG, "Сбор схемы")
//        }
//
//        cycles = mainView.textFieldTimeCycle.text.toInt()
//
//        val allTime =
//            ((controller.tableValuesTestTime[0].start.value.toDouble() * 60) + (controller.tableValuesTestTime[0].pause.value.toDouble() * 60) +
//                    (controller.tableValuesTestTime[1].start.value.toDouble() * 60) + (controller.tableValuesTestTime[1].pause.value.toDouble() * 60) +
//                    (controller.tableValuesTestTime[2].start.value.toDouble() * 60) + (controller.tableValuesTestTime[2].pause.value.toDouble() * 60) +
//                    (controller.tableValuesTestTime[3].start.value.toDouble() * 60) + (controller.tableValuesTestTime[3].pause.value.toDouble() * 60) +
//                    (controller.tableValuesTestTime[4].start.value.toDouble() * 60) + (controller.tableValuesTestTime[4].pause.value.toDouble() * 60) +
//                    (controller.tableValuesTestTime[5].start.value.toDouble() * 60) + (controller.tableValuesTestTime[5].pause.value.toDouble() * 60)
//                    * mainView.textFieldTimeCycle.text.toDouble()).toInt()
//        val callbackTimer = CallbackTimer(
//            tickPeriod = 1.seconds, tickTimes = allTime,
//            tickJob = {
//                if (!controller.isExperimentRunning) it.stop()
//                runLater {
//                    mainView.labelTimeRemaining.text =
//                        "Осталось всего: " + toHHmmss((allTime - it.getCurrentTicks()) * 1000L)
//                }
//            },
//            onFinishJob = {
//            })
//
//        if (controller.isExperimentRunning) {
//            startRecordValues()
//        }
//
//        while (controller.isExperimentRunning && controller.isDevicesResponding() && cycles-- > 0) {
//
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
//            val timeStart1 = (controller.tableValuesTestTime[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart1 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart1,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 1 cекции. Осталось: " + toHHmmss((timeStart1 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart1.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause1 = (controller.tableValuesTestTime[0].pause.value.toDouble() * 60 * 60).toInt()
//            val callbackTimerPause1 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause1,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 1 cекции. Осталось: " + toHHmmss((timePause1 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerPause1.isRunning) {
//                sleep(100)
//            }
//            owenPR.offAllKMs()
//
//            if (mainView.checkBoxTest1.isSelected) {
//                owenPR.on12()
//            }
//            if (mainView.checkBoxTest2.isSelected) {
//                owenPR.on22()
//            }
//            if (mainView.checkBoxTest3.isSelected) {
//                owenPR.on32()
//            }
//
//            val timeStart2 = (controller.tableValuesTestTime[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart2 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart2,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 1 cекции. Осталось: " + toHHmmss((timeStart2 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart2.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause2 = (controller.tableValuesTestTime[0].pause.value.toDouble() * 60 * 60).toInt()
//            val callbackTimerPause2 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause2,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 1 cекции. Осталось: " + toHHmmss((timePause2 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerPause2.isRunning) {
//                sleep(100)
//            }
//            owenPR.offAllKMs()
//
//            if (mainView.checkBoxTest1.isSelected) {
//                owenPR.on13()
//            }
//            if (mainView.checkBoxTest2.isSelected) {
//                owenPR.on23()
//            }
//            if (mainView.checkBoxTest3.isSelected) {
//                owenPR.on33()
//            }
//
//            val timeStart3 = (controller.tableValuesTestTime[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart3 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart3,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 3 cекции. Осталось: " + toHHmmss((timeStart3 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart3.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause3 = (controller.tableValuesTestTime[0].pause.value.toDouble() * 60 * 60).toInt()
//            val callbackTimerPause3 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause3,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 3 cекции. Осталось: " + toHHmmss((timePause3 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerPause3.isRunning) {
//                sleep(100)
//            }
//            owenPR.offAllKMs()
//
//            if (mainView.checkBoxTest1.isSelected) {
//                owenPR.on14()
//            }
//            if (mainView.checkBoxTest2.isSelected) {
//                owenPR.on24()
//            }
//            if (mainView.checkBoxTest3.isSelected) {
//                owenPR.on34()
//            }
//
//            val timeStart4 = (controller.tableValuesTestTime[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart4 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart4,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 4 cекции. Осталось: " + toHHmmss((timeStart4 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart4.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause4 = (controller.tableValuesTestTime[0].pause.value.toDouble() * 60 * 60).toInt()
//            val callbackTimerPause4 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause4,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 4 cекции. Осталось: " + toHHmmss((timePause4 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerPause4.isRunning) {
//                sleep(100)
//            }
//            owenPR.offAllKMs()
//
//            if (mainView.checkBoxTest1.isSelected) {
//                owenPR.on15()
//            }
//            if (mainView.checkBoxTest2.isSelected) {
//                owenPR.on25()
//            }
//            if (mainView.checkBoxTest3.isSelected) {
//                owenPR.on35()
//            }
//
//            val timeStart5 = (controller.tableValuesTestTime[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart5 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart5,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 5 cекции. Осталось: " + toHHmmss((timeStart5 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart5.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause5 = (controller.tableValuesTestTime[0].pause.value.toDouble() * 60 * 60).toInt()
//            val callbackTimerPause5 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause5,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 5 cекции. Осталось: " + toHHmmss((timePause5 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerPause5.isRunning) {
//                sleep(100)
//            }
//            owenPR.offAllKMs()
//
//            if (mainView.checkBoxTest1.isSelected) {
//                owenPR.on16()
//            }
//            if (mainView.checkBoxTest2.isSelected) {
//                owenPR.on26()
//            }
//            if (mainView.checkBoxTest3.isSelected) {
//                owenPR.on36()
//            }
//
//            val timeStart6 = (controller.tableValuesTestTime[0].start.value.toDouble() * 60).toInt()
//            val callbackTimerStart6 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timeStart6,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: нагрев 6 cекции. Осталось: " + toHHmmss((timeStart6 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerStart6.isRunning && controller.isDevicesResponding()) {
//                sleep(100)
//            }
//
//            owenPR.offAllKMs()
//
//
//            val timePause6 = (controller.tableValuesTestTime[0].pause.value.toDouble() * 60 * 60).toInt()
//            val callbackTimerPause6 = CallbackTimer(
//                tickPeriod = 1.seconds, tickTimes = timePause6,
//                tickJob = {
//                    if (!controller.isExperimentRunning) {
//                        it.stop()
//                    } else {
//                        runLater {
//                            mainView.labelTestStatus.text =
//                                "Статус: пауза 6 cекции. Осталось: " + toHHmmss((timePause6 - it.getCurrentTicks()) * 1000L)
//                        }
//                    }
//                },
//                onFinishJob = {
//                })
//
//            while (controller.isExperimentRunning && callbackTimerPause6.isRunning) {
//                sleep(100)
//            }
//            owenPR.offAllKMs()
//        }

//        if (listOfValues11.isNotEmpty()) {
//            saveProtocolToDB()
//        }
//        owenPR.offAllKMs()
//        setResult()
//
//        finalizeExperiment()
//        runLater {
//            mainView.labelTestStatus.text = "Статус: стоп"
//        }
    }

    private fun startRecordValues() {
        thread(isDaemon = true) {
            while (cycles > 0) {
                listOfValues11.add(String.format("%.1f", measuringt11))
                listOfValues12.add(String.format("%.1f", measuringt12))
                listOfValues13.add(String.format("%.1f", measuringt13))
                listOfValues14.add(String.format("%.1f", measuringt14))
                listOfValues15.add(String.format("%.1f", measuringt15))
                listOfValues16.add(String.format("%.1f", measuringt16))
                listOfValues21.add(String.format("%.1f", measuringt21))
                listOfValues22.add(String.format("%.1f", measuringt22))
                listOfValues23.add(String.format("%.1f", measuringt23))
                listOfValues24.add(String.format("%.1f", measuringt24))
                listOfValues25.add(String.format("%.1f", measuringt25))
                listOfValues26.add(String.format("%.1f", measuringt26))
                listOfValues31.add(String.format("%.1f", measuringt31))
                listOfValues32.add(String.format("%.1f", measuringt32))
                listOfValues33.add(String.format("%.1f", measuringt33))
                listOfValues34.add(String.format("%.1f", measuringt34))
                listOfValues35.add(String.format("%.1f", measuringt35))
                listOfValues36.add(String.format("%.1f", measuringt36))
                sleep(1000)
            }

        }
    }

    private fun saveProtocolToDB() {
        val dateFormatter = SimpleDateFormat("dd.MM.y")
        val timeFormatter = SimpleDateFormat("HH:mm:ss")
        val unixTime = System.currentTimeMillis()

        transaction {
            Protocol.new {
                date = dateFormatter.format(unixTime).toString()
                time = timeFormatter.format(unixTime).toString()
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
            }
        }
    }

    private fun getValuesInTable() {
        thread(isDaemon = true) {
            while (controller.isExperimentRunning) {
                runLater {
                    controller.tableValuesTest21[0].voltage.value = formatRealNumber(measuringUA).toString()
                    controller.tableValuesTest21[0].ampere.value = formatRealNumber(measuringIA).toString()
                    controller.tableValuesTest22[0].voltage.value = formatRealNumber(measuringUB).toString()
                    controller.tableValuesTest22[0].ampere.value = formatRealNumber(measuringIB).toString()
                    controller.tableValuesTest23[0].voltage.value = formatRealNumber(measuringUC).toString()
                    controller.tableValuesTest23[0].ampere.value = formatRealNumber(measuringIC).toString()
                    controller.tableValuesTest1[0].section1t.value = formatRealNumber(measuringt11).toString()
                    controller.tableValuesTest1[1].section1t.value = formatRealNumber(measuringt12).toString()
                    controller.tableValuesTest1[2].section1t.value = formatRealNumber(measuringt13).toString()
                    controller.tableValuesTest1[3].section1t.value = formatRealNumber(measuringt14).toString()
                    controller.tableValuesTest1[4].section1t.value = formatRealNumber(measuringt15).toString()
                    controller.tableValuesTest1[5].section1t.value = formatRealNumber(measuringt16).toString()
                    controller.tableValuesTest2[0].section21t.value = formatRealNumber(measuringt21).toString()
                    controller.tableValuesTest2[1].section21t.value = formatRealNumber(measuringt22).toString()
                    controller.tableValuesTest2[2].section21t.value = formatRealNumber(measuringt23).toString()
                    controller.tableValuesTest2[3].section21t.value = formatRealNumber(measuringt24).toString()
                    controller.tableValuesTest2[4].section21t.value = formatRealNumber(measuringt25).toString()
                    controller.tableValuesTest2[5].section21t.value = formatRealNumber(measuringt26).toString()
                    controller.tableValuesTest3[0].section31t.value = formatRealNumber(measuringt31).toString()
                    controller.tableValuesTest3[1].section31t.value = formatRealNumber(measuringt32).toString()
                    controller.tableValuesTest3[2].section31t.value = formatRealNumber(measuringt33).toString()
                    controller.tableValuesTest3[3].section31t.value = formatRealNumber(measuringt34).toString()
                    controller.tableValuesTest3[4].section31t.value = formatRealNumber(measuringt35).toString()
                    controller.tableValuesTest3[5].section31t.value = formatRealNumber(measuringt36).toString()
                }
                sleep(100)
            }
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