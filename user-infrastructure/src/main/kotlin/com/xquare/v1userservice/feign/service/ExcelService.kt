package com.xquare.v1userservice.feign.service

import com.xquare.v1userservice.feign.ExcelClient
import com.xquare.v1userservice.feign.property.ExcelProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse


@Service
class ExcelService(
    private val excelClient: ExcelClient,
    private val properties: ExcelProperties
) {

    fun createExcelSheet(httpServletResponse: HttpServletResponse){
        excelClient.createExcelSheet(httpServletResponse)
    }

    fun saveExcelInfo(file: MultipartFile){
        excelClient.saveExcelInfo(properties.scheme, properties.host, properties.port, properties.database, properties.username, properties.password, file)
    }

    fun createExcelSheetAsDB(){
        excelClient.createExcelSheetAsDB(properties.scheme, properties.host, properties.port, properties.database, properties.username, properties.password)
    }
}
