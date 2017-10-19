package com.wixsite.jingmacv;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestStream extends WebScraper {
	
	private final static Logger log1 = LogManager.getLogger("Logger1");
	private final static Logger log2 = LogManager.getLogger("Logger2");

	public static void main(String[] args) {
//		log1.error("THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
//		log1.warn("THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
//		log2.error("THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
//		log2.warn("THIS IS TO FILL THE DOCUMENT UNTIL ITS FULL AHAHA");
		
		File f = new File("/src/main/resources/logging/folder-File1.log");
		String absPath = f.getAbsolutePath();
		System.out.println(absPath.substring(0, absPath.lastIndexOf("\\") + 1) + f.getName().split("-")[1]);
	}
}
