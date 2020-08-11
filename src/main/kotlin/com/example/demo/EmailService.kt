package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class EmailService(private val builder:WebClient.Builder){
    private val log = org.slf4j.LoggerFactory.getLogger(EmailService::class.java)
    fun emails(emailId:String): Mono<Email> {

        log.info("api call emails")
        return builder.build().get().uri("https://run.mocky.io/v3/25c81f3e-8400-4c2a-a366-2331df517714")
                .retrieve().bodyToMono(Email::class.java)
//        return Mono.just(arrayListOf<Email>(Email("jyh@naver.com","naver.com"), Email("test@gmail.com","gmail.com")))
    }
}