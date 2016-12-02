package com.wellsfargo.wisp.portablecontent.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.alfresco.consulting.util.folder_hierarchy.FolderHierarchyHelper;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.QueryConsistency;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.extensions.surf.util.AbstractLifecycleBean;

import com.wellsfargo.wisp.portablecontent.PortableContentConstants;

public class PortableContentComponent extends AbstractLifecycleBean {
	private NodeService nodeService;
	private SearchService searchService;
	private NodeRef wispRoot=null;
	private NodeLocatorService nodeLocatorService;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private Pattern tsPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d");
	private FolderHierarchyHelper folderHierarchyHelper;

    private static Log logger = LogFactory.getLog(PortableContentComponent.class);

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.nodeService = serviceRegistry.getNodeService();
		this.searchService = serviceRegistry.getSearchService();
		this.nodeLocatorService = serviceRegistry.getNodeLocatorService();
	}

	public void setFolderHierarchyHelper(FolderHierarchyHelper folderHierarchyHelper) {
		this.folderHierarchyHelper = folderHierarchyHelper;
	}

	public class CannotUpdateContentIdException extends RuntimeException {
		private static final long serialVersionUID = 9095527425674308972L;

		CannotUpdateContentIdException(NodeRef nodeRef, String contentId) {
			super(String.format("Cannot Overwrite Content Id to %s on %s", contentId, nodeRef.toString()));
		}
	}
	
	public class InvalidMetadata extends RuntimeException {
		
		private static final long serialVersionUID = -2141931464527020764L;
		private NodeRef node;

		InvalidMetadata(NodeRef node, String message) {
			super(String.format("Error: %s for node %s", message,node.toString()));
			this.node = node;
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
	
	public NodeRef getWispRoot() {
		if (wispRoot==null) {
			logger.info("Creating: " + PortableContentConstants.WISP_ROOT_NAME);
			wispRoot = AuthenticationUtil.runAsSystem(new RunAsWork<NodeRef>() {
				@Override
				public NodeRef doWork() throws Exception {					
					return folderHierarchyHelper.getFolder(nodeLocatorService.getNode(CompanyHomeNodeLocator.NAME, null, null),PortableContentConstants.WISP_ROOT_NAME,true,true);
				}
			});
		}
		return wispRoot;
	}
	
	protected NodeRef getWispDocumentTypeRoot(String documentType) {
		return folderHierarchyHelper.getFolder(this.getWispRoot(), PortableContentConstants.WISP_PREFIX + documentType, true,true);
	}
	
	protected NodeRef getWispFolder(String documentType,String timestamp) {
		String[] folder_hierachy = { timestamp.substring(0, 4), timestamp.substring(4, 6)};
		return folderHierarchyHelper.getFolder(getWispDocumentTypeRoot(documentType), Arrays.asList(folder_hierachy),true);
	}
	
	public void fileWispDocument(NodeRef nodeRef) {
		String documentName = (String) nodeService.getProperty(nodeRef, PortableContentConstants.PROP_DOCUMENT_NAME);
		String documentType = (String) nodeService.getProperty(nodeRef, PortableContentConstants.PROP_DOCUMENT_TYPE);
		String timestamp = (String) nodeService.getProperty(nodeRef, PortableContentConstants.PROP_TIMESTAMP);
		String name = (String) nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
		if (documentName == null) { documentName = name; }
		if (timestamp == null) { 
			timestamp = dateFormat.format(new Date());
		} else if (!tsPattern.matcher(timestamp).matches()) {
			//Only check if we did not set it explicitly in this method
			throw new InvalidMetadata(nodeRef,String.format("%s is not a valid timestamp", timestamp));		
		}
		if (documentName == null) {
			throw new InvalidMetadata(nodeRef,"documentName cannot be null");
		}
		//TODO check for proper characters
		if (documentType == null) {
			throw new InvalidMetadata(nodeRef,"documentType cannot be null");
		}
		name = documentName + timestamp;
		nodeService.setProperty(nodeRef, ContentModel.PROP_NAME, name);
		nodeService.setProperty(nodeRef, PortableContentConstants.PROP_DOCUMENT_NAME, documentName);
		nodeService.setProperty(nodeRef, PortableContentConstants.PROP_DOCUMENT_TYPE, documentType);
		nodeService.setProperty(nodeRef, PortableContentConstants.PROP_TIMESTAMP, timestamp);
		nodeService.moveNode(nodeRef, getWispFolder(documentType,timestamp), ContentModel.ASSOC_CONTAINS, QName.createQName(PortableContentConstants.WFPC_MODEL_1_0_URI, name));
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
	
	public void makeWispDocument(NodeRef document, String documentType) {
		nodeService.setType(document, PortableContentConstants.TYPE_WISP_CONTENT);
		nodeService.setProperty(document, PortableContentConstants.PROP_DOCUMENT_TYPE, documentType);
	}

	@Override
	protected void onBootstrap(ApplicationEvent arg0) {
		logger.info("Bootstrapping");
		getWispRoot();
	}

	@Override
	protected void onShutdown(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
