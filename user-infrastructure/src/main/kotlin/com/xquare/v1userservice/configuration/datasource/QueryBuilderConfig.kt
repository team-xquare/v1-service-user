package com.xquare.v1userservice.configuration.datasource

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreator
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreatorImpl
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
import org.hibernate.reactive.mutiny.Mutiny
import org.hibernate.reactive.session.ReactiveSession
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ReactiveSession::class)
class QueryBuilderConfig {

    companion object {
        private const val DB_URL_PROPERTY = "javax.persistence.jdbc.url"
        private const val DB_USERNAME_PROPERTY = "javax.persistence.jdbc.user"
        private const val DB_PASSWORD_PROPERTY = "javax.persistence.jdbc.password"
        private const val DB_POOL_SIZE_PROPERTY = "hibernate.connection.pool_size"
        private const val DDL_AUTO_MODE_PROPERTY = "javax.persistence.schema-generation.database.action"
        private const val SHOW_SQL_PROPERTY = "hibernate.show_sql"
        private const val FORMAT_SQL_PROPERTY = "hibernate.format_sql"
        private const val HIGHLIGHT_SQL_PROPERTY = "hibernate.highlight_sql"
        private const val IMPLICIT_NAMING_STRATEGY = "hibernate.implicit_naming_strategy"
        private const val PHYSICAL_NAMING_STRATEGY = "hibernate.physical_naming_strategy"
    }

    @Bean
    fun subqueryCreator(): SubqueryCreator {
        return SubqueryCreatorImpl()
    }

    @Bean
    fun entityManagerFactory(datasourceProperties: DatasourceProperties): EntityManagerFactory {
        val properties = HashMap<String, String>().apply {
            put(DB_URL_PROPERTY, "jdbc:${datasourceProperties.dbms}://${datasourceProperties.host}:${datasourceProperties.port}/${datasourceProperties.database}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Seoul")
            put(DB_USERNAME_PROPERTY, datasourceProperties.username)
            put(DB_PASSWORD_PROPERTY, datasourceProperties.password)
            put(DB_POOL_SIZE_PROPERTY, datasourceProperties.poolSize.toString())
            put(DDL_AUTO_MODE_PROPERTY, datasourceProperties.ddlAuto)
            put(SHOW_SQL_PROPERTY, datasourceProperties.showSql.toString())
            put(FORMAT_SQL_PROPERTY, datasourceProperties.formatSql.toString())
            put(HIGHLIGHT_SQL_PROPERTY, datasourceProperties.highlightSql.toString())
            put(IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy::class.java.name)
            put(PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy::class.java.name)
        }.toMap()

        return Persistence.createEntityManagerFactory("user-service-mysql", properties)
    }

    @Bean
    fun mutinySessionFactory(entityManagerFactory: EntityManagerFactory): Mutiny.SessionFactory =
        entityManagerFactory.unwrap(Mutiny.SessionFactory::class.java)

    @Bean
    fun queryFactory(
        sessionFactory: Mutiny.SessionFactory,
        subqueryCreator: SubqueryCreator
    ): HibernateMutinyReactiveQueryFactory {
        return HibernateMutinyReactiveQueryFactory(
            sessionFactory = sessionFactory,
            subqueryCreator = subqueryCreator
        )
    }
}
