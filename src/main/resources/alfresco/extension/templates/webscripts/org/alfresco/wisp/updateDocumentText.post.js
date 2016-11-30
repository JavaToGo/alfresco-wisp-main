var documentID=args.documentID;
var documentContent=args.documentContent;
var mimeType=args.mimeType;
var node=null;
var filename="modified."+mimeType;
var errorMessage="";
var version=null;
var modifiedNode=null;
try {
	if (documentID.indexOf("workspace://SpacesStore/")!=-1){			
		node=search.findNode(documentID);
	}
	if(mimeType=="text" || mimeType=="txt" || mimeType=="rtf"){
		if(node!=null){		
			if(node.properties["workingCopyLabel"]=="(Working Copy)"){		
				node.content=documentContent;		
				node.properties.content.guessMimetype(filename);		
				modifiedNode=node.checkin();		
				version=modifiedNode.properties["versionLabel"];		
			}
			else if(node.properties["lockType"]!=null){		
				workingCopy = node.assocs["cm:workingcopylink"][0];		
				workingCopy.content=documentContent;		
				workingCopy.properties.content.guessMimetype(filename);	
				modifiedNode=workingCopy.checkin();	
				version=modifiedNode.properties["versionLabel"];		
			}
			else{
				errorMessage="Cannot checkin . Node doesn't have a working copy";
			}	
		}
		else{
			errorMessage="invalid document id";
		}
	}
	else{
		errorMessage="invalid mime type";
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

