package com.wixsite.jingmacv;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

/*
 * This class scrapes Damas data on transmissions.
 */
public class DamasScraperHtmlUnit {

	public static WebClient client;
	public static HtmlPage page;
	
	/*
	 * The only public method in this class. Scrapes a website for data.
	 */
	public static void scrape() throws IOException {
		// Initializes web driver.
		init("https://damasifa.unicorn.eu/DMSPCAS22.asp");
		// Navigates to the page with the data.
		HtmlTableCell btn = page.querySelector(".tlacitko-vel2");
		page = btn.click();
		// Finds all the data and prints it.
		parseTable();
		// Closes driver.
		client.close();
	}
	
	/*
	 * Initializes a headless web driver that represents a website DOM. 
	 */
	private static void init(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		client = new WebClient(BrowserVersion.FIREFOX_45);
		client.getOptions().setUseInsecureSSL(true);
		client.getOptions().setThrowExceptionOnScriptError(false);
		page = client.getPage(url);
	}

	
	/*
	 * Parses the data and prints it.
	 */
	private static void parseTable() {
		DomElement tbody = page.getElementsByTagName("tbody").get(27);		
		DomNodeList<HtmlElement> rowsUnfiltered = tbody.getElementsByTagName("tr");
		List<HtmlElement> rows = IntStream.range(0, rowsUnfiltered.size())
		         .filter(i -> i > 1 && i < 50)
		         .mapToObj(i -> rowsUnfiltered.get(i))
		         .collect(Collectors.toList());
		List<HtmlElement> fields = rows.stream().map(row -> row.getElementsByTagName("td")).flatMap(td -> td.stream()).collect(Collectors.toList());
		fields.forEach(field -> System.out.println(field.asText()));
	}
	
	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		//BasicConfigurator.configure();
		long startTime = System.nanoTime();
		try {
			scrape();
		} catch (IOException e) {
			e.printStackTrace();
		}; 
		long stopTime = System.nanoTime();
		System.out.println("DURATION: " + (stopTime - startTime) / 1000000000.0); // 7 seconds
	}
}
