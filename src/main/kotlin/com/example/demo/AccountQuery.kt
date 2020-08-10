package com.example.demo

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@Component
@GraphQLDescription("")
class AccountQuery(private val accountService: AccountService, private val gameAccountDataFetcher: GameAccountDataFetcher): Query{
    @GraphQLDescription("Account:GameAccount 1:1 mapping")
    fun getAccountListMono():Mono<List<Account>>{
        return accountService.getAccountList()
    }
}

@GraphQLDescription("")
data class Account(
    var userId:String,
    var loginName:String,
    var gameAccountId:List<String>
){

    fun gameAccounts():Mono<List<GameAccount>>{
        return this.gameAccountId.toList().toFlux()
                .flatMap { gameAccount(it) }
                .collectList()
    }
    fun gameAccount(id:String):Mono<GameAccount>{

        if(id == "G111")
            return WebClient.builder().build().get()
                    .uri("https://run.mocky.io/v3/33bba6c5-e621-4c42-9186-efdf2d02acc5")
                    .retrieve().bodyToMono(GameAccount::class.java)
        else if(id == "G222")
            return WebClient.builder().build().get()
                    .uri("https://run.mocky.io/v3/bdf8309a-7e6d-4430-8fb8-1dcd2682f289")
                    .retrieve().bodyToMono(GameAccount::class.java)
        else
            return WebClient.builder().build().get()
                    .uri("https://run.mocky.io/v3/869f4b5b-307e-47cf-b7e5-151345b9f5ec")
                    .retrieve().bodyToMono(GameAccount::class.java)
    }
}

data class GameAccount(
    var gameAccountId:String,
    var nickName:String,
    var emailId:String
){
    @Autowired
    private lateinit var emailService: EmailService
    fun emails():Mono<Email>{
        return emailService.emails(emailId)
    }
}

data class Email(
    val emailId:String,
    val email:String
)