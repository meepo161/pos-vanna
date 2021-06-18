package ru.avem.posvanna.view

import javafx.geometry.Pos
import javafx.scene.control.TextField
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.avem.posvanna.database.entities.ProtocolVars
import ru.avem.posvanna.database.entities.ProtocolVarsTable
import ru.avem.posvanna.utils.Toast
import ru.avem.posvanna.utils.callKeyBoard
import tornadofx.*

class ProtocolVarsWindow : View("Протоколы графиков") {
    var tfNameOfOperation: TextField by singleAssign()
    var tfNumberAndDateAttestation: TextField by singleAssign()
    var tfNumberController: TextField by singleAssign()
    var tft1: TextField by singleAssign()
    var tft2: TextField by singleAssign()
    var tft3: TextField by singleAssign()
    var tft4: TextField by singleAssign()
    var tft5: TextField by singleAssign()
    var tft6: TextField by singleAssign()
    var tft7: TextField by singleAssign()
    var tft8: TextField by singleAssign()
    var tft9: TextField by singleAssign()
    var tft10: TextField by singleAssign()
    var tft11: TextField by singleAssign()
    var tft12: TextField by singleAssign()
    var tft13: TextField by singleAssign()
    var tft14: TextField by singleAssign()
    var tft15: TextField by singleAssign()
    var tft16: TextField by singleAssign()
    var tft17: TextField by singleAssign()
    var tft18: TextField by singleAssign()
    var protocolVars = transaction {
        ProtocolVars.all().toList().asObservable()
    }.first()

    override fun onDock() {
        super.onDock()
        protocolVars = transaction {
            ProtocolVars.all().toList().asObservable()
        }.first()
        tfNameOfOperation.text = protocolVars.NAME_OF_OPERATION
        tfNumberAndDateAttestation.text = protocolVars.NUMBER_DATE_ATTESTATION
        tfNumberController.text = protocolVars.NUMBER_CONTROLLER
        tft1.text = protocolVars.T1
        tft2.text = protocolVars.T2
        tft3.text = protocolVars.T3
        tft4.text = protocolVars.T4
        tft5.text = protocolVars.T5
        tft6.text = protocolVars.T6
        tft7.text = protocolVars.T7
        tft8.text = protocolVars.T8
        tft9.text = protocolVars.T9
        tft10.text = protocolVars.T10
        tft11.text = protocolVars.T11
        tft12.text = protocolVars.T12
        tft13.text = protocolVars.T13
        tft14.text = protocolVars.T14
        tft15.text = protocolVars.T15
        tft16.text = protocolVars.T16
        tft17.text = protocolVars.T17
        tft18.text = protocolVars.T18

    }

    override val root = anchorpane {
        prefWidth = 1200.0
        prefHeight = 600.0

        vbox(spacing = 16.0) {
            anchorpaneConstraints {
                leftAnchor = 16.0
                rightAnchor = 16.0
                topAnchor = 16.0
                bottomAnchor = 16.0
            }
            alignmentProperty().set(Pos.CENTER)
            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)
                label("Наименование и шифр технологического процесса:")
                tfNameOfOperation = textfield {
                    minWidth = 400.0
                    callKeyBoard()
                }
            }
            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)
                label("Номер и дата аттестации:")
                tfNumberAndDateAttestation = textfield {
                    minWidth = 400.0
                    callKeyBoard()
                }
            }
            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)
                label("Зав.номер контроллера:")
                tfNumberController = textfield {
                    minWidth = 400.0
                    callKeyBoard()
                }
            }
            label("Значения поправок температуры нагревателей по каждому каналу регулирования")
            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)
                label("1 лопасть: ")
                tft1 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft2 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft3 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft4 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft5 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft6 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
            }
            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)
                label("2 лопасть: ")
                tft7 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft8 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft9 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft10 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft11 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft12 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
            }
            hbox(spacing = 16.0) {
                alignmentProperty().set(Pos.CENTER)
                label("3 лопасть: ")
                tft13 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft14 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft15 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft16 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft17 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
                tft18 = textfield {
                    maxWidth = 60.0
                    callKeyBoard()
                }
            }
            button("Сохранить") {
                action {
                    try {
                        transaction {
                            ProtocolVarsTable.update({
                                ProtocolVarsTable.NAME_OF_OPERATION eq ProtocolVarsTable.NAME_OF_OPERATION
                            }) {
                                it[NAME_OF_OPERATION] = tfNameOfOperation.text
                                it[NUMBER_DATE_ATTESTATION] = tfNumberAndDateAttestation.text
                                it[NUMBER_CONTROLLER] = tfNumberController.text
                                it[T1] = tft1.text
                                it[T2] = tft2.text
                                it[T3] = tft3.text
                                it[T4] = tft4.text
                                it[T5] = tft5.text
                                it[T6] = tft6.text
                                it[T7] = tft7.text
                                it[T8] = tft8.text
                                it[T9] = tft9.text
                                it[T10] = tft10.text
                                it[T11] = tft11.text
                                it[T12] = tft12.text
                                it[T13] = tft13.text
                                it[T14] = tft14.text
                                it[T15] = tft15.text
                                it[T16] = tft16.text
                                it[T17] = tft17.text
                                it[T18] = tft18.text
                            }
                        }
                        Toast.makeText("Успешно сохранено").show(Toast.ToastType.CONFIRM)
                    } catch (e: Exception) {
                        Toast.makeText("Ошибка").show(Toast.ToastType.ERROR)
                    }
                }
            }
        }
    }.addClass(Styles.blueTheme, Styles.hard)
}