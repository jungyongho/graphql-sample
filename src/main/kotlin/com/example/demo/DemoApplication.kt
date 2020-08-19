package com.example.demo

import com.expediagroup.graphql.execution.FunctionDataFetcher
import com.expediagroup.graphql.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetcherFactories
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.config.EnableWebFlux
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KType

@SpringBootApplication
@EnableWebFlux
class DemoApplication{

	@Bean
	fun hooks() = MonadHooks()

	@Bean
	fun dataFetcherFactoryProvider(
			objectMapper: ObjectMapper
	) = CustomDataFetcherFactoryProvider( objectMapper)

}


fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

//todo :willGenerrate 파보기.
class MonadHooks : SchemaGeneratorHooks {
	override fun willGenerateGraphQLType(type: KType): GraphQLType? {
		when(type.classifier){
			Mono::class -> type.arguments.firstOrNull()?.type
			else -> type
		} ?: type
		return super.willGenerateGraphQLType(type)

	}
	override fun willResolveMonad(type: KType): KType = when (type.classifier) {
		Mono::class -> type.arguments.firstOrNull()?.type
		else -> type
	} ?: type
}

class CustomFunctionDataFetcher(target: Any?, fn: KFunction<*>, objectMapper: ObjectMapper) : FunctionDataFetcher(target, fn, objectMapper) {
	override fun get(environment: DataFetchingEnvironment): Any? = when (val result = super.get(environment)) {
		is Mono<*> -> result.toFuture()
		else -> result //MONOFlatMap
	}
}

class CustomDataFetcherFactoryProvider(
		private val objectMapper: ObjectMapper
) : SimpleKotlinDataFetcherFactoryProvider(objectMapper) {

	override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>): DataFetcherFactory<Any?> = DataFetcherFactory<Any?> {
		CustomFunctionDataFetcher(
				target = target,
				fn = kFunction,
				objectMapper = objectMapper)
	}

	override fun propertyDataFetcherFactory(kClass: KClass<*>, kProperty: KProperty<*>): DataFetcherFactory<Any?> =
			if (kProperty.isLateinit) {
				DataFetcherFactories.useDataFetcher(GameAccountDataFetcher())
			} else {
				super.propertyDataFetcherFactory(kClass, kProperty)
			}
}