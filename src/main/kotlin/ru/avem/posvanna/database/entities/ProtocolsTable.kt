package ru.avem.posvanna.database.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProtocolsTable : IntIdTable() {
    val date = varchar("date", 256)
    val time = varchar("time", 256)
    val dateEnd = varchar("dateEnd", 256)
    val timeEnd = varchar("timeEnd", 256)
    val cipher1 = varchar("cipher1", 256)
    val productName1 = varchar("productName1", 256)
    val cipher2 = varchar("cipher2", 256)
    val productName2 = varchar("productName2", 256)
    val cipher3 = varchar("cipher3", 256)
    val productName3 = varchar("productName3", 256)
    val operator = varchar("operator", 256)
    val temp11 =  varchar("temp11", 99999999)
    val temp12 =  varchar("temp12", 99999999)
    val temp13 =  varchar("temp13", 99999999)
    val temp14 =  varchar("temp14", 99999999)
    val temp15 =  varchar("temp15", 99999999)
    val temp16 =  varchar("temp16", 99999999)
    val temp17 =  varchar("temp17", 99999999)
    val temp21 =  varchar("temp21", 99999999)
    val temp22 =  varchar("temp22", 99999999)
    val temp23 =  varchar("temp23", 99999999)
    val temp24 =  varchar("temp24", 99999999)
    val temp25 =  varchar("temp25", 99999999)
    val temp26 =  varchar("temp26", 99999999)
    val temp31 =  varchar("temp31", 99999999)
    val temp32 =  varchar("temp32", 99999999)
    val temp33 =  varchar("temp33", 99999999)
    val temp34 =  varchar("temp34", 99999999)
    val temp35 =  varchar("temp35", 99999999)
    val temp36 =  varchar("temp36", 99999999)

    var NUMBER_DATE_ATTESTATION = varchar("NUMBER_DATE_ATTESTATION", 512)
    var NAME_OF_OPERATION = varchar("NAME_OF_OPERATION", 512)
    var NUMBER_CONTROLLER = varchar("NUMBER_CONTROLLER", 512)
    var T1 = varchar("T1", 512)
    var T2 = varchar("T2", 512)
    var T3 = varchar("T3", 512)
    var T4 = varchar("T4", 512)
    var T5 = varchar("T5", 512)
    var T6 = varchar("T6", 512)
    var T7 = varchar("T7", 512)
    var T8 = varchar("T8", 512)
    var T9 = varchar("T9", 512)
    var T10 = varchar("T10", 512)
    var T11 = varchar("T11", 512)
    var T12 = varchar("T12", 512)
    var T13 = varchar("T13", 512)
    var T14 = varchar("T14", 512)
    var T15 = varchar("T15", 512)
    var T16 = varchar("T16", 512)
    var T17 = varchar("T17", 512)
    var T18 = varchar("T18", 512)
}

class Protocol(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Protocol>(ProtocolsTable)
    var date by ProtocolsTable.date
    var time by ProtocolsTable.time
    var dateEnd by ProtocolsTable.dateEnd
    var timeEnd by ProtocolsTable.timeEnd
    var operator by ProtocolsTable.operator
    var cipher1 by ProtocolsTable.cipher1
    var productName1 by ProtocolsTable.productName1
    var cipher2 by ProtocolsTable.cipher2
    var productName2 by ProtocolsTable.productName2
    var cipher3 by ProtocolsTable.cipher3
    var productName3 by ProtocolsTable.productName3
    var temp11 by ProtocolsTable.temp11
    var temp12 by ProtocolsTable.temp12
    var temp13 by ProtocolsTable.temp13
    var temp14 by ProtocolsTable.temp14
    var temp15 by ProtocolsTable.temp15
    var temp16 by ProtocolsTable.temp16
    var temp17 by ProtocolsTable.temp17
    var temp21 by ProtocolsTable.temp21
    var temp22 by ProtocolsTable.temp22
    var temp23 by ProtocolsTable.temp23
    var temp24 by ProtocolsTable.temp24
    var temp25 by ProtocolsTable.temp25
    var temp26 by ProtocolsTable.temp26
    var temp31 by ProtocolsTable.temp31
    var temp32 by ProtocolsTable.temp32
    var temp33 by ProtocolsTable.temp33
    var temp34 by ProtocolsTable.temp34
    var temp35 by ProtocolsTable.temp35
    var temp36 by ProtocolsTable.temp36
    var NUMBER_DATE_ATTESTATION by ProtocolsTable.NUMBER_DATE_ATTESTATION
    var NAME_OF_OPERATION by ProtocolsTable.NAME_OF_OPERATION
    var NUMBER_CONTROLLER by ProtocolsTable.NUMBER_CONTROLLER
    var T1 by ProtocolsTable.T1
    var T2 by ProtocolsTable.T2
    var T3 by ProtocolsTable.T3
    var T4 by ProtocolsTable.T4
    var T5 by ProtocolsTable.T5
    var T6 by ProtocolsTable.T6
    var T7 by ProtocolsTable.T7
    var T8 by ProtocolsTable.T8
    var T9 by ProtocolsTable.T9
    var T10 by ProtocolsTable.T10
    var T11 by ProtocolsTable.T11
    var T12 by ProtocolsTable.T12
    var T13 by ProtocolsTable.T13
    var T14 by ProtocolsTable.T14
    var T15 by ProtocolsTable.T15
    var T16 by ProtocolsTable.T16
    var T17 by ProtocolsTable.T17
    var T18 by ProtocolsTable.T18

    override fun toString(): String {
        return "$id"
    }
}
