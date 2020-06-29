package net.cirou.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.DfServiceException;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;

import net.cirou.tool.beans.DocumentObject;
import net.cirou.tool.beans.RenditionData;

public class DocumentExtractor {

	IDfSysObject sysObject = null;
	IDfSession idfSession = null;
	IDfSessionManager sessMgr = null;

	StringBuilder objectids = new StringBuilder("");

	ArrayList<DocumentObject> objectArray = new ArrayList<>();
	String objectNamesString = "";
	String objectIdsString = "";

	static Properties prop = new Properties();

	static String inputFile = "";
	static String outputPath = "";
	static String docIdQuery = "";
	static String docFileQuery = "";

	static String username = "";
	static String password = "";
	static String docbase = "";

	private static Logger logger = Logger.getLogger(DocumentExtractor.class);
	private static String renditionQuery;
	private static String docbaseId;

	public static void main(String[] args) throws Exception {

		loadProperties();

		DocumentExtractor object = new DocumentExtractor(username, password, docbase);

		try {

			object.readDocumentIds();
			object.extractDocumentDataFromDocumentIds();
			object.extractRenditionsDataFromDocumentIds();
			object.extractRenditions();

		} finally {
			object.releaseSession();
		}
	}

	public static void loadProperties() {

		try (InputStream input = new FileInputStream(".\\conf\\extractor.properties")) {
			prop.load(input);

			if (prop.get("extractor.file.output.path") != null) {
				outputPath = (String) prop.get("extractor.file.output.path");
			} else {
				logger.error("Missing \"extractor.file.output.path\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.file.input") != null) {
				inputFile = (String) prop.get("extractor.file.input");
			} else {
				logger.error("Missing \"extractor.file.input\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.document.id.query") != null) {
				docIdQuery = (String) prop.get("extractor.document.id.query");
			} else {
				logger.error("Missing \"extractor.document.id.query\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.docbase.username") != null) {
				username = (String) prop.get("extractor.docbase.username");
			} else {
				logger.error("Missing \"extractor.docbase.username\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.docbase.password") != null) {
				password = (String) prop.get("extractor.docbase.password");
			} else {
				logger.error("Missing \"extractor.docbase.password\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.docbase.name") != null) {
				docbase = (String) prop.get("extractor.docbase.name");
			} else {
				logger.error("Missing \"extractor.docbase.name\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.document.file.query") != null) {
				docFileQuery = (String) prop.get("extractor.document.file.query");
			} else {
				logger.error("Missing \"extractor.document.file.query\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.document.doc.rendition.data.query") != null) {
				renditionQuery = (String) prop.get("extractor.document.doc.rendition.data.query");
			} else {
				logger.error("Missing \"extractor.document.doc.rendition.data.query\" property");
				throw new InternalError();
			}

			if (prop.get("extractor.docbase.id") != null) {
				docbaseId = (String) prop.get("extractor.docbase.id");
			} else {
				logger.error("Missing \"extractor.docbase.id\" property");
				throw new InternalError();
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	public DocumentExtractor(String user, String passwd, String docbase) throws DfServiceException {
		getDfSession(user, passwd, docbase);
	}

	public IDfSession getDfSession(String args1, String args2, String args3) throws DfServiceException {

		IDfLoginInfo login = new DfLoginInfo();
		login.setUser(args1);
		login.setPassword(args2);
		IDfClient client = new DfClient();
		sessMgr = client.newSessionManager();
		sessMgr.setIdentity(args3, login);
		idfSession = sessMgr.getSession(args3);

		if (idfSession != null)
			logger.info("Session created successfully");

		return idfSession;
	}

	public void releaseSession() {
		sessMgr.release(idfSession);
	}

	public void readDocumentIds() throws IOException {

		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
			String line = reader.readLine();
			while (line != null) {
				DocumentObject doc = new DocumentObject();
				doc.setId(line);
				objectArray.add(doc);
				line = reader.readLine();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	public void extractDocumentDataFromDocumentIds() throws DfException {

		logger.info("extractDocumentDataFromDocumentIds");

		for (DocumentObject doc : objectArray) {
			IDfId id = new DfId(doc.getId());
			IDfSysObject docObj = (IDfSysObject) idfSession.getObject(id);
			
			doc.setName(docObj.getObjectName().trim().replaceAll("[\\\\/:*?\"<>|]", " "));
			doc.setTitle(docObj.getTitle());
			doc.setDocNum(docObj.getString("spm_doc_num"));
			doc.setDocType(docObj.getString("ecs_doc_type_name"));
			doc.setFirstIssuedDate(docObj.getString("spm_first_issued_date"));
			doc.setRevision(docObj.getString("spm_rev_code"));
			doc.setRevisionStatus(docObj.getString("spm_rev_status"));
			doc.setState(docObj.getString("ecs_lc_state"));
			doc.setProject(docObj.getString("spm_project"));
			doc.setCompanyProject(docObj.getString("spm_company_project"));
			doc.setIssuingDept(docObj.getString("spm_issuing_dept"));
			doc.setIssuingDesc(docObj.getString("spm_issuing_desc"));
			doc.setManagingDept(docObj.getString("spm_managing_dept"));
			doc.setDiscipline(docObj.getString("spm_discipline"));

		}

	}

	public void extractRenditionsDataFromDocumentIds() throws DfException {

		for (DocumentObject doc : objectArray) {

			String dql = String.format(renditionQuery, doc.getId());

			IDfQuery objDfXmlQuery = new DfQuery();
			objDfXmlQuery.setDQL(dql);

			IDfCollection row = objDfXmlQuery.execute(idfSession, DfQuery.READ_QUERY);

			while (row.next()) {
				RenditionData docRend = new RenditionData();
				docRend.setDataTicket(row.getValueAt(0).toString());
				docRend.setExtension(row.getValueAt(1).toString());
				docRend.setFileSystemPath(row.getValueAt(2).toString());
				docRend.setDocbaseId(docbaseId);
				doc.getRenditions().add(docRend);
			}

		}
	}

	public void extractRenditions() {

		logger.info("Number of document renditions to be exported: " + objectArray.size());

		int expDoc = 0;
		int expRend = 0;

		try {

			String timestamp = new SimpleDateFormat("'\\documents\\'yyyyMMddHHmm'\\'").format(new Date());
			new File(outputPath + timestamp).mkdirs();

			String docReport = "";

			for (DocumentObject doc : objectArray) {

				docReport += 
						
						doc.getId() + ";" + 
						doc.getName() + ";" + 
						doc.getTitle() + ";" + 
						doc.getDocNum() + ";" +
						doc.getDocType() + ";" + 
						doc.getProject() + ";" + 
						doc.getState() + ";" +
						doc.getRevision() + ";" + 
						doc.getRevisionStatus() + ";" + 
						
						doc.getFirstIssuedDate() + ";"
				
						;

				ArrayList<RenditionData> rendList = doc.getRenditions();

				for (RenditionData rendition : rendList) {

					String finalPath = getFilePathOnDocbase(rendition);

					String filePath = rendition.getFileSystemPath() + "\\" + rendition.getDocbaseId() + "\\" + finalPath
							+ "." + rendition.getExtension();

					File afile = new File(filePath);
					File bfile = new File(
							outputPath + timestamp + "\\" + doc.getName() + "." + rendition.getExtension());

					bfile = checkIfFileExists(bfile, timestamp, doc.getName(), rendition.getExtension(), 1);

					writeDocFile(afile, bfile);

					expRend++;
					docReport += doc.getName() + "." + rendition.getExtension() + ";";

					System.out.println(doc.getName() + " copied successfully");

				}

				expDoc++;
				docReport += "\n";

			}

			writeReportFile(timestamp, docReport);

		} catch (Exception dfe) {
			logger.error(dfe.getMessage());
		}

		logger.info("--------------- EXPORT RESULTS ---------------");
		logger.info("Total DOC Exported\t\t\t:" + expDoc);
		logger.info("Total REN Exported\t\t\t:" + expRend);
		logger.info("---------------------------------------");

	}

	private String getFilePathOnDocbase(RenditionData rendition) {
		long dataTicket = Integer.parseInt(rendition.getDataTicket()) + 4294967296L;
		String dataTicketPath = Long.toHexString(dataTicket);
		String[] path = dataTicketPath.split("(?<=\\G.{2})");
		String finalPath = String.join("\\", path);
		return finalPath;
	}

	private void writeDocFile(File afile, File bfile) throws FileNotFoundException, IOException {
		InputStream inStream = null;
		OutputStream outStream = null;

		inStream = new FileInputStream(afile);
		outStream = new FileOutputStream(bfile);

		byte[] buffer = new byte[1024];

		int length;
		while ((length = inStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, length);
		}

		inStream.close();
		outStream.close();
	}

	private void writeReportFile(String timestamp, String docReport) throws IOException {
		File reportFile = new File(outputPath + timestamp + "\\extraction_report.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile));
		writer.write(docReport);
		writer.close();
	}

	private File checkIfFileExists(File bfile, String timestamp, String name, String extension, int i) {
		if (bfile.exists()) {
			bfile = new File(outputPath + timestamp + "\\" + name + "_" + i + "." + extension);
			checkIfFileExists(bfile, timestamp, name, extension, i + 1);
		} else {
			return bfile;
		}

		return bfile;

	}

}
