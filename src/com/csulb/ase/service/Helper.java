package com.csulb.ase.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.csulb.ase.model.FileProperties;
/**
 * This class is a Helper file to create the Versions of the file, Artifact ID, Extension of the file and checks whether the file already exists.
 * @author Rushabh Bagadia(rushabh2208@gmail.com)
 * @author Subramaniam Seshadri(subramaniam.seshadri@outlook.com)
 * @author Paras Jagani(parasjagani11@gmail.com)
 * @version 1.0
 */

public class Helper {

	public static List<FileProperties> filePropertiesList = new ArrayList<FileProperties>();

	/**
	 * Calculates the Artifact ID of a file.
	 * @param file - The file whose artifact id is to be created
	 * @return ArtifactID
	 */
	public static File createArtifactID(File sourceFile) throws IOException {

		Scanner s = null;
		char[] myChar = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader(sourceFile)));
			String str = "";
			while (s.hasNext()) {
				str += s.nextLine();
			}
			myChar = str.toCharArray();
		} finally {
			if (s != null) {
				s.close();
			}
		}
		int fileVersion = 0;

		for (int i = 0; i < myChar.length; i++) {
			switch (i % 5) {
			case 0:
				fileVersion += (int) myChar[i] * 1;
				break;
			case 1:
				fileVersion += (int) myChar[i] * 3;
				break;

			case 2:
				fileVersion += (int) myChar[i] * 7;
				break;

			case 3:
				fileVersion += (int) myChar[i] * 11;
				break;
			case 4:
				fileVersion += (int) myChar[i] * 17;
				break;
			default:
				break;
			}
		}

		String fileName = fileVersion + "." + myChar.length;
		File targetFile = new File(fileName);

		return targetFile;
	}

	/**
	 * Returns the extension of a file.
	 * @param file - The name of the file that needs to copy.
	 * @return extension of the file.
	 */

	private static String getExtension(String fileName) {
		String extension = null;
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}

	/**
	 * Checks If the file exists in the repository.
	 * @param file - Source path and Target path.
	 * @return boolean
	 */
	public static boolean checkIfFileExists(File sourceFile, File targetDir) {

		File oldFile = sourceFile;
		File[] fileList = targetDir.listFiles();
		try {
			oldFile = createArtifactID(sourceFile);
		} catch (IOException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < fileList.length; i++) {

			if (fileList[i].getName().contains(oldFile.getName()))
				return false;
			else
				continue;

		}
		return true;

	}

	/**
	 * Create new File versions if changes are made to the file.
	 * @param file- Source path and Target Path.
	 */
	public static void createFileVersion(String sourcePath, String targetPath) throws IOException {
		
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}

		for (File f : new File(sourcePath).listFiles()) {
			String append = "/" + f.getName();
			if (f.isDirectory()) {
				if (new File(targetPath + append).mkdir()) {
					System.out.println("Creating Directory '" + targetPath + append + "': " + "success");
				} else {
					System.out.println("Creating Directory '" + targetPath + append + "': " + "failed");
				}
				createFileVersion(sourcePath + append, targetPath + append);
			} else if (f.isFile()) {
				File newFile = new File(targetPath + append);
				if (newFile.mkdir()) {
					System.out.println("Copying File'" + targetPath + append + "': " + "success");
				} else {
					System.out.println("Copying File'" + targetPath + append + "': " + "failed");
				}
				File oldFile = f;
				try {
					if (checkIfFileExists(oldFile, newFile)) {

						FileUtils.copyFileToDirectory(oldFile, newFile);
					} 
					File oldName = new File(newFile + "\\" + oldFile.getName());
					File newName = new File(
							newFile + "\\" + createArtifactID(oldFile) + "." + getExtension(oldFile.getName()));
					oldName.renameTo(newName);
					FileProperties fileProp = new FileProperties(newName.getName(), oldFile.getName(),
							newName.getAbsolutePath());
					filePropertiesList.add(fileProp);	
					
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

		}

	}
	
	/**
	 * Checks Out the whole file structure of repository to the target path.
	 * @param manifestFilePath - path of the selected manifest file
	 * @targetPath - path of the target where we need the checkout
	 */
	public static void checkOutFileVersion(String manifestFilePath,String targetPath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(manifestFilePath));
		int lines = 0;
		while (br.readLine() != null) lines++;
		//br.close();
		 List<String> fileList = new ArrayList<String>();
		for(int i=0;i<lines ; i++){
			if(i>6){
			String line1 = Files.readAllLines(Paths.get(manifestFilePath)).get(i);
			fileList.add(line1);
			
		}}
		String checkOutSourceRoot = Files.readAllLines(Paths.get(manifestFilePath)).get(4);
		String[] splitCheckOutSourceRoot = checkOutSourceRoot.split("\\s+");
		checkOutSourceRoot =  splitCheckOutSourceRoot[2];
		//System.out.println(fileList);
		
		for(String e : fileList){
			String[] splitStr = e.split("\\s+");
			String originalFileName = splitStr[1];
			String sourceFilePath = splitStr[2];
			String artifactId = splitStr[0];
			String	rootFilePath = splitStr[2].substring(checkOutSourceRoot.length());
			//System.out.println(sourceFilePath);
			System.out.println(rootFilePath);
			String destPath = targetPath + "\\" + rootFilePath;
			destPath = destPath.replace("\\"+ originalFileName, "");
			destPath = destPath.replace("\\"+artifactId, "");
			//destPath = destPath.replace(originalFileName,"");
			System.out.println("Destination Path : "+destPath);
			Path destDir =Paths.get(destPath);
			CopyFile(sourceFilePath,destDir.toString(),originalFileName);
			
		}
	}
	
	/**
	 * Copying files from repository to the target path.
	 * @param sourcePath - path of the repository.
	 * @param targetPath - path of the target path.
	 * @param originalFileName - file to copy from repository to target.
	 */	
	public static void CopyFile(String sourcePath,String targetPath,String originalFileName){
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}
		File sourceFile = new File(sourcePath);
		try {
			FileUtils.copyFileToDirectory(sourceFile, targetFile);
			File oldName = new File(targetFile + "\\"+sourceFile.getName());
			File newName = new File(targetFile+ "\\"+originalFileName);
			oldName.renameTo(newName);
			FileProperties fileProp = new FileProperties(oldName.getName(),newName.getName() ,
					newName.getAbsolutePath());
			filePropertiesList.add(fileProp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
