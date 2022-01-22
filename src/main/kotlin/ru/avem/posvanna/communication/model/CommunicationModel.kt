package ru.avem.posvanna.communication.model

import ru.avem.kserialpooler.communication.Connection
import ru.avem.kserialpooler.communication.adapters.modbusrtu.ModbusRTUAdapter
import ru.avem.kserialpooler.communication.utils.SerialParameters
import ru.avem.posvanna.app.Pos.Companion.isAppRunning
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrController
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Controller
import ru.avem.posvanna.communication.model.devices.parma.ParmaController
import java.lang.Thread.sleep
import kotlin.concurrent.thread

object CommunicationModel {
    @Suppress("UNUSED_PARAMETER")
    enum class DeviceID(description: String) {
        DD2("ПР"),
        PARMA1("ПАРМА1"),
        TRM1("TRM1"),
        TRM2("TRM2"),
        TRM3("TRM3")
    }

    private var isConnected = false

    private val connection = Connection(
            adapterName = "CP2103 USB to RS-485",
            serialParameters = SerialParameters(8, 0, 1, 38400),
            timeoutRead = 100,
            timeoutWrite = 100
    ).apply {
        connect()
        isConnected = true
    }

    private val modbusAdapter = ModbusRTUAdapter(connection)

    private val devices: Map<DeviceID, IDeviceController> = mapOf(
            DeviceID.PARMA1 to ParmaController(DeviceID.PARMA1.toString(), modbusAdapter, 1),
            DeviceID.DD2 to OwenPrController(DeviceID.DD2.toString(), modbusAdapter, 2),
            DeviceID.TRM1 to Trm136Controller(DeviceID.TRM1.toString(), modbusAdapter, 8),
            DeviceID.TRM2 to Trm136Controller(DeviceID.TRM2.toString(), modbusAdapter, 16),
            DeviceID.TRM3 to Trm136Controller(DeviceID.TRM3.toString(), modbusAdapter, 24)
    )

    init {
        with(devices.values.groupBy { it.protocolAdapter.connection }) {
            keys.forEach { connection ->
                thread(isDaemon = true) {
                    while (true) {
                        if (isConnected) {
                            this[connection]!!.forEach {
                                it.readPollingRegisters()
                                sleep(1)
                            }
                        }
                        sleep(1)
                    }
                }
            }
        }
        thread(isDaemon = true) {
            while (true) {
                if (isConnected) {
                    devices.values.forEach {
                        it.writeWritingRegisters()
                        sleep(1)
                    }
                }
                sleep(1)
            }
        }
    }

    fun <T : IDeviceController> device(deviceID: DeviceID): T {
        return devices[deviceID] as T
    }

    fun startPoll(deviceID: DeviceID, registerID: String, block: (Number) -> Unit) {
        val device = device<IDeviceController>(deviceID)
        val register = device.getRegisterById(registerID)
        register.addObserver { _, arg ->
            block(arg as Number)
        }
        device.addPollingRegister(register)
    }

    fun clearPollingRegisters() {
        devices.values.forEach(IDeviceController::removeAllPollingRegisters)
        devices.values.forEach(IDeviceController::removeAllWritingRegisters)
    }

    fun removePollingRegister(deviceID: DeviceID, registerID: String) {
        val device = device<IDeviceController>(deviceID)
        val register = device.getRegisterById(registerID)
        register.deleteObservers()
        device.removePollingRegister(register)
    }

    fun checkDevices(checkedDevices: List<IDeviceController>): List<DeviceID> {
        checkedDevices.forEach(IDeviceController::checkResponsibility)
        return listOfUnresponsiveDevices(checkedDevices)
    }

    fun listOfUnresponsiveDevices(checkedDevices: List<IDeviceController>) =
        devices.filter { checkedDevices.toList().contains(it.value) && !it.value.isResponding }.keys.toList()

    fun addWritingRegister(deviceID: DeviceID, registerID: String, value: Number) {
        val device = device<IDeviceController>(deviceID)
        val register = device.getRegisterById(registerID)
        device.addWritingRegister(register to value)
    }
}
