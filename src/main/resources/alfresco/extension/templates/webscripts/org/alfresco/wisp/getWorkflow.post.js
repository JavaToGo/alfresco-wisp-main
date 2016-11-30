var taskArray=workflow.getAssignedTasks();
var errorMessage="";
try {
if(taskArray.length!=0){
model.taskArray=taskArray;
}else{
    errorMessage="no tasks assigned for the user";
}
}
catch(err) {
errorMessage=err.message;

}
finally{

model.errorMessage=errorMessage;
 }


