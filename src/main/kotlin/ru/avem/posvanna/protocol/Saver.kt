package ru.avem.posvanna.protocol

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.charts.AxisPosition
import org.apache.poi.ss.usermodel.charts.ChartDataSource
import org.apache.poi.ss.usermodel.charts.DataSources
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFChart
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean
import ru.avem.posvanna.app.Pos
import ru.avem.posvanna.database.entities.Protocol
import ru.avem.posvanna.database.entities.ProtocolRotorBlade
import ru.avem.posvanna.database.entities.ProtocolSingle
import ru.avem.posvanna.utils.Toast
import ru.avem.posvanna.utils.copyFileFromStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException


var TO_DESIRED_ROW = 0

fun saveProtocolAsWorkbook(protocol: Protocol, path: String = "protocol.xlsx") {
    val template = File(path)
    copyFileFromStream(Pos::class.java.getResource("protocol.xlsx").openStream(), template)
    try {
        XSSFWorkbook(template).use { wb ->
            val sheet = wb.getSheetAt(0)
            for (iRow in 0 until 100) {
                val row = sheet.getRow(iRow)
                if (row != null) {
                    for (iCell in 0 until 100) {
                        val cell = row.getCell(iCell)
                        if (cell != null && (cell.cellType == CellType.STRING)) {
                            when (cell.stringCellValue) {
                                "#PROTOCOL_NUMBER#" -> cell.setCellValue(protocol.id.toString())
                                "#DATE#" -> cell.setCellValue(protocol.date)
                                "#DATE_END#" -> cell.setCellValue(protocol.dateEnd)
                                "#TIME#" -> cell.setCellValue(protocol.time)
                                "#TIME_END#" -> cell.setCellValue(protocol.timeEnd)
                                "#CIPHER1#" -> cell.setCellValue(protocol.cipher1)
                                "#NUMBER_PRODUCT1#" -> cell.setCellValue(protocol.productName1)
                                "#CIPHER2#" -> cell.setCellValue(protocol.cipher2)
                                "#NUMBER_PRODUCT2#" -> cell.setCellValue(protocol.productName2)
                                "#CIPHER3#" -> cell.setCellValue(protocol.cipher3)
                                "#NUMBER_PRODUCT3#" -> cell.setCellValue(protocol.productName3)
                                "#OPERATOR#" -> cell.setCellValue(protocol.operator)

                                "#NUMBER_DATE_ATTESTATION#" -> cell.setCellValue(protocol.NUMBER_DATE_ATTESTATION)
                                "#NAME_OF_OPERATION#" -> cell.setCellValue(protocol.NAME_OF_OPERATION)
                                "#NUMBER_CONTROLLER#" -> cell.setCellValue(protocol.NUMBER_CONTROLLER)
                                "#T1#" -> cell.setCellValue(protocol.T1)
                                "#T2#" -> cell.setCellValue(protocol.T2)
                                "#T3#" -> cell.setCellValue(protocol.T3)
                                "#T4#" -> cell.setCellValue(protocol.T4)
                                "#T5#" -> cell.setCellValue(protocol.T5)
                                "#T6#" -> cell.setCellValue(protocol.T6)
                                "#T7#" -> cell.setCellValue(protocol.T7)
                                "#T8#" -> cell.setCellValue(protocol.T8)
                                "#T9#" -> cell.setCellValue(protocol.T9)
                                "#T10#" -> cell.setCellValue(protocol.T10)
                                "#T11#" -> cell.setCellValue(protocol.T11)
                                "#T12#" -> cell.setCellValue(protocol.T12)
                                "#T13#" -> cell.setCellValue(protocol.T13)
                                "#T14#" -> cell.setCellValue(protocol.T14)
                                "#T15#" -> cell.setCellValue(protocol.T15)
                                "#T16#" -> cell.setCellValue(protocol.T16)
                                "#T17#" -> cell.setCellValue(protocol.T17)
                                "#T18#" -> cell.setCellValue(protocol.T18)

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
            fillParameters18(
                wb,
                protocol.temp11,
                protocol.temp12,
                protocol.temp13,
                protocol.temp14,
                protocol.temp15,
                protocol.temp16,
                protocol.temp17,
                protocol.temp21,
                protocol.temp22,
                protocol.temp23,
                protocol.temp24,
                protocol.temp25,
                protocol.temp26,
                protocol.temp31,
                protocol.temp32,
                protocol.temp33,
                protocol.temp34,
                protocol.temp35,
                protocol.temp36,
                0, 15
            )
            drawLineChart18(wb)
            sheet.protectSheet("avem")
            val outStream = ByteArrayOutputStream()
            wb.write(outStream)
            outStream.close()
        }
    } catch (e: Exception) {
        Toast.makeText("Не удалось сохранить протокол на диск")
    }
}

fun saveProtocolAsWorkbook(
    protocolRotorBlade: ProtocolRotorBlade,
    path: String = "protocol1RotorBlade.xlsx"
) {
    val template = File(path)
    copyFileFromStream(Pos::class.java.getResource("protocol1RotorBlade.xlsx").openStream(), template)
    try {
        XSSFWorkbook(template).use { wb ->
            val sheet = wb.getSheetAt(0)
            for (iRow in 0 until 100) {
                val row = sheet.getRow(iRow)
                if (row != null) {
                    for (iCell in 0 until 100) {
                        val cell = row.getCell(iCell)
                        if (cell != null && (cell.cellType == CellType.STRING)) {
                            when (cell.stringCellValue) {
                                "#PROTOCOL_NUMBER#" -> cell.setCellValue(protocolRotorBlade.id.toString())
                                "#DATE#" -> cell.setCellValue(protocolRotorBlade.date)
                                "#DATE_END#" -> cell.setCellValue(protocolRotorBlade.dateEnd)
                                "#TIME#" -> cell.setCellValue(protocolRotorBlade.time)
                                "#TIME_END#" -> cell.setCellValue(protocolRotorBlade.timeEnd)
                                "#CIPHER#" -> cell.setCellValue(protocolRotorBlade.cipher)
                                "#NUMBER_PRODUCT#" -> cell.setCellValue(protocolRotorBlade.productName)
                                "#OPERATOR#" -> cell.setCellValue(protocolRotorBlade.operator)
                                "#NUMBER_DATE_ATTESTATION#" -> cell.setCellValue(protocolRotorBlade.NUMBER_DATE_ATTESTATION)
                                "#NAME_OF_OPERATION#" -> cell.setCellValue(protocolRotorBlade.NAME_OF_OPERATION)
                                "#NUMBER_CONTROLLER#" -> cell.setCellValue(protocolRotorBlade.NUMBER_CONTROLLER)
                                "#T1#" -> cell.setCellValue(protocolRotorBlade.T1)
                                "#T2#" -> cell.setCellValue(protocolRotorBlade.T2)
                                "#T3#" -> cell.setCellValue(protocolRotorBlade.T3)
                                "#T4#" -> cell.setCellValue(protocolRotorBlade.T4)
                                "#T5#" -> cell.setCellValue(protocolRotorBlade.T5)
                                "#T6#" -> cell.setCellValue(protocolRotorBlade.T6)
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
            fillParameters6(
                wb,
                protocolRotorBlade.temp1,
                protocolRotorBlade.temp2,
                protocolRotorBlade.temp3,
                protocolRotorBlade.temp4,
                protocolRotorBlade.temp5,
                protocolRotorBlade.temp6,
                0, 15
            ) //TODO
            drawLineChart6(wb)
            sheet.protectSheet("avem")
            val outStream = ByteArrayOutputStream()
            wb.write(outStream)
            outStream.close()
        }
    } catch (e: Exception) {
        Toast.makeText("Не удалось сохранить протокол на диск")
    }
}

fun saveProtocolAsWorkbook(
    protocolSingle: ProtocolSingle,
    path: String = "protocol1Section.xlsx",
    start: Int,
    end: Int
) {
    val template = File(path)
    copyFileFromStream(Pos::class.java.getResource("protocol1Section.xlsx").openStream(), template)
    try {
        XSSFWorkbook(template).use { wb ->
            val sheet = wb.getSheetAt(0)
            for (iRow in 0 until 100) {
                val row = sheet.getRow(iRow)
                if (row != null) {
                    for (iCell in 0 until 100) {
                        val cell = row.getCell(iCell)
                        if (cell != null && (cell.cellType == CellType.STRING)) {
                            when (cell.stringCellValue) {
                                "#PROTOCOL_NUMBER#" -> cell.setCellValue(protocolSingle.id.toString())
                                "#DATE#" -> cell.setCellValue(protocolSingle.date)
                                "#TIME#" -> cell.setCellValue(protocolSingle.time)

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
            fillParameters(wb, protocolSingle.temp, start, end)
            drawLineChart(wb, protocolSingle.section)
            sheet.protectSheet("avem")
            val outStream = ByteArrayOutputStream()
            wb.write(outStream)
            outStream.close()
        }
    } catch (e: FileNotFoundException) {
        Toast.makeText("Не удалось сохранить протокол на диск")
    }
}

fun fillParameters(wb: XSSFWorkbook, dots: String, start: Int, end: Int) {
    var values = dots.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val valuesForExcel = arrayListOf<Double>()
    for (i in values.indices) {
        valuesForExcel.add(values[i])
    }
    val sheet = wb.getSheetAt(0)
    var row: Row
    var cellStyle: XSSFCellStyle = generateStyles(wb) as XSSFCellStyle
    var rowNum = sheet.lastRowNum + 1
    row = sheet.createRow(rowNum)
    var columnNum = 0
    for (i in values.indices) {
        columnNum = fillOneCell(row, columnNum, cellStyle, i + start)
        columnNum = fillOneCell(row, columnNum, cellStyle, values[i])
        row = sheet.createRow(++rowNum)
        columnNum = 0
    }
}

private fun drawLineChart(workbook: XSSFWorkbook, section: String) {
    val sheet = workbook.getSheet("Sheet1")
    val lastRowIndex = sheet.lastRowNum - 1
    val timeData = DataSources.fromNumericCellRange(sheet, CellRangeAddress(16, lastRowIndex, 0, 0))
    val valueData = DataSources.fromNumericCellRange(sheet, CellRangeAddress(16, lastRowIndex, 1, 1))

    var lineChart = createLineChart(sheet)
    drawLineChart(lineChart, timeData, valueData, section)
}

private fun drawLineChart(
    lineChart: XSSFChart,
    xAxisData: ChartDataSource<Number>,
    yAxisData: ChartDataSource<Number>,
    section: String
) {
    val data = lineChart.chartDataFactory.createLineChartData()

    val xAxis = lineChart.chartAxisFactory.createCategoryAxis(AxisPosition.BOTTOM)
    val yAxis = lineChart.createValueAxis(AxisPosition.LEFT)
    yAxis.crosses = org.apache.poi.ss.usermodel.charts.AxisCrosses.AUTO_ZERO

    val series = data.addSeries(xAxisData, yAxisData)
    series.setTitle("График")
    lineChart.plot(data, xAxis, yAxis)
    lineChart.axes[0].setTitle(section)
    lineChart.axes[1].setTitle("T, °C")

    val plotArea = lineChart.ctChart.plotArea
    plotArea.lineChartArray[0].smooth
    val ctBool = CTBoolean.Factory.newInstance()
    ctBool.`val` = false
    plotArea.lineChartArray[0].smooth = ctBool
    for (series in plotArea.lineChartArray[0].serArray) {
        series.smooth = ctBool
    }
}

fun fillParameters18(
    wb: XSSFWorkbook,
    dots11: String,
    dots12: String,
    dots13: String,
    dots14: String,
    dots15: String,
    dots16: String,
    dots17: String,
    dots21: String,
    dots22: String,
    dots23: String,
    dots24: String,
    dots25: String,
    dots26: String,
    dots31: String,
    dots32: String,
    dots33: String,
    dots34: String,
    dots35: String,
    dots36: String,
    columnNumber: Int, rawNumber: Int
) {
    val values11 = dots11.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values12 = dots12.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values13 = dots13.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values14 = dots14.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values15 = dots15.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values16 = dots16.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values17 = dots17.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values21 = dots21.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values22 = dots22.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values23 = dots23.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values24 = dots24.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values25 = dots25.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values26 = dots26.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values31 = dots31.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values32 = dots32.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values33 = dots33.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values34 = dots34.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values35 = dots35.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values36 = dots36.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)

    val valuesForExcel11 = arrayListOf<Double>()
    val valuesForExcel12 = arrayListOf<Double>()
    val valuesForExcel13 = arrayListOf<Double>()
    val valuesForExcel14 = arrayListOf<Double>()
    val valuesForExcel15 = arrayListOf<Double>()
    val valuesForExcel16 = arrayListOf<Double>()
    val valuesForExcel17 = arrayListOf<Double>()
    val valuesForExcel21 = arrayListOf<Double>()
    val valuesForExcel22 = arrayListOf<Double>()
    val valuesForExcel23 = arrayListOf<Double>()
    val valuesForExcel24 = arrayListOf<Double>()
    val valuesForExcel25 = arrayListOf<Double>()
    val valuesForExcel26 = arrayListOf<Double>()
    val valuesForExcel31 = arrayListOf<Double>()
    val valuesForExcel32 = arrayListOf<Double>()
    val valuesForExcel33 = arrayListOf<Double>()
    val valuesForExcel34 = arrayListOf<Double>()
    val valuesForExcel35 = arrayListOf<Double>()
    val valuesForExcel36 = arrayListOf<Double>()

    var step = 1
    if (values11.size > 200) {
        step = (values11.size - values11.size % 200) / 200
    }

    for (i in values11.indices step step) {
        valuesForExcel11.add(values11[i])
        valuesForExcel12.add(values12[i])
        valuesForExcel13.add(values13[i])
        valuesForExcel14.add(values14[i])
        valuesForExcel15.add(values15[i])
        valuesForExcel16.add(values16[i])
        valuesForExcel17.add(values17[i])
        valuesForExcel21.add(values21[i])
        valuesForExcel22.add(values22[i])
        valuesForExcel23.add(values23[i])
        valuesForExcel24.add(values24[i])
        valuesForExcel25.add(values25[i])
        valuesForExcel26.add(values26[i])
        valuesForExcel31.add(values31[i])
        valuesForExcel32.add(values32[i])
        valuesForExcel33.add(values33[i])
        valuesForExcel34.add(values34[i])
        valuesForExcel35.add(values35[i])
        valuesForExcel36.add(values36[i])
    }
    val sheet = wb.getSheetAt(0)
    var row: Row
    val cellStyle: XSSFCellStyle = generateStyles(wb) as XSSFCellStyle
    var rowNum = rawNumber
    row = sheet.createRow(rowNum)
    var dot = 0
    for (i in valuesForExcel11.indices) {
        fillOneCell(row, columnNumber, cellStyle, dot / 60)
        fillOneCell(row, columnNumber + 1, cellStyle, valuesForExcel11[i])
        fillOneCell(row, columnNumber + 2, cellStyle, valuesForExcel12[i])
        fillOneCell(row, columnNumber + 3, cellStyle, valuesForExcel13[i])
        fillOneCell(row, columnNumber + 4, cellStyle, valuesForExcel14[i])
        fillOneCell(row, columnNumber + 5, cellStyle, valuesForExcel15[i])
        fillOneCell(row, columnNumber + 6, cellStyle, valuesForExcel16[i])
        fillOneCell(row, columnNumber + 7, cellStyle, valuesForExcel21[i])
        fillOneCell(row, columnNumber + 8, cellStyle, valuesForExcel22[i])
        fillOneCell(row, columnNumber + 9, cellStyle, valuesForExcel23[i])
        fillOneCell(row, columnNumber + 10, cellStyle, valuesForExcel24[i])
        fillOneCell(row, columnNumber + 11, cellStyle, valuesForExcel25[i])
        fillOneCell(row, columnNumber + 12, cellStyle, valuesForExcel26[i])
        fillOneCell(row, columnNumber + 13, cellStyle, valuesForExcel31[i])
        fillOneCell(row, columnNumber + 14, cellStyle, valuesForExcel32[i])
        fillOneCell(row, columnNumber + 15, cellStyle, valuesForExcel33[i])
        fillOneCell(row, columnNumber + 16, cellStyle, valuesForExcel34[i])
        fillOneCell(row, columnNumber + 17, cellStyle, valuesForExcel35[i])
        fillOneCell(row, columnNumber + 18, cellStyle, valuesForExcel36[i])
        fillOneCell(row, columnNumber + 19, cellStyle, valuesForExcel17[i])
        row = sheet.createRow(++rowNum)
        dot += step
    }

}

fun fillParameters6(
    wb: XSSFWorkbook,
    dots11: String,
    dots12: String,
    dots13: String,
    dots14: String,
    dots15: String,
    dots16: String,
    columnNumber: Int, rawNumber: Int
) {
    val values11 = dots11.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values12 = dots12.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values13 = dots13.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values14 = dots14.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values15 = dots15.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)
    val values16 = dots16.removePrefix("[").removePrefix("'").removeSuffix("]")
        .split(", ").map { it.replace(',', '.') }.map(String::toDouble)

    val valuesForExcel11 = arrayListOf<Double>()
    val valuesForExcel12 = arrayListOf<Double>()
    val valuesForExcel13 = arrayListOf<Double>()
    val valuesForExcel14 = arrayListOf<Double>()
    val valuesForExcel15 = arrayListOf<Double>()
    val valuesForExcel16 = arrayListOf<Double>()

    var step = 1
    if (values11.size > 200) {
        step = (values11.size - values11.size % 200) / 200
    }

    for (i in values11.indices step step) {
        valuesForExcel11.add(values11[i])
        valuesForExcel12.add(values12[i])
        valuesForExcel13.add(values13[i])
        valuesForExcel14.add(values14[i])
        valuesForExcel15.add(values15[i])
        valuesForExcel16.add(values16[i])
    }
    val sheet = wb.getSheetAt(0)
    var row: Row
    val cellStyle: XSSFCellStyle = generateStyles(wb) as XSSFCellStyle
    var rowNum = rawNumber
    row = sheet.createRow(rowNum)
    var dot = 0
    for (i in valuesForExcel11.indices) {
        fillOneCell(row, columnNumber, cellStyle, dot / 60)
        fillOneCell(row, columnNumber + 1, cellStyle, valuesForExcel11[i])
        fillOneCell(row, columnNumber + 2, cellStyle, valuesForExcel12[i])
        fillOneCell(row, columnNumber + 3, cellStyle, valuesForExcel13[i])
        fillOneCell(row, columnNumber + 4, cellStyle, valuesForExcel14[i])
        fillOneCell(row, columnNumber + 5, cellStyle, valuesForExcel15[i])
        fillOneCell(row, columnNumber + 6, cellStyle, valuesForExcel16[i])
        row = sheet.createRow(++rowNum)
        dot += step
    }

}

private fun drawLineChart18(workbook: XSSFWorkbook) {
    val sheet = workbook.getSheet("Sheet1")
    val sheet2 = workbook.getSheet("Sheet2")
    val lastRowIndex = sheet.lastRowNum - 4

    var i = 0
    val timeData11 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, i, i))
    val valueData11 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData12 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData13 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData14 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData15 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData16 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData21 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData22 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData23 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData24 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData25 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData26 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData31 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData32 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData33 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData34 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData35 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData36 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData17 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))

    var lastRowForGraph = 0
    val graphHeight = 41
    val graphSpace = graphHeight + 3
    val lineChart11 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart11, timeData11, valueData11, "Время, мин.    1 лопасть 1 секция")
    lastRowForGraph += graphSpace
    val lineChart12 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart12, timeData11, valueData12, "Время, мин.    1 лопасть 2 секция")
    lastRowForGraph += graphSpace
    val lineChart13 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart13, timeData11, valueData13, "Время, мин.    1 лопасть 3 секция")
    lastRowForGraph += graphSpace
    val lineChart14 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart14, timeData11, valueData14, "Время, мин.    1 лопасть 4 секция")
    lastRowForGraph += graphSpace
    val lineChart15 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart15, timeData11, valueData15, "Время, мин.    1 лопасть 5 секция")
    lastRowForGraph += graphSpace
    val lineChart16 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart16, timeData11, valueData16, "Время, мин.    1 лопасть 6 секция")
    lastRowForGraph += graphSpace
    val lineChart21 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart21, timeData11, valueData21, "Время, мин.    2 лопасть 1 секция")
    lastRowForGraph += graphSpace
    val lineChart22 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart22, timeData11, valueData22, "Время, мин.    2 лопасть 2 секция")
    lastRowForGraph += graphSpace
    val lineChart23 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart23, timeData11, valueData23, "Время, мин.    2 лопасть 3 секция")
    lastRowForGraph += graphSpace
    val lineChart24 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart24, timeData11, valueData24, "Время, мин.    2 лопасть 4 секция")
    lastRowForGraph += graphSpace
    val lineChart25 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart25, timeData11, valueData25, "Время, мин.    2 лопасть 5 секция")
    lastRowForGraph += graphSpace
    val lineChart26 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart26, timeData11, valueData26, "Время, мин.    2 лопасть 6 секция")
    lastRowForGraph += graphSpace
    val lineChart31 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart31, timeData11, valueData31, "Время, мин.    3 лопасть 1 секция")
    lastRowForGraph += graphSpace
    val lineChart32 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart32, timeData11, valueData32, "Время, мин.    3 лопасть 2 секция")
    lastRowForGraph += graphSpace
    val lineChart33 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart33, timeData11, valueData33, "Время, мин.    3 лопасть 3 секция")
    lastRowForGraph += graphSpace
    val lineChart34 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart34, timeData11, valueData34, "Время, мин.    3 лопасть 4 секция")
    lastRowForGraph += graphSpace
    val lineChart35 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart35, timeData11, valueData35, "Время, мин.    3 лопасть 5 секция")
    lastRowForGraph += graphSpace
    val lineChart36 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart36, timeData11, valueData36, "Время, мин.    3 лопасть 6 секция")
    lastRowForGraph += graphSpace
    val lineChart17 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart17, timeData11, valueData17, "Время, мин.    Вода")
    lastRowForGraph += graphSpace
}

private fun drawLineChart6(workbook: XSSFWorkbook) {
    val sheet = workbook.getSheet("Sheet1")
    val sheet2 = workbook.getSheet("Sheet2")
    val lastRowIndex = sheet.lastRowNum - 1
    var i = 0

    val timeData11 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, i, i))
    val valueData11 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData12 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData13 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData14 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData15 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))
    val valueData16 = DataSources.fromNumericCellRange(sheet, CellRangeAddress(15, lastRowIndex, ++i, i))

    var lastRowForGraph = 0
    val graphHeight = 41
    val graphSpace = graphHeight + 3
    val lineChart11 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart11, timeData11, valueData11, "1 секция, сек")
    lastRowForGraph += graphSpace
    val lineChart12 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart12, timeData11, valueData12, "2 секция, сек")
    lastRowForGraph += graphSpace
    val lineChart13 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart13, timeData11, valueData13, "3 секция, сек")
    lastRowForGraph += graphSpace
    val lineChart14 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart14, timeData11, valueData14, "4 секция, сек")
    lastRowForGraph += graphSpace
    val lineChart15 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart15, timeData11, valueData15, "5 секция, сек")
    lastRowForGraph += graphSpace
    val lineChart16 = createLineChart(sheet2, lastRowForGraph, lastRowForGraph + graphHeight)
    drawLineChart18(lineChart16, timeData11, valueData16, "6 секция, сек")
}

private fun drawLineChart18(
    lineChart: XSSFChart,
    xAxisData: ChartDataSource<Number>,
    yAxisData: ChartDataSource<Number>,
    nameOfOI: String
) {
    val data = lineChart.chartDataFactory.createLineChartData()
    val xAxis = lineChart.chartAxisFactory.createCategoryAxis(AxisPosition.BOTTOM)
    val yAxis = lineChart.createValueAxis(AxisPosition.LEFT)
    yAxis.crosses = org.apache.poi.ss.usermodel.charts.AxisCrosses.AUTO_ZERO

    val series = data.addSeries(xAxisData, yAxisData)
    series.setTitle("График")
    lineChart.plot(data, xAxis, yAxis)

    lineChart.axes[0].setTitle(nameOfOI)
    lineChart.axes[1].setTitle("T, °C")

    val plotArea = lineChart.ctChart.plotArea
    plotArea.lineChartArray[0].smooth
    val ctBool = CTBoolean.Factory.newInstance()
    ctBool.`val` = false
    plotArea.lineChartArray[0].smooth = ctBool
    for (series in plotArea.lineChartArray[0].serArray) {
        series.smooth = ctBool
    }
}

private fun fillOneCell(row: Row, columnNum: Int, cellStyle: XSSFCellStyle, points: Double): Int {
    val cell: Cell = row.createCell(columnNum)
    cell.cellStyle = cellStyle
    cell.setCellValue(points)
    return columnNum + 1
}

private fun fillOneCell(row: Row, columnNum: Int, cellStyle: XSSFCellStyle, points: Int): Int {
    val cell: Cell = row.createCell(columnNum)
    cell.cellStyle = cellStyle
    cell.setCellValue(points.toString())
    return columnNum + 1
}

private fun generateStyles(wb: XSSFWorkbook): CellStyle {
    val headStyle: CellStyle = wb.createCellStyle()
    headStyle.wrapText = true
    headStyle.borderBottom = BorderStyle.THIN
    headStyle.borderTop = BorderStyle.THIN
    headStyle.borderLeft = BorderStyle.THIN
    headStyle.borderRight = BorderStyle.THIN
    headStyle.alignment = HorizontalAlignment.CENTER
    headStyle.verticalAlignment = VerticalAlignment.CENTER
    return headStyle
}

private fun createLineChart(sheet: XSSFSheet, rowStart: Int, rowEnd: Int, col1: Int = 1, col2: Int = 19): XSSFChart {
    val drawing = sheet.createDrawingPatriarch()
    val anchor = drawing.createAnchor(0, 0, 0, 0, col1, rowStart, col2, rowEnd)

    return drawing.createChart(anchor)
}

private fun createLineChart(sheet: XSSFSheet): XSSFChart {
    val drawing = sheet.createDrawingPatriarch()
    val anchor = drawing.createAnchor(0, 0, 0, 0, 3, 16, 36, 26)

    return drawing.createChart(anchor)
}