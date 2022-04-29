package ru.avem.posvanna.view

import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.stage.FileChooser
import javafx.stage.Modality
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import ru.avem.posvanna.database.entities.Protocol
import ru.avem.posvanna.database.entities.ProtocolRotorBlade
import ru.avem.posvanna.database.entities.ProtocolsTable
import ru.avem.posvanna.protocol.saveProtocolAsWorkbook
import ru.avem.posvanna.utils.Singleton
import ru.avem.posvanna.utils.Toast
import ru.avem.posvanna.utils.callKeyBoard
import ru.avem.posvanna.utils.openFile
import tornadofx.*
import tornadofx.controlsfx.confirmNotification
import tornadofx.controlsfx.errorNotification
import java.awt.Desktop
import java.io.File

class ProtocolListWindow : View("Протоколы графиков") {
    private var tableViewProtocols: TableView<Protocol> by singleAssign()
    private lateinit var protocols: ObservableList<Protocol>
    override fun onDock() {
        protocols = transaction {
            Protocol.all().toList().asObservable()
        }
        tableViewProtocols.items = protocols
    }


    override val root = anchorpane {
        prefWidth = 1600.0
        prefHeight = 900.0

        vbox(spacing = 16.0) {
            anchorpaneConstraints {
                leftAnchor = 16.0
                rightAnchor = 16.0
                topAnchor = 16.0
                bottomAnchor = 16.0
            }

            alignmentProperty().set(Pos.CENTER)

            textfield {
                callKeyBoard()
                prefWidth = 600.0

                promptText = "Фильтр"
                alignment = Pos.CENTER

                onKeyReleased = EventHandler {
                    if (!text.isNullOrEmpty()) {
                        tableViewProtocols.items = protocols.filter {
                            it.cipher1.contains(text) ||
                            it.productName1.contains(text)||
                            it.cipher2.contains(text) ||
                            it.productName2.contains(text)||
                            it.cipher3.contains(text) ||
                            it.productName3.contains(text)||
                            it.date.contains(text) ||
                                    it.time.contains(text) ||
                                    it.operator.contains(text)
                        }.asObservable()
                    } else {
                        tableViewProtocols.items = protocols
                    }
                }
            }

            tableViewProtocols = tableview {
                protocols = transaction {
                    Protocol.all().toList().asObservable()
                }
                items = protocols
                prefHeight = 700.0
                columnResizePolicyProperty().set(TableView.CONSTRAINED_RESIZE_POLICY)
                column("Шифр1", Protocol::cipher1)
                column("Номер1", Protocol::productName1)
                column("Шифр2", Protocol::cipher2)
                column("Номер2", Protocol::productName2)
                column("Шифр3", Protocol::cipher3)
                column("Номер3", Protocol::productName3)
                column("Оператор", Protocol::operator)
                column("Дата", Protocol::date)
                column("Время", Protocol::time)
            }

            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)

                button("Печать") {
                    action {
                        if (tableViewProtocols.selectedItem != null) {
                            Singleton.currentProtocol = transaction {
                                Protocol.find {
                                    ProtocolsTable.id eq tableViewProtocols.selectedItem!!.id
                                }.toList().asObservable()
                            }.first()
                            saveProtocolAsWorkbook(Singleton.currentProtocol)
                            close()
//                            openFile(File("protocol.xlsx"))
                            Desktop.getDesktop().print(File("protocol.xlsx"))
                        }
                    }
                }
                button("Открыть таблицу") {
                    action {
                        if (tableViewProtocols.selectedItem != null) {
                            Singleton.currentProtocol = transaction {
                                Protocol.find {
                                    ProtocolsTable.id eq tableViewProtocols.selectedItem!!.id
                                }.toList().asObservable()
                            }.first()


                            find<GraphHistoryWindow>().openModal(
                                modality = Modality.WINDOW_MODAL, escapeClosesWindow = true,
                                resizable = true, owner = this@ProtocolListWindow.currentWindow
                            )
                        }
                    }
                }
                button("Сохранить как") {
                    action {
                        if (tableViewProtocols.selectedItem != null) {
                            val files = chooseFile(
                                "Выберите директорию для сохранения",
                                arrayOf(FileChooser.ExtensionFilter("XLSX Files (*.xlsx)", "*.xlsx")),
                                FileChooserMode.Save,
                                this@ProtocolListWindow.currentWindow
                            ) {
                                this.initialDirectory = File(System.getProperty("user.home"))
                            }

                            if (files.isNotEmpty()) {
                                saveProtocolAsWorkbook(tableViewProtocols.selectedItem!!, files.first().absolutePath)

                                Platform.runLater {
                                    confirmNotification(
                                        "Готово",
                                        "Успешно сохранено",
                                        Pos.BOTTOM_CENTER,
                                        owner = this@ProtocolListWindow.currentWindow
                                    )
                                }
                            }
                        }
                    }
                }
                button("Печать 1 лопасть") {
                    action {
                        saveRotorBlade(1)
                    }
                }
                button("Печать 2 лопасть") {
                    action {
                        saveRotorBlade(2)
                    }
                }
                button("Печать 3 лопасть") {
                    action {
                        saveRotorBlade(3)
                    }
                }
                button("Сохранить все") {
                    action {
                        if (tableViewProtocols.items.size > 0) {
                            val dir = chooseDirectory(
                                "Выберите директорию для сохранения",
                                File(System.getProperty("user.home")),
                                this@ProtocolListWindow.currentWindow
                            )

                            if (dir != null) {
                                tableViewProtocols.items.forEach {
                                    val file = File(dir, "${it.id.value}.xlsx")
                                    saveProtocolAsWorkbook(it, file.absolutePath)
                                }
                                Platform.runLater {
                                    confirmNotification(
                                        "Готово",
                                        "Успешно сохранено",
                                        Pos.BOTTOM_CENTER,
                                        owner = this@ProtocolListWindow.currentWindow
                                    )
                                }
                            }
                        }
                    }
                }
                button("Удалить") {
                    action {
                        if (tableViewProtocols.selectedItem != null) {
                            transaction {
                                ProtocolsTable.deleteWhere {
                                    ProtocolsTable.id eq tableViewProtocols.selectedItem!!.id
                                }
                            }

                            tableViewProtocols.items = transaction {
                                Protocol.all().toList().asObservable()
                            }
                        }
                    }
                }
            }
        }
    }.addClass(Styles.hard)

    private fun saveRotorBlade(rotorBlade: Int) {
        if (tableViewProtocols.selectedItem != null) {
            val protocol = tableViewProtocols.selectedItem!!
            when (rotorBlade) {
                1 -> {
                    val protocolRotorBlade = transaction {
                        ProtocolRotorBlade.new {
                            date = protocol.date
                            time = protocol.time
                            dateEnd = protocol.dateEnd
                            timeEnd = protocol.timeEnd
                            cipher = protocol.cipher1
                            productName = protocol.productName1
                            operator = protocol.operator
                            temp1 = protocol.temp11
                            temp2 = protocol.temp12
                            temp3 = protocol.temp13
                            temp4 = protocol.temp14
                            temp5 = protocol.temp15
                            temp6 = protocol.temp16
                            temp7 = protocol.temp17
                            NUMBER_DATE_ATTESTATION = protocol.NUMBER_DATE_ATTESTATION
                            NAME_OF_OPERATION = protocol.NAME_OF_OPERATION
                            NUMBER_CONTROLLER = protocol.NUMBER_CONTROLLER
                            T1 = protocol.T1
                            T2 = protocol.T2
                            T3 = protocol.T3
                            T4 = protocol.T4
                            T5 = protocol.T5
                            T6 = protocol.T6
                        }
                    }
                    saveProtocolAsWorkbook(protocolRotorBlade)
                    Desktop.getDesktop().print(File("protocol1RotorBlade.xlsx"))
                }
                2 -> {
                    val protocolRotorBlade = transaction {
                        ProtocolRotorBlade.new {
                            date = protocol.date
                            time = protocol.time
                            dateEnd = protocol.dateEnd
                            timeEnd = protocol.timeEnd
                            cipher = protocol.cipher2
                            productName = protocol.productName2
                            operator = protocol.operator
                            temp1 = protocol.temp21
                            temp2 = protocol.temp22
                            temp3 = protocol.temp23
                            temp4 = protocol.temp24
                            temp5 = protocol.temp25
                            temp6 = protocol.temp26
                            temp7 = protocol.temp17
                            NUMBER_DATE_ATTESTATION = protocol.NUMBER_DATE_ATTESTATION
                            NAME_OF_OPERATION = protocol.NAME_OF_OPERATION
                            NUMBER_CONTROLLER = protocol.NUMBER_CONTROLLER
                            T1 = protocol.T7
                            T2 = protocol.T8
                            T3 = protocol.T9
                            T4 = protocol.T10
                            T5 = protocol.T11
                            T6 = protocol.T12
                        }
                    }
                    saveProtocolAsWorkbook(protocolRotorBlade)
                    Desktop.getDesktop().print(File("protocol1RotorBlade.xlsx"))
                }
                3 -> {
                    val protocolRotorBlade = transaction {
                        ProtocolRotorBlade.new {
                            date = protocol.date
                            time = protocol.time
                            dateEnd = protocol.dateEnd
                            timeEnd = protocol.timeEnd
                            cipher = protocol.cipher3
                            productName = protocol.productName3
                            operator = protocol.operator
                            temp1 = protocol.temp31
                            temp2 = protocol.temp32
                            temp3 = protocol.temp33
                            temp4 = protocol.temp34
                            temp5 = protocol.temp35
                            temp6 = protocol.temp36
                            temp7 = protocol.temp17
                            NUMBER_DATE_ATTESTATION = protocol.NUMBER_DATE_ATTESTATION
                            NAME_OF_OPERATION = protocol.NAME_OF_OPERATION
                            NUMBER_CONTROLLER = protocol.NUMBER_CONTROLLER
                            T1 = protocol.T13
                            T2 = protocol.T14
                            T3 = protocol.T15
                            T4 = protocol.T16
                            T5 = protocol.T17
                            T6 = protocol.T18
                        }
                    }
                    saveProtocolAsWorkbook(protocolRotorBlade)
                    Desktop.getDesktop().print(File("protocol1RotorBlade.xlsx"))

                }
            }

            runLater {
                Toast.makeText("Началась печать протокола").show(Toast.ToastType.INFORMATION)
            }
        } else {
            runLater {
                errorNotification(
                    "Ошибка",
                    "Выберите из какого протокола сохранять",
                    Pos.BOTTOM_CENTER,
                    owner = this@ProtocolListWindow.currentWindow
                )
            }
        }
    }

}