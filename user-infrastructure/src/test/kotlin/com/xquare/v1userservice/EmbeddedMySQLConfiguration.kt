package com.xquare.v1userservice

import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.config.Charset
import com.wix.mysql.config.MysqldConfig
import com.wix.mysql.distribution.Version
import com.xquare.v1userservice.configuration.datasource.DatasourceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import javax.annotation.PreDestroy

@EnableConfigurationProperties(DatasourceProperties::class)
@TestConfiguration
class EmbeddedMySQLConfiguration(
    datasourceProperties: DatasourceProperties
) {

    private final val mysqlServer: EmbeddedMysql

    init {
        val config = MysqldConfig.aMysqldConfig(Version.v8_latest)
            .withCharset(Charset.UTF8)
            .withPort(3306)
            .withUser(datasourceProperties.username, datasourceProperties.password)
            .build()

        mysqlServer = EmbeddedMysql.anEmbeddedMysql(config)
            .addSchema("test")
            .start()
    }

    @PreDestroy
    fun stopServer() {
        mysqlServer.stop()
    }
}
