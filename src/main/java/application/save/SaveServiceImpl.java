package application.save;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import application.files.FileActions;
import application.main.SampleController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class SaveServiceImpl implements SaveService {
	private final FileActions fileActions;

	public SaveServiceImpl(FileActions fileActions) {
		this.fileActions = fileActions;
	}

	@Override
	public String[] getLastGameState() {
		List<String> savedInfo = new ArrayList<>();
		try {
			savedInfo = fileActions.readTxtFile(FileActions.FILE_NAME);
		} catch (NoSuchFileException e1) {
			List<String> list = SampleController.WINING_SEQUENCE.stream()//
				.map(s -> String.valueOf(s))//
				.collect(Collectors.toList());
			Collections.shuffle(list);
			String[] result = new String[list.size()];
			list.toArray(result);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
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
