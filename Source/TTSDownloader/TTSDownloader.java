import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This Java class works for https://ttsmp3.com/ 
 * 1. Begin to select the language
 * 2. Select the desired voice you want to have 
 * 3. Clean up your Download folder
 * 4. Start this Java program and quickly pick bring the https://ttsmp3.com/ page up 
 * 5. Let it read the CSV file and download all the .mp3 files
 * 
 * NOTICE that ttsmp3 is word limited per each day! So don't download to much!
 * @author dell
 *
 */
public class TTSDownloader {

	// Coordinates for the "Downlaod MP3" button
	private final static int DOWNLOAD_MP3_BUTTON_X = 1153;
	private final static int DOWNLOAD_MP3_BUTTON_Y = 660;

	// Coordinates for the "OK" or "Download" button for the web browser pop-up
	// window when downloading a file
	private static boolean myWebBrowserAskingMeIfIwantToDownloadFile = true; // Enter false if your web browser just download directly without asking
	private final static int ACCEPT_WEBROWSER_DOWNLOAD_X = 1153;
	private final static int ACCEPT_WEBROWSER_DOWNLOAD_Y = 725;

	// Coordinates centrum of the text area where you want to write in text
	private final static int CENTER_OF_TEXT_AREA_X = 937;
	private final static int CENTER_OF_TEXT_AREA_Y = 514;

	// Path to the CSV file
	private final static String pathCSVFile = "/home/dell/French_Swedish_Step_1.csv";
	
	// Path to the folder where all the .mp3 are
	private final static String pathDownloadFolder = "/home/dell/Hämtningar";

	public static void main(String[] args) {
		boolean findCoordinates = false; // Write true at the first so you can collect all the coordinates
		if (findCoordinates) {
			findCoordinates();
		} else {
			runCollector();
		}
	}

	// Just collect
	private static void findCoordinates() {
		while (true) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			System.out.println("PositionX: " + p.x + " PositionY: " + p.y);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void runCollector() {
		try {
			// Robot object for moving mouse and writing with the key board
			Robot robot = new Robot();

			// Read the first column of the CSV file
			BufferedReader br = new BufferedReader(new FileReader(pathCSVFile));
			String line = "";
			while ((line = br.readLine()) != null) {
				String sentence = line.split(",")[0];
				moveToTextArea(robot); // Begin to click on center of the text area
				ctrlAltBackspace(robot); // Then enter CTRL+ALT and then backspace so we can delete the text
				pasteText(robot, sentence); // Write the first column, which is a sentence
				moveToMP3Button(robot); // Move to the download MP3 button
				if(myWebBrowserAskingMeIfIwantToDownloadFile) {
					moveToOKDownloadButton(robot); // Move to OK/Download button for the pop-up dialog
				}
				// Go to your Download folder and modify the file name that have "ttsMP3.com" included
				renameSingleFile(robot, pathDownloadFolder, "ttsMP3.com", sentence);
				
			}
			br.close();

		} catch (AWTException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void renameSingleFile(Robot robot, String pathdownloadfolder, String contains, String sentence) {
		robot.delay(3000);
		File[] renameThisFile = new File(pathdownloadfolder).listFiles((d, name) -> name.contains(contains));
		renameThisFile[0].renameTo(new File(pathdownloadfolder + "/" + sentence + ".mp3")); // e.g /home/computer/download/MyMP3.mp3
	}

	private static void moveToOKDownloadButton(Robot robot) {
		robot.mouseMove(ACCEPT_WEBROWSER_DOWNLOAD_X, ACCEPT_WEBROWSER_DOWNLOAD_Y);
		robot.delay(5000); // Sometimes it takes long time to get the pop-up
		leftClick(robot);
	}

	private static void moveToMP3Button(Robot robot) {
		robot.mouseMove(DOWNLOAD_MP3_BUTTON_X, DOWNLOAD_MP3_BUTTON_Y);
		robot.delay(2000);
		leftClick(robot);
	}

	private static void ctrlAltBackspace(Robot robot) {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(200);
		robot.keyPress(KeyEvent.VK_A);
		robot.delay(200);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_A);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_BACK_SPACE);
		robot.delay(500);
		robot.keyRelease(KeyEvent.VK_BACK_SPACE);
	}

	private static void moveToTextArea(Robot robot) {
		robot.mouseMove(CENTER_OF_TEXT_AREA_X, CENTER_OF_TEXT_AREA_Y);
		robot.delay(2000);
		leftClick(robot);
	}

	private static void leftClick(Robot robot) {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(200);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(200);
	}
	
	private static void pasteText(Robot robot, String sentence) {
		StringSelection selection = new StringSelection(sentence);
		// Copy it to clipboard
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, null);
		// Paste it
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
	}
}