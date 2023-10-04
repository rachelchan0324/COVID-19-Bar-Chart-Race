/**
 * OpenFile.java
 * 
 * @author   Scott DeRuiter and Rachel Chan
 * @version  1.0
 * @since    9/10/2020
 */

import java.util.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OpenFile {	
	/**
	 * Opens a file for reading. First, creates a Scanner instance and 
	 * sets it to null (a default) in case of a FileNotFoundException. 
	 * If the scanner instance can be created with the requested
	 * file, then return it, otherwise print out an error message 
	 * via a try-catch block.
	 *
	 * @param filestring   The name of the file to be opened.
	 * @return             A Scanner instance of the file to be opened.
	 */
	public static Scanner openToRead (String fileName) {		
		
		Scanner fromFile = null;
		File file = new File(fileName);
		try {
			fromFile = new Scanner(file);
		}
		catch(FileNotFoundException error) {
			System.err.println("Sorry, the file " + fileName + " can not be found.");
			System.exit(1);
		}
		return fromFile;
		
	}
	
	/**
	 * Opens a file for writing (very similar in structure to 
	 * openToRead()). First, creates a PrintWriter instance and sets it
	 * to null in case an IO exception occurs. If no exception, then 
	 * return it, otherwise print out an error message via a try-catch
	 * block.
	 *
	 * @param fileName    The name of the file to be opened (created).
	 * @return             A PrintWriter instance of the file to be opened (created).
	 */
	public static PrintWriter openToWrite (String fileName) {
		PrintWriter outFile = null;
		try {
			outFile = new PrintWriter(fileName); 
		}
		catch(IOException error) {
			System.err.println("Sorry, the file "  + fileName + " could not be created.");
			System.exit(-1);
		}
		return outFile;
	}
}
