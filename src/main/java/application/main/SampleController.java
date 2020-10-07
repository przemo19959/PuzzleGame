package application.main;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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

public class SampleController {
	private static final String EVENT_HANDLER_MESSAGE = "{0} doesn''t have assigned event handler!";
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
	public static final List<Integer> WINING_SEQUENCE = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, -1);
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

	private void stopFadingManually() {
		if(fadingTimeLine != null) {
			fadingTimeLine.stop();
			infoText.setTextFill(Color.rgb(51, 51, 51));
		}
	}

	@FXML
	private void initialize() {
		saveService = new SaveServiceImpl(new FileActionService());
		initFadingLabel(infoText);

		try {
			String[] savedState = saveService.getLastGameState();
			for(int i = 0;i < savedState.length;i++) {
				Button button = (Button) grid.getChildren().get(i);
				button.setText(savedState[i]);
				button.setVisible(savedState[i].equals("-1") == false);
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

			//@formatter:off
			button.setOnMouseEntered(val -> button.setStyle(STYLE_ENTERED));
			button.setOnMouseExited(val -> button.setStyle(STYLE_EXITED));
			button.setOnMouseClicked(val -> {stopFadingManually();scanButton(button);});
			//@formatter:on
		});

		gameMenu.getItems().stream()//
			.forEach(menuItem -> {
				switch (menuItem.getText()) { //@formatter:off
					case "New Game" : menuItem.setOnAction(val -> {stopFadingManually();startNewGame();});break;
					case "Exit" : menuItem.setOnAction(val -> Platform.exit());break;
					default: throw new IllegalArgumentException(MessageFormat.format(EVENT_HANDLER_MESSAGE, menuItem.getText()));
				}//@formatter:on
			});
	}

	private void startNewGame() {
		List<Integer> list = new ArrayList<>(WINING_SEQUENCE);
		Collections.shuffle(list);
		for(int i = 0;i < grid.getChildren().size();i++) {
			Button button = (Button) grid.getChildren().get(i);
			String value = String.valueOf(list.get(i));
			button.setText(value);
			button.setVisible(value.equals("-1") == false);
			if(value.equals("-1")) {
				emptyFieldColumn = GridPane.getColumnIndex(button);
				emptyFieldRow = GridPane.getRowIndex(button);
			}
		}
		logFading("New Game Started!!!");
	}

	private void scanButton(Button button) {
		int rowIndex = GridPane.getRowIndex(button);
		int columnIndex = GridPane.getColumnIndex(button);

		//@formatter:off
		if(columnIndex == emptyFieldColumn && rowIndex + 1 <= sideSize - 1 && rowIndex + 1 == emptyFieldRow) swapButtons(columnIndex, rowIndex);
		else if(columnIndex == emptyFieldColumn && rowIndex - 1 >= 0 && rowIndex - 1 == emptyFieldRow) swapButtons(columnIndex, rowIndex);
		else if(rowIndex == emptyFieldRow && columnIndex + 1 <= sideSize - 1 && columnIndex + 1 == emptyFieldColumn) swapButtons(columnIndex, rowIndex);
		else if(rowIndex == emptyFieldRow && columnIndex - 1 >= 0 && columnIndex - 1 == emptyFieldColumn) swapButtons(columnIndex, rowIndex);
		else
			logFading("Can't move that puzzle!!!");
		//@formatter:on
	}

	private void swapButtons(int puzzleColumn, int puzzleRow) {
		Button empty = (Button) grid.getChildren().get(sideSize * emptyFieldRow + emptyFieldColumn);
		Button button = (Button) grid.getChildren().get(sideSize * puzzleRow + puzzleColumn);

		//@formatter:off
		empty.setText(button.getText()); empty.setVisible(true);		
		button.setText("-1"); button.setVisible(false);
		//@formatter:on

		//change internal indexes
		int tmpColumn = emptyFieldColumn;
		int tmpRow = emptyFieldRow;
		emptyFieldColumn = puzzleColumn;
		emptyFieldRow = puzzleRow;
		puzzleColumn = tmpColumn;
		puzzleRow = tmpRow;

		checkForVictory();
	}

	private void checkForVictory() {
		List<Integer> gameArray = grid.getChildren().stream()//
			.map(node -> Integer.valueOf(((Button) node).getText()))//
			.collect(Collectors.toList());

		if(gameArray.equals(WINING_SEQUENCE)) {
			logFading("You Won!!!");
			startNewGame();
		}
	}

	private void initFadingLabel(Label fadingLabel) {
		final KeyValue value1 = new KeyValue(fadingLabel.textFillProperty(), Color.ORANGE);
		final KeyFrame frame1 = new KeyFrame(Duration.ZERO, value1);
		final KeyValue value2 = new KeyValue(fadingLabel.textFillProperty(), Color.rgb(51, 51, 51));
		final KeyFrame frame2 = new KeyFrame(Duration.millis(3500), value2);
		fadingTimeLine = new Timeline(frame1, frame2);
	}

	private void logFading(String text) {
		fadingTimeLine.stop();
		setInfo(text);
		fadingTimeLine.play();
	}
}
