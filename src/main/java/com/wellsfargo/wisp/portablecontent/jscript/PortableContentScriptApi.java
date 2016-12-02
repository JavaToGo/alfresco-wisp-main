package com.wellsfargo.wisp.portablecontent.jscript;

import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.repo.jscript.ScriptNode;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;

import com.wellsfargo.wisp.portablecontent.PortableContentService;

public class PortableContentScriptApi extends BaseScopableProcessorExtension {
	PortableContentService portableContentService;
	ServiceRegistry serviceRegistry;

	public void setPortableContentService(
			PortableContentService portableContentService) {
		this.portableContentService = portableContentService;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public String makePortable(ScriptNode node) {
		return portableContentService.makePortable(node.getNodeRef());
	}

	public ScriptNode findByContentId(String contentId) {
		return new ScriptNode(portableContentService.findByContentId(contentId),this.serviceRegistry,this.getScope());
	}

	public String getContentId(ScriptNode node) {
		return portableContentService.getContentId(node.getNodeRef());
	}
	
	public void makeWispDocument(ScriptNode document, String documentType) {
		portableContentService.makeWispDocument(document.getNodeRef(), documentType);
	}


}
