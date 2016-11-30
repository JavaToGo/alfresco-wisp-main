<main>
<#if node?exists>
<version>${node.properties["versionLabel"]}</version>
<documentID>${node.nodeRef}</documentID>
</#if>
<errorMessage>${errorMessage}</errorMessage>
</main>

