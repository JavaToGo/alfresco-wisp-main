var documentID=args.documentID;
var node=null;
var errorMessage="";
var versions=[];
try {
	if (documentID.indexOf("workspace://SpacesStore/")!=-1){
		node=search.findNode(documentID);
	}
	if(node!=null){
		var versionHistory=node.versionHistory;
		if(versionHistory!=null)
		{
			for(i=0;i<versionHistory.length;i++)
			{
			version=versionHistory[i];
			versions[i]=
				{
				nodeRef:version.node.nodeRef.toString(),
				name:version.node.name,
				label:version.label,
				description:version.description,
				createDate:version.createDate,
				creator:version.creator
				};
			}
		}
	}
	else{
		errorMessage="invalid document ID";
	}
}
catch(err) {
	errorMessage=err.message;
	//versions= "";
}
finally{
	model.versions=versions;
	model.errorMessage=errorMessage;
}
