package ru.avem.posvanna.communication.adapters.rele

import ru.avem.posvanna.communication.Connection
import ru.avem.posvanna.communication.adapters.modbusrtu.requests.*
import ru.avem.posvanna.communication.adapters.utils.BitVector
import ru.avem.posvanna.communication.adapters.utils.ModbusRegister

class ReleAdapter(val connection: Connection) {
    fun readCoilStatus(
        deviceId: Byte,
        registerId: Short,
        count: Int,
        customBaudrate: Int? = null
    ): BitVector {
        val request = ReadCoilStatus(
            deviceId = deviceId,
            registerId = registerId,
            count = count.toShort()
        )
        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        return request.parseResponse(response)
    }

    private fun doRequestForResponse(
        modbusRtuRequest: ModbusRtuRequest,
        customBaudrate: Int? = null
    ): ByteArray {
        val response = ByteArray(modbusRtuRequest.getResponseSize())
        connection.request(
            writeBuffer = modbusRtuRequest.getRequestBytes(),
            readBuffer = response,
            customBaudrate = customBaudrate
        )

        return response
    }

    fun readDiscreteInputs(
        deviceId: Byte,
        registerId: Short,
        count: Int,
        customBaudrate: Int? = null
    ): BitVector {
        val request = ReadDiscreteInputs(
            deviceId = deviceId,
            registerId = registerId,
            count = count.toShort()
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        return request.parseResponse(response)
    }

    fun readHoldingRegisters(
        deviceId: Byte,
        registerId: Short,
        count: Int,
        customBaudrate: Int? = null
    ): List<ModbusRegister> {
        val request = ReadHoldingRegisters(
            deviceId = deviceId,
            registerId = registerId,
            count = count.toShort()
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        return request.parseResponse(response)
    }


    fun readInputRegisters(
        deviceId: Byte,
        registerId: Short,
        count: Int,
        customBaudrate: Int? = null
    ): List<ModbusRegister> {
        val request = ReadInputRegisters(
            deviceId = deviceId,
            registerId = registerId,
            count = count.toShort()
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        return request.parseResponse(response)
    }

    fun forceSingleCoil(
        deviceId: Byte,
        registerId: Short,
        value: Boolean,
        customBaudrate: Int? = null
    ) {
        val request = ForceSingleCoil(
            deviceId = deviceId,
            registerId = registerId,
            coilValue = value
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        request.parseResponse(response)
    }

    // 01 06 00 01 01 00
    // 01 06 00 01 02 00


//    put(deviceId)                                 01
//    put(function)                                 06
//    putShort(registerId)                          00 01
//    putShort(registerData.toShort())              01 00 / 02 00

    fun presetSingleRegister(
        deviceId: Byte,
        registerId: Short,
        register: ModbusRegister,
        customBaudrate: Int? = null
    ) {
        val request = PresetSingleRegister(
            deviceId = deviceId,
            registerId = registerId,
            registerData = register
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        request.parseResponse(response)
    }

    fun forceMultipleCoils(
        deviceId: Byte,
        registerId: Short,
        coils: BitVector,
        customBaudrate: Int? = null
    ) {
        val request = ForceMultipleCoils(
            deviceId = deviceId,
            registerId = registerId,
            coils = coils
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        request.parseResponse(response)
    }

    fun presetMultipleRegisters(
        deviceId: Byte,
        registerId: Short,
        registers: List<ModbusRegister>,
        customBaudrate: Int? = null
    ) {
        val request = PresetMultipleRegisters(
            deviceId = deviceId,
            registerId = registerId,
            registers = registers
        )

        val response = doRequestForResponse(
            modbusRtuRequest = request,
            customBaudrate = customBaudrate
        )
        request.parseResponse(response)
    }
}
