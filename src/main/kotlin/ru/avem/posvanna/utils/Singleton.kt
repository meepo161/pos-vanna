package ru.avem.posvanna.utils

import ru.avem.posvanna.database.entities.Protocol
import ru.avem.posvanna.database.entities.ProtocolSingle
import ru.avem.posvanna.database.entities.TestObjectsType


object Singleton {
    lateinit var currentProtocol: Protocol
    lateinit var currentProtocolSingle: ProtocolSingle
    lateinit var currentTestItem: TestObjectsType
}
