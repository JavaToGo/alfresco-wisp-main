var workflowID=args.workflowID;
var instance =null;
var pathArray=null;
var taskArray=[];
var errorMessage="";
var u=null;
var count=0;
try {
if(workflowID.indexOf("activiti$")!=-1 && workflowID.length>9){
instance = workflow.getInstance(workflowID);
}
if(instance!=null){
pathArray=instance.getPaths();
for (var i = 0; i < pathArray.length; i++) {
            u = pathArray[i].getTasks();
            
           for(var j = 0; j < pathArray.length; j++){
                  taskArray[count] = u[j];
                  count=count+1;
            }
            
        }
}else{
errorMessage="invalid workflow ID";
}
}
catch(err) {
errorMessage=err.message;
taskArray= "";
}
finally{
 model.taskArray=taskArray;
model.errorMessage=errorMessage;

 }


