package Chess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PieceTest {
	
	private Piece p1;
	private Piece p2;
	
	@BeforeEach
	public void setup() {
		p1 = new Piece();
		p2 = new Piece();
	}
	
	@Test
	@DisplayName("Sjekker konstruktør")
	public void testEmptyConstructor() {
		assertEquals(' ', p1.type);
		assertEquals(' ', p2.type);
		assertFalse(p1.isWhite);
		assertFalse(p2.isWhite);
		assertFalse(p1.isKilled);
		assertFalse(p2.isKilled);
		assertEquals("Black", p1.getPieceColor());
	}
	
	@Test
	@DisplayName("Sjekker at tom konstruktør har 0-koordinater") 
	public void testZeroCoordinate() {
		assertEquals(0, p1.getX());
		assertEquals(0, p1.getY());
	
		assertEquals(0, p2.getX());
		assertEquals(0, p2.getY());
	}
	
	@Test
	@DisplayName("Sjekker settere for brikketype")
	public void testSetType() {
		p1.setBishop();
		assertEquals('B', p1.type);
		assertTrue(p1.isBishop());
		p1.setQueen();
		assertEquals('Q', p1.type);
		assertTrue(p1.isQueen());
		p1.setKing();
		assertEquals('K', p1.type);
		assertTrue(p1.isKing());
		p1.setKnight();
		assertEquals('N', p1.type);
		assertTrue(p1.isKnight());
		p1.setPawn();
		assertEquals('P', p1.type);
		assertTrue(p1.isPawn());
		p1.setRook();
		assertEquals('R', p1.type);
		assertTrue(p1.isRook());
		p1.setEmpty();
		assertTrue(p1.isEmpty());
	}
	
	
	@Test
	@DisplayName("Sjekker settere for koordinater") 
	public void testSetXY() {
		p1.setX(2);
		p1.setY(3);
		assertThrows(
				IllegalArgumentException.class, () -> p2.setX(-1),
				"IllegalState skal kastes om x er negativ!"
				);
		assertThrows(
				IllegalArgumentException.class, () -> p2.setY(-3),
				"IllegalState skal kastes om y er negativ!"
				);
		assertThrows(
				IllegalArgumentException.class, () -> p2.setX(9),
				"IllegalState skal kastes om x utenfor brettet!"
				);
		assertThrows(
				IllegalArgumentException.class, () -> p2.setY(10),
				"IllegalState skal kastes om y er utenfor brettet!"
				);
		}
	
	@Test
	@DisplayName("Sjekker metoder for brikkefarge")
	public void testGetPieceColor() {
		assertEquals("Black", p1.getPieceColor());
		assertEquals("Black", p2.getPieceColor());
		p1.setWhite();
		p2.setWhite();
		assertEquals("White", p1.getPieceColor());
		assertEquals("White", p2.getPieceColor());
	}
	
	@Test
	@DisplayName("Sjekker at endringer av type og farge ikke påvirker hverandre")
	public void testSetPieceColorType() {
		p1.setWhite();
		assertEquals(0, p1.getX());
		assertEquals(0, p1.getY());
		p2.setX(4);
		p2.setY(5);
		assertFalse(p2.isWhite);
		p2.setWhite();
		assertEquals(4, p2.getX());
		assertEquals(5, p2.getY());
		p1.setBishop();
		assertEquals('B', p1.type);
		assertTrue(p1.isBishop());
		assertEquals(0, p1.getX());
		assertEquals(0, p1.getY());
		assertTrue(p1.isWhite);
		p2.setQueen();
		assertEquals('Q', p2.type);
		assertTrue(p2.isQueen());
		assertEquals(4, p2.getX());
		assertEquals(5, p2.getY());
		assertTrue(p2.isWhite);
		
	}	
	
	@Test
	@DisplayName("Sjekker at man ikke kan sette ugyldige koordinater")
	public void testSetCoordinates() {
		assertThrows(
				IllegalArgumentException.class, () -> new Piece(false, -1,0));
		assertThrows(
				IllegalArgumentException.class, () -> new Piece(false, 8,-2));
		assertThrows(
				IllegalArgumentException.class, () -> new Piece(false, 10,-2));
		assertThrows(
				IllegalArgumentException.class, () -> new Piece(false, 0,10));
	}
	
	
}
