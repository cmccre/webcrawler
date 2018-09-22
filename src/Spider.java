import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Spider Implements the Functionality for Crawling Multiple URLs,
 * Found via Links From a Starting URL, and Searching the Corresponding
 * Found URLs (in a Prior Web Crawl) for a Given Word.
 * 
 * @author Christine McCreary
 * 
 * (Based on Java Web Crawler Tutorial:
 * http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/)
 *
 */
public class Spider {
	// Limit Number of Pages to Search to Prevent Infinite Search
	private static final int MAX_PAGES_TO_SEARCH = 10;
	
	// Set of URLs Already Visited
	private Set<String> pagesVisited = new HashSet<>();
	
	// List of URLs Found to Visit in Future
	private List<String> pagesToVisit = new LinkedList<>();
	
  /**
   * Crawls a Given URL to Search for a Given Word within the
   * Corresponding HTML Document or the HTML Documents Connected
   * via Links to the Given URL. Prints Information Concerning its
   * Search and the Pages on Which the Given Word is Found.
   * 
   * @param url : A String Specifying the Location of the HTML Document
   * 			  to Begin the Word Search At.
   * 
   * @param searchWord : The String to Search For Within the Found HTML Documents.
   */
	public void search(String url, String searchWord) {
		SpiderLeg leg = new SpiderLeg(); // Object to Perform Crawls and Word Search
		String currentUrl; // Current URL Crawling and Searching
		
		// Crawl Up to the Limit of Number of URLs to Search
		while (pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			// Set Current URL to Next Unvisited URL
			if (pagesToVisit.isEmpty()) {
				currentUrl = url;
				pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			
			// Crawl Current URL and Add Found URLs List of URLs
			// Visit Next
			leg.crawl(currentUrl);
			pagesToVisit.addAll(leg.getLinks());
		}
		// Search for Word at Crawled URLs, Indicate if Not Found
		boolean found = leg.searchForWord(searchWord);
		if (!found) {
			System.out.println(searchWord + " NOT FOUND");
		}
	}
	
  /**
   * @return nextUrl: A String Indicating the Location of the Next Unvisited
   *                  HTML Document Found in a Prior Search.
   */
	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = pagesToVisit.remove(0); // Next URL Found
		} while (pagesVisited.contains(nextUrl)); // Keep Searching if URL Already Visited
		pagesVisited.add(nextUrl); // Add URL to Visited Set
		return nextUrl;
	}

}
