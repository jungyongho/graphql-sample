package com.example.demo

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@Component("GameAccountDataFetcher")
class GameAccountDataFetcher:DataFetcher<Any?> {
//    https://expediagroup.github.io/graphql-kotlin/docs/next/schema-generator/execution/data-fetching-environment

    lateinit var gameAccountList:ArrayList<GameAccount>
    constructor(){

        gameAccountList = ArrayList<GameAccount>()
//        gameAccountList.add(GameAccount("G1", "G1"))
//        gameAccountList.add(GameAccount("G2", "G2"))
//        gameAccountList.add(GameAccount("G3", "G3"))
//        gameAccountList.add(GameAccount("G4", "G4"))
//        gameAccountList.add(GameAccount("G5", "G5"))
//        gameAccountList.add(GameAccount("G6", "G6"))
//        gameAccountList.add(GameAccount("G7", "G7"))
//        gameAccountList.add(GameAccount("G8", "G8"))
//        gameAccountList.add(GameAccount("G9", "G9"))

    }

    override fun get(environment: DataFetchingEnvironment): GameAccount {
        val gameAccountId = environment?.getSource<Account>()?.gameAccountId!!
        return getGameAccountStream(gameAccountId[0])
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