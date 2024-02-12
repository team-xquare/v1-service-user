package com.xquare.v1userservice.feign

import com.xquare.v1userservice.configuration.feign.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@FeignClient(
    name = "ExcelClient",
    url = "\${excel.url}",
    configuration = [FeignConfig::class]
)
interface ExcelClient {
    @GetMapping
    fun createExcelSheet(httpServletResponse: HttpServletResponse)

    @PostMapping
    fun saveExcelInfo(
        @RequestParam scheme: String,
        @RequestParam host: String,
        @RequestParam port: Int,
        @RequestParam database: String,
        @RequestParam username: String,
        @RequestParam password: String,
        file: MultipartFile
    )

    @GetMapping("/userInfo")
    fun createExcelSheetAsDB(
        @RequestParam scheme: String,
        @RequestParam host: String,
        @RequestParam port: Int,
        @RequestParam database: String,
        @RequestParam username: String,
        @RequestParam password: String
    )
}