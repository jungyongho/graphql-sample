package com.example.demo

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.adapter.rxjava.toCompletable
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.CompletableFuture

@Component("GameAccountDataFetcher")
class GameAccountDataFetcher:DataFetcher<Any?> {
//    https://expediagroup.github.io/graphql-kotlin/docs/next/schema-generator/execution/data-fetching-environment

    lateinit var gameAccountList:ArrayList<GameAccount>
    constructor(){

        gameAccountList = ArrayList<GameAccount>()
        gameAccountList.add(GameAccount("G111", "G1"))
        gameAccountList.add(GameAccount("G222", "G2"))
        gameAccountList.add(GameAccount("G333", "G3"))
        gameAccountList.add(GameAccount("G444", "G4"))
        gameAccountList.add(GameAccount("G555", "G5"))
        gameAccountList.add(GameAccount("G666", "G6"))
//        gameAccountList.add(GameAccount("G7", "G7"))
//        gameAccountList.add(GameAccount("G8", "G8"))
//        gameAccountList.add(GameAccount("G9", "G9"))

    }

    override fun get(environment: DataFetchingEnvironment): Mono<GameAccount> {
        val gameAccountId = environment?.getSource<Account>()?.gameAccountId!!
//        return getGameAccount(gameAccountId[0]).toFuture()
        return getGameAccount(gameAccountId[0])
    }
    fun getGameAccount(gameAccountId:String):Mono<GameAccount>{
        return gameAccountList.toFlux()
                .filter{ it.gameAccountId == gameAccountId }
                .last()
    }

    fun getGameAccountStream(gameAccountId:String):GameAccount{
        for(ga in gameAccountList){
            if(ga.gameAccountId == gameAccountId)
                return ga
        }
        return gameAccountList.get(0)
    }
}