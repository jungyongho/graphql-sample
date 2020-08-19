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

const val ACCOUNT_LOADER_NAME = "ACCOUNT_LOADER"

@Component
open class AccountService (private val builder : WebClient.Builder){
    private val log = org.slf4j.LoggerFactory.getLogger(Account::class.java)

    val accountLoader = DataLoader<String, Mono<Account>> { ids ->
        CompletableFuture.supplyAsync {
            listOf(getAccount(ids))
        }
    }

    fun getAccount(userId:List<String>): Mono<Account>{
            return Mono.just(Account(userId.get(0), "", listOf("GA")))
    }

    fun getAccountList(): Mono<List<Account>> {

        log.info("api call getAccountList")
        return builder.build().get()
                .uri("https://run.mocky.io/v3/ec48c536-fa44-405d-bfc1-6b7425b9c0af")
                .retrieve().bodyToFlux(Account::class.java)//bodyToMono(List<Account>::class.java)
                .collectList()
//        return accountList.toMono()
    }
}