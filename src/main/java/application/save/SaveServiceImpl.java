package application.save;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import application.files.FileActions;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class SaveServiceImpl implements SaveService {
	private final FileActions fileActions;

	public SaveServiceImpl(FileActions fileActions) {
		this.fileActions = fileActions;
	}

	@Override
	public String[] getLastGameState() throws IOException {
		List<String> savedInfo = fileActions.readTxtFile(FileActions.FILE_NAME);
		return savedInfo.get(0).split(FileActions.SEPARATOR);
	}

	@Override
	public void saveGame(ObservableList<Node> nodes) throws IOException {
		fileActions.clearTxtFile(FileActions.FILE_NAME);
		String save = nodes.stream()//
			.map(node -> ((Button) node).getText())//
			.collect(Collectors.joining(FileActions.SEPARATOR));
		fileActions.writeTxtFile(FileActions.FILE_NAME, save);
	}
}
