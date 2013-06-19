package com.neil.Note;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: saghosh
 * Date: 14/06/13
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Note {
    @Id
    private Long id;
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String text;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
