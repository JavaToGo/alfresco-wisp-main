<main>
<#if versions?exists>
<version>${versions}</version>
</#if>
<#if node?exists>
<documentID>${node.nodeRef}</documentID> 
</#if>
<errorMessage>${errorMessage}</errorMessage>
</main>