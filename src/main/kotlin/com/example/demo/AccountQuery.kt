package com.example.demo

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
@GraphQLDescription("")
class AccountQuery: Query{
    lateinit var list:ArrayList<Account>
    constructor(){
        list = ArrayList<Account>()
        list.add(Account("123","jyh"))
        list.add(Account("124","jyh123"))
    }
    @GraphQLDescription("")
    fun getAccountLists():List<Account>{
        return list
    }
    fun getAccountMono(): Mono<Account> {
        return Mono.just(Account("test","mono test success"))
    }
    fun getAccountListMono():Mono<List<Account>>{
        return Mono.just(list)
    }
}

@GraphQLDescription("")
data class Account(
        var userId:String,
        var loginName:String
)