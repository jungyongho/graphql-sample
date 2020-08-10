package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class EmailService{
    @Autowired
    lateinit var builder : WebClient.Builder
    fun emails(emailId:String): Mono<Email> {
        return builder.build().get().uri("https://run.mocky.io/v3/25c81f3e-8400-4c2a-a366-2331df517714")
                .retrieve().bodyToMono(Email::class.java)
//        return Mono.just(arrayListOf<Email>(Email("jyh@naver.com","naver.com"), Email("test@gmail.com","gmail.com")))
    }
}