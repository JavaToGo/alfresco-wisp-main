package com.wellsfargo.wisp.portablecontent.policy;

import org.alfresco.repo.node.NodeServicePolicies.OnAddAspectPolicy;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wellsfargo.wisp.portablecontent.PortableContentConstants;
import com.wellsfargo.wisp.portablecontent.impl.PortableContentComponent;

public class PortableContentPolicy implements OnAddAspectPolicy {

	private static final Log logger = LogFactory.getLog(PortableContentPolicy.class);
	private PolicyComponent policyComponent;
	private PortableContentComponent portableContentComponent;

	public void initialise() {

		this.policyComponent.bindClassBehaviour(
				OnAddAspectPolicy.QNAME,
				PortableContentConstants.ASPECT_PORTABLE_CONTENT,
				new JavaBehaviour(this, OnAddAspectPolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT));
		logger.debug("INITIALISED");
	}	
	
	@Override
	public void onAddAspect(NodeRef nodeRef, QName aspectTypeQName) {
		portableContentComponent.ensureContentId(nodeRef);
	}

}
