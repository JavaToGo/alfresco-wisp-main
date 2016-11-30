<main>
<#if node?exists>
<documentID>${node.nodeRef}</documentID>
<version>${(node.properties["cm:versionLabel"])!"1.0"}</version>
</#if>
<errorMessage>${errorMessage}</errorMessage>
</main>