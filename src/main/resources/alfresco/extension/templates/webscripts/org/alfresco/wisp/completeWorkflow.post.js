logger.log("####Start####");
var taskID = args.taskID ;
var output=args.output;
var status=null;
var task=null;
var errorMessage="";
var taskArray=new Array();
var taskId = "";
var arrayLength = 0;
var i = 0;
var isBreak = false;
var p= new Array();
var t= new Array();
try {

function fetchTask(wf)
{	
	for each (var path in wf.getPaths()) {			
	   for each (var task in path.getTasks()) {
		 // Collect in progress task.		
		 return task.id;
	   } 		
	}
}
if(taskID.indexOf("activiti$")!=-1 && taskID.length>9){

	taskID = taskID.replace("start","");
	var workflowInstance = workflow.getInstance(taskID);	
	taskArray.push(fetchTask(workflowInstance));
	
	arrayLength = taskArray.length;
}

if (!arrayLength == 0){
	
	task = workflow.getTaskById(taskArray[0]);
	var taskId = taskArray[0];
	
	if(task!=null)
	{
		 if(!task.complete){
		 
				if(task.title!="Approved" && task.title!="Rejected"){
					if(output=="approved")
				 {
					  task.endTask("Approve");
					  status="successful";
				  }
				  else if(output=="rejected")
				  {
					   task.endTask("rejected");
					   status="successful";        
				  }
				   else if(output=="next")
				  {
					   task.endTask("Next");
					   status="successful";        
				  }
				 else 
				 {
					status="failed";
					errorMessage="Wrong decision passed";
				 }
			}else{
				errorMessage="Task is already approved or rejected";
			}
				
		 }else{
			errorMessage="Task is complete";
		 }
		 
	}
	else
	{
		status="failed";
		errorMessage="Invalid task ID";  
	}
	
}


else
{
	status="failed";
	errorMessage="User doesn't have the permission to work on this task";  
}
}
catch(err) {
errorMessage=err.message;
}

finally {
	model.status=status;
	model.taskId = taskId;
	model.errorMessage=errorMessage;
}
