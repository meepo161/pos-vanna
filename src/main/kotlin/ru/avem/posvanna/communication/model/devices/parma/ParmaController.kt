package ru.avem.posvanna.communication.model.devices.parma

import ru.avem.posvanna.communication.adapters.modbusrtu.ModbusRTUAdapter
import ru.avem.posvanna.communication.adapters.utils.ModbusRegister
import ru.avem.posvanna.communication.model.DeviceRegister
import ru.avem.posvanna.communication.model.IDeviceController
import ru.avem.posvanna.communication.utils.TransportException
import ru.avem.posvanna.communication.utils.TypeByteOrder
import ru.avem.posvanna.communication.utils.allocateOrderedByteBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ParmaController(
        override val name: String, override val protocolAdapter: ModbusRTUAdapter, override val id: Byte
) : IDeviceController {
    private val model = ParmaModel()
    override var isResponding = false
    override var requestTotalCount = 0
    override var requestSuccessCount = 0
    override val pollingRegisters = mutableListOf<DeviceRegister>()
    override val pollingMutex = Any()
    override val writingMutex = Any()
    override val writingRegisters = mutableListOf<Pair<DeviceRegister, Number>>()

    override fun readRegister(register: DeviceRegister) {
        isResponding = try {
            transactionWithAttempts {
                when (register.valueType) {
                    DeviceRegister.RegisterValueType.SHORT -> {
                        val value = protocolAdapter.readHoldingRegisters(id, register.address, 1).first().toShort()
                        register.value = value
                    }
                    DeviceRegister.RegisterValueType.FLOAT -> {
                        val modbusRegister =
                                protocolAdapter.readHoldingRegisters(id, register.address, 2).map(ModbusRegister::toShort)
                        register.value = allocateOrderedByteBuffer(modbusRegister, TypeByteOrder.LITTLE_ENDIAN, 4).float
                    }
                    DeviceRegister.RegisterValueType.INT32 -> {
                        val modbusRegister =
                                protocolAdapter.readHoldingRegisters(id, register.address, 2).map(ModbusRegister::toShort)
                        register.value = allocateOrderedByteBuffer(modbusRegister, TypeByteOrder.LITTLE_ENDIAN, 4).int
                    }
                }
            }
            true
        } catch (e: TransportException) {
            false
        }
    }

    override fun <T : Number> writeRegister(register: DeviceRegister, value: T) {
        isResponding = try {
            when (value) {
                is Float -> {
                    val bb = ByteBuffer.allocate(4).putFloat(value).order(ByteOrder.LITTLE_ENDIAN)
                    val registers = listOf(ModbusRegister(bb.getShort(2)), ModbusRegister(bb.getShort(0)))
                    transactionWithAttempts {
                        protocolAdapter.presetMultipleRegisters(id, register.address, registers)
                    }
                }
                is Int -> {
                    val bb = ByteBuffer.allocate(4).putInt(value).order(ByteOrder.LITTLE_ENDIAN)
                    val registers = listOf(ModbusRegister(bb.getShort(2)), ModbusRegister(bb.getShort(0)))
                    transactionWithAttempts {
                        protocolAdapter.presetMultipleRegisters(id, register.address, registers)
                    }
                }
                is Short -> {
                    transactionWithAttempts {
                        protocolAdapter.presetSingleRegister(id, register.address, ModbusRegister(value))
                    }
                }
                else -> {
                    throw UnsupportedOperationException("Method can handle only with Float, Int and Short")
                }
            }
            true
        } catch (e: TransportException) {
            false
        }
    }

    override fun readAllRegisters() {
        model.registers.values.forEach {
            readRegister(it)
        }
    }

    override fun writeRegisters(register: DeviceRegister, values: List<Short>) {
        val registers = values.map { ModbusRegister(it) }
        isResponding = try {
            transactionWithAttempts {
                protocolAdapter.presetMultipleRegisters(id, register.address, registers)
            }
            true
        } catch (e: TransportException) {
            false
        }
    }

    override fun getRegisterById(idRegister: String) = model.getRegisterById(idRegister)

    override fun checkResponsibility() {
        model.registers.values.firstOrNull()?.let {
            readRegister(it)
        }
    }

    fun readIA(): Double {
        return getRegisterById(ParmaModel.IA).value.toDouble() / 5000.0
    }

    fun readIB(): Double {
        return getRegisterById(ParmaModel.IB).value.toDouble() / 5000.0
    }

    fun readIC(): Double {
        return getRegisterById(ParmaModel.IC).value.toDouble() / 5000.0
    }

    fun readUA(): Double {
        return getRegisterById(ParmaModel.UA).value.toDouble() / 100.0
    }

    fun readUB(): Double {
        return getRegisterById(ParmaModel.UB).value.toDouble() / 100.0
    }

    fun readUC(): Double {
        return getRegisterById(ParmaModel.UC).value.toDouble() / 100.0
    }
}

