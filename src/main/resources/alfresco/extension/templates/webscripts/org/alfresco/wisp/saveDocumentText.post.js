var documentID= args.documentID;
var documentContent=args.documentContent;
var mimeType=args.mimeType;
var filename="saveDoc."+mimeType;
var status=null;
var node=null;
var errorMessage="";

if (documentID.indexOf("workspace://SpacesStore/")!=-1){
        node=search.findNode(documentID);
}

if(mimeType=="text"){
  if(node!=null){
node.content=documentContent;
node.properties.content.setEncoding("UTF-8");
node.properties.content.guessMimetype(filename);
node.save();
status="successful";
}else{
status="failed";
}
}
else{
   errorMessage="invalid mime type";
}
 

model.status=status;
model.errorMessage=errorMessage;
