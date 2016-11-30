package org.alfresco.wisp;


import java.util.HashMap;
import java.util.Map;


import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionHistory;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;


public class DeleteDocument extends DeclarativeWebScript  
{
	private ServiceRegistry serviceRegistry;

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status httpStatus, Cache cache)  {
		String status="failed";
		String errorMessage="";
		NodeRef noderef=null;
		Map<String, Object> model = new HashMap<String, Object>();
		final String documentID = (String) req.getParameter("documentID");
		final String versionNumber = (String) req.getParameter("versionNumber");
		final String deleteVersion = (String) req.getParameter("deleteVersion");
		
		try {
			if (documentID.contains("workspace://SpacesStore/")) {
				noderef = new NodeRef(documentID);
			}
			NodeService nodeService=serviceRegistry.getNodeService();
		
			if (noderef!=null) {
				if (nodeService.exists(noderef)) {
					if (deleteVersion.equalsIgnoreCase("false")
							&& serviceRegistry.getVersionService()
									.getVersionHistory(noderef) != null) {
						VersionHistory history = serviceRegistry.getVersionService().getVersionHistory(noderef);
						for (Version version : history.getAllVersions()) {
							if (version.getVersionLabel().equals(versionNumber)) {
								serviceRegistry.getVersionService().deleteVersion(noderef, version);
								status = "success";
								break;
							}
						}

					} else if (deleteVersion.equalsIgnoreCase("true")
							&& serviceRegistry.getVersionService()
									.getVersionHistory(noderef) != null) {
						nodeService.deleteNode(noderef);
						status = "success";
					} else {
						errorMessage = "invalid choice";
					}
				}else{
					errorMessage="node not available";
				}
			}else{
				errorMessage="invalid document ID";
			}
			model.put("message", status);
			model.put("errorMessage", errorMessage);			
			
		}
		
		catch (Exception e) {
			errorMessage=e.getMessage();
			model.put("message", status);
			model.put("errorMessage", errorMessage);		
			
		}
		
		
	return model;	
		
}
}
