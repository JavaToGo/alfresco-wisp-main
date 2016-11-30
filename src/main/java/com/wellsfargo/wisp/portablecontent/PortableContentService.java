package com.wellsfargo.wisp.portablecontent;

import org.alfresco.service.cmr.repository.NodeRef;


public interface PortableContentService {
	String makePortable(NodeRef nodeRef);
	NodeRef findByContentId(String contentId);
	String getContentId(NodeRef nodeRef);

}
