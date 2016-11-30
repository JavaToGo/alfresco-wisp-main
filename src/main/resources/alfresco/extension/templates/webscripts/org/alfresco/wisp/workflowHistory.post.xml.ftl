<main>
<#if taskArray?exists>
<#list taskArray as t>
<task>

<description>${t.description}</description>
<taskId>${t.getId()}</taskId>
         <startDate><#if t.properties["bpm:startDate"]?exists>${t.properties["bpm:startDate"]?datetime}<#else>None</#if></startDate>
         <completionDate><#if t.properties["cm:completionDate"]?exists>${t.properties["bpm:dueDate"]?datetime}<#else>None</#if></completionDate>
         <priority>${t.properties["bpm:priority"]}</priority>
    
         <status>${t.properties["bpm:status"]}</status>
         

<#if (t_has_next)></#if>
</task>
</#list>
</#if>
<errorMessage>${errorMessage}</errorMessage>

</main>
