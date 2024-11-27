package com.example.emailmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ListEmailServlet", value = "/list-email-servlet")
public class ListEmailServlet extends HttpServlet {
    // Liste des adresses email
    //Initialisation : Au départ, emailList est une liste vide, c'est-à-dire qu'elle ne contient aucun élément.
    private List<String> emailList = new ArrayList<>();

    public void init() throws ServletException {
        // Récupérer le chemin relatif depuis le paramètre d'initialisation

        String relativePath = getServletConfig().getInitParameter("filePath");
        // Obtient le paramètre d'initialisation filePath défini dans le fichier web.xml.

        String filePath = getServletContext().getRealPath(relativePath);
        //Convertit le chemin relatif en chemin absolu sur le système de fichiers du serveur.

        if (filePath == null) {
            throw new ServletException("Le chemin du fichier n'est pas défini dans web.xml");
        }

        //lire une liste d'emails depuis un fichier sérialisé.
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            // Charger la liste des emails depuis le fichier s'il existe

            // ObjectInputStream est utilisé pour lire des objets depuis un fichier binaire.
            // Ici, il est connecté à un FileInputStream qui ouvre le fichier spécifié par filePath.

            // Charger la liste des emails depuis le fichier sérialisé :Lorsque le servlet est initialisé dans la méthode init(), le fichier contenant les emails sérialisés est lu avec un ObjectInputStream. L'objet List<String> récupéré (s'il existe) est alors affecté à emailList.
            emailList = (List<String>) in.readObject();

            // in.readObject() : Cette méthode lit un objet sérialisé depuis le fichier.
            // Nous supposons que cet objet est une List<String>, donc il est nécessaire de faire un casting explicite (List<String>).
            // Si le fichier ne contient pas une liste sérialisée ou si son contenu est corrompu, une exception sera levée.
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé : " + filePath);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de la lecture du fichier des adresses email", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configurer la réponse en tant que HTML
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>Liste des Emails</title></head>");
            out.println("<body>");
            out.println("<h1>Membres:</h1>");

            /*if (emailList.isEmpty()) {
                out.println("<p>Aucune adresse email n'est disponible.</p>");
            } else {
                out.println("<ul>");
                for (String email : emailList) {
                    out.println("<li>" + email + "</li>");
                }
                out.println("</ul>");
            }*/
            out.println("<hr>");
            out.println("<br>");
            out.println("<h3>Entrez votre addresse email:</h3>");
            out.println("<form action='/list-email-servlet' method='POST'>");
            out.println("<input type='email name='email required><br> <br>");
            out.println("<input type='submit' value='subscribe'>");
            out.println("<input type='submit' value='unsubscribe'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    public void subscribe(String email){
        
    }
}
