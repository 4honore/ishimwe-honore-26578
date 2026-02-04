package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get the parameters
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 2. Logic for password length
        if (pass.length() < 8) {
            out.println("<h3>Hello " + user + ", your password is weak. Try a strong one.</h3>");
        } else {
            out.println("<h3>Welcome " + user + "</h3>");
        }
    }
}