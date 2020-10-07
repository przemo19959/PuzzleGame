package application.files;

import java.io.IOException;
import java.util.List;

public interface FileActions {
	//TODO - 26 lip 2020:tutaj trzeba poprawić, bo w wersji JAR już nie będzie działać
	public final static String PATH_TO_MAIN_FOLDER = "D:/java-workspace/PuzzleProject/save";
	public final static String FILE_NAME = "save";
	public final static String SEPARATOR = ";";

	public void writeTxtFile(String fileName, String input) throws IOException;
	public void clearTxtFile(String fileName) throws IOException;
	public List<String> readTxtFile(String fileName) throws IOException;
}
