/**
 * SpiderMain Wraps a Main Function for Testing and Running a Web Crawler.
 * 
 * @author Christine McCreary
 * 
 * (Based on Java Web Crawler Tutorial:
 * http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/)
 *
 */
public class SpiderMain {
	
	public static void main(String[] args) {
		Spider spider = new Spider();
		spider.search("http://arstechnica.com/", "computer");
	}

}
