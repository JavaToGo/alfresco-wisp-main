var documentID=args.documentID;
var errorMessage="";
var node=null;
try {
	if (documentID.indexOf("workspace://SpacesStore/")!=-1){
		node=search.findNode(documentID);
	}
	else{
		errorMessage="invalid document ID";
	}
}
catch(err) {
	errorMessage=err.message;
	//node= "";
}
finally{
	model.node=node;
	model.errorMessage=errorMessage;
}
