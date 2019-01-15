package application.files;

import java.io.IOException;
import java.util.List;

public interface FileActions {
	public final static String pathToMainFolder="D:/java-workspace/PuzzleProject/save"; //�cie�ka do folderu glownego
	public final static String fileName="save";
	public final static String separator=";";
	
	public void writeTxtFile(String fileName, String input) throws IOException;
	public void clearTxtFile(String fileName) throws IOException;
	public List<String> readTxtFile(String fileName) throws IOException;
}
