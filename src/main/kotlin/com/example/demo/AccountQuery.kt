package com.example.demo

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
@GraphQLDescription("")
class AccountQuery(private val accountService: AccountService, private val gameAccountDataFetcher: GameAccountDataFetcher): Query{
//    @GraphQLDescription("")
//    fun getAccountLists():List<Account>{
//        return accountService.getAccountList()
//    }
//    fun getAccountMono(): Mono<Account> {
//        accountService.get()
//        return Mono.just(Account("test","mono test success"))
//    }
    @GraphQLDescription("Account:GameAccount 1:1 mapping")
    fun getAccountListMono():Mono<List<Account>>{
        return accountService.getAccountList()
    }
//    fun getGameAccountList(gameAccountId:Array<String>):List<GameAccount>{
//        return gameAccountDataFetcher.getGameAccountList(gameAccountId)
//    }
}

@GraphQLDescription("")
data class Account(
    var userId:String,
    var loginName:String,
    var gameAccountId:String
){
    lateinit var gameAccount:GameAccount
}

data class GameAccount(
    var GameAccountId:String,
    var nickName:String
)

