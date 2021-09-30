package Chess;


public class Game {
	private Tile[][] board;
	public boolean isGameWon = false; 			//Tilsvarer checkmated
	public boolean check = false;
	public boolean whitePlayerWon = false;
	public boolean whitePlayer = true;
	

	//Konstruktør
	public Game() {	 							//Initialisere spillbrett, implementere brikker
		this.board = new Tile[8][8];
		
		for(int y = 0; y < 8; y++) { 			//Kolonne i brett
			for (int x = 0; x < 8; x++) { 		//Rad i brett
				board[y][x] = new Tile(x, y);
			}
		}

		/*
		 *implementerer bakerste brikker 
		 * itererer gjennom hver rad (horisontalt), og plasserer brikker med sine korresponderende brikketyper
		 */
		for (int y = 0; y < 8; y++) {
			if( y == 0 || y == 7) {
				setTilePiece('R', 0, y);
				setTilePiece('R', 7, y);
				
			} else if (y == 1 || y == 6) {
				setTilePiece('N', 0, y);
				setTilePiece('N', 7,y);
				
			} else if (y == 2 || y == 5) {
				setTilePiece('B', 0, y);
				setTilePiece('B', 7, y);
				
			} else if (y == 3) {
				setTilePiece('Q', 0, y);
				setTilePiece('Q', 7, y);
				
			} else if (y == 4) {
				setTilePiece('K', 0, y);
				setTilePiece('K', 7, y);
			}
		}
		
		/*implementerer pawns
		 * iterer gjennom hver rad(horisontalt), setter Pawn langs hele raden i kolonne 1 og 6
		 */
		for (int y = 0; y < 8; y++) {
			setTilePiece('P', 1, y);
			setTilePiece('P', 6, y);
		}
		
		//Implementere farge
		for (int x = 6; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				getTile(x,y).piece.setWhite();
			}
		}
		
	}

	
	public Piece setTilePiece(char type, int x, int y) {	//Setter brikketype
		if (type == 'P') {
			this.getTile(x,y).piece.setPawn();
			
		} else if (type == 'R') {
			this.getTile(x,y).piece.setRook();
			
		} else if (type == 'N') {
			this.getTile(x,y).piece.setKnight();
			
		} else if (type == 'B') {
			this.getTile(x,y).piece.setBishop();
			
		} else if (type == 'Q') {
			this.getTile(x,y).piece.setQueen();
			
		} else if (type == 'K') {
			this.getTile(x,y).piece.setKing();
			
		} else if (type == ' ') {
			this.getTile(x,y).piece.setEmpty();
			
		}
		return getTile(x,y).piece;
	}
	
	
	public Piece getTilePiece(int x, int y) {
		return getTile(x,y).piece;
	}
	
	
	// Hjelpemetode som sjekker om koordinat er innenfor eller utenfor spillbrettet.
	public boolean isTile(int x, int y) {		
		return x >= 0 && y >= 0 && x < 8 && y < 8;
	}
	
	
	public Tile getTile(int x, int y) {
		if(!isTile(x, y)) {
			throw new IllegalArgumentException("Coordinates are out of bounds");
		}
		return this.board[y][x];
	}
	
//Validatør for trekk
	public boolean canMoveTo(Tile tile, Piece piece, int dx, int dy) {	//Tar inn startrute, brikke som skal flyttes på, og x og y endring.
		
		//kan ikke spille brikke av annen farge
		if (piece.getPieceColor() != getPlayerColor()) {
			return false;
		}
		
		//koordinater til ruten brikken vil flytte til
		int targetX = tile.getX() + dx;
		int targetY = tile.getY() + dy;
		
		
		Tile targetTile = getTile(targetX, targetY);
		
		//Brikke kan ikke bevege seg utenfor brettet
		if (!isTile(targetX, targetY)) { 
			return false;					
		}
		
		//Kan ikke ta brikke av samme farge
		if (targetTile.isOccupied() && targetTile.getPieceColor() == piece.getPieceColor()) {
			return false;
		}
		
		//Pawn atferd avhenger av farge da de ikke kan gå bakover.
		if (piece.type == 'P') {		
			//Atferd til svart pawn	
			if (!piece.isWhite) { 			
				/*
				 * Svart Pawn går 1 nedover
				 * flytter 2 ruter dersom det er første trekk
				 * flytter skrått dersom den kan capture
				 */
				
				if (dx == 1 && dy == 0) {		//går 1 rute nedover
					if (targetTile.isOccupied()) {
						return false;
					} else {
						return true;
					}
					
				} else if (dx == 1 && (dy == -1 || dy == 1) ) {		//skrått for å capture
					if (targetTile.isOccupied() && targetTile.getPieceColor() != piece.getPieceColor()) {
						return true;
					} else {
						return false;
					}
				}
				else if (dx == 2 && dy == 0 ) {		//2 ruter første trekk. Kan ikke gå bakover så det er gitt at de må være på x = 6 ved første trekk.
					
					if(piece.getX() == 1 && !getTile(targetX-1, targetY).isOccupied() && !targetTile.isOccupied()) {
						return true;
					} else {
						return false;
					}
				}
			} 
			
			/* Atferd til hvit pawn
			 * Inversert trekk ift. svarte pawn. 
			 */
			else {
					if (dx == -1 && dy == 0) {
						if (!targetTile.isOccupied()) {
							return true;
						} 
						
					} else if (dx == -1 && (dy == -1 || dy == 1)) {
						
						if((targetTile.isOccupied() && targetTile.getPieceColor() != piece.getPieceColor())) {
							return true;
						} else {
							return false;
						}
					} else if (dx == -2 && dy == 0) {
						if (piece.getX() == 6 && !getTile(targetX+1, targetY).isOccupied() && !targetTile.isOccupied()) {
							return true;
						} 
						else {
							return false;
					}
				}
			}
		}
		
			
		//De andre brikkene har samme atferd uansett farge
		if (piece.type == 'R') {		//Atferd til rook

			if ((dx != 0 && dy == 0) || (dx == 0 && dy != 0)) {			//Kan kun bevege i en retning
				
				if (dx < 0) {					//Beveger seg vertikalt oppover. kardinalitet omgjøres for å få pos. verdi.
					int cardinality = (-1)*dx;
					
					/*
					 * sjekker om 1. rute foran er okkupert hvis brikken skal flytte seg lenger (kardinalitet større enn 1)
					 * for-løkken som sjekker mellombrikker hopper over i=1 noen ganger så det er ekstra tiltak
					 */
					if (getTile(tile.getX()-1,tile.getY()).isOccupied() && cardinality > 1) {
						return false;
					}
					
					//sjekker mellombrikker
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX()-i , tile.getY()).isOccupied()) {
							return false;
						}
					}
					
				} else if (dx > 0) {		//Beveger seg vertikalt nedover
					
					int cardinality = dx;
					if (getTile(tile.getX()+1,tile.getY()).isOccupied() && cardinality > 1) {
						return false;
					}
					
					//sjekker mellombrikker
					for (int i = 1; i < cardinality; i++) {
						
						if (getTile(tile.getX()+i , tile.getY()).isOccupied()) {
							return false;
						}
					}
					
				} else if (dy < 0) {		//Beveger seg horisontalt mot venstre
					
					int cardinality = (-1)*dy;
					if (getTile(tile.getX(),tile.getY()-1).isOccupied() && cardinality > 1) {
						return false;
					}
					
					//sjekker mellombrikker
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX(), tile.getY()-i).isOccupied()) {
							return false;
						}
					}

					
				} else if (dy > 0) {		//Beveger seg horisontalt mot høyre
					int cardinality = dy;
					if (getTile(tile.getX(),tile.getY()+1).isOccupied() && cardinality > 1) {
						return false;
					}
					
					//sjekker mellombrikker
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX(), tile.getY()+i).isOccupied()) {
							return false;
						}
					}
				}
				
				return true;	
			}
				
		}
		
			 else if (piece.type == 'N') { 		//Atferd til knight
				
				// kan kun bevege seg to ruter horisontalt/vertikalt og deretter enten en rute andre retningen 

				if (dx == 2 || dx == -2) {
					if (dy == 1 || dy == -1 ) {
						return true;
					}
				} else if ( dy == 2 || dy == -2) {
					if ( dx == 1 || dx == -1) {
						return true;
					}
				}
				return false;
				
				
			} else if (piece.type == 'B') {		//Atferd til bishop
				
				
				int cardinality = dx;
				if (dx < 0) {
					cardinality = (-1)*dx;
				}
				/* sjekker om det er noen brikker imellom på veien
				 * sjekker om okkuperte brikke ikke er av samme farge
				 *sjekker om dx og dy er direkte proporsjonale
				 */
				
				//beveger seg skrått oppover mot venstre (oppover er mot svart)
				if (dx < 0 && dy < 0) {
					
					//sjekker dir. proporsjonalitet
					if (dx/cardinality != -1 || dy/cardinality != -1) {
						return false;
					}
					
					if (getTile(tile.getX()-1,tile.getY()-1).isOccupied() && cardinality > 1) {
						return false;
					}
					
					//sjekker mellombrikker
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX()-i , tile.getY()-i ).isOccupied()) {
							return false;
						}
					}
					
					return true;
					
					
				}
				
				else if (dx < 0 && dy > 0) {		//beveger seg skrått oppover mot høyre
					
					if (dx/cardinality != -1 || dy/cardinality != 1) {
						return false;
					}
					
					if (getTile(tile.getX()-1,tile.getY()+1).isOccupied() && cardinality > 1) {
						return false;
					}
					
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX()-i , tile.getY()+i ).isOccupied()) {
							return false;
						}
					}
					
					return true;
				}
				
				else if (dx > 0 && dy < 0) {		//beveger seg skrått nedover mot venstre
					
					if (dx/cardinality != 1 || dy/cardinality != -1) {
						return false;
					}
					
					if (getTile(tile.getX()+1,tile.getY()-1).isOccupied() && cardinality > 1) {
						return false;
					}
				
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX()+i , tile.getY()-i).isOccupied()) {
							return false;
						}
					}
					return true;

				} 
				
				else if (dx > 0 && dy > 0) {		//beveger seg skrått nedover mot høyre
					
					if (dx/cardinality != 1 || dy/cardinality != 1) {
						return false;
					}
					
					if (getTile(tile.getX()+1,tile.getY()+1).isOccupied() && cardinality > 1) {
						return false;
					}
					for (int i = 1; i < cardinality; i++) {
						if (getTile(tile.getX()+i , tile.getY()+i).isOccupied()) {
							return false;
						}
					}
					return true;	
				}
			//Dronningen kan bevege seg som en rook, bishop (eller king)
			} else if (piece.type == 'Q') {		//Atferd til dronning
			

					if ((dx != 0 && dy == 0) || (dx == 0 && dy != 0)) {			//Beveger seg i en retning (rook)
						
						if (dx < 0) {			//beveger seg vertikalt oppover
							int cardinality = (-1)*dx;
							
							if (getTile(tile.getX()-1,tile.getY()).isOccupied() && cardinality > 1) {
								return false;
							}
							
							//sjekker mellombrikker
							for (int i = 1; i < cardinality; i++) {
								if (getTile(tile.getX()-i , tile.getY()).isOccupied()) {
									return false;
								}
							}
							
						} else if (dx > 0) {	//beveger seg vertikalt nedover
							
							int cardinality = dx;
							if (getTile(tile.getX()+1,tile.getY()).isOccupied() && cardinality > 1) {
								return false;
							}
							
							//sjekker mellombrikker
							for (int i = 1; i < cardinality; i++) {
								
								if (getTile(tile.getX()+i , tile.getY()).isOccupied()) {
									return false;
								}
							}
							
						} else if (dy < 0) {		//beveger seg horisontalt til venstre
							int cardinality = (-1)*dy;
							if (getTile(tile.getX(),tile.getY()-1).isOccupied() && cardinality > 1) {
								return false;
							}
							
							//sjekker mellombrikker
							for (int i = 1; i < cardinality; i++) {
								if (getTile(tile.getX() , tile.getY()-i).isOccupied()) {
									return false;
								}
							}

							
						} else if (dy > 0) {		//beveger seg horisontalt til høyre
							int cardinality = dy;
							if (getTile(tile.getX(),tile.getY()+1).isOccupied() && cardinality > 1) {
								return false;
							}
							
							//sjekker mellombrikker
							for (int i = 1; i < cardinality; i++) {
								if (getTile(tile.getX(), tile.getY()+i).isOccupied()) {
									return false;
								}
							}
							
						}
						return true;
					}
			
				 	//Beveger seg som en bishop
				 	int cardinality = dx;
				 	if (dx < 0) {
					 cardinality = (-1)*dx;
				 	}
					 

					if (dx < 0 && dy < 0) {		// beveger seg skrått oppover mot venstre
						
						//sjekker dir. proporsjonalitet
						if (dx/cardinality != -1 || dy/cardinality != -1) {
							return false;
						}
						
						if (getTile(tile.getX()-1,tile.getY()-1).isOccupied() && cardinality > 1) {
							return false;
						}
						
						//sjekker mellombrikker
						for (int i = 1; i < cardinality; i++) {
							if (getTile(tile.getX()-i , tile.getY()-i ).isOccupied()) {
								return false;
							}
						}
						
						return true;
						
						
					}
					
					else if (dx < 0 && dy > 0) {		//beveger seg skrått oppover mot høyre
						
						if (dx/cardinality != -1 || dy/cardinality != 1) {
							return false;
						}
						
						if (getTile(tile.getX()-1,tile.getY()+1).isOccupied() && cardinality > 1) {
							return false;
						}
						
						for (int i = 1; i < cardinality; i++) {
							if (getTile(tile.getX()-i , tile.getY()+i ).isOccupied()) {
								return false;
							}
						}
						
						return true;
					}
					
					else if (dx > 0 && dy < 0) {		//beveger seg skrått nedover mot venstre
						
						if (dx/cardinality != 1 || dy/cardinality != -1) {
							return false;
						}
						
						if (getTile(tile.getX()+1,tile.getY()-1).isOccupied() && cardinality > 1) {
							return false;
						}
					
						for (int i = 1; i < cardinality; i++) {
							if (getTile(tile.getX()+i , tile.getY()-i).isOccupied()) {
								return false;
							}
						}
						return true;

					} 
					
					else if (dx > 0 && dy > 0) {			//beveger seg skrått nedover mot høyres
						
						if (dx/cardinality != 1 || dy/cardinality != 1) {
							return false;
						}
						
						if (getTile(tile.getX()+1,tile.getY()+1).isOccupied() && cardinality > 1) {
							return false;
						}
						
						for (int i = 1; i < cardinality; i++) {
							if (getTile(tile.getX()+i , tile.getY()+i).isOccupied()) {
								return false;
							}
						}
						return true;
					}

		/* atferd som konge 
		 * beveger seg enten en rute i x- eller y-retning eller en rute i begge. dvs. ruter rundt.
		 */
		if (dx >= 0 && dx <= 1 && dy >= 0 && dy <= 1) {
			//enten er targetTile ledig, elller så er den okkupert med en motstanderbrikke
			if (!targetTile.isOccupied() || (targetTile.isOccupied() && targetTile.getPieceColor() != tile.getPieceColor())) {
				return true;
			}
		}
		
			}
		
		else if (piece.type == 'K') {		//Atferd til konge
			
			//beveger seg en rute opp eller en ned vertikalt
			if ((dx == 1 || dx == -1) && dy == 0) {
				if (targetTile.isOccupied() && targetTile.getPieceColor() != tile.getPieceColor()) {
					return true;
				} else if (!targetTile.isOccupied()) {
					return true;
				}
			} 
			//beveger seg skrått
			else if((dx == 1 || dx == -1) && (dy == 1 || dy == -1)) {
				if (targetTile.isOccupied() && targetTile.getPieceColor() != tile.getPieceColor()) {
					return true;
				} else if (!targetTile.isOccupied()) {
					return true;
				}
				
			} 
			//beveger seg en til venstre eller høyre horisontalt
			else if (dx == 0 && (dy == 1 || dy == -1)) {
				if (targetTile.isOccupied() && targetTile.getPieceColor() != tile.getPieceColor()) {
					return true;
				} else if (!targetTile.isOccupied()) {
					return true;
				}
				
			}

			return false;

			
			}
		
		//Kan ikke ta av samme farge. 
		if (targetTile.isOccupied()) {
			if (targetTile.getPieceColor() == tile.getPieceColor())
			return false;
		}
		
		return false;
		}
		

	
	
//Bevege brikker)	
	public void move(int x ,int y, int dx, int dy) {
		//henter Tile og Piece til brikken som skal flyttes på
		Tile start = getTile(x,y);
		Piece startPiece = getTilePiece(x,y);
		
		if (isGameWon) {		//Kan ikke spille hvis spillet er over.
			throw new IllegalStateException("Game is over");
		}
		
		if (!(startPiece.isWhite == this.whitePlayer)) {	//Kan ikke spille brikke av annen farge.
			throw new IllegalStateException("Invalid piece chosen");
		}
		
		if(start == null || startPiece == null) {
			throw new IllegalStateException("No piece chosen");
		}
		
		if(!canMoveTo(start, startPiece, dx, dy)) {				//Kan ikke gjøre trekket iht. valideringsmetoden canMoveTo()
			throw new IllegalArgumentException("Not a valid move");
		}
		
		//Kan ikke bevege kongen i sjakk. checkIllegalMove() gjør det umulig for konge å gjøre trekk som setter seg selv i sjakk
		if(startPiece.type == 'K' &&  canMoveTo(start, startPiece, dx, dy) && checkIllegalMove(start, startPiece, dx, dy)) {
			throw new IllegalArgumentException("Illegal move");
		}
	
		int targetX = x + dx;
		int targetY = y + dy;
		
		Tile targetTile = getTile(targetX, targetY);
		
		if (targetTile.isOccupied() && targetTile.getPiece().type == 'K') {		//hvis det er kongebrikken til motstanderen som blir tatt er det game over.
			isGameWon = true;
			
			if (targetTile.getPieceColor() == "Black") {
				whitePlayerWon = true;
			}
		}
		
		start.capture(targetTile);			//Metode som returnerer ingenting om det ikke er motstanderbrikke å capture.

		startPiece.setX(targetX);			//Setter nye koordinater
		startPiece.setY(targetY);
		targetTile.setPiece();				//Initialiserer et tomt Piece-objekt 
		targetTile.movePiece(startPiece);	//movePiece er som en setter for Tile. Den setter Piece-objektet oppå Tile. 

		start.moveFromSpot();				//moveFromSpot() fjerner Piece fra opprinnelige Tile.

		
		//resetter check
		if (check) {
			check = false;
		}
		//nåværende spiller har gjort trekk og skal sjekke om den setter motstanderen i sjakk. 
		ifCheckMove();
		setNewPlayerColor();
	}
	

	//checkIllegalMove
	public boolean checkIllegalMove(Tile tempTile, Piece tempPiece, int dx, int dy) {
		int currentX = tempTile.getX();
		int currentY = tempTile.getY();
		
		int targetX = dx + currentX;
		int targetY = dy + currentY;
		
		/*
		 * Itererer gjennom brettet etter motstående spiller sine brikker
		 * Sjekker om de kan bevege seg til targetTile. 
		 */
		
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					
					if (getTilePiece(x, y).type != ' ') {
						
						if (getTilePiece(x,y).isWhite != this.whitePlayer) {
							
							Piece thisPlayerPiece = getTilePiece(x,y);
							Tile thisPlayerTile = getTile(x,y);
							
							int opponentdx = targetX - x;
							int opponentdy = targetY - y;
							
							//Setter ny spillerfarge midlertidig under iterasjonen, slik at canMoveTo() fungerer slik den skal uten hensyn til den egentlige spillerfargen.
							setNewPlayerColor();
							
							//annen type validering for pawns, da de kan kun ta kongen om de kan capture den ved å bevege seg skrått. 
							if(thisPlayerPiece.type == 'P') {
								//hvit pawn
								if (thisPlayerPiece.isWhite) {
									if (opponentdx == -1 && (opponentdy == 1 || opponentdy == -1)) {
										setNewPlayerColor();
										return true;
									}
									
								} else {
									if (opponentdx == 1 && (opponentdy == 1 || opponentdy == -1)) {
										setNewPlayerColor();
										return true;
									}
								}
							}
							
							//Ellers
								if (canMoveTo(thisPlayerTile, thisPlayerPiece, opponentdx, opponentdy) && thisPlayerPiece.type != 'P') {
									setNewPlayerColor();
									return true;
								}
								setNewPlayerColor();
							
								
							}
						}
					}
					}
			
			return false;	
	}
	
	// metode som ser om kongen er i sjakk
	public boolean ifCheckMove() {
		int kingX = 0;
		int kingY = 0;
		
		//Itererer gjennom brikker på brettet for å finne kongen til motstander
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				
				if (getTilePiece(x,y) != null ) {
					
				if (getTilePiece(x, y).type == 'K' && getTilePiece(x,y).isWhite != this.whitePlayer) {
					kingX = x;
					kingY = y;
				}
				}
			}
		}
			
		/*
		 * Itererer gjennom brettet etter nåverende spiller sine brikker
		 * Sjekker om de kan bevege seg til kongen av motstander spiller.
		 */
		
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					
					if (getTilePiece(x, y) != null) {
						
						if (getTilePiece(x,y).isWhite == this.whitePlayer) {
							
							
							Piece thisPlayerPiece = getTilePiece(x,y);
							Tile thisPlayerTile = getTile(x,y);
							
							if (!(kingX == 0 && kingY == 0)) {
								int dx = kingX - x;
								int dy = kingY - y;
								
								if (canMoveTo(thisPlayerTile, thisPlayerPiece, dx, dy)) {
									this.check = true;
									return true;	
								}
							}
								
							}
						}
					}
					}
			return false;
					
				}
	
	//Bytte spiller til nestemann. 
	public void setNewPlayerColor() {
		if (!whitePlayer) {
			whitePlayer = true;
		} else {
			whitePlayer = false;
		}
	}
	
	public String getPlayerColor() {
		if (!whitePlayer) {
			return "Black";
		} else {
			return "White";
		}
	}
	
	
	public boolean isGameWon() {
		return isGameWon;
	}
	
	
	public boolean isOccupied(int x, int y) {	//Sjekker om det er noe på posisjonen
		return (board[y][x] != null);
	}
	
	@Override
	public String toString() {				//tekststreng som representerer spillbrettet.
		String boardString = "";
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
					boardString += getTile(x,y);
				}
			boardString += "\n";
			} 
		
		if (isGameWon) {
			boardString += "\n\nGame won!";
		}

	return boardString + "----------------------";
	}
	
public static void main(String[] args) {
	//Indices: Arraylist[row(hor)][col(vert)]
		Game game = new Game();

		System.out.println(game);
		game.move(6, 1, -2, 0);
		System.out.println(game);
		game.move(1, 0, 2, 0);				
		System.out.println(game);
		game.move(4, 1, -1, -1);
		System.out.println(game);
		game.move(1, 1, 2, 0);
		System.out.println(game);
		game.move(7, 1, -2, -1);
		
		System.out.println(game);
		game.move(0,0,3,0);
		System.out.println(game);
		game.move(7, 2 , -1, -1);
		System.out.println(game);
		game.move(0, 2, 1, -1);
		System.out.println(game);
		
		Game game1 = new Game();
		game1.move(6, 3, -2, 0);
		System.out.println(game1);
		game1.move(1, 3, 2, 0);
		System.out.println(game1);
		
	}
}


