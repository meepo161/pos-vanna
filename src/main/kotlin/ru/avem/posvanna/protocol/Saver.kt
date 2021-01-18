package ru.avem.posvanna.protocol

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.avem.posvanna.app.Pos
import ru.avem.posvanna.database.entities.Protocol
import ru.avem.posvanna.utils.Toast
import ru.avem.posvanna.utils.copyFileFromStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException

fun saveProtocolAsWorkbook(protocol: Protocol, path: String = "protocol.xlsx") {
    val template = File(path)
    copyFileFromStream(Pos::class.java.getResource("protocol.xlsx").openStream(), template)

    try {
        XSSFWorkbook(template).use {
            val sheet = it.getSheetAt(0)
            for (iRow in 0 until 100) {
                val row = sheet.getRow(iRow)
                if (row != null) {
                    for (iCell in 0 until 100) {
                        val cell = row.getCell(iCell)
                        if (cell != null && (cell.cellType == CellType.STRING)) {
                            when (cell.stringCellValue) {
                                "#PROTOCOL_NUMBER#" -> cell.setCellValue(protocol.id.toString())
                                "#SERIAL_NUMBER#" -> cell.setCellValue(protocol.factoryNumber)
                                "#DATE#" -> cell.setCellValue(protocol.date)
                                "#TIME#" -> cell.setCellValue(protocol.dateTime)
                                else -> {
                                    if (cell.stringCellValue.contains("#")) {
                                        cell.setCellValue("")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            val outStream = ByteArrayOutputStream()
            it.write(outStream)
            outStream.close()
        }
    } catch (e: FileNotFoundException) {
        Toast.makeText("Не удалось сохранить протокол на диск")
    }
}
