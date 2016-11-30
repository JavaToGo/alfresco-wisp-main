var documentID = args.documentID;
var version=args.version;
var node=null;
var finalNode=null;
var versionContent=null;
var content="";
var errorMessage="";
var count=0;
var versionArray=new Array();
try {
if (documentID.indexOf("workspace://SpacesStore/")!=-1){
	node=search.findNode(documentID);
	finalNode=node;
	versionHistory=node.versionHistory;
	for(var i=0;i<versionHistory.length;i++){
		versionArray.push(versionHistory[i].label);
	}
}
if(node!=null)
{
for(var i=0;i<versionArray.length;i++){
	
	if(versionArray[i].toString()==version){
	versionContent = node.getVersion(version);
    content=versionContent.node.content;
	count=count+1;
	break;
	}
}
if(count!=1){	
errorMessage="entered version not available";
}
}
else{
errorMessage="invalid document ID";
}
}
catch(err) {
errorMessage=err.message;
//content= "";
//node="";
}
finally{
model.node=content;
model.finalNode=node;
model.errorMessage=errorMessage;
}

