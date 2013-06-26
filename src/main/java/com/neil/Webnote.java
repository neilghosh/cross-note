package com.neil;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.googlecode.objectify.ObjectifyService;
import com.neil.Note.Note;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This class file uses Google Sitebricks to load and enter data to database upon get and put
 * requests.
 * @Show annotation tells to use an html template while serving and
 * @At annotation tells which URL this class should be mapped to
 * @Get and @Post tells which method should the control be redirected when get and post
 * requests come from client.
 *
 * Note that we all the classes in this package in MyGuiceServletConfig.java via Sitebricks() module of Guice
 */
@At("/webnotes")
@Show("/notes.html")
public class Webnote {

    //When HTML page is rendered , this instance variable would be used for the placeholders to populate
    private List<Note> notes;
    private String token;
    //Following instance variable will be populated when form is submitted from the HTML template
    //The name of the input fields will be mapped to the entity components
    private Note note = new Note();


    @Get
    public void get() {

        ChannelService channelService = ChannelServiceFactory.getChannelService();
        token = channelService.createChannel(UUID.randomUUID().toString());
        this.notes = ObjectifyService.ofy().load().type(Note.class).order("-id").list();  //load from db
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Post
    public String post() {
        //Date is not provided by the form, server date is populayted
        note.setDate(new Date());
        ObjectifyService.ofy().save().entities(note).now();

        //Redirect to same class and render the same HTML template
        return "webnotes";
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
