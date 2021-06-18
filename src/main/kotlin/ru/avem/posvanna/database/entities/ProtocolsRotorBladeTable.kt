package ru.avem.posvanna.database.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProtocolsRotorBladeTable : IntIdTable() {
    val date = varchar("date", 256)
    val time = varchar("time", 256)
    val dateEnd = varchar("dateEnd", 256)
    val timeEnd = varchar("timeEnd", 256)
    val operator = varchar("operator", 256)
    val cipher = varchar("cipher", 256)
    val productName = varchar("productName", 256)
    val temp1 =  varchar("temp1", 99999999)
    val temp2 =  varchar("temp2", 99999999)
    val temp3 =  varchar("temp3", 99999999)
    val temp4 =  varchar("temp4", 99999999)
    val temp5 =  varchar("temp5", 99999999)
    val temp6 =  varchar("temp6", 99999999)
    var NUMBER_DATE_ATTESTATION = varchar("NUMBER_DATE_ATTESTATION", 512)
    var NAME_OF_OPERATION = varchar("NAME_OF_OPERATION", 512)
    var NUMBER_CONTROLLER = varchar("NUMBER_CONTROLLER", 512)
    var T1 = varchar("T1", 512)
    var T2 = varchar("T2", 512)
    var T3 = varchar("T3", 512)
    var T4 = varchar("T4", 512)
    var T5 = varchar("T5", 512)
    var T6 = varchar("T6", 512)
}

class ProtocolRotorBlade(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProtocolRotorBlade>(ProtocolsRotorBladeTable)
    var date by ProtocolsRotorBladeTable.date
    var time by ProtocolsRotorBladeTable.time
    var dateEnd by ProtocolsRotorBladeTable.dateEnd
    var timeEnd by ProtocolsRotorBladeTable.timeEnd
    var operator by ProtocolsRotorBladeTable.operator
    var cipher by ProtocolsRotorBladeTable.cipher
    var productName by ProtocolsRotorBladeTable.productName
    var temp1 by ProtocolsRotorBladeTable.temp1
    var temp2 by ProtocolsRotorBladeTable.temp2
    var temp3 by ProtocolsRotorBladeTable.temp3
    var temp4 by ProtocolsRotorBladeTable.temp4
    var temp5 by ProtocolsRotorBladeTable.temp5
    var temp6 by ProtocolsRotorBladeTable.temp6
    var NUMBER_DATE_ATTESTATION by ProtocolsRotorBladeTable.NUMBER_DATE_ATTESTATION
    var NAME_OF_OPERATION by ProtocolsRotorBladeTable.NAME_OF_OPERATION
    var NUMBER_CONTROLLER by ProtocolsRotorBladeTable.NUMBER_CONTROLLER
    var T1 by ProtocolsRotorBladeTable. T1
    var T2 by ProtocolsRotorBladeTable. T2
    var T3 by ProtocolsRotorBladeTable. T3
    var T4 by ProtocolsRotorBladeTable. T4
    var T5 by ProtocolsRotorBladeTable. T5
    var T6 by ProtocolsRotorBladeTable. T6

    override fun toString(): String {
        return "$id"
    }
}
