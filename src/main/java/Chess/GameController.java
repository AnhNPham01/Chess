package Chess;

import javafx.fxml.FXML;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javafx.event.EventHandler;

import javafx.scene.input.MouseEvent;

import javafx.scene.image.ImageView;

public class GameController {
	
	private Game game;
	
	private Piece gamePiece;
	
	private Label selectedPiece = null;
	private int posX = 0;
	private int posY = 0;
	
	private SaveHandler saveHandler = new SaveHandler();
	
	@FXML 
	private GridPane board;
	
	@FXML
	private TextField filename;
	
	@FXML
	private StackPane side = new StackPane();
	
	@FXML 
	void handleSave() throws IOException {
		saveHandler.save(getFilename(), game);
		
	}
	
	@FXML 
	void handleLoad() throws IOException {
		game = saveHandler.load(getFilename());
		drawBoard();
	}
	
	@FXML
	Text winText = new Text();
	@FXML
	Text loseText = new Text();
	
	Text outputText = new Text();
	
	@FXML
	public void initialize() {
		setInitialGameState();
		createBoard();
		drawBoard();
	}
		
	//Henter filnavnet
	private String getFilename() {
		
		if (filename == null) {				//Hvis ingen filnavn er gitt, 
			String filename = "save_file";	//lagres filen med navnet "save_file"
			return filename;
		}
		
		String filename = this.filename.getText();
		if (filename.isEmpty()) {
			filename = "save_file";
		}
		return filename;
	}
	
	
	//aksessere Tile-objekt
	private StackPane getNodeGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (node instanceof StackPane 
	         && GridPane.getColumnIndex(node) == col 
	         && GridPane.getRowIndex(node) == row) {
	            return (StackPane) node;
	        }
	    }
	    return null;
	}
	
	private void setInitialGameState() {
		game = new Game();
		outputText.setText("         Welcome\n\nWhite player's turn");
	}
	
	private String getTileColor(Tile tile) {	//Setter farge på hvit/svart rute
		if (tile.getTileColor() == "White") { //Hvit rute	
			return "#eeeed2";
			
		} else {							//Svart rute
			return "#769656";
		}
	}
	
	
	private void createBoard() {
		
		board.getChildren().clear();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				StackPane tile = new StackPane();
				tile.setPrefWidth(80);
				tile.setPrefHeight(80);
				tile.setBorder(new Border(new BorderStroke(Color.rgb(43, 29, 14), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				board.add(tile, x, y);
				
				//Legger til eventhandlers på tile som er target	
				tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if(!game.isGameWon) {
						//nullstiller outputText
						outputText.setText("");
						
						if (selectedPiece != null) {
			            	StackPane t = (StackPane) event.getSource();
			            	int targetX = GridPane.getRowIndex(t);
			            	int targetY = GridPane.getColumnIndex(t);
			            	
			            	
			            	int dx = targetX - posY;
			            	int dy = targetY - posX;     	
			            	
			            	if (!(dx == 0 && dy == 0)) {
			            		//
			            		try {
			            			game.move(posY, posX, dx, dy);
			            		} catch(Exception e) {
			            			outputText.setText(e.getMessage());
			            		}
			            		//
			            	//game.move(posY, posX, dx, dy);
			            	}
						}
			                selectedPiece = null;
			                posY = 0;
			                posX = 0;
			                drawBoard();
			            	}
					}}) ;
			
				}   
				}
			//Legge til lagringsknapper. 
			side.setPrefWidth(300);
			board.add(side, 8, 0);
			board.setStyle("-fx-background-color: #121212;");
			
			Text upperText = new Text("FILENAME");
			upperText.setFill(Color.BEIGE);
			
			TextField filename = new TextField();
			this.filename = filename;
			
			side.getChildren().add(filename);
			side.getChildren().add(upperText);
			filename.setPromptText("save_file");
			filename.setTranslateY(535.0);
			upperText.setTranslateY(510.0);
			upperText.setStyle("-fx-font-size: 17px");
			StackPane.setAlignment(upperText, Pos.CENTER_LEFT);
			
			Button saveKnapp = new Button("Save");
			Button loadKnapp = new Button("Load");
			Button resetKnapp = new Button("Reset");
			
			side.getChildren().add(saveKnapp);
			side.getChildren().add(loadKnapp);
			side.getChildren().add(resetKnapp);
			
			saveKnapp.setTranslateY(570.0);
			loadKnapp.setTranslateY(570.0);
			resetKnapp.setTranslateY(570.0);
			saveKnapp.setTranslateX(-10.0);
			loadKnapp.setTranslateX(58.0);
			resetKnapp.setTranslateX(-80.0);
			
			
			
			saveKnapp.setOnMouseClicked(new EventHandler<>() {

				@Override
				public void handle(MouseEvent evt) {
					try {
						handleSave();
						outputText.setText("File saved successfully");
					} catch (IOException e) {
						outputText.setText("Unable to save file.");
					}
					
				
				}
				
			});
			
			loadKnapp.setOnMouseClicked(new EventHandler<>() {
				@Override
				public void handle(MouseEvent evt) {
					try {
						handleLoad();
						outputText.setText("File loaded successfully");
						
					} catch (IOException e) {
						outputText.setText("Unable to load file");
					}
					
					
				}
				
			});
			
			resetKnapp.setOnMouseClicked(new EventHandler<> () {
				@Override
				public void handle(MouseEvent evt) {
					handleReset();
				}
				
			});
		
			//Styler outputText
			outputText.setStyle("-fx-font-size: 20px");
			outputText.setFill(Color.RED);

			outputText.setTranslateX(0.0);
			outputText.setTranslateY(260.0);
			side.getChildren().add(outputText);
			}
	
			
	
	private void drawBoard() {
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				board.getChildren().get(y*8 + x).setStyle("-fx-background-color: " + getTileColor(game.getTile(x, y)) + "; ");
			}
		}
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				
				StackPane tile = (StackPane) getNodeGridPane(board, x, y);
				//System.out.println(game);
				
				//fjerner alle tidligere brikker før implementering av nye
				tile.getChildren().clear();
				
				//Generer piece-objekter oppå tile som Button
				if (this.game.getTile(y, x).isOccupied() && this.game.getTilePiece(y, x)!= null) {
					
					//button = piece
					Label button = new Label();
					StackPane.setAlignment(button, Pos.CENTER);
					int xPos = x;
					int yPos = y;
					
					
		//Initalisere brikker
		this.gamePiece = game.getTilePiece(y,x);
		
		if (gamePiece.type != ' ') {
			Image img = null;
			//Finne ut type
			if (gamePiece.type == 'Q') {
				//Bildet er i Chess.images package av classpath.
				if (gamePiece.isWhite) {
					img = new Image("Chess/images/whiteQueen.png");
				} else {
					img = new Image("Chess/images/blackQueen.png");
				}
				
			} else if (gamePiece.type == 'K') {
				
				if (gamePiece.isWhite) {
					img = new Image("Chess/images/whiteKing.png");
				} else {
					img = new Image("Chess/images/blackKing.png");
				}
				
			} else if (gamePiece.type == 'R') {
				if (gamePiece.isWhite) {
					img = new Image("Chess/images/whiteTower.png");
				} else {
				 	img = new Image("Chess/images/blackTower.png");
				}
				
			} else if (gamePiece.type == 'B') {
				if (gamePiece.isWhite) {
					 img = new Image("Chess/images/whiteBishop.png");
				} else {
					img = new Image("Chess/images/blackBishop.png");
				}
			
			} else if (gamePiece.type == 'N') {
				if (gamePiece.isWhite) {
					 img = new Image("Chess/images/whiteKnight.png");
				} else {
					img = new Image("Chess/images/blackKnight.png");
				}
			
			} else if (gamePiece.type == 'P') {
				if (gamePiece.isWhite) {
					 img = new Image("Chess/images/whitePawn.png");
				} else {
				 img = new Image("Chess/images/blackPawn.png");
				}
			}
			
			ImageView view = new ImageView(img);
			
			button.setGraphic(view);
			tile.getChildren().add(button);
			button.setPadding(Insets.EMPTY);
			
			//Legger til listener på knapp
			button.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					
					if(!game.isGameWon) {
					outputText.setText("");
					//hvis det ikke er angrep
					if (selectedPiece == null) {
						System.out.println("col is " + xPos + " row is " + yPos);
						selectedPiece = (Label) event.getSource();
						posY = yPos;
						posX = xPos;
						event.consume();
						

						
						if (game.getTilePiece(yPos, xPos).getPieceColor() == game.getPlayerColor()) {

								tile.setStyle("-fx-background-color: #baca44;");
								}	
						
						//motstanderspillerbrikker
						else {
						tile.setStyle("-fx-background-color: #af4035;");	
						
						}
					} 
					
					//valgt brikke utsatt for angrep
					else if (selectedPiece != null) {
		            	int dx = yPos - posY;
		            	int dy = xPos - posX;
		            	
		            	if ( !(posY == yPos && posX == xPos) ) {
		            		
		            		try {
		            			game.move(posY, posX, dx, dy);
		            		} catch (Exception e) {
		       
		            			outputText.setText(e.getMessage());
		            			
		            		}
		            	}
		            	
		                selectedPiece = null;
		                posY = 0;
		                posX = 0;
		                event.consume();
		                drawBoard();

				}
					}}});
			
			}
		
	}
}
}		

		if (game.isGameWon()) {
			if (game.whitePlayerWon == false) {
				
				outputText.setText("    GAME OVER\nBlack player won");
			} else {
				
				outputText.setText("    GAME OVER\nWhite player won");
			}
			
		} else if(game.check) {
			if (game.whitePlayer) {
				outputText.setText("White player in check!");
			} else {
				outputText.setText("Black player in check!");
			}
		}
	}
	
	
	public void handleReset() {
		this.game = null;
		setInitialGameState();
		drawBoard();
		outputText.setText("Game resetted");
		}
	
	
}
