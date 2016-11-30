var versionRequired = args.versionNumber;
var documentID=args.documentID;
var node=null;
var version=null;
var workingCopy=null;
var working=null;
var versionContent=null;
var errorMessage="";
var versionHistory=null;
var versionArray=new Array();

try {

if (documentID.indexOf("workspace://SpacesStore/")!=-1){
        node=search.findNode(documentID);
		versionHistory=node.versionHistory;
		for(var i=0;i<versionHistory.length;i++){
			versionArray.push(versionHistory[i].label);
		}
}

if(node!=null)
{
if(node.assocs["cm:workingcopylink"]==null){
if(node.properties["workingCopyLabel"]!="(Working Copy)"){
for(var i=0;i<versionArray.length;i++){
	if(versionArray[i].toString()==versionRequired){
	versionContent = node.getVersion(versionRequired);
	workingCopy = node.checkout();
	break;
	}
	
}
if(versionContent!=null)
{
workingCopy.content=versionContent.node.content;
working=workingCopy.checkin();
version=working.properties["versionLabel"];
}
else{
errorMessage="entered version not available";
}
}
else{
errorMessage="node is a working copy";
}
}
else{
errorMessage="node is locked";
}
}
else{
errorMessage="invalid document ID";
}
}
catch(err) {
errorMessage=err.message;
//version="";
}

finally {
	model.versions=version;
	model.errorMessage=errorMessage;
}



