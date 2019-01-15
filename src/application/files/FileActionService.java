package application.files;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileActionService implements FileActions {
	@Override
	public void clearTxtFile(String fileName) throws IOException{
		Path path = Paths.get(pathToMainFolder+"/"+fileName+".txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
			writer.write("");
		}
	}
	
	@Override
	public void writeTxtFile(String fileName, String input) throws IOException {
		Path path = Paths.get(pathToMainFolder+"/"+fileName+".txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"),StandardOpenOption.APPEND,StandardOpenOption.WRITE)) {
			writer.write(input);
		}
	}
	
	@Override
	public List<String> readTxtFile(String fileName) throws IOException{
		Path path=Paths.get(pathToMainFolder+"/"+fileName+".txt");
		return Files.readAllLines(path);
	}
}
