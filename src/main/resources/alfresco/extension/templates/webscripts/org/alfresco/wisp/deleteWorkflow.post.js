var workflowID=args.workflowID;
var instance =null;;
var status=null;
var errorMessage="";

try {
	if(workflowID.indexOf("activiti$")!=-1 && workflowID.length>9){
		instance = workflow.getInstance(workflowID);
	}
	if(instance ==null)
	{
		status="failure";
		errorMessage="Invalid workflow ID";
	}
	else
	{
		status="success";
		instance['delete'](); 
	}

}

catch(err) {
	errorMessage=err.message;
	status= "Error";
}
finally{
	model.status= status;
	model.errorMessage=errorMessage;
 }


