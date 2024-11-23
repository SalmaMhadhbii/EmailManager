package com.example.emailmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ListEmailServlet", value = "/list-email-servlet")
public class ListEmailServlet extends HttpServlet {
    //list des address:
    private List<String> emailList = new ArrayList<>();

    public void init() throws ServletException {
        // Récupérer le chemin du fichier depuis les paramètres d'initialisation
        String filePath = getServletConfig().getInitParameter("filePath");

        if(filePath == null) {
            throw new ServletException("filepath not set in web.xml");
        }
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))){
            // charger list email depuis fichier si existe
            emailList=(List<String>) in.readObject();
        } catch (Exception e) {
            throw new ServletException("erreur lors de la lecture des fichier des adresses email",e);
        }

    }



}