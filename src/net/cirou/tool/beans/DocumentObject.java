package net.cirou.tool.beans;

import java.util.ArrayList;
import java.util.List;

public class DocumentObject {

	private String id;
	private String name;
	private String title;

	private List<RenditionData> renditions = new ArrayList<>();

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

	public List<RenditionData> getRenditions() {
		return renditions;
	}

	public void setRenditions(List<RenditionData> renditions) {
		this.renditions = renditions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
