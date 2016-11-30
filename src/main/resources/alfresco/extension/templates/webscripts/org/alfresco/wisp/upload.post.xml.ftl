<main>
<#if upload?exists>
<documentID>${upload.nodeRef}</documentID>
<name>${upload.name}</name>
<version>${(upload.properties["cm:versionLabel"])!"1.0"}</version>
<contentURL>${upload.url}</contentURL>
</#if>
<errorMessage>${errorMessage}</errorMessage>
</main>
