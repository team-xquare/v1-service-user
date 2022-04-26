package com.xquare.v1userservice.configuration.datasource

import com.linecorp.kotlinjdsl.query.HibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreator
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreatorImpl
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import org.hibernate.reactive.mutiny.Mutiny
import org.hibernate.reactive.session.ReactiveSession
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ReactiveSession::class)
class QueryBuilderConfig {

    @Bean
    fun subqueryCreator(): SubqueryCreator {
        return SubqueryCreatorImpl()
    }

    @Bean
    fun entityManagerFactory(datasourceProperties: DatasourceProperties): EntityManagerFactory {
        val datasourcePropertyBuilder = DatasourcePropertyBuilder(datasourceProperties)
        val properties = datasourcePropertyBuilder.buildEntityManagerPropertiesFromDatasourceProperties()
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
