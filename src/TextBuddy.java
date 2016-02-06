import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;


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
	
	String fileName;
	ArrayList<String> contents;
	Scanner scanner;
	
	public TextBuddy() {
	}
	
	public void run(String[] args) {
		checkArgumentInput(args);
		openOrCreateFile(fileName);
		printWelcomeMessage();
		loopPrompt();
	}
	
	public void loopPrompt() {
		while (true) {
			promptCommand();
			executeCommand();
		}
	}

	public void executeCommand() {
		String command = readCommand();

		switch (command) {
			case "add":
				add();
				break;
	
			case "display":
				print();
				break;
	
			case "delete":
				delete();
				break;
	
			case "clear":
				clear();
				break;
	
			case "exit":
				saveAndExit();
				break;
	
			default:
				inputError();
				break;
		}
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
		String dir = System.getProperty("user.dir");
		File file = new File(dir + "\\" + fileName);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < contents.size(); i++) {
				bw.write(contents.get(i));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		endProgram();
	}

	public void add() {
		String line = scanner.nextLine().trim();
		contents.add(line);
		printMessage(String.format(MESSAGE_ADD, fileName, line));
	}

	public void print() {
		int num = 1;
		if (isEmpty()) {
			printMessage(String.format(MESSAGE_EMPTY_FILE, fileName));
		} else {
			for (int i = 0; i < contents.size(); i++) {
				printMessage(num + ". " + contents.get(i));
				num++;
			}
		}
	}

	public void delete() {
		int lineNumber = getLineNumber();
		
		if (isEmpty()) {
			printMessage(String.format(MESSAGE_EMPTY_FILE, fileName));
		} else if (isNegativeInput(lineNumber)) {
			printMessage(MESSAGE_INVALID_INPUT);
		} else if (indexOutOfBound(lineNumber)) {
			printMessage(MESSAGE_DELETE_ERROR);
		} else {
			String line = deleteLine(lineNumber);
			printMessage(String.format(MESSAGE_DELETE, fileName, line));
		}
	}

	public String deleteLine(int lineNumber) {
		String line = contents.get(lineNumber);
		contents.remove(lineNumber);
		return line;
	}

	public boolean indexOutOfBound(int lineNumber) {
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

	public boolean isEmpty() {
		if (contents.size() == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public int getLineNumber() {
		int input = scanner.nextInt();
		int lineNumber = input - 1;
		return lineNumber;
	}
	
	@Test
	public void clear() {
		reinitializeArray();
		String message = String.format(MESSAGE_CLEAR, fileName);
		printMessage(message);
		assertEquals(message, "all content deleted from " + fileName + ".");
		assertEquals(0, contents.size());
	}

	public void reinitializeArray() {
		contents = new ArrayList<String>();
	}

	public void promptCommand() {
		if (scanner == null) {
			scanner = new Scanner(System.in);
		}
		System.out.print(MESSAGE_PROMPT);
	}

	public void openOrCreateFile(String fileName) {
		String dir = System.getProperty("user.dir");
		File file = new File(dir + "\\" + fileName);
		reinitializeArray();

		try {
			if (!file.exists()) {
				file.createNewFile();
			} else {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null) {
					contents.add(line);
				}
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
	
	@Test
	public void printWelcomeMessage() {
		String message = String.format(MESSAGE_WELCOME, fileName);
		printMessage(message);
		assertEquals(message, message);
	}
	
	public void printMessage(String message) {
		System.out.println(message);
	}
}
