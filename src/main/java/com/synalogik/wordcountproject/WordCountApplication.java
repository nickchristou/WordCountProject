package com.synalogik.wordcountproject;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 
 * Project created using maven: https://start.spring.io/
 *
 */
@SpringBootApplication
public class WordCountApplication {

    public static void main(String[] args) throws Exception {
    	
    	boolean openBrowser = false;
    	
    	//check if openbrowser flag is set
    	for (String arg : args) 
    	{
    		if (arg.equals("-openbrowser"))
    			openBrowser = true;
    	}
    	
    	//Check if desktop supported
    	if (openBrowser && !Desktop.isDesktopSupported()) 
    	{
    		openBrowser = false;
    		System.out.println("Desktop not supported");
    	}
    	
    	//main app launch
        new SpringApplicationBuilder(WordCountApplication.class).headless(!openBrowser).run(args);
        
        if (openBrowser)
        	openHomePage();
    	}
    
    /**
     * Open index in default browser
     */
    private static void openHomePage() {
        try {
            URI homepage = new URI("http://localhost:8080/");
            Desktop.getDesktop().browse(homepage);
        } catch (URISyntaxException | IOException e) 
        {
    		System.out.println("Failed to start browser");
        	e.printStackTrace();
        }
    }
}
