package com.wellsfargo.wisp.portablecontent;

import org.alfresco.service.namespace.QName;

public interface PortableContentConstants {
	public static final String WFPC_MODEL_PREFIX = "wfpc";
	public static final String WFPC_MODEL_1_0_URI = "http://www.wellsfargo.com/portable/content/1.0";

	public static final QName ASPECT_PORTABLE_CONTENT = QName.createQName(WFPC_MODEL_1_0_URI, "portableContent");
	public static final QName PROP_CONTENT_ID = QName.createQName(WFPC_MODEL_1_0_URI, "contentId");
	
	public static final QName TYPE_WISP_CONTENT = QName.createQName(WFPC_MODEL_1_0_URI, "wispContent");
	public static final QName PROP_DOCUMENT_NAME = QName.createQName(WFPC_MODEL_1_0_URI, "documentName");
	public static final QName PROP_DOCUMENT_TYPE = QName.createQName(WFPC_MODEL_1_0_URI, "documentType");
	public static final QName PROP_TIMESTAMP = QName.createQName(WFPC_MODEL_1_0_URI, "timestamp");
	
	public static final String WISP_ROOT_NAME = "WISP";
	public static final String WISP_PREFIX = "WISP_";
}
