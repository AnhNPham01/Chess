package Chess;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class GameTest {
	//2 trekk for hver brikketype uavh. farge.
	
	private Game game;
	private Game game1;


	@BeforeEach
	public void setup() {
		game = new Game();
		
		//wiper ut brikkene
		for(int y = 0; y < 8; y++) { 		
			for (int x = 0; x < 8; x++) {
				game.getTilePiece(x, y).setEmpty();
				game.getTile(x, y).moveFromSpot();
			}
		}

		//lager nytt Game-objekt uten at brikker er wiped out
		game1 = new Game();
	}
	
	@Test
	@DisplayName("Sjekker at konstruktøren implementerer riktige brikker")
	public void testConstructor() {
		
		assertEquals('R', game1.getTilePiece(0, 7).type);
		assertEquals('R', game1.getTilePiece(7, 7).type);
		assertEquals('R', game1.getTilePiece(0, 0).type);
		assertEquals('R', game1.getTilePiece(7, 7).type);
		
		assertEquals('N', game1.getTilePiece(0, 1).type);
		assertEquals('N', game1.getTilePiece(7, 1).type);
		assertEquals('N', game1.getTilePiece(0, 6).type);
		assertEquals('N', game1.getTilePiece(7, 6).type);
		
		assertEquals('Q', game1.getTilePiece(7, 3).type);
		assertEquals('Q', game1.getTilePiece(0, 3).type);
		
		assertEquals('K', game1.getTilePiece(7, 4).type);
		assertEquals('K', game1.getTilePiece(0, 4).type);
		
		for (int y = 0; y < 8 ; y++) {
			assertEquals('P', game1.getTilePiece(1, y).type);
			assertEquals('P', game1.getTilePiece(6, y).type);
		}
	}
	
	@Test
	@DisplayName("Sjekker at konstruktøren implementerer riktige farger")
	public void testConstructorColor() {
		assertTrue(game1.getTilePiece(7, 4).isWhite);
		assertTrue(game1.getTilePiece(7, 4).isWhite);
		
		assertFalse(game1.getTilePiece(0, 0).isWhite);
		assertFalse(game1.getTilePiece(1, 3).isWhite);
	}
	
	@Test
	@DisplayName("Sjekker trekkene til Pawn")
	public void testPawnMove( ) {
		game.setTilePiece('P', 1, 0);
		game.setTilePiece('P', 6, 3);
		
		Tile pawnTile1 = game.getTile(1, 0); 	
		Piece pawnPiece1 = game.getTilePiece(1, 0);
		assertFalse(pawnPiece1.isWhite);
		
		Tile pawnTile2 = game.getTile(6, 3);
		Piece pawnPiece2 = game.getTilePiece(6, 3);
		pawnPiece2.setWhite();
		assertTrue(pawnPiece2.isWhite);
		
		
		//Kan bevege seg to steg første gangen
		assertTrue(game.canMoveTo(pawnTile2, pawnPiece2, -2, 0));
		game.move(6, 3, -2, 0);
		assertEquals(4, pawnPiece2.getX());
		assertEquals(3, pawnPiece2.getY());
		
		//kan bevege seg skrått om den skal capture
		game.setTilePiece('P', 2, 1);
		game.getTilePiece(2, 1).setWhite();
		assertTrue(game.canMoveTo(pawnTile1, pawnPiece1, 1, 1));
		
		//Kan ikke bevege seg bakover
		game.move(1, 0, 2, 0);
		assertEquals(3, pawnPiece1.getX());
		assertEquals(0, pawnPiece1.getY());
		game.setNewPlayerColor();
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(3, 0, -1, 0);
		});
	}
	
	@Test
	@DisplayName("Sjekker trekkene til Rook")
	public void testRookMove() {
		game.setTilePiece('R', 0, 0);
		game.setTilePiece('R', 3, 5);
		
		Tile rt1 = game.getTile(0, 0);
		Piece rp1 = game.getTilePiece(0, 0);
		rp1.setWhite();
		
		Tile rt2 = game.getTile(3, 5);
		Piece rp2 = game.getTilePiece(3, 5);
		
		//Kan bevege seg vertikalt
		assertTrue(game.canMoveTo(rt1, rp1, 7, 0));
		game.move(0, 0, 7, 0);
		assertEquals(7, rp1.getX());
		assertEquals(0, rp1.getY());
		
		
		//Kan bevege seg horisontalt
		assertTrue(game.canMoveTo(rt2, rp2, 0, -5));
		game.move(3, 5, 0, -5);
		assertEquals(3, rp2.getX());
		assertEquals(0, rp2.getY());
		
		//Kan ikke bevege seg skrått
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(7, 0, -5, 5);
		});
	}
	
	@Test
	@DisplayName("Sjekker trekkene til Knight")
	public void testKnightMove() {
		game.setTilePiece('N', 4, 5);
		game.setTilePiece('N', 2, 2);
		game.setTilePiece('P', 3, 5);
		
		Tile nt1 = game.getTile(4, 5);
		Piece np1 = game.getTilePiece(4, 5);
		
		
		Tile nt2 = game.getTile(2, 2);
		Piece np2 = game.getTilePiece(2, 2);
		np2.setWhite();
		
		
		//Beveger seg som en Knight
		assertTrue(game.canMoveTo(nt2, np2, -1, -2));
		game.move(2, 2, -1, -2);
		assertEquals(1, np2.getX());
		assertEquals(0, np2.getY());
		
		//Kan gjøre trekk selv om det står en brikke i feltet foran
		assertTrue(game.canMoveTo(nt1, np1, -2, 1));
		game.move(4, 5, -2, -1);
		assertEquals(2, np1.getX());
		assertEquals(4, np1.getY());
		
	}

	
	@Test 
	@DisplayName("Sjekker trekkene til Bishop") 
	public void testBishopMove() {
		game.setTilePiece('B', 4, 4);
		game.setTilePiece('B', 2, 7);
		
		Tile bt1 = game.getTile(4, 4);
		Piece bp1 = game.getTilePiece(4, 4);
		bp1.setWhite();
		
		Tile bt2 = game.getTile(2, 7);
		Piece bp2 = game.getTilePiece(2, 7);
		
		//Kan bevege seg skrått
		assertTrue(game.canMoveTo(bt1, bp1, -2, -2));
		game.move(4, 4, -2, -2);
		assertEquals( 2, bp1.getX() );
		assertEquals( 2, bp1.getY() );
		
		//Kan bevege seg skrått
		assertTrue(game.canMoveTo(bt2, bp2, 3, -3));
		game.move(2, 7, 3, -3);
		assertEquals(5, bp2.getX());
		assertEquals(4, bp2.getY());
		
		//Utløser unntak ved ikke-skrått trekk
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(4, 4, 2, 1);
		});

	}
	
	@Test
	@DisplayName("Sjekker trekkene til Queen")
	public void testQueenMove() {
		game.setTilePiece('Q', 4, 4);
		game.setTilePiece('Q', 2, 3);
		
		Tile qt1 = game.getTile(4, 4);
		Piece qp1 = game.getTilePiece(4, 4);
		qp1.setWhite();
		
		Tile qt2 = game.getTile(2, 3);
		Piece qp2 = game.getTilePiece(2, 3);
		
		//Kan bevege seg skrått
		assertTrue(game.canMoveTo(qt1, qp1, 3, 3));
		game.move(4, 4, 3, 3);
		assertEquals(7, qp1.getX());
		assertEquals(7, qp1.getY());
		
		//Kan bevege seg rett horisontalt 
		assertTrue(game.canMoveTo(qt2, qp2, 0, 4));
		game.move(2, 3, 0, 4);
		assertEquals(2, qp2.getX());
		assertEquals(7, qp2.getY());

	}
	
	@Test
	@DisplayName("Sjekker trekkene til King")
	public void testKingMove() {
		game.setTilePiece('K', 1, 1);
		game.setTilePiece('K', 6, 6);
		
		Tile kt1 = game.getTile(1, 1);
		Piece kp1 = game.getTilePiece(1, 1);
		
		
		Tile kt2 = game.getTile(6, 6);
		Piece kp2 = game.getTilePiece(6, 6);
		
		
		//Kan bevege seg en rute skrått
		assertTrue(game.canMoveTo(kt2, kp2, -1, 1));
		game.move(6, 6, -1, 1);
		assertEquals(5, kp2.getX());
		assertEquals(7, kp2.getY());
		
		//Kan bevege seg en rute horisontalt
		assertTrue(game.canMoveTo(kt1, kp1, 0, 1));
		game.move(1, 1, 0, 1);
		assertEquals(1, kp1.getX());
		assertEquals(2, kp1.getY());
		
		//Kan ikke bevege seg mer enn en rute
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(5, 7, 2, 0);
		});
		
		
	}
	
	@Test
	@DisplayName("Sjekker at brikke ikke kan bevege seg utenfor brettet")
	public void testInvalidMove() {
		assertThrows(IllegalArgumentException.class, () -> {
			game1.move(7, 0, -1, 0);
		});
		
		game.setTilePiece('Q', 4, 4);
		Piece p = game.getTilePiece(4, 4);
		p.setWhite();
		
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(4, 4, -5, 5);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(7, 0, 0, 6);
		});
		
	}
	
	@Test
	@DisplayName("Sjekker at man ikke kan bevege brikke av annen farge")
	public void testMoveOtherColor() {
		
		//Kan ikke bevege seg om det ikke er spillerens tur.
		//Svart Pawn
		assertThrows(IllegalStateException.class, () -> {
			game1.move(1, 7, 2, 0);
		});
		//Svart Knight
		assertThrows(IllegalStateException.class, () -> {
			game1.move(0, 6, 2, -1);
		});
		
		//setter til svart spiller
		game1.setNewPlayerColor();
		//Hvit Pawn
		assertThrows(IllegalStateException.class, () -> {
			game1.move(6, 5, -2, 0);
		});
		
		
	}
	
	@Test
	@DisplayName("Sjekker at man ikke kan capture samme farge") 
	public void testCaptureSameColor() {
		game.setTilePiece('B', 1, 1);
		game.setTilePiece('R', 4, 4);
		
		Tile t = game.getTile(1, 1);
		Piece p = game.getTilePiece(1, 1);
		//Setter begge brikker lik hvit
		p.setWhite();
		game.getTilePiece(4, 4).setWhite();
		
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(1, 1, 3, 3);
		});
		
		game.getTilePiece(4, 4).isWhite = false;
		assertTrue(game.canMoveTo(t, p, 3, 3));
		
		
	}
	
	@Test
	@DisplayName("Sjekker at man ikke kan spille etter avsluttet spill")
	public void testGameOverMove() {
		game1.isGameWon = true;
		//Hvit Pawn
		assertThrows(IllegalStateException.class, () -> {
			game1.move(6, 5, -2, 0);
		});
		
		//Svart Knight
		assertThrows(IllegalStateException.class, () -> {
			game1.move(0, 6, 2, -1);
		});
		
		
	}
	
	@Test
	@DisplayName("Sjekker at kongen ikke kan gjøre ulovlige trekk")
	public void testKingIllegalMove() {
		game.setTilePiece('K', 3, 4);
		game.setTilePiece('R', 4, 7);
		game.setTilePiece('N', 1, 2);
		
		
		Piece p = game.getTilePiece(3, 4);
		p.setWhite();
		
		
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(3, 4, 1, 0);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			game.move(3, 4, -1, 0);
		});
	}
	
	@Test
	@DisplayName("Sjekker at spillet registrerer når kongen er i sjakk")
	public void testCheckedKing() {
		game.setTilePiece('K', 3, 4);
		game.setTilePiece('R', 4, 7);
		
		game.getTilePiece(4, 7).setWhite();
		
		game.move(4, 7, -1, 0);
		assertTrue(game.check);
		
		game.move(3, 4, -1, 0);
		assertFalse(game.check);
		
	}
	
	@Test
	@DisplayName("Sjekke at man ikke kan aksessere Piece og Tile utenfor brettet")
	public void test( ) {
		assertThrows(IllegalArgumentException.class, () -> {
			game.getTile(10, -14);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.getTile(8, 8);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.getTilePiece(10, -14);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			game.getTilePiece(8, 7);
		});
	}

	

}
