package com.neil;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


public class TrackerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        ChannelPresence presence = channelService.parsePresence(req);
        System.out.print("Client trying to connect with ID " + presence.clientId());
        //Save the new client in servlet context
        ServletContext context = getServletContext();
        //Object liveUsers = context.getAttribute("liveUsers");
        HashMap<String, ChannelPresence> liveUsers = (HashMap<String, ChannelPresence>) context.getAttribute("liveUsers");
        if (null == liveUsers) {
            System.out.println("Initialising client list");
            liveUsers = new HashMap<String, ChannelPresence>();
            context.setAttribute("liveUsers", liveUsers);
        }
        if(liveUsers.containsKey(presence.clientId())) {
            System.out.println("Err.... this guy was already connected ! ");
        } else {
            liveUsers.put(presence.clientId(), presence);
            System.out.println(" New client connected with ID  " + presence.clientId());
            //broadCastClientList(liveUsers);
            //broadCastClientList(liveUsers);
        }

    }
    /*
    private void broadCastClientList(HashMap<String, ChannelPresence> liveUsers) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        for(ChannelPresence cp : liveUsers.values()){
            System.out.println(cp.clientId());
            String channelMessageStr="{\"command\":\"note\",\"data\":"+noteStr+"}";
            System.out.print(" Sending message to client after wrapping --> " +channelMessageStr);
            channelService.sendMessage(new ChannelMessage(cp.clientId(),channelMessageStr));
        }
    }
    */
}

