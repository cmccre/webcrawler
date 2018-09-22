import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * SpiderLeg Implements the Functionality for Crawling Individual URLs
 * and Searching Found URLs (in a Prior Web Crawl) for a Given Word.
 * 
 * @author Christine McCreary
 * 
 * (Based on Java Web Crawler Tutorial:
 * http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/)
 *
 */
public class SpiderLeg {
	
	// Fake User Agent so Web Server thinks bot is Normal Web Browser
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	// Set of Unique URLs Crawled
	private Set<String> links = new HashSet<>();
	
    /**
     * Searches and Stores the Unique Links Found at a Given URL.
     * 
     * @param url : A String Specifying the Location of the HTML Document
     *              to be Searched for Other Links.
     *              
     * @return Boolean Value Indicating if the Search was Successful or Not.
     */
    public boolean crawl(String url) {
    	
    	// Attempt Retrieval of HTML Document at url
        Document htmlDocument = getHTMLDoc(url);
        
        // Failed HTTP Request/HTML Retrieval
        if (htmlDocument == null) {
        	return false;
        }
            
        // Find, Print, and Store All Unique Links at url
        Elements linksOnPage = htmlDocument.select("a[href]");
        System.out.println("Performing Crawl at " + url);
        int linkNum = 0;
        for (Element link : linksOnPage) {
        	String linkStr = link.absUrl("href");
        	if (!links.contains(linkStr)) {
        		linkNum++;
        		links.add(linkStr);
        		System.out.println(linkNum + ": " + linkStr);
        	}
        }
        // Successful Crawl
        return true;
    }
    
    /**
     * @return links : If a Successful Crawl was Previously Performed,
     * 				   a Copy of the Set of URLs (Encoded as Strings)
     * 				   Found. Else, an Empty Set.
     */
	public Set<String> getLinks() {
		return new HashSet<>(links);
	}
	
	/**
     * Searches for a Word within the HTML Documents Found in a Prior Crawl.
     * 
     * @param searchWord : The String to Search For Within the Found HTML Documents.
     * 
     * @throws IllegalStateException : When a Successful Crawl was Not Previously Completed.
     * 
     * @return found : A Boolean Value Indicating if searchWord was Found at Least Once.
     */
    public boolean searchForWord(String searchWord) {
    	
        // Error: No Successful Crawl Previously Completed
        if (links.isEmpty()) {
            throw new IllegalStateException("crawl Must be Called Before searchForWord");
        }
        
    	searchWord = searchWord.toLowerCase(); // Ignore Case
        boolean found = false; // Flag if Word Found at All
        
        System.out.println("Performing Search for " + searchWord + ":");
        
        // For All Crawled Links, Search their HTML Documents for searchWord
        for (String link : links) {
        	Document htmlDocument = getHTMLDoc(link);
        	if (htmlDocument != null) {
        		String bodyText = htmlDocument.body().text();
                // Update found when searchWord Found
        		if (bodyText.toLowerCase().contains(searchWord)) {
        			System.out.println("Found " + searchWord + " at: " + link);
        			found = true;
        		}
        	}
        }
        // Return Flag for Found Word
        return found;
    }
    
    /** Attempts HTTP Request for an HTML Document.
     *
     * @param url : A String Specifying the Location of the HTML Document
     * 				to be Requested in an HTTP Request.
     * @return htmlDocument : If the HTTP Request was Successful, an HTML
     * 						  Document Found at the Given url. Otherwise,
     *                        null.
     */
    private Document getHTMLDoc(String url) {
    	 try {
             Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
             Document htmlDocument = connection.get();
             
             // Unsuccessful HTTP Request for HTML Document (Non-HTML Doc. Found)
             if (!connection.response().contentType().contains("text/html")) {
                 return null;
             }
             
             // Successful HTTP Request for HTML Document
             return htmlDocument;
             
         // Unsuccessful HTTP Request    
         } catch (IOException ioe) {
             return null;
         }
    }
}
