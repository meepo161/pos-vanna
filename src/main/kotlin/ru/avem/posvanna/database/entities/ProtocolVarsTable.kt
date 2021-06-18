package ru.avem.posvanna.database.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProtocolVarsTable : IntIdTable() {
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

class ProtocolVars(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProtocolVars>(ProtocolVarsTable)
    var NUMBER_DATE_ATTESTATION by ProtocolVarsTable.NUMBER_DATE_ATTESTATION
    var NAME_OF_OPERATION by ProtocolVarsTable.NAME_OF_OPERATION
    var NUMBER_CONTROLLER by ProtocolVarsTable.NUMBER_CONTROLLER
    var T1 by ProtocolVarsTable. T1
    var T2 by ProtocolVarsTable. T2
    var T3 by ProtocolVarsTable. T3
    var T4 by ProtocolVarsTable. T4
    var T5 by ProtocolVarsTable. T5
    var T6 by ProtocolVarsTable. T6
    var T7 by ProtocolVarsTable. T7
    var T8 by ProtocolVarsTable. T8
    var T9 by ProtocolVarsTable. T9
    var T10 by ProtocolVarsTable.T10
    var T11 by ProtocolVarsTable.T11
    var T12 by ProtocolVarsTable.T12
    var T13 by ProtocolVarsTable.T13
    var T14 by ProtocolVarsTable.T14
    var T15 by ProtocolVarsTable.T15
    var T16 by ProtocolVarsTable.T16
    var T17 by ProtocolVarsTable.T17
    var T18 by ProtocolVarsTable.T18

    override fun toString(): String {
        return id.toString()
    }
}
