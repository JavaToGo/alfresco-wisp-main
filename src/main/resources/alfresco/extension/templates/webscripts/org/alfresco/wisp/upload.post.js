var documentType= args.documentType;
var filename = null;
var content = null;
var title = "";
var description = "";
var currentDate = new Date();
var errorMessage="";

try {
// locate file attributes
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
		
		filename = field.filename;
		var index = filename.lastIndexOf(".");
		var extension=filename.slice(index);
		var firstName= filename.substr(0,index);
		var name=firstName.concat(currentDate.getTime());
		filename=name.concat(extension);
		content = field.content;
	  }
	}

	// ensure mandatory file attributes have been located
	if (filename == undefined || content == undefined)
	{
	  status.code = 400;
	  status.message = "Uploaded file cannot be located in request";
	  status.redirect = true;
	}
	else
	{
	  // create document in company home for uploaded file
	  var xFolder =companyhome.childByNamePath("WISP/WISP_"+documentType);
	  upload = xFolder.createFile(filename) ;  
	  upload.properties.content.write(content);
	  upload.properties.content.setEncoding("UTF-8");
	  upload.properties.content.guessMimetype(filename);  
	  upload.properties.title = title; 
	  upload.properties.description = description;
	  upload.addAspect("cm:versionable");
	  upload.ensureVersioningEnabled(true, true); 
	  upload.save(); 
	  // setup model for response template
	  model.upload = upload;
	}
}
catch(err) {
	errorMessage=err.message;
}
finally{
	
	model.errorMessage=errorMessage;
}