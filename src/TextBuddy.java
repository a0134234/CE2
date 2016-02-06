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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextBuddy {
	
	private static final String MESSAGE_NO_FILE_NAME = "File name not specified.";
	private static final String MESSAGE_INVALID_INPUT = "Invalid command.";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use.";
	private static final String MESSAGE_PROMPT = "command: ";
	private static final String MESSAGE_ADD = "added to %s: \"%s\"";
	private static final String MESSAGE_EMPTY_FILE = "%s is empty.";
	private static final String MESSAGE_DELETE_ERROR = "Invalid line selected.";
	private static final String MESSAGE_DELETE = "deleted from %s: \"%s\"";
	private static final String MESSAGE_CLEAR = "all content deleted from %s.";

	static String fileName;
	static ArrayList<String> contents;
	static Scanner scanner;

	public static void main(String[] args) {
		checkArgumentInput(args);
		openOrCreateFile(fileName);
		printWelcomeMessage();
		loopPrompt();
	}

	private static void loopPrompt() {
		while (true) {
			promptCommand();
			executeCommand();
		}
	}

	private static void executeCommand() {
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

	private static String readCommand() {
		String input = scanner.next();
		String command = input.toLowerCase();
		return command;
	}

	private static void inputError() {
		printMessage(MESSAGE_INVALID_INPUT);
		loopPrompt();
	}

	private static void saveAndExit() {
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

	private static void add() {
		String line = scanner.nextLine().trim();
		contents.add(line);
		printMessage(String.format(MESSAGE_ADD, fileName, line));
	}

	private static void print() {
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

	private static void delete() {
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

	private static String deleteLine(int lineNumber) {
		String line = contents.get(lineNumber);
		contents.remove(lineNumber);
		return line;
	}

	private static boolean indexOutOfBound(int lineNumber) {
		if (lineNumber >= contents.size()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isNegativeInput(int lineNumber) {
		if (lineNumber < 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isEmpty() {
		if (contents.size() == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	private static int getLineNumber() {
		int input = scanner.nextInt();
		int lineNumber = input - 1;
		return lineNumber;
	}

	private static void clear() {
		reinitializeArray();
		printMessage(String.format(MESSAGE_CLEAR, fileName));
	}

	private static void reinitializeArray() {
		contents = new ArrayList<String>();
	}

	private static void promptCommand() {
		if (scanner == null) {
			scanner = new Scanner(System.in);
		}
		System.out.print(MESSAGE_PROMPT);
	}

	private static void openOrCreateFile(String fileName) {
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

	private static void checkArgumentInput(String[] args) {
		if (hasNoArgument(args)) {
			printMessage(MESSAGE_NO_FILE_NAME);
			endProgram();
		} else {
			fileName = args[0];
		}
	}
	
	private static boolean hasNoArgument(String[] args) {
		if (args.length == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private static void endProgram() {
		System.exit(0);
	}

	private static void printWelcomeMessage() {
		printMessage(String.format(MESSAGE_WELCOME, fileName));
	}
	
	private static void printMessage(String message) {
		System.out.println(message);
	}
}
