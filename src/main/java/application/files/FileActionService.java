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
	private static final String TARGET = "target";
	private static final String SAVE_FILE_FILE_PATTERN = "{0}/{1}.txt";
	public final String PATH_TO_MAIN_FOLDER;

	public FileActionService() {
		PATH_TO_MAIN_FOLDER = createExternalResourcesPath();
		Path path = Paths.get(PATH_TO_MAIN_FOLDER);
		if(Files.exists(path) == false) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getPATH_TO_MAIN_FOLDER() {
		return PATH_TO_MAIN_FOLDER;
	}

	private String createExternalResourcesPath() {
		String result = getClass().getResource("").toString();
		if(result.contains(".jar!")) { //from JAR
			result = result.substring("jar:file:/".length());
			result = result.substring(0, result.lastIndexOf('!'));
			return MessageFormat.format("{0}/{1}/", result.substring(0, result.lastIndexOf('/')), FILE_NAME);
		}
		return MessageFormat.format("{0}/{1}/", result.replace("file:/", "").substring(0, result.indexOf(TARGET) - TARGET.length()), FILE_NAME);
	}

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
