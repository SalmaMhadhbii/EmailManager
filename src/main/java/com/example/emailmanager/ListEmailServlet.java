package com.example.emailmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ListEmailServlet", value = "/list-email-servlet")
public class ListEmailServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ListEmailServlet.class.getName());

    //Initialisation : Au départ, emailList est une liste vide, c'est-à-dire qu'elle ne contient aucun élément.
    private final List<String> emailList = new ArrayList<>();
    private String filePath;
    private String subscribe;
    private String unsubscribe;

    //question 1
    @Override
    public void init() throws ServletException {
        //1. Récupération du chemin du fichier via getInitParameter à partir de paramétre d'initialisation emailFilePath dans web.xml:
        filePath = getServletContext().getInitParameter("emailFilePath");

        //2. Vérification de la validité du chemin:
        if (filePath == null || filePath.isEmpty()) {
            throw new ServletException("Le chemin du fichier d'adresses email est manquant.");
        }
        //3.Chargement des emails depuis le fichier:
        // getServletContext().getRealPath(filePath):
        // Cette méthode convertit le chemin relatif donné dans le fichier web.xml
        // en un chemin absolu sur le système de fichiers du serveur.
        // Cela permet d'accéder au fichier indépendamment de l'emplacement de déploiement de l'application.

        // FileReader : Ouvre ce fichier pour permettre sa lecture.

        //BufferedReader : Lit les données du fichier ligne par ligne

        try (BufferedReader br = new BufferedReader(new FileReader(getServletContext().getRealPath(filePath)))) {

            String line;
            // br.readLine():
            // Lit une ligne du fichier. Si le fichier est vide ou toutes les lignes ont été lues,
            // la méthode retourne null, ce qui met fin à la boucle.
            while ((line = br.readLine()) != null) {
                // line.trim():
                // Supprime les espaces inutiles au début et à la fin de la chaîne de caractères.
                // Cela garantit qu'aucun email incorrect (avec des espaces) ne sera ajouté à la liste.
                // emailList.add():
                // Ajoute l'email extrait (après nettoyage) à la liste en mémoire (emailList).
                // Cette liste sera utilisée pour afficher, ajouter ou supprimer des emails.
                emailList.add(line.trim());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erreur lors de la lecture du fichier d'adresses email : {0}", e.getMessage());
            throw new ServletException("Erreur lors de la lecture du fichier d'adresses email.", e);
        }
    }

    //question 2
    /*
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>Liste des Emails</title></head>");
            out.println("<body>");
            out.println("<h1>Membres:</h1>");

            if (emailList.isEmpty()) {
                out.println("<p>Aucune adresse email n'est disponible.</p>");
            } else {
                out.println("<ul>");
                for (String email : emailList) {
                    out.println("<li>" + email + "</li>");
                }
                out.println("</ul>");
            }

            out.println("<hr>");
            out.println("<br>");
            out.println("<h3>Entrez votre addresse email:</h3>");
            out.println("<form method='post'>");
            out.println("<input type='email' name='email' required><br> <br>");
            out.println("<button type='submit' name='action' value='subscribe'>subscribe</button>");
            out.println("<button type='submit' name='action' value='unsubscribe'>unsubscribe</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    */
    //question 3
    public void subscribe(String email) {
        //if (!emailList.contains(email)) {
            emailList.add(email);
            saveEmailsToFile();

            // Met à jour une variable (probablement pour une notification ou un suivi)
            subscribe = email;
        //}
    }
    public void unsubscribe(String email) {
        //if (emailList.contains(email)) {
            emailList.remove(email);
            saveEmailsToFile();
            unsubscribe = email;
        //}
    }
    private void saveEmailsToFile() {
        try (BufferedWriter writer  = new BufferedWriter(
                // FileWriter ouvre le fichier spécifié en mode écriture
                // getServletContext().getRealPath(filePath) obtient le chemin absolu du fichier dans le système
                new FileWriter(getServletContext().getRealPath(filePath), false))) {
            // false en second argument signifie que le fichier sera écrasé à chaque appel

            // Parcourir chaque email dans la liste des emails
            for (String email : emailList) {
                writer.write(email);// Écrit l'adresse email dans le fichier
                writer.newLine();// Ajoute un saut de ligne après chaque email
            }
        } catch (IOException e) {
            // En cas d'erreur (ex : fichier non accessible, problème d'écriture), on log l'erreur
            logger.log(Level.SEVERE, "Erreur lors de l'enregistrement des adresses email dans le fichier : {0}", e.getMessage());
        }
    }

    //question 4
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //recuperer les parametres de formulaire
        // Récupère l'adresse e-mail saisie par l'utilisateur.
        String email = request.getParameter("email");
        //Récupère l'action choisie par l'utilisateur qui est specifie dans lattribut value de form (ex. subscribe ou unsubscribe).
        String action = request.getParameter("action");

        //gestion erreurs
        if (email == null || email.trim().isEmpty()) {
            //erreur email non fourni
            //response.sendError : Retourne une erreur HTTP 400 (Bad Request) avec un message expliquant le problème.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erreur : L'adresse email ne peut pas être vide.");
            //Le return interrompt l'exécution de la méthode.
            return;
        }
        if (action == null || action.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Erreur : Une action est requise (Subscribe ou Unsubscribe).");
            return;
        }


            //gestion de l'action
            if (action.equals("subscribe")) {
                if (emailList.contains(email)) {
                    //erreur :lemail est deja dans la liste
                    out.println("<html><body>");
                    out.println("<h3 style='color:red;'>erreur : email est deja dans la liste</h3>");
                }else{
                    //ajouter email a la liste
                    subscribe(email);
                    out.println("<html><body>");
                    out.println("<h3 style='color:green;'>succés:adresse email ajoutée</h3>");
                }
            } else if (action.equals("unsubscribe")) {
                if (!emailList.contains(email)) {
                    out.println("<html><body>");
                    out.println("<h3 style='color:red;'>erreur : email nest pas inscrit</h3>");
                }else {
                    //supprimer email de la liste
                    unsubscribe(email);
                    out.println("<html><body>");
                    out.println("<h3 style='color:green;'>succés:adresse email supprimée</h3>");
                }
            }


        //reafficher page
        doGet(request, response);

        // out.println("<a href=\"EmailServlet\">Afficher la liste</a>");
    }

    //Partie 2
    //question 1
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher=getServletContext.getRequestDispatcher()
    }

}
