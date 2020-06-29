package net.cirou.tool.beans;

import java.util.List;

public class RenditionData {

	private String name;
	private String dataTicket;
	private String extension;
	private String fileSystemPath;
	private String docbaseId;
	private List<DocumentObject> documents;

	public String getDataTicket() {
		return dataTicket;
	}

	public void setDataTicket(String dataTicket) {
		this.dataTicket = dataTicket;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFileSystemPath() {
		return fileSystemPath;
	}

	public void setFileSystemPath(String fileSystemPath) {
		this.fileSystemPath = fileSystemPath;
	}

	public String getDocbaseId() {
		return docbaseId;
	}

	public void setDocbaseId(String docbaseId) {
		this.docbaseId = docbaseId;
	}

	public List<DocumentObject> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentObject> documents) {
		this.documents = documents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
