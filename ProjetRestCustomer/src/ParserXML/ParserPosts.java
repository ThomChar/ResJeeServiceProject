package ParserXML;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Model.Post;

public class ParserPosts {
	
	public ParserPosts() {
	}
	
	public static Post getPost(Node node) {
	    Post post = new Post();
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
	        Element element = (Element) node;
	        post.setId(Integer.parseInt(Parser.getTagValue("idPost", element)));
	        post.setImage(Parser.getTagValue("image", element));
	        post.setComment(Parser.getTagValue("commentPost", element));
	        post.setPostDate(Parser.getTagValue("postDate", element));
	        post.setUser(ParserUsers.getUser(element));
	    }

	    return post;
	}
	
	public static List<Post> getPostsXML(String postsXML) {
		List<Post> postList = new ArrayList<Post>();
        
        try {
        	// load XML as Document in memory
            Document doc = Parser.convertStringToDocument(postsXML);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("post");
            
            // convert document to Object List
            for (int i = 0; i < nodeList.getLength(); i++) {
            	postList.add(getPost(nodeList.item(i)));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return postList;
	}
	
	public static Post getPostXML(String postXML) {
		Post post = new Post();
		
		try {
		// load XML as Document in memory
		 Document doc = Parser.convertStringToDocument(postXML);
         doc.getDocumentElement().normalize();
         
         NodeList nodeList = doc.getElementsByTagName("post");

         // convert document to Object
         post = getPost(nodeList.item(0));

		} catch (Exception e1) {
            e1.printStackTrace();
        }

	    return post;
	}
}
