package ru.avem.posvanna.database.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProtocolsRotorBladeTable : IntIdTable() {
    val date = varchar("date", 256)
    val time = varchar("time", 256)
    val operator = varchar("operator", 256)
    val cipher = varchar("cipher", 256)
    val productName = varchar("productName", 256)
    val temp1 =  varchar("temp1", 99999999)
    val temp2 =  varchar("temp2", 99999999)
    val temp3 =  varchar("temp3", 99999999)
    val temp4 =  varchar("temp4", 99999999)
    val temp5 =  varchar("temp5", 99999999)
    val temp6 =  varchar("temp6", 99999999)
}

class ProtocolRotorBlade(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProtocolRotorBlade>(ProtocolsRotorBladeTable)
    var date by ProtocolsRotorBladeTable.date
    var time by ProtocolsRotorBladeTable.time
    var operator by ProtocolsRotorBladeTable.operator
    var cipher by ProtocolsRotorBladeTable.cipher
    var productName by ProtocolsRotorBladeTable.productName
    var temp1 by ProtocolsRotorBladeTable.temp1
    var temp2 by ProtocolsRotorBladeTable.temp2
    var temp3 by ProtocolsRotorBladeTable.temp3
    var temp4 by ProtocolsRotorBladeTable.temp4
    var temp5 by ProtocolsRotorBladeTable.temp5
    var temp6 by ProtocolsRotorBladeTable.temp6

    override fun toString(): String {
        return "$id"
    }
}
