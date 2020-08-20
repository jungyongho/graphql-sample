package com.example.demo

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.runBlocking
import org.dataloader.DataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.adapter.rxjava.toCompletable
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.CompletableFuture
import kotlin.random.Random
import kotlin.streams.toList

const val ACCOUNT_LOADER_NAME = "ACCOUNT_LOADER"

@Component
open class AccountService (private val builder : WebClient.Builder){
    private val log = org.slf4j.LoggerFactory.getLogger(AccountService::class.java)

    val accountLoader = DataLoader<String, Mono<Account>> { ids ->
        CompletableFuture.supplyAsync {
            getAccountList(ids)
        }
    }

    fun getAccount(userId:List<String>): Mono<Account>{
            return Mono.just(Account(userId.get(0), "", listOf("GA")))
    }

    fun getAccount(id:String): Mono<Account> {

        log.info("api call getAccountList::" + "https://run.mocky.io/v3/ec48c536-fa44-405d-bfc1-6b7425b9c0af/"+ id)
        return builder.build().get()
                .uri("https://run.mocky.io/v3/ec48c536-fa44-405d-bfc1-6b7425b9c0af/"+ id)
                .retrieve().bodyToFlux(Account::class.java)//bodyToMono(List<Account>::class.java)
                .last()
    }
//    Mono<List<Acconut>>
    fun getAccountList(ids: List<String>): List<Mono<Account>>{
        return ids.toList().stream().map { getAccount(it) }.toList()

//        toFlux().flatMap { getAccount(it) }
//                .collectList()
    }
}