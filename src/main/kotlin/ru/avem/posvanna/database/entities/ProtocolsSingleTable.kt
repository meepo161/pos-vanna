package ru.avem.posvanna.database.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ProtocolsSingleTable : IntIdTable() {
    val date = varchar("date", 256)
    val time = varchar("time", 256)
    val section =  varchar("section", 256)
    val temp =  varchar("temp", 99999999)
}

class ProtocolSingle(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProtocolSingle>(ProtocolsSingleTable)
    var date by ProtocolsSingleTable.date
    var time by ProtocolsSingleTable.time
    var section by ProtocolsSingleTable.section
    var temp by ProtocolsSingleTable.temp

    override fun toString(): String {
        return "$id"
    }
}
