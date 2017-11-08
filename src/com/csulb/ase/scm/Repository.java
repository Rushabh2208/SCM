package com.csulb.ase.scm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import com.csulb.ase.service.Helper;
import com.csulb.ase.service.Manifest;

/**
 * This is the entry point of the SCM project.
 * 
 * @author Rushabh Bagadia(rushabh2208@gmail.com)
 * @author Subramaniam Seshadri(subramaniam.seshadri@outlook.com)
 * @author Paras Jagani(parasjagani11@gmail.com)
 * @version 1.0
 */

public class Repository {

	@SuppressWarnings("rawtypes")
	public static void main(String args[]) throws IOException {
		File indexProperties = null;
		String manifestFilePath = null;
		Properties prop = new Properties();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		do {
			System.out.println("Enter the Operation you want to perform");
			System.out.println("1. Create \n2. Check-in \n3. Check-Out \n4. Exit");
			int choice = Integer.parseInt(br.readLine());
			String sourceDirStr = null;
			String desDirStr = null;
			Path sourceDir = null;
			Path destDir = null;
			Manifest loggerFile = new Manifest();
			switch (choice) {

			case 1:
				try {

					System.out.println("Enter Source"); // get source path from
														// user
					sourceDirStr = br.readLine();
					sourceDir = Paths.get(sourceDirStr);

					System.out.println("Enter Destination"); // get destination
																// path
																// from user
					desDirStr = br.readLine();
					destDir = Paths.get(desDirStr);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Invalid path");
					System.exit(0);

				}
				try {
					Helper.createFileVersion(sourceDir.toString(), destDir.toString()); // create
																						// repository
					System.out.println("Operation Done.");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Operation Failed!");
				}
				loggerFile.createManifest("Create", sourceDir.toString(), destDir.toString(),
						Helper.filePropertiesList); // create Manifest file
													// containing details for
													// further use

				String fileLabel = destDir.toString() + "\\" + "index.properties";
				Path fileLabelProperties = Paths.get(fileLabel);
				Files.createFile(fileLabelProperties);
				// indexProperties = new File(fileLabelProperties.toString());
				/*
				 * try { setHiddenProperty(indexProperties); } catch
				 * (InterruptedException e1) { // TODO Auto-generated catch
				 * block e1.printStackTrace(); }
				 */
				Helper.filePropertiesList.clear();
				break;

			case 2:

				try {

					System.out.println("Enter Source"); // get source path from
														// user
					sourceDirStr = br.readLine();
					sourceDir = Paths.get(sourceDirStr);

					System.out.println("Enter Destination"); // get destination
																// path
																// from user
					desDirStr = br.readLine();
					destDir = Paths.get(desDirStr);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("No such path");
					System.exit(0);

				}
				try {
					Helper.createFileVersion(sourceDir.toString(), destDir.toString()); // check-in

					System.out.println("Operation Done.");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Operation Failed.");
				}
				manifestFilePath = loggerFile.createManifest("Check-in", sourceDir.toString(), destDir.toString(),
						Helper.filePropertiesList); // create Manifest file
													// containing details for
													// further use
				indexProperties = initializeIndexFile(destDir.toString());
				prop.load(new FileInputStream(indexProperties));
				labelOperation(indexProperties, prop, br, manifestFilePath);

				Helper.filePropertiesList.clear();
				break;

			case 3:
				File indexFile = null;
				String repoDirPath = null;
				try {
					System.out.println("Enter Repository Path : "); // get
																	// source
																	// path from
																	// user

					sourceDirStr = br.readLine();
					repoDirPath = sourceDirStr;
					System.out.println("Do you want to select label? Y/N");
					char userChoice = br.readLine().charAt(0);
					indexFile = initializeIndexFile(sourceDirStr);
					Properties props = new Properties();
					props.load(new FileInputStream(indexFile));
					
					if (userChoice == 'Y' || userChoice == 'y') {
						System.out.println("Enter Label Name");
						String userLabel = br.readLine();
						Enumeration enuKeys = props.keys();
						while (enuKeys.hasMoreElements()) {
							String key = (String) enuKeys.nextElement();
							if (key.equals(userLabel)) {
								sourceDirStr = props.get(userLabel).toString();

							}
						}
					} else {
						System.out.println("\nEnter the name of the Manifest File ");
						String manifestName = br.readLine();
						sourceDirStr += "\\Activity_Log\\" + manifestName;
						sourceDir = Paths.get(sourceDirStr);
					}

					// String repositoryPath = br.readLine();
					sourceDir = Paths.get(sourceDirStr);

					System.out.println("Enter Destination"); // get destination
																// path from
																// user
					desDirStr = br.readLine();
					destDir = Paths.get(desDirStr);

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Helper.checkOutFileVersion(sourceDir.toString(), destDir.toString());
					System.out.println("Operation Done.");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Operation Failed.");
				}
				manifestFilePath = loggerFile.createManifest("Check-Out", sourceDir.toString(), destDir.toString(),
						Helper.filePropertiesList); // create Manifest file
													// containing details for
													// further use
				indexProperties = initializeIndexFile(repoDirPath);
				prop.load(new FileInputStream(indexProperties));
				labelOperation(indexProperties, prop, br, manifestFilePath);
				Helper.filePropertiesList.clear();
				break;
				
			case 4: 
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input");

			}

		} while (true);

	}

	private static File initializeIndexFile(String sourceDirStr) {
		File indexFile = null;

		for (File f : new File(sourceDirStr).listFiles()) {
			if (f.isFile()) {
				if (f.getName().contains("index")) {
					indexFile = f;
				}
			}
		}
		return indexFile;
	}

	/**
	 * This function gives a provision to give a label to a manifest file.
	 * @param indexProperties - gives the properties of the index file where all labels are stored.
	 * @param prop - property to store the value of the label and the label itself.
	 * @param br - BufferedReader object to take input from user.
	 * @param manifestFilePath - path of the manifest file path.
	 */
	public static void labelOperation(File indexProperties, Properties prop, BufferedReader br, String manifestFilePath)
			throws IOException, FileNotFoundException {
		System.out.println("\nDo you want to label this Manifest File? : Y/N ");
		char userChoice = br.readLine().charAt(0);
		if (userChoice == 'Y' || userChoice == 'y') {
			System.out.println("\nEnter Label :");
			String label = br.readLine();
			prop.setProperty(label, manifestFilePath);
			FileOutputStream fileOut = new FileOutputStream(indexProperties);
			prop.store(fileOut, "index file to store labels");
			fileOut.flush();
			fileOut.close();
		}
	}

	/**
	 * This function makes the view property of a file hidden.
	 * @param file - A file that needs to be made hidden.
	 */
	static void setHiddenProperty(File file) throws InterruptedException, IOException {
		Process p = Runtime.getRuntime().exec("attrib +H " + file.getPath());
		p.waitFor();
	}
}