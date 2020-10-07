package application.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import application.files.FileActionService;
import application.save.SaveService;
import application.save.SaveServiceImpl;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Menu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;

class SampleController {
	private static final String STYLE_EXITED = "-fx-background-color:rgb(51,51,51);-fx-text-fill:orange;";
	private static final String STYLE_ENTERED = "-fx-background-color:orange;-fx-text-fill:rgb(51,51,51);";

	//@formatter:off	
	@FXML private GridPane grid;
	@FXML private Menu gameMenu;
	@FXML private Label infoText;
	@FXML private MenuBar menu;
	//@formatter:on

	private int emptyFieldRow = -1;
	private int emptyFieldColumn = -1;
	private int sideSize = 4;
	private SaveService saveService;
	private Integer[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, -1};
	private Timeline fadingTimeLine;

	void saveGame() {
		try {
			saveService.saveGame(grid.getChildren());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//@formatter:off
	private void setInfo(String text) {infoText.setText(text);}
	//@formatter:on

	private void finishEffect() {
		fadingTimeLine.stop();
		infoText.setTextFill(Color.rgb(51, 51, 51));
	}

	@FXML
	private void initialize() {
		saveService = new SaveServiceImpl(new FileActionService());
		fadingLabelInit(infoText);

		try {
			String[] savedState = saveService.getLastGameState();
			for(int i = 0;i < savedState.length;i++) {
				Button button = (Button) grid.getChildren().get(i);
				button.setText(savedState[i]);
				if(button.getText().equals("-1"))
					button.setVisible(false);
				else
					button.setVisible(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		grid.getChildren().stream().forEach(node -> {
			Button button = (Button) node;
			if(emptyFieldColumn == -1 && emptyFieldRow == -1 && button.getText().equals("-1")) {
				emptyFieldColumn = GridPane.getColumnIndex(button);
				emptyFieldRow = GridPane.getRowIndex(button);
			}
			
			button.setOnMouseEntered(val -> button.setStyle(STYLE_ENTERED));
			button.setOnMouseExited(val -> button.setStyle(STYLE_EXITED));
			button.setOnMouseClicked(val -> {
				finishEffect();
				scanButton(button);
			});
		});

		gameMenu.getItems().stream().forEach(menuItem -> {
			switch (menuItem.getText()) {
				case "New Game" : {
					menuItem.setOnAction(val -> {
						finishEffect();
						newGame();
					});
					break;
				}
				case "Exit" : {
					menuItem.setOnAction(val -> Platform.exit());
					break;
				}
			}
		});

	}

	private void newGame() {
		List<Integer> list = Arrays.asList(values);
		Collections.shuffle(list);
		for(int i = 0;i < grid.getChildren().size();i++) {
			Button button = (Button) grid.getChildren().get(i);
			button.setText(String.valueOf(list.get(i)));
			if(button.getText().equals("-1")) {
				button.setVisible(false);
				emptyFieldColumn = GridPane.getColumnIndex(button);
				emptyFieldRow = GridPane.getRowIndex(button);
			} else
				button.setVisible(true);
		}
		startFading("New Game Started!!!");
	}

	private void scanButton(Button button) {
		int rowIndex = GridPane.getRowIndex(button);
		int columnIndex = GridPane.getColumnIndex(button);
		if(columnIndex == emptyFieldColumn && rowIndex + 1 <= sideSize - 1 && rowIndex + 1 == emptyFieldRow)
			swapButtons(columnIndex, rowIndex);
		else if(columnIndex == emptyFieldColumn && rowIndex - 1 >= 0 && rowIndex - 1 == emptyFieldRow)
			swapButtons(columnIndex, rowIndex);
		else if(rowIndex == emptyFieldRow && columnIndex + 1 <= sideSize - 1 && columnIndex + 1 == emptyFieldColumn)
			swapButtons(columnIndex, rowIndex);
		else if(rowIndex == emptyFieldRow && columnIndex - 1 >= 0 && columnIndex - 1 == emptyFieldColumn)
			swapButtons(columnIndex, rowIndex);
		else
			startFading("Can't move that puzzle!!!");
	}

	private void swapButtons(int puzzleColumn, int puzzleRow) {
		//pobierz przyciski do zamiany
		Button empty = (Button) grid.getChildren().get(sideSize * emptyFieldRow + emptyFieldColumn);
		Button button = (Button) grid.getChildren().get(sideSize * puzzleRow + puzzleColumn);

		//zamie� teksty przycisk�w
		empty.setText(button.getText());
		empty.setVisible(true);
		button.setVisible(false);
		button.setText("-1");

		//zamiana indeks�w wewn�trznych
		int tmpColumn = emptyFieldColumn;
		int tmpRow = emptyFieldRow;
		emptyFieldColumn = puzzleColumn;
		emptyFieldRow = puzzleRow;
		puzzleColumn = tmpColumn;
		puzzleRow = tmpRow;

		checkForVictory();
	}

	private void checkForVictory() {
		List<Integer> list = Arrays.asList(values);
		List<Integer> gameArray = grid.getChildren().stream().map(node -> {
			Button button = (Button) node;
			return Integer.valueOf(button.getText());
		}).collect(Collectors.toList());
		//		System.out.println(list+"\n"+gameArray);
		if(listEqual(list, gameArray)) {
			startFading("You Won!!!");
			newGame();
		}
	}

	private boolean listEqual(List<Integer> list1, List<Integer> list2) {
		if(!list1.equals(list2))
			return false;
		for(int i = 0;i < list1.size();i++) {
			if(!(list1.get(i).equals(list2.get(i))))
				return false;
		}
		return true;
	}

	private void fadingLabelInit(Label fadingLabel) {
		final KeyValue value1 = new KeyValue(fadingLabel.textFillProperty(), Color.ORANGE);
		final KeyFrame frame1 = new KeyFrame(Duration.ZERO, value1);
		final KeyValue value2 = new KeyValue(fadingLabel.textFillProperty(), Color.rgb(51, 51, 51));
		final KeyFrame frame2 = new KeyFrame(Duration.millis(3500), value2);
		fadingTimeLine = new Timeline(frame1, frame2);
	}

	private void startFading(String text) {
		fadingTimeLine.stop();
		setInfo(text);
		fadingTimeLine.play();
	}
}
