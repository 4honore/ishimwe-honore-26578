package com.example;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Get the value from the input field
        String searchTerm = request.getParameter("query");

        // 2. Redirect the user to Google
        // We append the user's input to the standard Google search URL
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            response.sendRedirect("https://www.google.com/search?q=" + searchTerm);
        } else {
            response.sendRedirect("https://www.google.com");
        }
    }
}