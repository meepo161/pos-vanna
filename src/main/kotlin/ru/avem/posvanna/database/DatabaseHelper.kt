package ru.avem.posvanna.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import ru.avem.posvanna.database.entities.*
import ru.avem.posvanna.database.entities.Users.login
import java.sql.Connection

fun validateDB() {
    Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
        SchemaUtils.create(Users, ProtocolsTable, ProtocolsSingleTable, ObjectsTypes)
    }

    transaction {
        if (User.all().count() < 2) {
            val admin = User.find {
                login eq "admin"
            }

            if (admin.empty()) {
                User.new {
                    login = "admin"
                    password = "avem"
                    fullName = "admin"
                }
            }

            if (TestObjectsType.all().count() < 1) {
                TestObjectsType.new {
                    serialNumber = "111111"
                    resistanceCoil = "0.1"
                    resistanceContactGroup = "0.2"
                    voltageMin = "0.3"
                    voltageMax = "0.4"
                    timeOff = "0.5"
                }

                TestObjectsType.new {
                    serialNumber = "222222"
                    resistanceCoil = "1.1"
                    resistanceContactGroup = "1.2"
                    voltageMin = "1.3"
                    voltageMax = "1.4"
                    timeOff = "1.5"
                }

                TestObjectsType.new {
                    serialNumber = "3333333"
                    resistanceCoil = "2.1"
                    resistanceContactGroup = "2.2"
                    voltageMin = "2.3"
                    voltageMax = "2.4"
                    timeOff = "2.5"
                }

                ProtocolSingle.new {
                    date = "10.03.2020"
                    time = "11:30:00"
                    temp = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                }

                Protocol.new {
                    date = "10.03.2020"
                    time = "11:30:00"
                    temp11 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp12 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp13 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    temp14 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp15 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp16 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    temp21 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp22 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp23 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    temp24 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp25 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp26 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    temp31 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp32 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp33 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    temp34 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp35 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp36 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                }
            }
        }
    }
}
