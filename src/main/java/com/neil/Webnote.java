package com.neil;

import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.neil.Note.Note;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: saghosh
 * Date: 18/06/13
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
@At("/webnotes")
@Show("/notes.html")
public class Webnote {

    private List<Note> notes;
    private Note note = new Note();

    @Get
    public void get() {
        this.notes = ObjectifyService.ofy().load().type(Note.class).list();  //load from db
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Post
    public String post() {

        note.setDate(new Date());
        ObjectifyService.ofy().save().entities(note).now();
        return "webnotes";
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
