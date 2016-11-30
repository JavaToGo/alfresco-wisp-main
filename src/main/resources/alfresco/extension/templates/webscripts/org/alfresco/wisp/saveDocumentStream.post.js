var documentID= args.documentID;
var documentContent=args.documentContent;
var status=null;
var node=null;


if (documentID.indexOf("workspace://SpacesStore/")!=-1){
        node=search.findNode(documentID);
}

  if(node!=null){
node.content=documentContent;
node.properties.content.setEncoding("UTF-8");
node.save();
status="successful";
}else{
status="failed";
}
 

model.status=status;
