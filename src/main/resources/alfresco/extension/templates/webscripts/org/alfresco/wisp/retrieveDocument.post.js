var documentID = args.documentID;
var purpose = args.purpose;
var node=null;
var errorMessage="";
var finalNode=null;

try {

	if (documentID.indexOf("workspace://SpacesStore/")!=-1){
		node=search.findNode(documentID);
	}
	if(node!=null){
		if(purpose=="view"){
			finalNode=search.findNode(documentID);

		}
		else if(purpose=="edit"){
			if(node.isLocked==true){
				errorMessage="node is already checked out";
			}
			else{
				if(node.properties["workingCopyLabel"]!="(Working Copy)"){
					finalNode=node.checkout();
				}
				else{
					errorMessage="node is a working copy"
				}
			}
			
		}
		else{
			errorMessage="Invalid purpose";
		}
	}
	else{
		errorMessage="Invalid documentID";
	}
}
catch(err) {
	errorMessage=err.message;
	//finalNode= "";
}
finally{
	model.node=finalNode;
	model.errorMessage=errorMessage;
}


