var filename = null;
var content = null;
var title = "";
var description = "";
var documentID=args.documentID;
var mimeType=args.mimeType; 
var node=null;
var errorMessage="";
var version=null;
var modifiedNode=null;
try 
{

	if (documentID.indexOf("workspace://SpacesStore/")!=-1)
	{
		node=search.findNode(documentID);
	}
	
	for each (field in formdata.fields)
	{
	  if (field.name == "title")
	  {
		title = field.value;
	  }
	  else if (field.name == "desc")
	  {
		description = field.value;
	  }
	  else if (field.name == "file" && field.isFile)
	  {
		//filename = field.filename;
		content = field.content;
	  }
	}

	if(node!=null)
	{
		if(node.properties["workingCopyLabel"]=="(Working Copy)")
		{			
			node.ensureVersioningEnabled(true, true);			
			node.properties.content.write(content);
			node.properties.content.setEncoding("UTF-8");
		    node.mimetype=mimeType;		
			modifiedNode=node.checkin();	
			version=modifiedNode.properties["versionLabel"];
		}
		else if(node.properties["lockType"]!=null)
		{			
			node.ensureVersioningEnabled(true, true);
			workingCopy = node.assocs["cm:workingcopylink"][0];			
			modifiedNode=workingCopy.checkin();			
			version=modifiedNode.properties["versionLabel"];
		}
		else
		{
			errorMessage="Cannot checkin . Node doesn't have a working copy";
		}	

	}
	else
	{
		errorMessage="invalid document id";
	}

}
catch(err) 
{
	errorMessage=err.message;
	//version="";
	//modifiedNode="";
}

finally 
{	
	model.versions=version;
	model.node=modifiedNode;
	model.errorMessage=errorMessage;
	
}

