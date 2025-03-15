package com.graffiti.multipad.controller

import java.time.LocalDateTime
import java.time.LocalTime
import org.slf4j.Logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import com.graffiti.multipad.model.Book
import reactor.core.publisher.Mono

@RequestMapping("/v1/books")
@RestController
class SpringReactiveHeadOfficeController {

    val baseUri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(7081)
        .path("/v1/books")
        .build()
        .encode()
        .toUri()

    @GetMapping("/{book-id}")
    fun getBook(@PathVariable("book-id") bookId: Long): Mono<Book> {
        val getBookUri = UriComponentsBuilder.fromUri(baseUri)
            .path("/{book-id}")
            .build()
            .expand(bookId)
            .encode()
            .toUri()

        return WebClient.create()
            .get()
            .uri(getBookUri)
            .retrieve()
            .bodyToMono(Book::class.java)
    }

    @Bean
    fun run(): CommandLineRunner {
        return CommandLineRunner { args ->
            println("# 요청 시작 시간: ${LocalDateTime.now()}")

            for(i in 0..5) {
                var a = i
            }
        }
    }
}