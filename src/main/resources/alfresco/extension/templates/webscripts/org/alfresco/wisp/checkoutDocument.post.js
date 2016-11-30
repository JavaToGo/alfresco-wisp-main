var documentID=args.documentID;
var node=null;
var finalNode=null;
var errorMessage="";
try {
if (documentID.indexOf("workspace://SpacesStore/")!=-1){
            node=search.findNode(documentID);
 }        
      if(node!=null) {                
                finalNode= node.checkout();
                   }
    else {
      errorMessage="invalid document ID";
       }
}
catch(err){
errorMessage=err.message;
//finalNode="";
}
finally{
model.node=finalNode;
model.errorMessage=errorMessage;
 }
