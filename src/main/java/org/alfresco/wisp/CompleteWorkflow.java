/**
 * 
 */
package org.alfresco.wisp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowTask;
import org.alfresco.service.cmr.workflow.WorkflowTaskQuery;
import org.alfresco.service.cmr.workflow.WorkflowTaskState;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * @author u312395
 * 
 */
public class CompleteWorkflow extends DeclarativeWebScript {

	private ServiceRegistry serviceRegistry;

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	protected Map<String, Object> executeImpl(WebScriptRequest req,
			Status httpStatus, Cache cache) {

		String errorMessage = "";
		String taskInstance="";
		String finalStatus="";
		String decision="";		
		boolean sameSession=true;
		Map<String, Object> model = new HashMap<String, Object>();
		String taskID = (String) req.getParameter("taskID");
		String output = (String) req.getParameter("output");
		taskID=taskID.replace("start", "");

		if (null!= taskID) {
			if (output.equalsIgnoreCase("approved")
					|| output.equalsIgnoreCase("rejected")) {			
				
				WorkflowService workflowService = serviceRegistry.getWorkflowService();
				WorkflowTaskQuery query = new WorkflowTaskQuery();
				query.setActive(true);
				query.setProcessId(taskID);
				query.setTaskState(WorkflowTaskState.IN_PROGRESS);
				query.setOrderBy(new WorkflowTaskQuery.OrderBy[] { 
				WorkflowTaskQuery.OrderBy.TaskId_Asc });
				List<WorkflowTask> tasks = workflowService.queryTasks(query,sameSession);				
				taskInstance=tasks.get(0).getId();
				if (null!=taskInstance) {					
					if(output.equalsIgnoreCase("approved")) {
						decision="Approve";
						workflowService.endTask(taskInstance,decision); 
						finalStatus="successful";
					}
					else {
						decision="Rejected";
						workflowService.endTask(taskInstance,decision); 
						finalStatus="successful";						
					}
					
				}
				else {
					finalStatus="Failed";
					errorMessage = "Task is not available";
				}
				
			} else {
				finalStatus="Failed";
				errorMessage = "Wrong decision passed";
			}

		}

		else {
			finalStatus = "Failed";
			errorMessage = "taskID is null";
		}

		model.put("message", finalStatus);		
		model.put("errorMessage", errorMessage);

		return model;
	}

}
