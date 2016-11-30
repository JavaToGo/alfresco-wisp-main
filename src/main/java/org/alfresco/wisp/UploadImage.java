package org.alfresco.wisp;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.coci.CheckOutCheckInService;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionHistory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class UploadImage extends DeclarativeWebScript {

	private ServiceRegistry serviceRegistry;
	private ContentService contentService;
	private CheckOutCheckInService checkOutCheckInService;
	
	

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	
	public ContentService getContentService() {
		return contentService;
	}

	public CheckOutCheckInService getCheckOutCheckInService() {
		return checkOutCheckInService;
	}	

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}
	
	public void setCheckOutCheckInService(CheckOutCheckInService checkOutCheckInService) {
		this.checkOutCheckInService = checkOutCheckInService;
	}	

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	protected Map<String, Object> executeImpl(WebScriptRequest req, Status httpStatus, Cache cache) {
		String status="Fail";
		String errorMessage="";
		String versionLabel="";
		NodeRef noderef=null;
		boolean versionFlag = false;
		//NodeRef noderefNew=null;
		Map<String, Object> model = new HashMap<String, Object>();
		final String documentID = (String) req.getParameter("documentID");
		final String versionNumber = (String) req.getParameter("versionNumber");
		

		if (documentID.contains("workspace://SpacesStore/")) {
			noderef = new NodeRef(documentID);
		}
		NodeService nodeService=serviceRegistry.getNodeService();

		if (noderef!=null) {
			if (nodeService.exists(noderef)) {
				VersionHistory versionHistory = serviceRegistry.getVersionService().getVersionHistory(noderef);				
				if(null!= versionHistory){
					Iterator<Version> versionIterator = versionHistory.getAllVersions().iterator();
					Version version;
					while(versionIterator.hasNext()){
						version = versionIterator.next();
						if(version.getVersionLabel().equals(versionNumber)) {
							NodeRef node = version.getFrozenStateNodeRef();
							ContentReader reader = contentService.getReader(node,ContentModel.PROP_CONTENT);
							InputStream originalInputStream = reader.getContentInputStream();
							if(!checkOutCheckInService.isCheckedOut(noderef))
							{
								NodeRef checkedOutCopy = checkOutCheckInService.checkout(noderef);
	
								ContentWriter writer = contentService.getWriter(checkedOutCopy, ContentModel.PROP_CONTENT, true);
								writer.putContent(originalInputStream);
								//Map<QName, Serializable> props = nodeService.getProperties(noderef);
								//Map<String, Serializable> propertyList = new HashMap<String, Serializable> ();
								//propertyList.put("name", nodeService.getProperty(noderefNew, ContentModel.PROP_NAME));
								checkOutCheckInService.checkin(checkedOutCopy,null);							
								status="success";
								versionFlag = true;
							}
							else
							{
								errorMessage="Document is already checkedout";
							}
							   // Fetch a few common properties
							//versionLabel =(String) nodeService.getProperty(checkin, ContentModel.PROP_VERSION_LABEL);
							//break;

						}
						
					}
					if (!versionFlag)
					{
						errorMessage="Version is not avaiable";
					}
				}
				
				else{
					errorMessage="Version history not avaiable";
				}
			}
			else{
				errorMessage="node not available";
			}
		}
		else{
			errorMessage="invalid document ID";
		}
		//noderefNew=new NodeRef(documentID); 
		
		model.put("message",status);
		model.put("version",versionLabel);
		model.put("errorMessage", errorMessage);
		
		return model;

	}
}
