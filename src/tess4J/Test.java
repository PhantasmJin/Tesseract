package tess4J;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.*;


public class Test { // NOTE: lib folder contains the jar files used in this project.

	static String question = "";
	static String choice = "";
	static File questionImage = new File("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\Question.png"); // Change the file location to anywhere you want
	static File choice1Image = new File("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\Choice1.png"); // change the file location here as well
	static int num = 0;
	static int counter = 0;
	
	public static void takePhoto(int x, int y, int width, int height) throws AWTException, IOException{ // This method takes screen shot
        	Rectangle screenRectangle = new Rectangle(x, y, width, height); // size of the picture
        	BufferedImage image = new Robot().createScreenCapture(screenRectangle);
		if (counter == 0){
        		ImageIO.write(image, "png", new FileOutputStream("Question.png")); // NOTE: picture is saved in the project main directory
		} else if (counter == 1){
			ImageIO.write(image, "png", new FileOutputStream("Choice1.png")); // This screen shots image on the pc screen at x, y location
		}
	}
	
	public static void getTess(File imageFile) throws IOException{ // this method converts screen shot int a string
		ITesseract instance = new Tesseract();
		instance.setDatapath("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\tessdata"); // this should be the location of your tessdata
		
		try {
			String result = instance.doOCR(imageFile);
			result = result.replace("\n", "-").replace(" ", "-").substring(0, result.length()-2); // take out new lines from the string and replace space with dash
			if(counter == 0)
				question = result;
			else if (counter == 1)
				choice = result;
			counter++;
			System.out.println(result); // prints what characters the string contains
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	public static void getResultNumber(String c) throws IOException{ // This class finds the number of results on google
	    String key="AIzaSyBoSXsswVlG9iwZ2hpMQticVv0MUCGBEdM";
	    String qry = question + "-" + c;
	    String chs = choice + "-" + c;
	    URL url = new URL(
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&q="+ qry + chs + "&alt=json");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	    String output;
	    String numOutput = "";
	    //System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {

	        if(output.contains("\"totalResults\": \"")){                
	            numOutput = output.substring(output.indexOf("\"totalResults\": \"")+("\"totalResults\": \"").length(), output.indexOf("\","));
	        }     
	    }
	    //System.out.println(numOutput);
	    	num = Integer.parseInt(numOutput); // numOutput contains the number of results from the search
	    conn.disconnect();  
	}
	
	public static void main(String[] args) throws AWTException, IOException{
		takePhoto(30, 270, 490, 160);
        	getTess(questionImage);
        
        	takePhoto(65, 490, 400, 70);
       		getTess(choice1Image);
        	getResultNumber(choice1);
        
        System.out.println(num);

	}
	
}

/* working code
package tess4J;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.*;


public class Test { // NOTE: lib folder contains the jar files used in this project.

	static String question = "";
	static String choice1 = "";
	static String choice2 = "";
	static String choice3 = "";
	static File questionImage = new File("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\Question.png");
	static File choice1Image = new File("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\Choice1.png");
	static File choice2Image = new File("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\Choice2.png");
	static File choice3Image = new File("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\Choice3.png");
	static int num1 = 0;
	static int num2 = 0;
	static int num3 = 0;
	static int counter = 0;
	
	public static void takePhoto(int x, int y, int width, int height) throws AWTException, IOException{
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(x, y, width, height); // size of the picture
        BufferedImage image = new Robot().createScreenCapture(screenRectangle);
        if(counter == 0)
        	ImageIO.write(image, "png", new FileOutputStream("Question.png")); // NOTE: picture is saved in the project main directory
        else if(counter == 1)
        	ImageIO.write(image, "png", new FileOutputStream("Choice1.png"));
        else if(counter == 2)
        	ImageIO.write(image, "png", new FileOutputStream("Choice2.png"));
        else
        	ImageIO.write(image, "png", new FileOutputStream("Choice3.png"));
	}
	
	public static void getTess(File imageFile) throws IOException{	
		ITesseract instance = new Tesseract();
		instance.setDatapath("D:\\My work\\MyEclipseProjects\\Tess4JOCR\\tessdata");
		
		try {
			String result = instance.doOCR(imageFile);
			result = result.replace("\n", "-").replace(" ", "-").substring(0, result.length()-2); // take out new lines from the string
			if (counter == 0)
				question = result;
			else if (counter == 1)
				choice1 = result;
			else if(counter == 2)
				choice2 = result;
			else
				choice3 = result;
			counter++;
			System.out.println(result);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	public static void getResultNumber(String c) throws IOException{
	    String key="AIzaSyBoSXsswVlG9iwZ2hpMQticVv0MUCGBEdM";
	    String qry = question + "-" + c;
	    URL url = new URL(
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&q="+ qry + "&alt=json");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	    String output;
	    String numOutput = "";
	    //System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {

	        if(output.contains("\"totalResults\": \"")){                
	            numOutput = output.substring(output.indexOf("\"totalResults\": \"")+("\"totalResults\": \"").length(), output.indexOf("\","));
	        }     
	    }
	    //System.out.println(numOutput);
	    if (counter == 2)
			num1 = Integer.parseInt(numOutput);
		else if (counter == 3)
			num2 = Integer.parseInt(numOutput);
		else
			num3 = Integer.parseInt(numOutput);
	    conn.disconnect();  
	}
	
	public static void main(String[] args) throws AWTException, IOException{
		takePhoto(30, 270, 490, 160); //CS: 30, 270, 490, 170 //HQ: 30, 270, 490, 160
        getTess(questionImage);
        
        takePhoto(65, 490, 400, 70); //CS: 65, 475, 400, 75 //HQ: 65, 460, 400, 70
        getTess(choice1Image);
        getResultNumber(choice1);
        
        takePhoto(65, 580, 400, 70); //CS: 65, 575, 400, 75 //HQ: 65, 560, 400, 70
        getTess(choice2Image);
        getResultNumber(choice2);
        
        takePhoto(65, 660, 400, 70); //CS: 65, 680, 400,75 //HQ: 65, 660, 400, 70
        getTess(choice3Image);
        getResultNumber(choice3);
        
        System.out.println(num1 + "\n" + num2 + "\n" + num3);
        System.out.println("-------------------");
        
        if(num1 > num2 && num1 > num3) System.out.println(1);
        else if (num2 > num3 && num2 > num1) System.out.println(2);
        else System.out.println(3);
	}
	
}
*/
