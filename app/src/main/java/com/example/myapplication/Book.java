package com.example.myapplication;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hth on 2017/12/15 0015.
 */

public class Book extends DataSupport implements Serializable {
    @Column(unique = true)
    private int id;
    private int bookClass;
    private String title;
    private String author;
    private String content;

    @Column(nullable = true)
    private Date finishDate;





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookClass() {return bookClass;}

    public void setBookClass(int bookClass) {this.bookClass = bookClass;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
