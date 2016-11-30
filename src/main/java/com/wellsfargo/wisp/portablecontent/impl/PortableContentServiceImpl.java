package com.wellsfargo.wisp.portablecontent.impl;

import org.alfresco.service.cmr.repository.NodeRef;

import com.wellsfargo.wisp.portablecontent.PortableContentService;

public class PortableContentServiceImpl implements PortableContentService {
	PortableContentComponent portableContentComponent;

	public void setPortableContentComponent(
			PortableContentComponent portableContentComponent) {
		this.portableContentComponent = portableContentComponent;
	}

	@Override
	public String makePortable(NodeRef nodeRef) {
		portableContentComponent.ensureContentId(nodeRef);
		return portableContentComponent.getContentId(nodeRef);
	}

	@Override
	public NodeRef findByContentId(String contentId) {
		return portableContentComponent.getByContentId(contentId);
	}

	@Override
	public String getContentId(NodeRef nodeRef) {
		return portableContentComponent.getContentId(nodeRef);
	}
}
