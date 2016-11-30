var userName=args.userName;
var errorMessage="";
var status=[];
var validLength=userName.length;
var nameArray=userName.split(",");
//var arrayLength=nameArray.length;

try {
	
if(validLength>0) {
	for(i=0;i<nameArray.length;i++) { 
		
		if (people.getPerson(nameArray[i])) {
                status[i]=nameArray[i]+"#Y";				
		}
		else {			
        status[i]=nameArray[i]+"#N";		
			}
      
     
	}
}
else {
 status="Error";        
errorMessage="user name is null";
}
}
catch(err) {
errorMessage=err.message;
status="Error";
}

finally {	

model.status=status;


model.errorMessage=errorMessage;
}

