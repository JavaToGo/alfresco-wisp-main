package com.wellsfargo.wisp.portablecontent;

import org.alfresco.service.namespace.QName;

public interface PortableContentConstants {
	public static final String WFPC_MODEL_PREFIX = "wfpc";
	public static final String WFPC_MODEL_1_0_URI = "http://www.wellsfargo.com/portable/content/1.0";

	public static final QName ASPECT_PORTABLE_CONTENT = QName.createQName(WFPC_MODEL_1_0_URI, "portableContent");
	public static final QName PROP_CONTENT_ID = QName.createQName(WFPC_MODEL_1_0_URI, "contentId");
}
