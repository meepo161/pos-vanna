package ru.avem.posvanna.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import ru.avem.posvanna.database.entities.*
import ru.avem.posvanna.database.entities.Users.fullName
import java.sql.Connection

fun validateDB() {
    Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    transaction {
        SchemaUtils.create(Users, ProtocolsTable, ProtocolsSingleTable, ProtocolsRotorBladeTable, ObjectsTypes, ProtocolVarsTable)
    }

    transaction {
        if (User.all().count() < 1) {
            val admin = User.find {
                fullName eq "admin"
            }

            if (admin.empty()) {
                User.new {
                    password = "avem"
                    fullName = "admin"
                }
            }

            if (TestObjectsType.all().count() < 1) {
                ProtocolVars.new {
                    NUMBER_DATE_ATTESTATION = "номер и дата аттестации"
                    NAME_OF_OPERATION = "Наименование и шифр технологического процесса"
                    NUMBER_CONTROLLER = "1"
                    T1 = "1"
                    T2 = "2"
                    T3 = "3"
                    T4 = "4"
                    T5 = "5"
                    T6 = "6"
                    T7 = "7"
                    T8 = "8"
                    T9 = "9"
                    T10 = "10"
                    T11 = "11"
                    T12 = "12"
                    T13 = "13"
                    T14 = "14"
                    T15 = "15"
                    T16 = "16"
                    T17 = "17"
                    T18 = "18"
                }


                ProtocolSingle.new {
                    date = "10.03.2020"
                    time = "11:30:00"
                    section = "1 лопасть 1 секция"
                    temp = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                }

                ProtocolRotorBlade.new {
                    date = "10.03.2020"
                    time = "11:30:00"
                    dateEnd = "10.03.2021"
                    timeEnd = "15:25:35"
                    cipher = "#666"
                    productName = "123456789"
                    operator = "Иванов И.И."
                    temp1 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp2 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp3 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    temp4 = "[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 1,0]"
                    temp5 = "[1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0, 8,0, 9,0, 10,0, 11,0, 12,0]"
                    temp6 = "[0,1, 0,2, 0,4, 0,8, 1,6, 3,2, 6,4, 12,8, 25,6, 51,2, 102,4, 204,8]"
                    NUMBER_DATE_ATTESTATION = "номер и дата аттестации"
                    NAME_OF_OPERATION = "Наименование и шифр технологического процесса"
                    NUMBER_CONTROLLER = "1"
                    T1 = "1"
                    T2 = "2"
                    T3 = "3"
                    T4 = "4"
                    T5 = "5"
                    T6 = "6"
                }

                Protocol.new {
                    date = "10.03.2020"
                    time = "11:30:00"
                    dateEnd = "10.03.2021"
                    timeEnd = "15:25:35"
                    cipher1 = "#111"
                    productName1 = "1111111111"
                    cipher2 = "#222"
                    productName2 = "22222222"
                    cipher3 = "#333"
                    productName3 = "3333333333"
                    operator = "Иванов И.И."

                    val list1 = mutableListOf<String>()
                    val list2 = mutableListOf<String>()
                    val list3 = mutableListOf<String>()
                    for (i in 0..10000) {
                        list1.add("1")
                        list2.add((i).toString())
                        list3.add((i * i).toString())
                    }

                    temp11 = list1.toString()
                    temp12 = list2.toString()
                    temp13 = list3.toString()
                    temp14 = list1.toString()
                    temp15 = list2.toString()
                    temp16 = list3.toString()
                    temp17 = list1.toString()
                    temp21 = list2.toString()
                    temp22 = list3.toString()
                    temp23 = list1.toString()
                    temp24 = list2.toString()
                    temp25 = list3.toString()
                    temp26 = list1.toString()
                    temp31 = list2.toString()
                    temp32 = list3.toString()
                    temp33 = list1.toString()
                    temp34 = list2.toString()
                    temp35 = list3.toString()
                    temp36 = list1.toString()
                    NUMBER_DATE_ATTESTATION = "Номер и дата аттестации"
                    NAME_OF_OPERATION = "Наименование и шифр технологического процесса"
                    NUMBER_CONTROLLER = "№666-777-1337"
                    T1 = "1"
                    T2 = "1"
                    T3 = "1"
                    T4 = "1"
                    T5 = "1"
                    T6 = "1"
                    T7 = "1"
                    T8 = "1"
                    T9 = "1"
                    T10 = "1"
                    T11 = "1"
                    T12 = "1"
                    T13 = "1"
                    T14 = "1"
                    T15 = "1"
                    T16 = "1"
                    T17 = "1"
                    T18 = "1"
                }
            }
        }
    }
}
