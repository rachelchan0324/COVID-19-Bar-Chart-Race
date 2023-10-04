/**
 * CoronaVirusData.java
 * 
 * @author   Rachel Chan
 * @version  1.0
 * @since    9/13/2020
 */

import java.util.*;
import java.awt.Color;
import java.awt.Font;

public class CoronaVirusData {	
	
	/** The number of cases in ALL states (needed to determine scaling
	 *  when creating the bar graphs). */
	public static int totalCaseCount;
	
	/** An array that contains the number of cases in the 5 states we 
	 *  are creating bar graphs of (California, Texas, Florida, New York,
	 *  Illinois, respectively). */
	public static int [] stateCounts;
	
	/** The date of the set of data that is currently being displayed */
	public static String date;
	
	public CoronaVirusData () {
		totalCaseCount = 0;
		stateCounts = new int [5];
		date = "";
	}

	/**
	 * Creates an instance of CoronaVirusData and calls helper methods
	 * to read in data from the text file and create a corresponding bar 
	 * graph.
	 */
	public static void main(String [] args) {
		CoronaVirusData barGraph = new CoronaVirusData();
		barGraph.setUpCanvas();
		barGraph.getDataFromFile();
	}
	
	/**
	 * Prepares the canvas before drawing objects onscreen. 
	 * 
	 * Sets the background of the canvas to white. Sets the canvas size 
	 * for StdDraw and sets up the x and y scale to 0-1400 and 0-900 
	 * pixels, respectively. Enables double buffering for more fluid 
	 * animation. Sets the font mode for drawing text on bar labels and
	 * scale.
	 */
	private static void setUpCanvas() {
		StdDraw.clear(StdDraw.WHITE);
		
		StdDraw.setCanvasSize(1400, 900);
		
		StdDraw.setXscale(0, 1500);
		StdDraw.setYscale(0, 900);
		
		StdDraw.enableDoubleBuffering();
		
		StdDraw.setFont(new Font("Sans Serif", Font.BOLD, 20));
	}

	/**
	 * Creates a scanner instance to open data.txt with the help of the
	 * OpenFile class. Collects the date, state name, and number of COVID cases
	 * in each line through temporary variables. Updates int totalCaseCount
	 * and stores the case counts of the the 5 states we are creating bar
	 * graphs through stateCounts [].
	 * 
	 * If the date is a NEW date (i.e. it is not the same date 
	 * as the previous date), then it means all the data for the previous
	 * date has been collected and it is time to start drawing the graphs
	 * through drawGraphs().
	 * 
	 * Finally, closes Scanner when the work is done (garbage collection).
	 */
	private static void getDataFromFile() {
		
		Scanner readFile = OpenFile.openToRead("data.txt");
		readFile.nextLine(); // skips the first line of headers
		boolean firstLine = true;
		boolean firstRound = true;
		
		String prevDate = "";
		
		while(readFile.hasNext()){
			String line = readFile.nextLine();
			
			String currDate = line.substring(0, 10);
			
			if(!firstLine && !prevDate.equals(currDate)){ // new date = new set of data
				if(firstRound)
					date = currDate;
				else
					date = prevDate;
				drawGraphs();
				totalCaseCount = 0;
				for(int i = 0; i < 5; i++)
					stateCounts[i] = 0;
				prevDate = currDate;
				firstRound = false;
			}
				
			String state = line.substring(11, line.indexOf(',',11)); 
			
			// find index of the 2nd to last comma and the last comma (the case count is between these 2 indexes)
			int lastCommaIndex = line.lastIndexOf(",");
			int secondToLastCommaIndex = -1;		
			for(int i = lastCommaIndex - 1; i >= 0; i--){
				if(line.charAt(i) == ','){
					secondToLastCommaIndex = i;
					break;
				}
			}
			
			int cases = Integer.parseInt(line.substring(secondToLastCommaIndex + 1, lastCommaIndex));
			
			totalCaseCount += cases;
			
			switch(state){
				case "California": 
					stateCounts[0] = cases;
					break;
				case "Texas":
					stateCounts[1] = cases;
					break;
				case "Florida":
					stateCounts[2] = cases;
					break;
				case "New York": 
					stateCounts[3] = cases;
					break;
				case "Illinois":
					stateCounts[4] = cases;
					break;
			}
			firstLine = false;
		}
		readFile.close();
	}
	
	/**
	 * First, clears the canvas to a white background. Then calls helper
	 * methods to draw the scale, bars, and text of the graphs. Animates 
	 * all the objects at once through StdDraw.show() and DoubleBuffering
	 * and pauses the animation briefly for 100 milliseconds so the 
	 * viewer has enough time to process the frame.
	 */
	private static void drawGraphs() {
		StdDraw.clear(StdDraw.BLACK);

		drawScale();
		drawBars();
		drawTitles();
		
		StdDraw.show();
		StdDraw.pause(75);
	}

	/**
	 * Draws vertical lines with unit labels for the bar graph. First,
	 * searches through stateCounts [] to look for the largest case count,
	 * since that determines how large our units need to be. Determines
	 * unit by calculating the greatest power of 10 that is LESS than the
	 * largest case count (ex. if the largest case count is 34,000, the 
	 * units of the bar graph would be 10,000; if the largest case count
	 * is 24, the units would be 10). The unit can be doubled or halved
	 * if the number of vertical lines is too much or too less, 
	 * respectively. Finally, the line is drawn along with its unit label.
	 */
	private static void drawScale() {
		int largestCaseCount = 0;
		for(int i = 0; i < stateCounts.length; i++){
			if(stateCounts[i] > largestCaseCount)
				largestCaseCount = stateCounts[i];
		}
		
		int unit = 1;
		while (unit * 10 < largestCaseCount)
			unit *= 10;
		
		if(unit != 0 && largestCaseCount/unit < 3)
			unit /= 2;
		else if(unit != 0 && largestCaseCount/unit > 8)
			unit *= 2;
			
		int numberOfLines;
		if(unit == 0)
			numberOfLines = 1;
		else
			numberOfLines = largestCaseCount/unit;
		
		for(int i = 0; i < numberOfLines + 1; i++){
			// scale = how many horizontal pixels = 1 case count
			double scale = 0;
			if(largestCaseCount != 0)
				scale = 1300.0 / largestCaseCount; 
			// + 50 to create a small, white border
			double lineLocation = i * unit * scale + 50;
			
			// for spacing purposes, replace 1000 with k
			String unitLabel;
			
			if(unit >= 1000)
				unitLabel = i * unit / 1000 + "k";
			else
				unitLabel = "" + i * unit;
				
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);	
			
			if(lineLocation > 1000)
				StdDraw.line(lineLocation, 300, lineLocation, 850);
			else
				StdDraw.line(lineLocation, 50, lineLocation, 850);
				
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.setFont(new Font("Sans Serif", Font.PLAIN, 20));
			StdDraw.text(lineLocation, 870, unitLabel);
		}
	}

	/**
	 * Finds the largest case count to determine scale (how many case
	 * counts = 1 pixel) for drawing the bar graph. For each of the 5 
	 * states, draw a horizontal bar with a length that corresponds to
	 * the the number of covid cases reported in that state. Label each
	 * bar with the state name and the number of case counts.
	 */
	private static void drawBars() {
		for(int i = 0; i < 5; i++) {
			
			int largestCaseCount = 0;
			for(int j = 0; j < stateCounts.length; j++){
				if(stateCounts[j] > largestCaseCount)
					largestCaseCount = stateCounts[j];
			}
			// scale = how many case counts 1 pixel represents
			double scale = 0;
			if(largestCaseCount != 0)
				scale = 1300.0 / largestCaseCount;
			
			double length = scale * stateCounts[i];
			double barY = 800 - 85 * i;

			String stateName = "";
			switch(i){
				case 0: 
					stateName = "California"; 
					StdDraw.setPenColor(206,218,245);
					break;
				case 1: 
					stateName = "Texas";
					StdDraw.setPenColor(211,247,193);
					break;
				case 2: 
					stateName = "Florida";
					StdDraw.setPenColor(248,250,217);
					break;
				case 3: 
					stateName = "New York";
					StdDraw.setPenColor(232,206,245);
					break;
				case 4: 
					stateName = "Illinois";
					StdDraw.setPenColor(StdDraw.ORANGE);
					break;
			}
			
			StdDraw.filledRectangle(50 + length/2, barY + 15, length/2, 35);

			double barXEnd = length + 50;
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.textLeft(barXEnd + 10, barY + 15, stateName);
			StdDraw.setPenColor(StdDraw.BLACK);
			String stateCountString = addCommas("" + stateCounts[i]);
			
			StdDraw.textRight(barXEnd - 20, barY + 15, stateCountString);
		}
	}

	/**
	 * Displays the date of the current data set and the total case count 
	 * in the bottom right corner. Changes the font to a bigger size.
	 * Displays the title of this project, "Coronavirus Cases by State".
	 */
	private static void drawTitles()
	{
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		
		switch(month){
			case "01": 
				month = "January";
			case "02":
				month = "February";
			case "03":
				month = "March";
			case "04":
				month = "April";
			case "05":
				month = "May";
			case "06":
				month = "June";
			case "07":
				month = "July";
			case "08":
				month = "August";
			case "09":
				month = "September";
			case "10":
				month = "October";
			case "11":
				month = "November";
			case "12":
				month = "December";
		}
		
		StdDraw.setFont(new Font("Sans Serif", Font.PLAIN, 35));
		StdDraw.setPenColor(StdDraw.WHITE);
		
		StdDraw.textLeft(1050, 250, month + " " + day + ", " + year);
		StdDraw.textLeft(1050, 200, "US Total: " + addCommas("" + totalCaseCount));
		StdDraw.textLeft(1050, 150, "Coronavirus");
		StdDraw.textLeft(1050, 100, "Cases by State");
	}
	
	/** Takes in a number as a String and inserts a comma between every 3
	 *  digits. If the number is less than 4 digits, then no commas
	 *  are needed. */
	public static String addCommas (String str){
		if(str.length() < 4)
			return str;
		else{
			String newStr = "";			
			for(int j = 0; j < str.length(); j++){
				if((str.length() - j - 1) % 3 == 0)
					newStr += str.charAt(j) + ",";
				else
					newStr += str.charAt(j);
			}
			return newStr.substring(0, newStr.length() - 1);
		}
	}
}
