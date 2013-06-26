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


public class TrackerServlet1 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        ChannelPresence presence = channelService.parsePresence(req);
        System.out.print("Client disconnected with ID " + presence.clientId());

        ServletContext context = getServletContext();
        HashMap<String, ChannelPresence> liveUsers = (HashMap<String, ChannelPresence>) context.getAttribute("liveUsers");
        if (null != liveUsers) {
            if (liveUsers.containsKey(presence.clientId())) {
                liveUsers.remove(presence.clientId());
                System.out.println("Client was disconnected");
            } else {
                System.out.println("Client was not connected");
            }
        } else {
            System.out.println("No client was ever connected");
        }
    }
}
