package com.neil;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.neil.Note.Note;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class NotebookServlet extends HttpServlet{

    static {
        ObjectifyService.register(Note.class);
    }

    @Override public void doGet(
            HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        //Render the form for adding notes
        resp.getWriter().println(
                "<form method=post  action=\"/notes\" >" +
                        "<input name=\"note.text\" size=\"20\" type=text/>" +
                        "<input type=submit value=\"Add Note\">" +
                        "</form>");

        resp.getWriter().println("List of notes");

        //load all the data from datastore
        Objectify objectify= ObjectifyService.ofy();
        List<Note> notes = objectify.load().type(Note.class).list();
        resp.getWriter().println("<table><tr style=\"background:grey\"><th>Date</th><th>Note</th></tr>");
        for(Note noteEntry:notes){
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<td>"+noteEntry.getDate().toString() + "</td><td>" + noteEntry.getText()+"</td>");
            resp.getWriter().println("</tr>");
        }
        resp.getWriter().println("<table>");


    }
    }