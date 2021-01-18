package ru.avem.posvanna.communication.model.devices.owen.pr

import ru.avem.posvanna.communication.adapters.modbusrtu.ModbusRTUAdapter
import ru.avem.posvanna.communication.adapters.utils.ModbusRegister
import ru.avem.posvanna.communication.model.DeviceRegister
import ru.avem.posvanna.communication.model.IDeviceController
import ru.avem.posvanna.communication.utils.TransportException
import ru.avem.posvanna.communication.utils.TypeByteOrder
import ru.avem.posvanna.communication.utils.allocateOrderedByteBuffer
import ru.avem.posvanna.utils.sleep
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.pow

class OwenPrController(
        override val name: String,
        override val protocolAdapter: ModbusRTUAdapter,
        override val id: Byte
) : IDeviceController {
    val model = OwenPrModel()
    override var isResponding = false
    override var requestTotalCount = 0
    override var requestSuccessCount = 0
    override val pollingRegisters = mutableListOf<DeviceRegister>()
    override val writingMutex = Any()
    override val writingRegisters = mutableListOf<Pair<DeviceRegister, Number>>()
    override val pollingMutex = Any()

    var outMask: Short = 0
    var outMask2: Short = 0
    var outMask3: Short = 0

    companion object {
        const val TRIG_RESETER: Short = 0xFFFF.toShort()
        const val WD_RESETER: Short = 0b10
    }

    override fun readRegister(register: DeviceRegister) {
        transactionWithAttempts {
            val modbusRegister =
                    protocolAdapter.readHoldingRegisters(id, register.address, 2).map(ModbusRegister::toShort)
            register.value = allocateOrderedByteBuffer(modbusRegister, TypeByteOrder.LITTLE_ENDIAN, 4).float.toDouble()
        }
    }

    override fun readAllRegisters() {
        model.registers.values.forEach {
            readRegister(it)
        }
    }

    override fun <T : Number> writeRegister(register: DeviceRegister, value: T) {
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
    }

    override fun writeRegisters(register: DeviceRegister, values: List<Short>) {
        val registers = values.map { ModbusRegister(it) }
        transactionWithAttempts {
            protocolAdapter.presetMultipleRegisters(id, register.address, registers)
        }
    }

    override fun checkResponsibility() {
        try {
            model.registers.values.firstOrNull()?.let {
                readRegister(it)
            }
        } catch (ignored: TransportException) {
        }
    }

    override fun getRegisterById(idRegister: String) = model.getRegisterById(idRegister)

    private fun onBitInRegister(register: DeviceRegister, bitPosition: Short) {
        val nor = bitPosition - 1
        outMask = outMask or 2.0.pow(nor).toInt().toShort()
        writeRegister(register, outMask)
        sleep(300)
    }

    private fun onBitInRegister2(register: DeviceRegister, bitPosition: Short) {
        val nor = bitPosition - 1
        outMask2 = outMask2 or 2.0.pow(nor).toInt().toShort()
        writeRegister(register, outMask2)
        sleep(300)
    }

    private fun onBitInRegister3(register: DeviceRegister, bitPosition: Short) {
        val nor = bitPosition - 1
        outMask3 = outMask3 or 2.0.pow(nor).toInt().toShort()
        writeRegister(register, outMask3)
        sleep(300)
    }

    private fun offBitInRegister(register: DeviceRegister, bitPosition: Short) {
        val nor = bitPosition - 1
        outMask = outMask and 2.0.pow(nor).toInt().inv().toShort()
        writeRegister(register, outMask)
        sleep(300)
    }

    private fun offBitInRegister2(register: DeviceRegister, bitPosition: Short) {
        val nor = bitPosition - 1
        outMask2 = outMask2 and 2.0.pow(nor).toInt().inv().toShort()
        writeRegister(register, outMask2)
        sleep(300)
    }

    private fun offBitInRegister3(register: DeviceRegister, bitPosition: Short) {
        val nor = bitPosition - 1
        outMask3 = outMask3 and 2.0.pow(nor).toInt().inv().toShort()
        writeRegister(register, outMask3)
        sleep(300)
    }


    fun initOwenPR() {
        writeRegister(getRegisterById(OwenPrModel.RES_REGISTER), 1)
    }

    fun resetKMS() {
        writeRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 0)
        writeRegister(getRegisterById(OwenPrModel.KMS2_REGISTER), 0)
        writeRegister(getRegisterById(OwenPrModel.KMS3_REGISTER), 0)
    }

    fun on11() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 1)
    }

    fun on12() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 2)
    }

    fun on13() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 3)
    }

    fun on14() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 4)
    }

    fun on15() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 5)
    }

    fun on16() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 6)
    }

    fun on21() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 7)
    }

    fun on22() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 8)
    }

    fun on23() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 1)
    }

    fun on24() {
        onBitInRegister(getRegisterById(OwenPrModel.KMS2_REGISTER), 2)
    }

    fun on25() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 3)
    }

    fun on26() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 4)
    }

    fun on31() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 5)
    }

    fun on32() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 6)
    }

    fun on33() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 7)
    }

    fun on34() {
        onBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 8)
    }

    fun on35() {
        onBitInRegister3(getRegisterById(OwenPrModel.KMS3_REGISTER), 1)
    }

    fun on36() {
        onBitInRegister3(getRegisterById(OwenPrModel.KMS3_REGISTER), 2)
    }

    fun off11() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 1)
    }

    fun off12() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 2)
    }

    fun off13() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 3)
    }

    fun off14() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 4)
    }

    fun off15() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 5)
    }

    fun off16() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 6)
    }

    fun off21() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 7)
    }

    fun off22() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 8)
    }

    fun off23() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 1)
    }

    fun off24() {
        offBitInRegister(getRegisterById(OwenPrModel.KMS2_REGISTER), 2)
    }

    fun off25() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 3)
    }

    fun off26() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 4)
    }

    fun off31() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 5)
    }

    fun off32() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 6)
    }

    fun off33() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 7)
    }

    fun off34() {
        offBitInRegister2(getRegisterById(OwenPrModel.KMS2_REGISTER), 8)
    }

    fun off35() {
        offBitInRegister3(getRegisterById(OwenPrModel.KMS3_REGISTER), 1)
    }

    fun off36() {
        offBitInRegister3(getRegisterById(OwenPrModel.KMS3_REGISTER), 2)
    }


    fun offAllKMs() {
        writeRegister(getRegisterById(OwenPrModel.KMS1_REGISTER), 0)
        writeRegister(getRegisterById(OwenPrModel.KMS2_REGISTER), 0)
        writeRegister(getRegisterById(OwenPrModel.KMS3_REGISTER), 0)
        outMask = 0
        outMask2 = 0
        outMask3 = 0
    }
}
