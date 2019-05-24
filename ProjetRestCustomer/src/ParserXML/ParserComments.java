package ParserXML;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Model.Comment;

public class ParserComments {
	
	public ParserComments() {
	}
	
	public static Comment getComment(Node node) {
	    Comment comment = new Comment();
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
	        // comment response
	        NodeList commentResponse = ((Element)node).getElementsByTagName("responseComment");
	        if(commentResponse.getLength() > 0) {
	        	comment.setComment(getComment(commentResponse.item(0)));
	        	node.removeChild(commentResponse.item(0));
	        }

	        Element element = (Element) node;
	        comment.setId(Integer.parseInt(Parser.getTagValue("idComment", element)));
	        comment.setMessage(Parser.getTagValue("message", element));
	        comment.setPost(ParserPosts.getPost(element));
	        comment.setUser(ParserUsers.getUser(element));
	        comment.setCommentDate(Parser.getTagValue("commentDate", element));
	    }
	    return comment;
	}
	
	public static List<Comment> getCommentsXML(String commentsXML) {
		List<Comment> commentList = new ArrayList<Comment>();
        
        try {
        	// load XML as Document in memory
            Document doc = Parser.convertStringToDocument(commentsXML);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("comment");
            
            // convert document to Object List
            for (int i = 0; i < nodeList.getLength(); i++) {
            	commentList.add(getComment(nodeList.item(i)));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return commentList;
	}
	
	public static Comment getCommentXML(String commentXML) {
		Comment comment = new Comment();
		
		try {
		// load XML as Document in memory
		 Document doc = Parser.convertStringToDocument(commentXML);
         doc.getDocumentElement().normalize();
         
         NodeList nodeList = doc.getElementsByTagName("comment");

         // convert document to Object
         comment = getComment(nodeList.item(0));

		} catch (Exception e1) {
            e1.printStackTrace();
        }

	    return comment;
	}
}
