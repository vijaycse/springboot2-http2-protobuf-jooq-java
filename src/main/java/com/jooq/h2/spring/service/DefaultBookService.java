package com.jooq.h2.spring.service;


import com.jooq.h2.spring.BookDataProto;
import com.jooq.h2.spring.model.BookData;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


import static org.jooq.example.db.h2.Tables.BOOK;


@Service
public class DefaultBookService implements BookService {

    @Autowired
    DSLContext dsl;

    @Override
    @Transactional
    public void create(int id, int authorId, String title) {
        dsl.insertInto(BOOK).set(BOOK.ID, id).set(BOOK.AUTHOR_ID, authorId).set(BOOK.TITLE, title).execute();
    }

    @Override
    public List<BookData> getBooksJson() {
        return getCollectBooks();
    }

    @Override
    public BookDataProto.BookList getBooks() {
        return convertToBookListProto(getCollectBooks());

    }



    @Override
    public BookData getBookByIdJson(int bookId) {
        return getBookData(bookId);
    }


    @Override
    public BookDataProto.Book getBookById(int bookId) {
        return convertToBookProto(getBookData(bookId));
    }

    private BookData getBookData(int bookId) {
        return dsl.select(BOOK.fields()).from(BOOK).where(BOOK.ID.eq(bookId)).fetchInto(BookData.class)
                .stream()
                .findFirst()
                .get();
    }


    private BookDataProto.Book convertToBookProto(BookData bookData) {
        return BookDataProto.Book.newBuilder()
                .setAuthorId(bookData.getAuthorId())
                .setPublished(bookData.getPublished())
                .setBookTitle(bookData.getBookTitle())
                .build();
    }

    private List<BookData> getCollectBooks() {
        return dsl
                .select(BOOK.fields())
                .from(BOOK)
                .fetchInto(BookData.class)
                .stream()
                .collect(Collectors.toList());
    }

    private BookDataProto.BookList convertToBookListProto(List<BookData> bookDataList) {
        BookDataProto.BookList bookProtoList = BookDataProto.BookList.newBuilder().build();
        bookDataList.forEach(bookData -> {
            bookProtoList.toBuilder().addBook(
                    BookDataProto.Book.newBuilder()
                            .setAuthorId(bookData.getAuthorId())
                            .setPublished(bookData.getPublished())
                            .setBookTitle(bookData.getBookTitle())
                            .build()
            );
        });
        return bookProtoList;
    }
}
