package com.graffiti.multipad.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.graffiti.multipad.model.Book
import reactor.core.publisher.Mono

@RequestMapping("/v1/books")
@RestController
class SpringReactiveBranchOfficeController {
    val bookMap = mapOf(
        1L to Book("1984"),
        2L to Book("Brave New World"),
        3L to Book("Fahrenheit 451"),
        4L to Book("Animal Farm"),
        5L to Book("The Catcher in the Rye")
    )

    @GetMapping("/{book-id}")
    fun getBook(@PathVariable("book-id") bookId: Long): Mono<Book> {
        Thread.sleep(5_000L)
        val book = bookMap[bookId] ?: return Mono.empty()

        return Mono.just(book)
    }
}