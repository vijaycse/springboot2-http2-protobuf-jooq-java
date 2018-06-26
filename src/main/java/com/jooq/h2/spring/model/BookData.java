package com.jooq.h2.spring.model;


import lombok.Data;
import org.jooq.example.db.h2.tables.Book;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BookData implements Serializable {

    @Column(name = "title")
    private String bookTitle;

    @Column(name = "author_id")
    private int authorId;

    @Column(name = "published_in")
    private int published;

}
