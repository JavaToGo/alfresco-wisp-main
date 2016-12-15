# WISP Project

This project provides the following functionality
* Portable Content Id
* New Functionality for WispContent

## WISP Content JS API

```java
	public String makePortable(ScriptNode node) {
		return portableContentService.makePortable(node.getNodeRef());
	}

	public ScriptNode findByContentId(String contentId) {
		return new ScriptNode(portableContentService.findByContentId(contentId),this.serviceRegistry,this.getScope());
	}

	public String getContentId(ScriptNode node) {
		return portableContentService.getContentId(node.getNodeRef());
	}
	
	public void makeWispDocument(ScriptNode document, String documentType) {
		portableContentService.makeWispDocument(document.getNodeRef(), documentType);
	}

```

Use makeWispDocument to turn a document into a WispDocument.  Currently this is not checking the following
* The documentType (to verify that it is valid)
* The mimeType (to compare it against the documentType)


### From the email

You can use the JS Console to turn any document into a Wisp Content with a documentType.

* This triggers a content policy that moves the document into the proper folder
* This also adds the following properties
  * documentName
  * documentType
  * Timestamp
* This does not yet do the following:
  * Check to make sure the documentType is a valid type
  * Check to make sure that the mimeType of the document is valid for the documentType