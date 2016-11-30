var workFlowName=args.workFlowName;
var attachment=args.attachment;
var reviewerName=args.reviewerName;
var documentType=args.documentType;
var documentName=args.documentName;
var errorMessage="";
var wfdef=null;
var node=null;
try {
if(workFlowName!=""){
	wfdef = workflow.getDefinitionByName("activiti$"+workFlowName);
}

if (attachment.indexOf("workspace://SpacesStore/")!=-1){
        node=search.findNode(attachment);
}

if(reviewerName!=""){
if (wfdef!=null && node!=null) {
    var wfparams = new Array();
    wfparams["bpm:workflowDescription"] = "Please review";
	var assigneeArray=reviewerName.split(",");
	var personArray=new Array();
	var reviewerCount = assigneeArray.length;
	for(var task=0;task<reviewerCount;task++){
		if(people.getPerson(assigneeArray[task])!=null){
			personArray.push(people.getPerson(assigneeArray[task]));
		}else{
			errorMessage="invalid reviewer name";
			break;
        }
	}
	if(personArray.length>0){
		wfparams["bpm:assignees"]=personArray;
		wfparams['bpm:workflowPriority'] = 2;
		wfparams["initiator"] = "admin";
		wfparams["requiredApprovePercent"] = 1;
		wfparams["wisp:documentType"] =documentType;
		wfparams["wisp:documentName"] =documentName;
		var wfpackage = workflow.createPackage();
		var document=node;
		wfpackage.addNode(document);  
		var wfpath = wfdef.startWorkflow(wfpackage, wfparams);
		var wfinstance=wfpath.getInstance();
		var taskArray=wfpath.getTasks();
		var taskID=taskArray[0].getId();
		var taskObj = workflow.getTaskById(taskID);
		model.taskID=taskID;
		model.definition = wfinstance.id;
		//routing the start task
		if(taskObj!=null)
		{
			if(taskObj.title!="Approved" && taskObj.title!="Rejected"){						
				taskObj.endTask("Approve");
			}else{
				errorMessage="Error in finding the correct task";
			}
		}
		else
		{
			errorMessage="Invalid task id";  
		}
	}
}
else{
	errorMessage="invalid workflow name or attachment";
}
}
else{
	errorMessage="invalid reviewer name";
}


}
catch(err) {
errorMessage=err.message;
}

finally {
	model.errorMessage=errorMessage;
}





	

        
    