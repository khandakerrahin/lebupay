package com.lebupay.common;

import java.io.File;
import java.io.PrintStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.springframework.stereotype.Service;

/**
 * This Class is used Filter.
 * @author Java-Team
 *
 */
@Service
public class PrintlnSeterToFile {
	
	private static final PrintStream CONSOLE_PRINTSTREAM = System.out;
	private static PrintStream FILE_PRINTSTREAM;
	
	/**
	 * This method is print in log.
	 * @param request
	 */
	public static void setPrintlnSeterToFile(ServletRequest request){
		
		if(!request.getServerName().equals("localhost")) {
			if(FILE_PRINTSTREAM == null){
				try {
					
					// get absolute path of the application
			        ServletContext context = request.getServletContext();
			        String appPath = context.getRealPath("");
			        // construct the complete absolute path of the file
			        String fullPath = appPath + "logs"; 
			        File dir = new File(fullPath);
			        
			        File serverFile = null;
			        if (!dir.exists()) {
			        	dir.mkdirs();
			        }
			        serverFile = new File(dir.getPath() + File.separator + "console_log.txt");
		        	FILE_PRINTSTREAM = new PrintStream(serverFile);
		        	System.setOut(FILE_PRINTSTREAM);
		        	System.setErr(FILE_PRINTSTREAM);
				    
				} catch (Exception e) {
				     e.printStackTrace();
				}
			}
			else if(!FILE_PRINTSTREAM.equals(System.out)){
					System.setOut(FILE_PRINTSTREAM);
			}
		}
		else if(!CONSOLE_PRINTSTREAM.equals(System.out)){
				System.setOut(CONSOLE_PRINTSTREAM);
				System.setErr(CONSOLE_PRINTSTREAM);
		}
		
	}
}
