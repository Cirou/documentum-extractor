package net.cirou.tool.beans;

import java.util.ArrayList;

public class DocumentObject {

	private String id;
	private String name;
	private String title;
	private String docNum;
	private String docType;
	private String firstIssuedDate;
	private String revision;
	private String revisionStatus;
	private String state;
	private String project;
	private String companyProject;
	private String issuingDept;
	private String issuingDesc;
	private String managingDept;
	private String discipline;

	private ArrayList<RenditionData> renditions = new ArrayList<>();

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

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getFirstIssuedDate() {
		return firstIssuedDate;
	}

	public void setFirstIssuedDate(String firstIssuedDate) {
		this.firstIssuedDate = firstIssuedDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getRevisionStatus() {
		return revisionStatus;
	}

	public void setRevisionStatus(String revisionStatus) {
		this.revisionStatus = revisionStatus;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCompanyProject() {
		return companyProject;
	}

	public void setCompanyProject(String companyProject) {
		this.companyProject = companyProject;
	}

	public String getIssuingDept() {
		return issuingDept;
	}

	public void setIssuingDept(String issuingDept) {
		this.issuingDept = issuingDept;
	}

	public String getIssuingDesc() {
		return issuingDesc;
	}

	public void setIssuingDesc(String issuingDesc) {
		this.issuingDesc = issuingDesc;
	}

	public String getManagingDept() {
		return managingDept;
	}

	public void setManagingDept(String managingDept) {
		this.managingDept = managingDept;
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

}
