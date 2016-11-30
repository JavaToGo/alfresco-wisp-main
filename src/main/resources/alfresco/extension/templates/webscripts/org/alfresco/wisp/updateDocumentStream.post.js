var documentID=args.documentID;
var documentContent=args.documentContent;
var modifiedNode=null;
var node=null;
var errorMessage="";
var version=null;
var workingCopy=null;
try {

	if (documentID.indexOf("workspace://SpacesStore/")!=-1){
		node=search.findNode(documentID);
	}
	if(node!=null){
		if(node.properties["workingCopyLabel"]=="(Working Copy)"){
			node.content=documentContent;
			node.properties.content.setEncoding("UTF-8");
			modifiedNode=node.checkin();		
			version=modifiedNode.properties["versionLabel"];
		}
		else if(node.properties["lockType"]!=null){
			workingCopy = node.assocs["cm:workingcopylink"][0];
			workingCopy.content=documentContent;
			workingCopy.properties.content.setEncoding("UTF-8");
			modifiedNode=workingCopy.checkin();
			version=modifiedNode.properties["versionLabel"];
		}else{
			errorMessage="Cannot checkin . Node doesn't have a working copy";
		}
	
	}
	else{
		errorMessage="invalid document id";
	}
}
catch(err) {
	errorMessage=err.message;
	//version= "";
	//modifiedNode="";
}
finally{
	model.versions=version;
	model.errorMessage=errorMessage;
	model.node=modifiedNode;

 }

