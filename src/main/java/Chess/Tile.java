package Chess;

public class Tile {
	public char type = ' ';
	public int x;
	public int y;
	public boolean isWhite = false;
	Piece piece;

	
	public Tile(int x, int y) {
		setX(x);
		setY(y);
		this.piece = new Piece(false, x, y);
		this.setColorTile();
		
	}
	
	public void setPiece() {
		this.piece = new Piece(false, x, y);
	}
	
	//Settere	
	public void setEmpty() {
		type = ' ';
	}
	
	public void setX(int x) {
		if (x >= 0 && x <= 7) {
			this.x = x;
		} else {
			throw new IllegalArgumentException("Invalid x-coordinates");
		}
		
	}
	
	public void setY(int y) {
		if ( y >= 0 && y <= 7) {
			this.y = y;
		} else {
		throw new IllegalArgumentException("Invalid y-coordinates");
		}
	}
	
	//Getter for koordinater
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getPieceColor() {
		return piece.getPieceColor();
	}
	
	public String getTileColor() {				
		if (!isWhite) {
			return "Black";
		} else {
			return "White";
		}
	}
	
	public boolean isOccupied() {	//Sjekke om felt inneholder brikke
		if (piece == null) {
			return false;
		} else if (piece.type == ' ') {
			return false;
		} 
		return true;
	}
	
	public void moveFromSpot() {	//Fjerner brikke fra opprinnelig felt
		setEmpty();
		this.piece.setEmpty();
	}
	
	public void movePiece(Piece startPiece) { //Flytter på brikke
		Piece newPiece = this.getPiece();
		newPiece.type = startPiece.type;
		
		if (startPiece.isWhite) {
			newPiece.setWhite();
		}
	}
	
	
	public Piece getPiece() {
		return piece;
	}
				
	//Sette farge på felt i spillbrettet
	public void setColorTile() {
				if ((y % 2) == 0 && (x % 2) == 0) {	//Partallsrad
					this.isWhite = true;
					}
				
				if ((y % 2) != 0 && (x % 2) != 0 ) {
					this.isWhite = true;
				}
			}
	public String getColor() {
		if (this.isWhite) {
			return "White";
		} else {
			return "Black";
		}
	}
	
	//Metode for angrep
	public void capture(Tile targetTile) {
		if (targetTile.isOccupied()) {
			Piece targetPiece = targetTile.getPiece();
			targetPiece.setIsKilled(true);
			targetPiece.setEmpty();
		} 
	}
	
	@Override
	public String toString() {
		if (piece == null) {
			return " ";			//Returnerer tom streng om det ikke er noen piece.
		} else {
			return Character.toString(piece.type);
		}
	}
}
