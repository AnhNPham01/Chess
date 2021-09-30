package Chess;

import java.io.IOException;

public interface SaveHandlerInterface {
	
	public void save(String filename, Game game) throws IOException;	//Metode for skriving til fil
	public Game load(String filename) throws IOException;				//Metode for lesing fra fil
	static void getFilePath(String filename) {							//Hjelpemetode
	}							
	
}