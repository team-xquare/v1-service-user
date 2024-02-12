package com.xquare.v1userservice.feign.contoller

import com.xquare.v1userservice.feign.service.ExcelService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/excel")
class ExcelController(
    private val excelService: ExcelService
) {
    @GetMapping
    fun createExcelSheet(httpServletResponse: HttpServletResponse) {
        excelService.createExcelSheet(httpServletResponse)
    }

    @PostMapping
    fun saveExcelInfo(file: MultipartFile) {
        excelService.saveExcelInfo(file)
    }

    @GetMapping("/userInfo")
    fun createExcelSheetAsDB() {
        excelService.createExcelSheetAsDB()
    }
}
