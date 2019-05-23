package ConsoleApplication;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.Object;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;

import org.glassfish.jersey.client.ClientConfig;

import com.sun.xml.internal.ws.util.Pool.Unmarshaller;

import Gestion.*;
import Model.*;

public class ProjetRestClient {
	
	private static String webApiUrl;
	private static List<String> transactionParams;
	private static GestionInstagram gestionInstagram;
	 /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
    	if (args.length < 1)
        {
            System.out.println("Usage: java ConsoleApplication.ProjetRestClient <adresse du serveur>, par défaut : http://localhost:8080/InstaWRest/");
            return;
        }
    	
    	webApiUrl = args[0]; // add test webApiUrl
    	transactionParams = new ArrayList<String>();
    	gestionInstagram = new GestionInstagram(webApiUrl);
    	
    	System.out.println("Bienvenue sur Instagram, saisissez vos commandes (pour obtenir l'aide, saisire : aide)");
    	
        try
        {
            BufferedReader reader = ouvrirFichier(args);  
            String transaction = lireTransaction(reader);
            Init();
            while (!finTransaction(transaction))
            {
                executerTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        }
        finally
        {
        	// détruire le token
        }
    }
    
    static void Init()
    {

    }

    /**
     * Decodage et traitement d'une transaction
     */
    static void executerTransaction(String transaction) throws Exception
    {
        try
        {
            System.out.print(transaction +"\n");
            // Decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, ",");
            if (tokenizer.hasMoreTokens())
            {
                String command = tokenizer.nextToken();
                
                /*
                 * USER
                 */
                if(command.equals("aide")) 
                {
                	System.out.println("Vous pouvez utilisez les commandes suivantes");
                	gestionInstagram.getGestionUser().displayHelp();
                	GestionInstagram.getGestionPost().displayHelp();
                }
                
                /**
                 * 		User Management
                 */
                
                else if(command.equals("afficherUtilisateurs")) 
                {
                	gestionInstagram.getGestionUser().displayUsers();
                }
                else if(command.equals("afficherUtilisateur")) 
                {
                	String idUtilisateur = readString(tokenizer);
                	gestionInstagram.getGestionUser().displayUser(idUtilisateur);
                }
                else if(command.equals("ajouterUtilisateur")) 
                {
                	String pseudo = readString(tokenizer);
                	String firstName = readString(tokenizer);
                	String lastName = readString(tokenizer);
                	String password = readString(tokenizer);
                	gestionInstagram.getGestionUser().ajouter(firstName, lastName, pseudo, password);
                }
                else if(command.equals("login")) 
                {
                	String pseudo = readString(tokenizer);
                	String password = readString(tokenizer);
                	gestionInstagram.getGestionUser().login(pseudo, password);
                }
                else if(command.equals("modifierProfile")) 
                {
                	String pseudo = readString(tokenizer);
                	String firstName = readString(tokenizer);
                	String lastName = readString(tokenizer);
                	gestionInstagram.getGestionUser().update(firstName, lastName, pseudo);
                }
                else if(command.equals("supprimerProfile")) 
                {
                	gestionInstagram.getGestionUser().delete();
                }
                
                /**
                 * 		Post Management
                 */
                
                else if(command.equals("afficherPostes")) 
                {
                	GestionInstagram.getGestionPost().displayPosts();
                }
                else if(command.equals("afficherPoste")) 
                {
                	String idPost = readString(tokenizer);
                	GestionInstagram.getGestionPost().displayPost(idPost);
                }
                else if(command.equals("afficherPostesPourUtilisateur")) 
                {
                	String idUtilisateur = readString(tokenizer);
                	GestionInstagram.getGestionPost().displayPostsForUser(idUtilisateur);
                }
                else if(command.equals("ajoutPoste")) 
                {
                	String comment = readString(tokenizer);
                	String image = readString(tokenizer);
                	GestionInstagram.getGestionPost().ajouter(comment, image);
                }
                else if(command.equals("modifierPoste")) 
                {
                	String idPost = readString(tokenizer);
                	String comment = readString(tokenizer);
                	String image = readString(tokenizer);
                	GestionInstagram.getGestionPost().update(idPost, comment, image);
                }
                else if(command.equals("supprimerPoste")) 
                {
                	String idPost = readString(tokenizer);
                	GestionInstagram.getGestionPost().delete(idPost);
                }
                
                
                /**
                 * 		PAR DEFAUT
                 */
                else
                {
                    System.out.println(" : Transaction non reconnue");
                }
                
            }
        }
        catch (Exception e)
        {
            System.out.println(" " + e.toString());
        }
    }

    
    // ****************************************************************
    // *   Les methodes suivantes n'ont pas besoin d'etre modifiees   *
    // ****************************************************************

    /**
     * Ouvre le fichier de transaction, ou lit à partir de System.in
     */
    public static BufferedReader ouvrirFichier(String[] args) throws FileNotFoundException
    {
        if (args.length < 5)
            // Lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // Lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(new FileInputStream(args[4])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException
    {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        return (transaction == null || transaction.equals("quitter"));
    }

    /** Lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("Autre parametre attendu");
    }

    /**
     * Lecture d'un int java de la transaction entree a l'ecran
     */
    static int readInt(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Integer.valueOf(token).intValue();
            }
            catch (NumberFormatException e)
            {
                throw new Exception("Nombre attendu a la place de \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }

    static Date readDate(StringTokenizer tokenizer) throws Exception
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Date.valueOf(token);
            }
            catch (IllegalArgumentException e)
            {
                throw new Exception("Date dans un format invalide - \"" + token + "\"");
            }
        }
        else
            throw new Exception("Autre parametre attendu");
    }
}
