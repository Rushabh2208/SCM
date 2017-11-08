package com.csulb.ase.model;
/**
 * This class is a Object class for File. It captures a single file resource with its artifact ID(checksum.length), filename and absolute path.
 * @author Rushabh Bagadia(rushabh2208@gmail.com)
 * @author Subramaniam Seshadri(subramaniam.seshadri@outlook.com)
 * @author Paras Jagani(parasjagani11@gmail.com)
 * @version 1.0
 */
public class FileProperties {

	private String artifactID = "";
	private String oldFileName = "";
	private String newfilePath = "";

	public FileProperties(String artifactID, String oldFileName, String filePath) {
		this.artifactID = artifactID;
		this.oldFileName = oldFileName;
		this.newfilePath = filePath;
	}

	public String getArtifactID() {
		return artifactID;
	}

	public void setArtifactID(String artifactID) {
		this.artifactID = artifactID;
	}

	public String getOldFileName() {
		return oldFileName;
	}

	public String getNewfilePath() {
		return newfilePath;
	}

	public void setNewfilePath(String newfilePath) {
		this.newfilePath = newfilePath;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	



}
