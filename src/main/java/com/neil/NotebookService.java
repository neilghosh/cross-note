package com.neil;

import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.neil.Note.Note;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: saghosh
 * Date: 14/06/13                    ß
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
@At("/notes")
@Service
public class NotebookService {

    private Note note = new Note();

    public NotebookService() {
        ObjectifyService.register(Note.class);
    }

    @Get
    Reply<List<Note>> showNotes() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        List<Note> notes = ObjectifyService.ofy().load().type(Note.class).list();
        return Reply.with(notes).as(Json.class).headers(headers);
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }


    @Post
    public Reply postNote(Request request) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        note = request.read(Note.class).as(Json.class);


        note.setDate(new Date());
        Objectify objectify = ObjectifyService.ofy();
        objectify.save().entities(note).now();

        return Reply.with(note).as(Json.class).headers(headers);
        /*
        //to redirect to a url e.g. when called after a form submit
        return Reply.saying().redirect("/servlet");
        */
    }
}
