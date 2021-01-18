package ru.avem.posvanna.communication.model.devices.parma

import ru.avem.posvanna.communication.model.DeviceRegister
import ru.avem.posvanna.communication.model.IDeviceModel

class ParmaModel : IDeviceModel {
    companion object {
        const val IA = "IA"
        const val IB = "IB"
        const val IC = "IC"
        const val UA = "UA"
        const val UB = "UB"
        const val UC = "UC"
    }

    override val registers: Map<String, DeviceRegister> = mapOf(
            IA to DeviceRegister(7, DeviceRegister.RegisterValueType.SHORT),
            IB to DeviceRegister(8, DeviceRegister.RegisterValueType.SHORT),
            IC to DeviceRegister(9, DeviceRegister.RegisterValueType.SHORT),
            UA to DeviceRegister(11, DeviceRegister.RegisterValueType.SHORT),
            UB to DeviceRegister(12, DeviceRegister.RegisterValueType.SHORT),
            UC to DeviceRegister(13, DeviceRegister.RegisterValueType.SHORT)
    )

    override fun getRegisterById(idRegister: String) =
            registers[idRegister] ?: error("Такого регистра нет в описанной карте $idRegister")
}
