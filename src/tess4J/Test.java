package tess4J;

import java.awt.Rectangle;
import java.awt.Robot;
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
	
	public static void getTess(File imageFile) throws IOException{	 // get the ASCII format of the image
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
	
	public static void getResultNumber(String c) throws IOException{ // web crawl
	    String key="AIzaSyBoSXsswVlG9iwZ2hpMQticVv0MUCGBEdM";
	    String qry = question;//question + "-" + c;
	    URL url = new URL(
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&q="+ qry + "&alt=json");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	    /*String output;
	    String numOutput = "";
	    while ((output = br.readLine()) != null) {
	    	//System.out.println(output);
	        if(output.contains("\"totalResults\": \"")){                
	            numOutput = output.substring(output.indexOf("\"totalResults\": \"")+("\"totalResults\": \"").length(), output.indexOf("\","));
	        }
	    }
	    if (counter == 2)
			num1 = Integer.parseInt(numOutput);
		else if (counter == 3)
			num2 = Integer.parseInt(numOutput);
		else
			num3 = Integer.parseInt(numOutput);*/
	    
	    String output;
	    int i = 0;
	    while((output = br.readLine()) != null) {
	    	System.out.println(output);
	    	if (output.toLowerCase().contains(c.toLowerCase())) {
	    		i++;
	    	}
	    }
		if (counter == 2)
			num1 = i;
		else if (counter == 3)
			num2 = i;
		else
			num3 = i;
	    conn.disconnect();  
	}
	
	public static void main(String[] args) throws AWTException, IOException{
		takePhoto(200, 220, 550, 140); //CS: 30, 270, 490, 170 //HQ: 30, 270, 490, 160
        getTess(questionImage);
        
        takePhoto(220, 370, 400, 80); //CS: 65, 475, 400, 75 //HQ: 65, 460, 400, 70
        getTess(choice1Image);
        getResultNumber(choice1);
        
        takePhoto(220, 490, 400, 80); //CS: 65, 575, 400, 75 //HQ: 65, 560, 400, 70
        getTess(choice2Image);
        getResultNumber(choice2);
        
        takePhoto(220, 570, 400, 80); //CS: 65, 680, 400,75 //HQ: 65, 660, 400, 70
        getTess(choice3Image);
        getResultNumber(choice3);
        
        System.out.println("-------------------");
        System.out.println(choice1 + ": " + num1 + "\n" + choice2 + ": " + num2 + "\n" + choice3 + ": " +  num3); // prints the number of search results for each answer choice.
        System.out.println("-------------------");
        
        if(num1 > num2 && num1 > num3) System.out.println(1); // prints the highest search result.
        else if (num2 > num3 && num2 > num1) System.out.println(2);
        else System.out.println(3);
	}
	
}
