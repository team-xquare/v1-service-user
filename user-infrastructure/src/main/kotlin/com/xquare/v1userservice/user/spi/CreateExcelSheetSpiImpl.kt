package com.xquare.v1userservice.user.spi

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CreateExcelSheetSpiImpl {
    fun createExcelSheet(serverWebExchange: ServerWebExchange) {
        val workbook = createExcelWorkbook()

        serverWebExchange.response.headers.add("Content-Disposition", "attachment; filename=spring_excel_download.xlsx")

        val response = serverWebExchange.response

        val dataMono = Mono.fromCallable {
            val outputStream = response.bufferFactory().allocateBuffer()
            workbook.write(outputStream.asOutputStream())
            outputStream
        }

        response.writeWith(dataMono)
    }

    private fun createExcelWorkbook(): Workbook {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")
        sheet.defaultColumnWidth = 28

        val headerCellStyle = createHeaderCellStyle(workbook)

        val headerNames = arrayOf("name", "birthDay", "")
        var rowCount = 0

        // Header
        val headerRow: Row = sheet.createRow(rowCount++)
        headerNames.forEachIndexed { i, header ->
            val headerCell = headerRow.createCell(i)
            headerCell.setCellValue(header)
            headerCell.cellStyle = headerCellStyle
        }

        return workbook
    }


    fun createHeaderCellStyle(workbook: Workbook): XSSFCellStyle {
        val font = workbook.createFont().apply {
            //color = XSSFColor(255, 255)
        }

        return (workbook.createCellStyle() as XSSFCellStyle).apply {
            setFont(font)
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderBottom = BorderStyle.THIN
        }
    }
}