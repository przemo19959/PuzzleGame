package application.files;

import java.io.IOException;
import java.util.List;

public interface FileActions {
	public final static String FILE_NAME = "save";
	public final static String SEPARATOR = ";";

	public void writeTxtFile(String fileName, String input) throws IOException;
	public void clearTxtFile(String fileName) throws IOException;
	public List<String> readTxtFile(String fileName) throws IOException;
}
