package Chess;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveHandlerTest {

	private Game game;
	private SaveHandler saveHandler = new SaveHandler();
	
	private void createBoard() {
		game = new Game();
	}
	
	@BeforeEach 
	public void setup() {
		createBoard();
	}
	
	@Test
	public void testSave() {
		try {											
			saveHandler.save("test-save-new", game);		//Oppretter ny fil
			
		} catch (IOException e) {
			fail("Could not save file");
		}
		
		byte[] testFile = null, newFile = null;		
		
		try {
			testFile = Files.readAllBytes(Path.of(SaveHandler.getFilePath("test-save-new")));
			
		} catch (IOException e) {
			fail("Could not load test file");
		}
		
		try {
			newFile = Files.readAllBytes(Path.of(SaveHandler.getFilePath("test-save-new")));
			
		} catch (IOException e) {
			fail("Could not load test file");
		}
		
		assertNotNull(testFile);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(testFile, newFile));
	} 
	
	@Test 
	@DisplayName("Sjekker loading av fil")
	public void testLoad() {
		Game savedNewGame; // Required to ignore Eclipse warning
		
		try {											
			saveHandler.save("test-save-new", game);		//Oppretter ny fil
			
		} catch (IOException e) {
			fail("Could not save file");
		}
		
		try {
			savedNewGame = saveHandler.load("test-save-new");
	
		} catch (IOException e) {
			fail("Could not load saved file");
			return;
		}
		
		assertEquals(game.toString(), savedNewGame.toString());
		assertFalse(game.isGameWon());
		assertEquals(game.whitePlayer, savedNewGame.whitePlayer);
	}
	
	@Test
	public void testLoadNonExistingFile() {
		assertThrows(
				FileNotFoundException.class,
				() -> game = saveHandler.load("foo"),
				"FileNotFoundException should be thrown when file does not exist!"
		);
		
	}
	
	@Test
	public void testLoadInvalidFile() {
		assertThrows(
			Exception.class,
			() -> game = saveHandler.load("invalid-save"),
			"An exception should be thrown if loaded file is invalid!"
		);
	}
	
	
	
	@AfterAll						//Sletter test-savefilen for opprydding. 
	static void teardown() {
		File newTestSaveFile = new File(SaveHandler.getFilePath("test-save-new"));
		newTestSaveFile.delete();
	}
}
