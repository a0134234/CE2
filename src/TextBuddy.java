import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddy {
	
	public final String MESSAGE_NO_FILE_NAME = "File name not specified.";
	public final String MESSAGE_INVALID_INPUT = "Invalid command.";
	public final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use.";
	public final String MESSAGE_PROMPT = "command: ";
	public final String MESSAGE_ADD = "added to %s: \"%s\"";
	public final String MESSAGE_EMPTY_FILE = "%s is empty.";
	public final String MESSAGE_DELETE_ERROR = "Invalid line selected.";
	public final String MESSAGE_DELETE = "deleted from %s: \"%s\"";
	public final String MESSAGE_CLEAR = "all content deleted from %s.";
	
	String fileName = null;
	ArrayList<String> contents = new ArrayList<String>();
	Scanner scanner = new Scanner(System.in);
	
	public TextBuddy() {
	}

	//Initialize the program's state when launched
	public void run(String[] args) {
		checkArgumentInput(args);
		openOrCreateFile(fileName);
		printWelcomeMessage();
		loopPrompt();
	}
	
	//Keeps running until the program is terminated by the user
	public void loopPrompt() {
		while (true) {
			promptCommand();
			executeCommand();
		}
	}
	
	//Retrieves user input and executes command
	public void executeCommand() {
		String command = readCommand();

		switch (command) {
			case "add":
				add();
				break;
	
			case "display":
				display();
				break;
	
			case "delete":
				delete();
				break;
	
			case "clear":
				clear();
				break;
			
			case "sort":
				sort();
				break;
				
			case "search":
				search();
				break;
			
			case "exit":
				saveAndExit();
				break;
	
			default:
				inputError();
				break;
		}
	}
	
	//Reads and add input into contents
	public void add() {
		String line = scanner.nextLine().trim();
		String message = addLine(line);
		printMessage(message);
	}
	
	
	public String addLine(String line) {
		contents.add(line);
		String message = String.format(MESSAGE_ADD, fileName, line);
		return message;
	}
	
	public void display() {
		if (isEmptyContents()) {
			String message = String.format(MESSAGE_EMPTY_FILE, fileName);
			printMessage(message);
		} else {
			displayContents(contents);
		}
	}
	
	public void delete() {
		int lineNumber = getLineNumber();
		String message = deleteLine(lineNumber);
		printMessage(message);
	}
	
	//Deletes the line number based on input
	public String deleteLine(int lineNumber) {		
		if (isEmptyContents()) {
			String message = String.format(MESSAGE_EMPTY_FILE, fileName);
			return message;
		} else if (isNegativeInput(lineNumber)) {
			return MESSAGE_INVALID_INPUT;
		} else if (isOutOfBoundIndex(lineNumber)) {
			return MESSAGE_DELETE_ERROR;
		} else {
			String line = getDeletedLine(lineNumber);
			String message = String.format(MESSAGE_DELETE, fileName, line);
			return message;
		}
	}
	
	//Clears the contents of TextBuddy
	public void clear() {
		String message = clearContents();
		printMessage(message);
	}

	public String clearContents() {
		initializeArray();
		String message = String.format(MESSAGE_CLEAR, fileName);
		return message;
	}
	
	public void sort() {
		Collections.sort(contents, String.CASE_INSENSITIVE_ORDER);
	}
	
	public void search() {
		String searchTerm = scanner.next();
		ArrayList<String> results = getSearchResults(searchTerm);
		displayContents(results);
	}
	
	//Open file if it exists, otherwise create a new file
	public void openOrCreateFile(String fileName) {
		File file = getFile(fileName);
		initializeArray();

		try {
			if (file.exists()) {
				readFile(file);
			} else {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Reads from given file name into the contents of TextBuddy
	public void readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			contents.add(line);
		}
		br.close();
	}
	
	//Writes the contents of TextBuddy into the given file name
	public void writeFile(File file) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (String current : contents) {
			bw.write(current);
			bw.newLine();
		}
		bw.close();
	}
	
	public String readCommand() {
		String input = scanner.next();
		String command = input.toLowerCase();
		return command;
	}

	public void inputError() {
		printMessage(MESSAGE_INVALID_INPUT);
		loopPrompt();
	}
	
	public void saveAndExit() {
		saveProgram();
		endProgram();
	}
	
	public void saveProgram() {
		File file = getFile(fileName);

		try {
			writeFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Searches the contents based on the search term and returns the result
	public ArrayList<String> getSearchResults(String searchTerm) {
		ArrayList<String> results = new ArrayList<String>();
		
		for (String current : contents) {
			if (current.contains(searchTerm)) {
				results.add(current);
			}
		}
		return results;
	}
	
	//Formats and display the parameter ArrayList
	public void displayContents(ArrayList<String> lines) {
		int num = 1;
		for (String current : lines) {
			printMessage(num + ". " + current);
			num++;
		}
	}
	
	public String getDeletedLine(int lineNumber) {
		String line = contents.get(lineNumber);
		contents.remove(lineNumber);
		return line;
	}

	public boolean isOutOfBoundIndex(int lineNumber) {
		if (lineNumber >= contents.size()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNegativeInput(int lineNumber) {
		if (lineNumber < 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmptyContents() {
		if (contents.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//reduces given input by 1 to match array index
	public int getLineNumber() {
		int input = 0;
		input = scanner.nextInt();
		int lineNumber = input - 1;
		return lineNumber;
	}
	
	public void initializeArray() {
		contents = new ArrayList<String>();
	}

	public void promptCommand() {
		System.out.print(MESSAGE_PROMPT);
	}
	
	public File getFile(String fileName) {
		String dir = System.getProperty("user.dir");
		File file = new File(dir + "\\" + fileName);
		return file;
	}
	
	//Checks if file name is supplied, otherwise terminate the program
	public void checkArgumentInput(String[] args) {
		if (hasNoArgument(args)) {
			printMessage(MESSAGE_NO_FILE_NAME);
			endProgram();
		} else {
			fileName = args[0];
		}
	}
	
	public boolean hasNoArgument(String[] args) {
		if (args.length == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void endProgram() {
		System.exit(0);
	}
	
	public void printWelcomeMessage() {
		String message = String.format(MESSAGE_WELCOME, fileName);
		printMessage(message);
	}

	public void printMessage(String message) {
		System.out.println(message);
	}
}
