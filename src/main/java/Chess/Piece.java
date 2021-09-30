package Chess;

public class Piece {
	public char type = ' ';
	public int x;
	public int y;
	public boolean isWhite = false;
	public boolean isKilled = false;

	
	public Piece(boolean isKilled, int x, int y) {
		setX(x);
		setY(y);
	}
	
	public Piece() {
	}
	
	//Settere	
	public void setEmpty() {
		this.type = ' ';
	}
	
	public void setPawn() {
		this.type = 'P';
	}
	
	public void setRook() {
		this.type = 'R';
	}
	
	public void setKnight() {
		this.type = 'N';				//Bokstav K er forbeholdt kongen. 
	}
	
	public void setBishop() {
		this.type = 'B';
	}
	
	public void setQueen() {
		this.type = 'Q';
	}
	
	public void setKing() {
		this.type = 'K';
	}
	
	public void setWhite() {
		this.isWhite = true;
	}
	
	//Validatør
	public boolean isEmpty() {
		return type == ' ';
	}
	
	public boolean isPawn() {
		return type == 'P';
	}
	
	public boolean isRook() {
		return type == 'R';
	}
	
	public boolean isKnight() {
		return type == 'N';
	}
	
	public boolean isBishop() {
		return type == 'B';
	}
	
	public boolean isQueen() {
		return type == 'Q';
	}
	
	public boolean isKing() {
		return type == 'K';
	}
	
	//Getter for koordinater
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	//Settere for koordinater
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
	
	//Gettere
	public String getPieceColor() {			
		if (!isWhite) {
			return "Black";
		} else {
			return "White";
		}
	}
	
	public boolean isKilled() {
		return isKilled;
	}
	
	public void setIsKilled(boolean isKilled) {
		this.isKilled = isKilled;
	}
	
	@Override
	public String toString() {
			return Character.toString(type);
	}

}
