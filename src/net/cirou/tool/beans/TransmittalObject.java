package net.cirou.tool.beans;

import java.util.ArrayList;

public class TransmittalObject {

	private String id;
	private String name;
	private String title;
	private ArrayList<RenditionData> renditions = new ArrayList<>();
	private ArrayList<DocumentObject> documents = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<RenditionData> getRenditions() {
		return renditions;
	}

	public void setRenditions(ArrayList<RenditionData> renditions) {
		this.renditions = renditions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<DocumentObject> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<DocumentObject> documents) {
		this.documents = documents;
	}

}
