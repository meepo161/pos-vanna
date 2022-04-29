package ru.avem.posvanna.communication.model

import mu.KotlinLogging
import ru.avem.kserialpooler.communication.adapters.modbusrtu.ModbusRTUAdapter
import ru.avem.posvanna.communication.utils.TransportException
import ru.avem.posvanna.utils.sleep

interface IDeviceController {
    val name: String

    val protocolAdapter: ModbusRTUAdapter

    val id: Byte

    var isResponding: Boolean

    var requestTotalCount: Int
    var requestSuccessCount: Int

    fun readRegister(register: DeviceRegister) {

    }

    fun readRequest(request: String): Int {
        return 0
    }

    fun <T : Number> writeRegister(register: DeviceRegister, value: T) {

    }

    fun readAllRegisters() {

    }

    fun writeRegisters(register: DeviceRegister, values: List<Short>) {

    }

    fun writeRequest(request: String) {

    }

    val pollingRegisters: MutableList<DeviceRegister>
    val writingRegisters: MutableList<Pair<DeviceRegister, Number>>

    fun IDeviceController.transactionWithAttempts(block: () -> Unit) {
        var attempt = 0
        while (true) {
            requestTotalCount++

            try {
                block()
                requestSuccessCount++
                break
            } catch (e: TransportException) {
                val message =
                    "repeat $attempt/${protocolAdapter.connection.attemptCount} attempts with common success rate = ${(requestSuccessCount) * 100 / requestTotalCount}%"
                KotlinLogging.logger(name).info(message)

                if (attempt++ >= protocolAdapter.connection.attemptCount) {
                    throw e
                }
            }
            sleep(10)
        }
    }

    fun getRegisterById(idRegister: String): DeviceRegister

    fun addPollingRegister(register: DeviceRegister) {
        synchronized(protocolAdapter.connection) {
            pollingRegisters.add(register)
        }
    }

    fun addWritingRegister(writingPair: Pair<DeviceRegister, Number>) {
        synchronized(protocolAdapter.connection) {
            writingRegisters.add(writingPair)
        }
    }

    fun removePollingRegister(register: DeviceRegister) {
        synchronized(protocolAdapter.connection) {
            pollingRegisters.remove(register)
        }
    }

    fun removeAllPollingRegisters() {
        synchronized(protocolAdapter.connection) {
            pollingRegisters.forEach(DeviceRegister::deleteObservers)
            pollingRegisters.clear()
        }
    }

    fun removeAllWritingRegisters() {
        synchronized(protocolAdapter.connection) {
            writingRegisters.map {
                it.first
            }.forEach(DeviceRegister::deleteObservers)
            writingRegisters.clear()
        }
    }

    fun readPollingRegisters() {
        synchronized(protocolAdapter.connection) {
            for (register in pollingRegisters) {
                readRegister(register)
                if (!isResponding) break
            }
        }
    }

    fun writeWritingRegisters() {
        synchronized(protocolAdapter.connection) {
            for (pair in writingRegisters) {
                writeRegister(pair.first, pair.second)
                if (!isResponding) break
            }
        }
    }

    fun checkResponsibility()
}
