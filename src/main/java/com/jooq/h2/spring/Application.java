package com.jooq.h2.spring;

import com.jooq.h2.spring.model.BookData;
import com.jooq.h2.spring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


@SpringBootApplication
@RestController
public class Application {



    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



    @PostMapping("/book/{authorId}/{bookTitle}")
    public void createBook(@PathVariable int authorId, @PathVariable String bookTitle){
        bookService.create(new Random().nextInt(100), authorId,bookTitle);
    }
    
    @GetMapping("/book")
    public BookDataProto.BookList getBooks(){
        return bookService.getBooks();
    }


    @GetMapping("/book/{bookId}")
    public BookDataProto.Book getBookById(@PathVariable int bookId){
        return bookService.getBookById(bookId);
    }



    // JSON response body if we want
    @GetMapping("/book.json")
    public List<BookData> getBooksByJson(){
        return bookService.getBooksJson();
    }


    @GetMapping("/book/{bookId}.json")
    public BookData getBookByIdJson(@PathVariable int bookId){
        return bookService.getBookByIdJson(bookId);
    }




}
