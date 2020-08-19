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
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.CompletableFuture

const val ACCOUNT_LOADER_NAME = "ACCOUNT_LOADER"

//@Autowired
//lateinit var accountService: AccountService
val accountLoader = DataLoader<String, Mono<Account>> { ids ->
    CompletableFuture.supplyAsync {
        runBlocking { listOf(AccountService().getAccount(ids)) }
    }
}

@Component
class AccountService (){
    private val log = org.slf4j.LoggerFactory.getLogger(Account::class.java)

    @Autowired
    lateinit var builder : WebClient.Builder
//    lateinit var accountList:ArrayList<Account>
//    constructor(){
//        accountList = ArrayList<Account>()
//        accountList.add(Account("1","1", "G1"))//arrayOf("G1","G2","G3","G4")))
//        accountList.add(Account("2","2", "G5"))//arrayOf("G5","G6","G7","G8")))
//    }
//    https://run.mocky.io/v3/ec48c536-fa44-405d-bfc1-6b7425b9c0af
//    companion object{
        suspend fun getAccount(userId:List<String>): Mono<Account>{
            return Mono.just(Account(userId.get(0), "", listOf("GA")))
//            return builder.build().get()
//                    .uri("https://run.mocky.io/v3/ec48c536-fa44-405d-bfc1-6b7425b9c0af")
//                    .retrieve().bodyToFlux(Account::class.java)//bodyToMono(List<Account>::class.java)
//                    .collectList()
        }
//    }
    fun getAccountList(): Mono<List<Account>> {

        log.info("api call getAccountList")
        return builder.build().get()
                .uri("https://run.mocky.io/v3/ec48c536-fa44-405d-bfc1-6b7425b9c0af")
                .retrieve().bodyToFlux(Account::class.java)//bodyToMono(List<Account>::class.java)
                .collectList()
//        return accountList.toMono()
    }
}