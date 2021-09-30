package Chess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TileTest {

	private Tile t1;
	private Tile t2;

	
	@BeforeEach
	public void setup() {
		t1 = new Tile(0, 0); 
		t2 = new Tile(7,6);
		
	}
	
	@Test
	@DisplayName("Sjekker at konstruktøren setter riktige farger")
	public void testSetValidColorTile() {
		t1.setColorTile();
		assertTrue(t1.isWhite); 
		t2.setColorTile();
		assertFalse(t2.isWhite);
		
		assertEquals("White", t1.getColor());
		assertEquals("Black", t2.getColor());
	}
	
	@Test
	@DisplayName("Sjekker at konstruktøren ikke oppretter brikker") 
	public void testConstructorPiece() {
		assertFalse(t1.isOccupied());
		assertFalse(t2.isOccupied());
	}
	
	@Test
	@DisplayName("Sjekker at setEmpty() virker")
	public void testSetEmpty() {
		t1.setEmpty();
		t2.setEmpty();
		assertEquals(' ', t1.type);
		assertEquals(' ', t2.type);
	}
	
	@Test 
	@DisplayName("Sjekker om koordinatene er riktige")
	public void testCoordinates() {
		assertEquals(0, t1.getX());
		assertEquals(0, t1.getY());
		assertEquals(7, t2.getX());
		assertEquals(6, t2.getY());
	}
	
	@Test
	@DisplayName("Sjekker om piece blir satt empty ved moveFromSpot()") 
	public void testMoveFromSpot() {
		t1.moveFromSpot();
		t2.moveFromSpot();
		assertTrue(t1.piece.isEmpty());
		assertTrue(t2.piece.isEmpty());
		assertFalse(t1.isOccupied());
		assertFalse(t2.isOccupied());
	}
	
	@Test
	@DisplayName("Sjekker konstruktøren ved ugyldige koordinater")
	public void testNegativeCoordinates() {
		assertThrows(
				IllegalArgumentException.class, () -> new Tile(-1,0));
		assertThrows(
				IllegalArgumentException.class, () -> new Tile(8,-2));
		assertThrows(
				IllegalArgumentException.class, () -> new Tile(10,-2));
		assertThrows(
				IllegalArgumentException.class, () -> new Tile(0,10));
	}
	
	
}
