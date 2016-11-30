var documentID=args.documentID;
var majorVersion=args.majorVersion;
var node=null;
var working=null;
var errorMessage="";
var version=null;
var workingCopy=null;
try {
if (documentID.indexOf("workspace://SpacesStore/")!=-1){
            node=search.findNode(documentID);
 }
if(node!=null){
	if(majorVersion=="true"){
		if(node.properties["workingCopyLabel"]=="(Working Copy)"){
				working=node.checkin("major version change", true);
                version=working.properties["versionLabel"];
			}
			else if(node.properties["lockType"]!=null){
				//errorMessage="Cannot checkin . Node is not a working copy";
				workingCopy = node.assocs["cm:workingcopylink"][0];
				working=workingCopy.checkin("major version change",true);
				//version=working.properties["versionLabel"];
			}else{
				errorMessage="Cannot checkin . Node doesn't have a working copy";
			}
	}else if(majorVersion=="false"){
		if(node.properties["workingCopyLabel"]=="(Working Copy)"){
				working=node.checkin();
                version=working.properties["versionLabel"];
			}
			else if(node.properties["lockType"]!=null){
				//errorMessage="Cannot checkin . Node is not a working copy";
				workingCopy = node.assocs["cm:workingcopylink"][0];
				working=workingCopy.checkin();
				//version=working.properties["versionLabel"];
			}else{
				errorMessage="Cannot checkin . Node doesn't have a working copy";
			}

	}else{
		errorMessage="Invalid choice";
	}
}
else{
errorMessage="invalid document ID";
}
}

catch(err)
{
errorMessage=err.message;
//working="";
}
finally{
model.node=working;
model.errorMessage=errorMessage;
 }


