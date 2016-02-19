import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class JUnitTest {
	
	public final String MESSAGE_INVALID_INPUT = "Invalid command.";
	public final String MESSAGE_DELETE_ERROR = "Invalid line selected.";
	
	TextBuddy textBuddy = new TextBuddy();
	
	@Before
	public void testSetup() {
		String[] args = new String[] {"testFile.txt"};
		textBuddy.checkArgumentInput(args);
		textBuddy.openOrCreateFile(textBuddy.fileName);
		textBuddy.addLine("test line 0");
		textBuddy.addLine("test line 1");
	}

	@Test
	public void fileTest() {
		// check if file is created after method call
		textBuddy.openOrCreateFile(textBuddy.fileName);
		assertTrue(textBuddy.getFile(textBuddy.fileName).exists());
	}

	@Test
	public void addTest() {
		// check if "add" method returns the right status message
		assertEquals(textBuddy.addLine("test line"), "added to " + textBuddy.fileName
				+ ": \"test line\"");
	}

	@Test
	public void deleteTest() {
		// check if "delete" method returns the right status message
		assertEquals(textBuddy.deleteLine(-1), MESSAGE_INVALID_INPUT);
		assertEquals(textBuddy.deleteLine(Integer.MAX_VALUE), MESSAGE_DELETE_ERROR);
		assertEquals(textBuddy.deleteLine(0), "deleted from " + textBuddy.fileName + ": "
				+ "\"test line 0\"");
	}

	@Test
	public void clearTest() {
		// check if the "clear" method returns the right status message
		assertEquals(textBuddy.clearContents(), "all content deleted from " + textBuddy.fileName
				+ ".");
		assertEquals(0, textBuddy.contents.size());
	}

	@Test
	public void searchTest() {
		// Sample array list to compare against search results
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("test line 0");
		testList.add("test line 1");

		// check if the "search" method returns the right result
		assertEquals(textBuddy.getSearchResults("line 0").get(0), "test line 0");
		assertEquals(textBuddy.getSearchResults("line"), testList);
	}
}
