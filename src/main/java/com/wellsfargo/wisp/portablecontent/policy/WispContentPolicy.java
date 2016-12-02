package com.wellsfargo.wisp.portablecontent.policy;

import org.alfresco.repo.node.NodeServicePolicies.OnCreateNodePolicy;
import org.alfresco.repo.node.NodeServicePolicies.OnSetNodeTypePolicy;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wellsfargo.wisp.portablecontent.PortableContentConstants;
import com.wellsfargo.wisp.portablecontent.impl.PortableContentComponent;

public class WispContentPolicy implements OnCreateNodePolicy, OnSetNodeTypePolicy {

	private static final Log logger = LogFactory.getLog(WispContentPolicy.class);
	private PolicyComponent policyComponent;
	private PortableContentComponent portableContentComponent;

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}

	public void setPortableContentComponent(
			PortableContentComponent portableContentComponent) {
		this.portableContentComponent = portableContentComponent;
	}

	public void initialise() {

		this.policyComponent.bindClassBehaviour(
				OnCreateNodePolicy.QNAME,
				PortableContentConstants.TYPE_WISP_CONTENT,
				new JavaBehaviour(this, OnCreateNodePolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT));
		this.policyComponent.bindClassBehaviour(
				OnSetNodeTypePolicy.QNAME,
				PortableContentConstants.TYPE_WISP_CONTENT,
				new JavaBehaviour(this, OnSetNodeTypePolicy.QNAME.getLocalName(), NotificationFrequency.TRANSACTION_COMMIT));
		logger.debug("INITIALISED");
	}	
	

	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		portableContentComponent.fileWispDocument(childAssocRef.getChildRef());
	}

	@Override
	public void onSetNodeType(NodeRef nodeRef, QName oldType, QName newType) {
		portableContentComponent.fileWispDocument(nodeRef);
		
	}

}
