package application.save;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public interface SaveService {
	public String[] getLastGameState() throws IOException;
	public void saveGame(ObservableList<Node> nodes) throws IOException; 
}
