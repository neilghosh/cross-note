/**
 * This is a servlet which is configured through Guice via the servlet listener MyGuiceConfig.java
 * The requests to the URL /note are redirected to this servlet.
 *
 * This is a conventional way to rednder HTML pages in the servlet and also accept the GET/POST
 * requests upon form submition of the same servlet.
 *
 * However mixing HTML and Java code is as ugly as JSP, I am looking for a templating technique
 *
 * This also uses Objectify to persisting data in Google AppEngine BigTable.
 *
 */
package com.neil;

import com.googlecode.objectify.ObjectifyService;
import com.neil.Note.Note;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class NotebookServlet extends HttpServlet {

    //Register the entity class for data persistance  service
    static {
        ObjectifyService.register(Note.class);
    }

    /**
     * Get requests come here
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        //Render the form for adding notes
        resp.getWriter().println(
                "<form method=post  action=\"/servlet\" >" +
                        "<input name=\"note.text\" size=\"20\" type=text/>" +
                        "<input type=submit value=\"Add Note\">" +
                        "</form>");

        resp.getWriter().println("List of notes");

        //load all the data from datastore
        List<Note> notes = ObjectifyService.ofy().load().type(Note.class).list();
        //Render the notes in each row of the table.
        resp.getWriter().println("<table><tr style=\"background:grey\"><th>Date</th><th>Note</th></tr>");
        for (Note noteEntry : notes) {
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<td>" + noteEntry.getDate().toString() + "</td><td>" + noteEntry.getText() + "</td>");
            resp.getWriter().println("</tr>");
        }
        resp.getWriter().println("<table>");
    }

    /**
     * Handles the form submit post requests and redirect to the same get request to display the list of
     * notes
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Note note = new Note();
        note.setDate(new Date());
        note.setText(req.getParameter("note.text"));
        ObjectifyService.ofy().save().entities(note).now();
        doGet(req, resp);

    }

}