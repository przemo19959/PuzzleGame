package application.save;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import application.files.FileActionService;
import application.files.FileActions;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class SaveServiceImpl implements SaveService {
	private FileActions fileActions;
	
	public SaveServiceImpl() {
		this.fileActions = new FileActionService();
	}

	@Override
	public String[] getLastGameState() throws IOException {
		List<String> savedInfo=fileActions.readTxtFile(FileActions.fileName);
		return savedInfo.get(0).split(FileActions.separator);
	}
	
	@Override
	public void saveGame(ObservableList<Node> nodes) throws IOException {
		fileActions.clearTxtFile(FileActions.fileName);
		String save=nodes.stream()
				.map(node->((Button)node).getText())
				.collect(Collectors.joining(FileActions.separator));
		fileActions.writeTxtFile(FileActions.fileName, save);
	}
}
