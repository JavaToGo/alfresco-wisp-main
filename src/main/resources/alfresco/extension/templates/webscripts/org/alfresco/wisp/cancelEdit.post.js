var documentID= args.documentID;
var errorMessage="";
var node=null;
var status=null;

try {
if (documentID.indexOf("workspace://SpacesStore/")!=-1){
            node=search.findNode(documentID);
  }

if(node==null)
{
status="failure";
errorMessage="invalid document ID";
}
else
{
if(node.properties["workingCopyLabel"]=="(Working Copy)"){
        status="success";
        node.cancelCheckout();
}else if(node.properties["lockType"]!=null){
				workingCopy = node.assocs["cm:workingcopylink"][0];
				workingCopy.cancelCheckout();
				status="success";
}
else{
      status="failure";
	  errorMessage="node does not have working copy";
}
}
}

catch(err)
{
errorMessage=err.message;
model.status= "Error";
}
finally{
model.status= status;
model.errorMessage=errorMessage;
 }


