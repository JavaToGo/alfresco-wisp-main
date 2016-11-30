<main>
<#if taskArray?exists>
<#list taskArray as v>
<task>
<taskId>${v.getId()}</taskId>
<status>${v.properties["bpm:status"]}</status>
<#list v.getTransitions()?keys as prop>
<transitionId>
    ${prop} = ${v.getTransitions()[prop]}
</#list>
</transitionId>
<#if (v_has_next)></#if>
</task>
</#list>
</#if>
<errorMessage>${errorMessage}</errorMessage>
</main>
