package com.neil;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.googlecode.objectify.ObjectifyService;
import com.neil.Note.Note;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sitebricks class for REST Webservice endpoints
 * So its annotated with @Service , hence it will always expect a return type Reply from
 * the endpoint methods annotated with @Get and @Post etc. On the brighter side we don't have to
 * associate any HTMl template
 */
@At("/notes")
@Service
public class NotebookService {

    @Inject
    private HttpServletRequest hsr;

    private Note note = new Note();
    //Register the entity class for the Objectify persistance service.
    public NotebookService() {
        ObjectifyService.register(Note.class);
    }

    @Get
    Reply<List<Note>> showNotes() {
        //Prepare the HTTP headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        //Fetch data from database
        List<Note> notes = ObjectifyService.ofy().load().type(Note.class).list();
        //Convert the entity object to JSON and return.
        return Reply.with(notes).as(Json.class).headers(headers);
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    /**
     * Post request endpoint here inserts data in database
     * and returns the JSON of the single entry which was newly
     * created
     *
     * @param request the body of the request containing data is
     *                obtained from here.
     * @return
     */
    @Post
    public Reply postNote(Request request) {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        //Read JSON data and create entity object
        note = request.read(Note.class).as(Json.class);
        //System generated date instead of user
        note.setDate(new Date());

        //Store data
        ObjectifyService.ofy().save().entities(note).now();
        //Broadcast the messages to all the connected clients
        boradCastNotes(request,note);
        //Just return the newly added data
        return Reply.with(note).as(Json.class).headers(headers);
        /*
        //to redirect to a url but this class is only for
        //webservice call, we don't ahve to redirect to any page
        return Reply.saying().redirect("/servlet");
        */
    }

    private void boradCastNotes(Request request,Note note) {
        ServletContext context = hsr.getSession().getServletContext();
        HashMap<String,ChannelPresence> liveUsers = (HashMap<String,ChannelPresence>)context.getAttribute("liveUsers");
        if(liveUsers != null){
            ChannelService channelService = ChannelServiceFactory.getChannelService();
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("List of connected client ... ");
            String noteStr = null;
            try {
                noteStr = mapper.writeValueAsString(note);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            for(ChannelPresence cp : liveUsers.values()){
                System.out.println(cp.clientId());
                System.out.print(" Sending message to client --> " +noteStr);
                String channelMessageStr="{\"command\":\"note\",\"data\":"+noteStr+"}";
                System.out.print(" Sending message to client after wrapping --> " +channelMessageStr);
                channelService.sendMessage(new ChannelMessage(cp.clientId(),channelMessageStr));


            }
        }
    }
}
