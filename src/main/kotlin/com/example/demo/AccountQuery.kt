package com.example.demo

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import com.fasterxml.jackson.annotation.JacksonInject
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirstOrNull
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
    fun getCustomer():Mono<Customer>{
        return Mono.just(Customer("jung"))
    }
}

data class Customer(
    var name:String
){
    //success
    suspend fun accounts(environment: DataFetchingEnvironment): Account? {
        return environment.getDataLoader<String, Mono<Account>>(ACCOUNT_LOADER_NAME)
                .load("userID0").await().awaitFirstOrNull()
    }
    //fail
    suspend fun accounts2(environment: DataFetchingEnvironment): Mono<Account> {
        return environment.getDataLoader<String, Mono<Account>>(ACCOUNT_LOADER_NAME)
                .load("userID0").await()
    }
    //success
     fun accounts3(): Mono<Account> {
        return Mono.just(Account("userId3", "", listOf("GA")))
    }
    //fail
    suspend fun accounts4(environment: DataFetchingEnvironment): Mono<Account> {
        return coroutineScope {
            async{
                environment.getDataLoader<String, Mono<Account>>(ACCOUNT_LOADER_NAME)
                        .load("userID0").await()
            }.await()
        }

    }
}

@GraphQLDescription("")
data class Account(
    var userId:String,
    var loginName:String,
    var gameAccountId:List<String>
){
    private val log = org.slf4j.LoggerFactory.getLogger(Account::class.java)
//    lateinit var gameAccountDatafetcher:Mono<GameAccount>
    fun gameAccounts():Mono<List<GameAccount>>{
        return this.gameAccountId.toList().toFlux()
                .flatMap { gameAccount(it) }
                .collectList()
    }
    fun gameAccount(id:String):Mono<GameAccount>{
        log.info("api call gameAccount")
        var url = "https://run.mocky.io/v3/33bba6c5-e621-4c42-9186-efdf2d02acc5"
        if(id == "G222")
            url = "https://run.mocky.io/v3/bdf8309a-7e6d-4430-8fb8-1dcd2682f289"
        else
            url = "https://run.mocky.io/v3/33bba6c5-e621-4c42-9186-efdf2d02acc5"

        return WebClient.builder().build().get()
                .uri(url)
                .retrieve().bodyToMono(GameAccount::class.java)
                .flatMap { it.nickName = it.nickName.plus("nn")
                            Mono.just(it)}
    }

    fun emails():Mono<Email>{
        log.info("api call emails")
        return WebClient.builder().build().get().uri("https://run.mocky.io/v3/25c81f3e-8400-4c2a-a366-2331df517714")
                .retrieve().bodyToMono(Email::class.java)
//        return emailService.emails(emailId)
    }
}

data class GameAccount(
    var gameAccountId:String,
    var nickName:String
){
    private val log = org.slf4j.LoggerFactory.getLogger(GameAccount::class.java)
//    @Autowired
//    private lateinit var emailService: EmailService

    fun emails():Mono<Email>{
        log.info("api call emails")
        return WebClient.builder().build().get().uri("https://run.mocky.io/v3/25c81f3e-8400-4c2a-a366-2331df517714")
                .retrieve().bodyToMono(Email::class.java)
//        return emailService.emails(emailId)
    }
}

data class Email(
    val emailId:String,
    val email:String
)