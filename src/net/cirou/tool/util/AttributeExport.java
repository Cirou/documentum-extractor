package net.cirou.tool.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfTime;

public class AttributeExport {

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	private static final String TAG_ATTRIBUTES = "attributes";
	private static final String TAG_ATTRIBUTE = "attribute";
	private static final String TAG_VALUE = "value";
	private static final String TAG_TRANSMITTAL = "transmittal";

	private static final String ATTR_NAME = "name";
	private static final String ATTR_POS = "pos";

	private static final String BLANK = "";
	
	public AttributeExport() {
		super();
	}

	public static Element exportTransmittalAttributes(Document export, IDfSysObject transmittal) throws DfException {
		Element transmittalElement = createElement(export, TAG_TRANSMITTAL);
		appendAttributeData(export, transmittalElement, transmittal);
		export.appendChild(transmittalElement);
		return transmittalElement;
	}

	private static void appendAttributeData(Document export, Element elObject, IDfTypedObject contextObject)
			throws DfException {
		Element elAttributes = createElement(export, TAG_ATTRIBUTES);
		appendAttributeValues(export, elAttributes, contextObject);
		elObject.appendChild(elAttributes);
	}

	private static void appendAttributeValues(Document export, Element elAttributes, IDfTypedObject contextObject)
			throws DfException {

		for (int i = 0; i < contextObject.getAttrCount(); i++) {
			IDfAttr att = contextObject.getAttr(i);
			String attName = att.getName();
			if (contextObject.isAttrRepeating(attName)) {
				appendRepeatingAttributeValues(export, elAttributes, contextObject, attName);
			} else {
				appendSingleAttributeValues(export, elAttributes, contextObject, attName, null);
			}
		}

	}

	private static void appendSingleAttributeValues(Document export, Element elAttributes, IDfTypedObject contextObject,
			String attributeName, String attributeValue) throws DfException {

		attributeValue = attributeValue != null ? attributeValue
				: getSingleAttributeValue(contextObject, attributeName);

		Element elAttribute = createElement(export, TAG_ATTRIBUTE);
		elAttribute.setAttribute(ATTR_NAME, attributeName);
		elAttribute.appendChild(createValueElement(export, attributeValue));
		elAttributes.appendChild(elAttribute);

	}

	private static void appendRepeatingAttributeValues(Document export, Element elAttributes,
			IDfTypedObject contextObject, String attributeName) throws DfException {
		List<String> attrValues = getAllRepeatingAttributeValues(contextObject, attributeName);

		Element elAttribute = createElement(export, TAG_ATTRIBUTE);
		elAttribute.setAttribute(ATTR_NAME, attributeName);
		for (int i = 0, j = attrValues.size(); i < j; i++) {
			String attrValue = attrValues.get(i);
			elAttribute.appendChild(createValueElement(export, attrValue, i));
		}
		elAttributes.appendChild(elAttribute);

	}

	private static Element createValueElement(Document export, String attrValue) {
		Element ret = createElement(export, TAG_VALUE);
		ret.appendChild(createTextElement(export, attrValue));
		return ret;
	}

	private static Element createElement(Document export, String tagName) {
		return export.createElement(tagName);
	}

	private static Text createTextElement(Document export, String textValue) {
		return export.createTextNode(textValue);
	}

	private static Element createValueElement(Document export, String attrValue, int i) {
		Element ret = createValueElement(export, attrValue);
		ret.setAttribute(ATTR_POS, String.valueOf(i));
		return ret;
	}

	public static String getSingleAttributeValue(IDfTypedObject persObj, String attrName) throws DfException {
		String ret = BLANK;
		int attrType = persObj.getAttrDataType(attrName);
		switch (attrType) {
		case IDfType.DF_TIME:
			IDfTime value = persObj.getTime(attrName);
			if (!value.isNullDate()) {
				ret = parseDateValue(value);
			}
			break;
		case IDfType.DF_BOOLEAN:
			ret = String.valueOf(persObj.getBoolean(attrName));
			break;
		case IDfType.DF_INTEGER:
			ret = String.valueOf(persObj.getInt(attrName));
			break;
		case IDfType.DF_DOUBLE:
			ret = String.valueOf(persObj.getDouble(attrName));
			break;
		default:
			ret = persObj.getString(attrName);
			break;
		}
		return ret;
	}

	protected static String parseDateValue(IDfTime value) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String ret = sdf.format(value.getDate());
		StringBuilder sb = new StringBuilder(ret);
		sb.insert(22, ':');
		return sb.toString();
	}

	public static List<String> getAllRepeatingAttributeValues(IDfTypedObject persObj, String attrName)
			throws DfException {
		ArrayList<String> ret = new ArrayList<>();
		int attrType = persObj.getAttrDataType(attrName);
		for (int i = 0, j = persObj.getValueCount(attrName); i < j; i++) {
			String attrValue = BLANK;
			switch (attrType) {
			case IDfType.DF_TIME:
				IDfTime value = persObj.getRepeatingTime(attrName, i);
				if (!value.isNullDate()) {
					attrValue = parseDateValue(value);
				}
				break;
			case IDfType.DF_BOOLEAN:
				attrValue = String.valueOf(persObj.getRepeatingBoolean(attrName, i));
				break;
			case IDfType.DF_INTEGER:
				attrValue = String.valueOf(persObj.getRepeatingInt(attrName, i));
				break;
			case IDfType.DF_DOUBLE:
				attrValue = String.valueOf(persObj.getRepeatingDouble(attrName, i));
				break;
			default:
				attrValue = persObj.getRepeatingString(attrName, i);
				break;
			}
			ret.add(attrValue);
		}
		return ret;
	}

}
