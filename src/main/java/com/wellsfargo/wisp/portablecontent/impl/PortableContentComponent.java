package com.wellsfargo.wisp.portablecontent.impl;

import java.io.Serializable;
import java.util.List;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.QueryConsistency;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;

import com.wellsfargo.wisp.portablecontent.PortableContentConstants;

public class PortableContentComponent {
	private NodeService nodeService;
	private SearchService searchService;

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.nodeService = serviceRegistry.getNodeService();
		this.searchService = serviceRegistry.getSearchService();
	}

	public class CannotUpdateContentIdException extends RuntimeException {
		private static final long serialVersionUID = 9095527425674308972L;

		CannotUpdateContentIdException(NodeRef nodeRef, String contentId) {
			super(String.format("Cannot Overwrite Content Id to %s on %s", contentId, nodeRef.toString()));
		}
	}
	
	public class CannotFindByContentId extends RuntimeException {
		private static final long serialVersionUID = 8520517667365439937L;

		CannotFindByContentId(String contentId) {
			super(String.format("No Object with Content Id %s found", contentId));
		}
		
	}
	public class TooManyFoundByContentId extends RuntimeException {

		private static final long serialVersionUID = -8450302136302170706L;
		List<NodeRef> nodes;
		TooManyFoundByContentId(String contentId,List<NodeRef> nodes) {
			super(String.format("Too Many Objects (%d) found with Content Id %s", nodes.size(),contentId));
			this.nodes = nodes;
		}
		
	}
	public class NoContentIdFound extends RuntimeException {
		private static final long serialVersionUID = -8924653398248622118L;
		
		NoContentIdFound(NodeRef nodeRef) {
			super(String.format("Node %s does not have a content Id", nodeRef.toString()));
		}
		
	}
	private void setContentIdInternal(NodeRef nodeRef, String contentId) {
		nodeService.setProperty(nodeRef, PortableContentConstants.PROP_CONTENT_ID, nodeRef.toString());		
	}
	
	public NodeRef getByContentId(String contentId) {

		SearchParameters sp = new SearchParameters();
		sp.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
		sp.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
		sp.setQueryConsistency(QueryConsistency.TRANSACTIONAL);
		sp.setQuery(String.format("=%s:'%s'",PortableContentConstants.PROP_CONTENT_ID.toString(),contentId ));
		
		ResultSet rs = searchService.query(sp);
		
		if (rs.getNodeRefs().isEmpty()) {
			throw new CannotFindByContentId(contentId);
		}
		
		if (rs.getNodeRefs().size() > 1) {
			throw new TooManyFoundByContentId(contentId,rs.getNodeRefs());
		}
		
		return rs.getNodeRef(0);
	}
	
	public String getContentId(NodeRef nodeRef) {
		String contentId = (String) nodeService.getProperty(nodeRef, PortableContentConstants.PROP_CONTENT_ID);
		if (contentId == null) {
			throw new NoContentIdFound(nodeRef);
		}
		return contentId;
	}
	
	public void setContentId(NodeRef nodeRef, String contentId) {
		Serializable oldValue = nodeService.getProperty(nodeRef, PortableContentConstants.PROP_CONTENT_ID);
		if (null != null && !oldValue.equals(contentId)) {
			throw new CannotUpdateContentIdException(nodeRef,contentId);
		}
		setContentIdInternal(nodeRef, contentId);		
	}
	public void ensureContentId(NodeRef nodeRef) {
		String contentId = (String) nodeService.getProperty(nodeRef, PortableContentConstants.PROP_CONTENT_ID);
		if (contentId == null) {
			setContentIdInternal(nodeRef, nodeRef.toString());
		}
		
	}
}
