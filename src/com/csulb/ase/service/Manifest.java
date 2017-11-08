package com.csulb.ase.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.csulb.ase.model.FileProperties;
/**
 * This class is used for making manifest file(logging). The createManifest method creates the manifest file.
 * @author Rushabh Bagadia(rushabh2208@gmail.com)
 * @author Subramanian Seshadri(subramaniam.seshadri@outlook.com)
 * @author Paras Jagani(parasjagani11@gmail.com)
 * @version 1.0
 */
public class Manifest {
	/**
	 * A new Manifest file is created with date/time attached. This file contains all the files activity by the command.
	 * @param command - The command triggered.
	 * @param sourcePath - Source path of directory.
	 * @param repoPath - The destination path.
	 * @param fileProperties - An ArrayList of all the classes that were copied/affected by the command.
	 * @throws IOException If an I/O error occurs when destination or the source directory does not exist.
	 * @return String
	 */
	public String createManifest(String command, String sourcePath, String repoPath, List<FileProperties> fileProp)
			throws IOException {
		String repositoryTemp=null;
		ArrayList<String> lines = new ArrayList<String>();
		LocalDateTime currentDateTime = LocalDateTime.now();
		String timePath = String.valueOf(currentDateTime.getHour() + "." + currentDateTime.getMinute() + "."
				+ currentDateTime.getSecond() + " hrs");
		String fileName = "Manifest - " + currentDateTime.toLocalDate() + " - " + timePath + ".txt";
		Path manifestPath = FileSystems.getDefault().getPath(repoPath + FileSystems.getDefault().getSeparator()
				+ "Activity_Log" + FileSystems.getDefault().getSeparator() + fileName);
		Charset charset = StandardCharsets.US_ASCII;
		
		//Rushabh's Code Starts
		if (command.equalsIgnoreCase("Check-Out")) {
			repositoryTemp = sourcePath;
			int indexOf= repositoryTemp.indexOf("Activity_Log");
			repositoryTemp = repositoryTemp.substring(0,indexOf-1);
			manifestPath = FileSystems.getDefault().getPath(repositoryTemp + FileSystems.getDefault().getSeparator()
					+ "Activity_Log" + FileSystems.getDefault().getSeparator() + fileName);
			verifyOrCreateActivityDir(repositoryTemp);
		} else {
			verifyOrCreateActivityDir(repoPath);
		}
		//Rushabh's Code Ends
		
		Files.createFile(manifestPath);
		lines.add("XIV");
		lines.add(fileName.split(".txt")[0]);
		lines.add("Command: " + command);
		lines.add("Source Path: "+ sourcePath);
		lines.add("Target Path: "+ repoPath);
		lines.add("Files created: ");
		lines.add("Version\t" + "\t File Name \t" + "\tFile Path");
		if(!fileProp.isEmpty()) {
		fileProp.forEach((newfileProp) -> {
			String line = newfileProp.getArtifactID() + "\t" + newfileProp.getOldFileName() + "\t"
					+ newfileProp.getNewfilePath();
			lines.add(line);
		});
		} else {
			lines.add("No File Created");
		}
		Files.write(manifestPath, lines, charset, StandardOpenOption.APPEND);
		
		return manifestPath.toString();
	}
	
	/**
	 * Creates an "Activity" directory if it is not present at the path that is specified.
	 * @param pathToRepo - The path to verify/create the activity directory at.
	 * @throws IOException  If an I/O error occurs or the source directory does not exist.
	 */
	public void verifyOrCreateActivityDir(String pathToRepo) throws IOException {
		Path path = FileSystems.getDefault()
				.getPath(pathToRepo + FileSystems.getDefault().getSeparator() + "Activity_Log");
		if (!Files.exists(path)) {
			Files.createDirectory(path);
		}

	}
}
