package Chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SaveHandler implements SaveHandlerInterface{
	
	public final static String SAVE_FOLDER = "src/main/java/Chess/saves/";			//Lagde variabelen SAVE_FOLDER som tar vare på stien til mappen hvor filene skal lagres 
	
	public void save (String filename, Game game) throws IOException {				//"throws IOException" kaster vi metoden videre til når vi bruker save. 
		try (PrintWriter writer = new PrintWriter(getFilePath(filename))) {			//Lagd save metode med try-syntaks som sørger for at hvis det utløses et unntak,mens vi skriver til fil, vil filen lukkes. 
			
			writer.println(game.isGameWon);
			writer.println(game.whitePlayer);
			writer.println(game.check);
			
			writer.println(8);
			writer.println(8);

			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					
					if (game.getTilePiece(x, y).isEmpty()) {
						writer.print(' ');  
					} 
					else {
						writer.print(game.getTilePiece(x,y).type);		//print() ikke println fordi vi ønsker det etter hverandre
					}								
						
				}
				
				writer.println();
			}
			writer.println(0);
			
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					
					if (game.getTilePiece(x, y).isEmpty()) {
						writer.print(' ');
					} else {
						writer.print(game.getTilePiece(x, y).isWhite);
						writer.print(' ');
					}
					
				}
				writer.println();
			}
			
			writer.flush();
			writer.close();
			
		}
	}
	
	public Game load (String filename) throws IOException{				//Load leser fra fil
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {		//Bruker try for å slippe å close filen
			
			Game game = new Game();
			
			//De to første linjene tar inn verdier for isGameWon and whitePlayer
			
			game.isGameWon = Boolean.parseBoolean(scanner.next());
			game.whitePlayer = Boolean.parseBoolean(scanner.next());
			game.check = Boolean.parseBoolean(scanner.next());
			
			
			scanner.nextLine();
			
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			
			
			scanner.nextLine();
			
			String board = scanner.nextLine();
			
			for (int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					
					char symbol = board.charAt(y);
					
					if (symbol == ' ') {
						game.setTilePiece(' ', x, y);
					}
					
					else  {
					}
					game.setTilePiece(symbol, x, y);
				}
				
				//iterer til neste rad
				if (!scanner.hasNextInt()) {
					board = scanner.nextLine();
				}
				}
			
			//hopper over 0 som ble satt der for scannerens skyld
			scanner.nextLine();
			
			//Henter farge
			
			for (int x = 0; x < 8; x++) {
				for(int y = 0; y < 8; y++) {
					
					if (game.getTilePiece(x, y).type != ' ' && scanner.hasNext()) {
						String bool = scanner.next();
						game.getTilePiece(x, y).isWhite = Boolean.parseBoolean(bool);
					}
					}
				if (!scanner.hasNextLine()) {
					scanner.close();
				}
				}
			scanner.close();
			
			return game;
			}
			
		}
	
	
	static String getFilePath(String filename) {
		return SAVE_FOLDER + filename + ".txt";
		}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		SaveHandler handler = new SaveHandler();	
	}
}


