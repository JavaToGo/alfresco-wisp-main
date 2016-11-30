var workflowID=args.workflowID;
var instance =null;
var errorMessage="";
var taskArray=[];
try {
	if(workflowID.indexOf("activiti$")!=-1 && workflowID.length>9){
		instance = workflow.getInstance(workflowID);
	}
	if(instance !=null){
		var pathArray=instance.getPaths();    
		var count=0;
		for (var i = 0; i < pathArray.length; i++) {
			var u = pathArray[i].getTasks();            
			for(var j = 0; j < pathArray.length; j++){
				taskArray[count] = u[j];
				count=count+1;
			}            
		} 
	}
	else if(instance ==null){
		errorMessage="invalid workflow ID"
	}
	else{
		errorMessage="workflow instance empty";
	}
}
catch(err) {
	errorMessage=err.message;
	//taskArray="";
}
finally{
	model.taskArray=taskArray;
	model.errorMessage=errorMessage;
 }
