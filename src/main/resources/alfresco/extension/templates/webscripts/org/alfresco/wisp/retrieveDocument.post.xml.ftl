<main>
<#if node?exists>
 <content> <![CDATA[${node.content}]]></content>
  <documentID>${node.nodeRef}</documentID> 
</#if>
<errorMessage>${errorMessage}</errorMessage>
  </main>