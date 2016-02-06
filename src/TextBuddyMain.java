/**
 * TextBuddy is used to manipulate text in a file. 
 * A parameter(file name e.g. mytextfile.txt) have to be provided when running the program.
 * Available commands include add, delete, display, clear and exit. 
 *
 * Example command format:
 * c:> java TextBuddy mytextfile.txt
 * Welcome to TextBuddy. mytextfile.txt is ready for use
 *
 * command: add little brown fox
 * added to mytextfile.txt: "little brown fox"
 * command: display
 * 1. little brown fox
 * command: add jumped over the moon
 * added to mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * 2. jumped over the moon
 * command: delete 2
 * deleted from mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * command: clear
 * all content deleted from mytextfile.txt
 * command: display
 * mytextfile.txt is empty
 * command: exit
 * c:>
 *
 * @author Bevin Seetoh Jia Jin
 */

public class TextBuddyMain {
	
	public static void main(String[] args) {
		TextBuddy textBuddy = new TextBuddy();
		textBuddy.run(args);
	}

}