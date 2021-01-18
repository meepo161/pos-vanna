package ru.avem.posvanna.communication.model

import ru.avem.posvanna.app.Pos.Companion.isAppRunning
import ru.avem.posvanna.communication.Connection
import ru.avem.posvanna.communication.adapters.modbusrtu.ModbusRTUAdapter
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrController
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Controller
import ru.avem.posvanna.communication.model.devices.parma.ParmaController
import ru.avem.posvanna.communication.utils.SerialParameters
import java.lang.Thread.sleep
import kotlin.concurrent.thread

object CommunicationModel {
    @Suppress("UNUSED_PARAMETER")
    enum class DeviceID(description: String) {
        DD2("ПР"),
        PARMA1("ПАРМА1"),
        PARMA2("ПАРМА2"),
        PARMA3("ПАРМА3"),
        PARMA4("ПАРМА4"),
        PARMA5("ПАРМА5"),
        PARMA6("ПАРМА6"),
        TRM1("TRM1"),
        TRM2("TRM2"),
        TRM3("TRM3")
    }

    private var isConnected = false

    private val connection = Connection(
            adapterName = "CP2103 USB to RS-485",
            serialParameters = SerialParameters(8, 0, 1, 115200),
            timeoutRead = 100,
            timeoutWrite = 100
    ).apply {
        connect()
        isConnected = true
    }

    private val modbusAdapter = ModbusRTUAdapter(connection)

    private val deviceControllers: Map<DeviceID, IDeviceController> = mapOf(
            DeviceID.PARMA1 to ParmaController(DeviceID.PARMA1.toString(), modbusAdapter, 1),
            DeviceID.PARMA2 to ParmaController(DeviceID.PARMA2.toString(), modbusAdapter, 2),
            DeviceID.PARMA3 to ParmaController(DeviceID.PARMA3.toString(), modbusAdapter, 3),
            DeviceID.PARMA4 to ParmaController(DeviceID.PARMA4.toString(), modbusAdapter, 4),
            DeviceID.PARMA5 to ParmaController(DeviceID.PARMA5.toString(), modbusAdapter, 5),
            DeviceID.PARMA6 to ParmaController(DeviceID.PARMA6.toString(), modbusAdapter, 6),
            DeviceID.DD2 to OwenPrController(DeviceID.DD2.toString(), modbusAdapter, 7),
            DeviceID.TRM1 to Trm136Controller(DeviceID.TRM1.toString(), modbusAdapter, 8),
            DeviceID.TRM2 to Trm136Controller(DeviceID.TRM2.toString(), modbusAdapter, 9),
            DeviceID.TRM3 to Trm136Controller(DeviceID.TRM3.toString(), modbusAdapter, 10)
    )

    init {
        thread(isDaemon = true) {
            while (isAppRunning) {
                if (isConnected) {
                    deviceControllers.values.forEach {
                        it.readPollingRegisters()
                    }
                }
                sleep(100)
            }
        }
        thread(isDaemon = true) {
            while (isAppRunning) {
                if (isConnected) {
                    deviceControllers.values.forEach {
                        it.writeWritingRegisters()
                    }
                }
                sleep(100)
            }
        }
    }

    fun getDeviceById(deviceID: DeviceID) = deviceControllers[deviceID] ?: error("Не определено $deviceID")

    fun startPoll(deviceID: DeviceID, registerID: String, block: (Number) -> Unit) {
        val device = getDeviceById(deviceID)
        val register = device.getRegisterById(registerID)
        register.addObserver { _, arg ->
            block(arg as Number)
        }
        device.addPollingRegister(register)
    }

    fun clearPollingRegisters() {
        deviceControllers.values.forEach(IDeviceController::removeAllPollingRegisters)
    }

    fun removePollingRegister(deviceID: DeviceID, registerID: String) {
        val device = getDeviceById(deviceID)
        val register = device.getRegisterById(registerID)
        register.deleteObservers()
        device.removePollingRegister(register)
    }

    fun checkDevices(): List<DeviceID> {
        deviceControllers.values.forEach(IDeviceController::checkResponsibility)
        return deviceControllers.filter { !it.value.isResponding }.keys.toList()
    }

    fun addWritingRegister(deviceID: DeviceID, registerID: String, value: Number) {
        val device = getDeviceById(deviceID)
        val register = device.getRegisterById(registerID)
        device.addWritingRegister(register to value)
    }
}
