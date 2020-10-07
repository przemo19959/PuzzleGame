package application.files;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.List;

public class FileActionService implements FileActions {
	private static final String SAVE_FILE_FILE_PATTERN = "{0}/{1}.txt";

	@Override
	public void clearTxtFile(String fileName) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(getPathToFile(fileName), Charset.forName("UTF-8"))) {
			writer.write("");
		}
	}

	@Override
	public void writeTxtFile(String fileName, String input) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(getPathToFile(fileName), Charset.forName("UTF-8"), StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
			writer.write(input);
		}
	}

	@Override
	public List<String> readTxtFile(String fileName) throws IOException {
		return Files.readAllLines(getPathToFile(fileName));
	}
	
	//@formatter:off
	private Path getPathToFile(String fileName) {return Paths.get(MessageFormat.format(SAVE_FILE_FILE_PATTERN, PATH_TO_MAIN_FOLDER, fileName));}
	//@formatter:on
}
