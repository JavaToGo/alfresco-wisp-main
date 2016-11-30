<main>
<#if node?exists>
<content><![CDATA[${node}]]></content>
<name>${finalNode.properties.name}</name>
</#if>
<errorMessage>${errorMessage}</errorMessage>
  </main>
