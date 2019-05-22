package com.instablog.connexion;

import org.apache.commons.codec.binary.Base64;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import com.instablog.model.*;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;

import org.json.simple.JSONObject;
//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//permet conversion d'objet json en xml
import cjm.component.cb.xml.ToXML;
import jdk.internal.org.xml.sax.XMLReader;

import com.google.gson.Gson;
import com.instablog.dao.*;


public class TokenManagement {

   //private static final Logger logger = Logger.getLogger(TokenManagement.class);
   
   public static String generateToken(int userID, UserDao uamanager) throws InstaException{
       
       //Generation of a random UUID
       UUID uuid = UUID.randomUUID();
       String randomUUIDString = uuid.toString();
       
       //Stocke UUID dans la base de données pour l'utilisateur correspondant à l'ID     // à implémenter
       uamanager.changeUserUUID(userID, randomUUIDString);
       
       //Generation of the Expiration Date (here localdatetime + 15 min)
       LocalDateTime today = LocalDateTime.now();
       LocalDateTime expirationDate = today.plusMonths(6);
       
       //Creation de l'objet JSON
       JSONObject obj = new  JSONObject();
       obj.put("userID",userID);
       obj.put("uuid",randomUUIDString);
       obj.put("dateExp",expirationDate);
       
       //StringBuilder xmlobj = new ToXML().convertToXML(obj, true);
       //Encrypte le String de l'objet en BASE64
       byte[] bytesEncoded = Base64.encodeBase64(obj.toString().getBytes());
       System.out.println("encoded value is " + new String(bytesEncoded));
       String token = new String(bytesEncoded);
       
       return token;

   }
   
   public static JSONObject recreateJsonObjectToken(String decodedString) throws Exception{
	   
	   JSONObject objToken = null;
	   
	   try {
		   //1st split
		   String [] stringSplitByComa = decodedString.split(",");
	       
	       String str1 = stringSplitByComa[0];
	       //System.out.println(str1);
	       String str2 = stringSplitByComa[1];
	       //System.out.println(str2);
	       String str3 = stringSplitByComa[2];
	       //System.out.println(str3);
	       
	      //2nd split
	       String [] str1SplitByColon = str1.split(":");
	       String strDateExp = str1SplitByColon[1]+":"+str1SplitByColon[2]+":"+str1SplitByColon[3];
	       //System.out.println(strDateExp);
	       
	       String [] str2SplitByColon = str2.split(":");
	       String strUserId = str2SplitByColon[1];
	       //System.out.println(strUserId);
	       
	       /*String hel = str3.replaceAll("\"","");
	       System.out.println(hel);*/
	       String [] str3SplitByInvertedCommas = str3.split("(:|})");
	       String strUUID = str3SplitByInvertedCommas[1].replaceAll("\"","");
	       /*System.out.println(str4.replaceAll("\"",""));
	       String [] str4SplitBybrackets = str4.split("}");
	       String strUUID = str4SplitBybrackets[0];
	       System.out.println(strUUID);*/
	       
	     //Creation de l'objet JSON
	       objToken = new  JSONObject();
	       objToken.put("userID",Integer.parseInt(strUserId));
	       objToken.put("uuid",strUUID);
	       objToken.put("dateExp",LocalDateTime.parse(strDateExp, DateTimeFormatter.ISO_DATE_TIME));
	       
	       System.out.println(objToken);
		
	} catch (Exception e) {
		throw e;
	}
	return objToken;
	   
   }
   
   /**
    * Decrypte Le Token pour verifier ci celui correspond à la string stocké dans la BD
    * @param token 
    * @return  
    */
   public static boolean verifyToken(String token, String UUID) throws Exception{
       
       boolean verifie = true;
       
       //Decrypte le Token
       String decodedString = new String(DatatypeConverter.parseBase64Binary(token)); 
       System.out.println("Decoded value is " + decodedString);
       //logger.info(decodedString);
       System.out.println("decodedString");
       
       System.out.println("init parser done");
       
       /*Object obj = new JSONParser().parse(new StringReader(decodedString)); 
       JSONObject objToken = (JSONObject) obj;*/
       
       JSONObject objToken = recreateJsonObjectToken(decodedString);	// Pas très optimisé mais fonctionnel
       
       System.out.println(objToken);
       //JSONObject objToken = new  JSONObject(decodedString);
       //int userIDMember = objToken.getInt("userID");
       String UUIDMember = objToken.get("uuid").toString();
       System.out.println(UUIDMember);
       //String UUIDMember = objToken.getString("uuid");
       LocalDateTime expirationDate = LocalDateTime.parse(objToken.get("dateExp").toString(), DateTimeFormatter.ISO_DATE_TIME);
       //LocalDateTime expirationDate = LocalDateTime.parse(objToken.getString("dateExp"), DateTimeFormatter.ISO_DATE_TIME);
               
       //Verification pour voir si toujours valide(ExpirationDate) et correspond à la personne qui se connecte
       if(!UUID.equals(UUIDMember)){//Controle UUID attribuer à userIDMember doit correspndre à celui donné dans le Token
    	   System.out.println("UUIDMember ne correspond pas");
    	   throw new Exception("L'UUID du token ne correspond pas à l'UUID de l'utilisateur");
       }else if(LocalDateTime.now().isAfter(expirationDate) || LocalDateTime.now().isEqual(expirationDate)){   // Controle Date Expiration
    	   System.out.println("date expire");
             throw new Exception("La Date d'expiration est depasse veuillez vous reconnecter");  
       }
       
       return verifie;
   }
}