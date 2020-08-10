package com.example.demo

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@Component
class AccountService {
    lateinit var accountList:ArrayList<Account>
    constructor(){
        accountList = ArrayList<Account>()
        accountList.add(Account("1","1", "G1"))//arrayOf("G1","G2","G3","G4")))
        accountList.add(Account("2","2", "G5"))//arrayOf("G5","G6","G7","G8")))
    }
    fun getAccountList(): Mono<List<Account>> {
        return accountList.toMono()
    }
}