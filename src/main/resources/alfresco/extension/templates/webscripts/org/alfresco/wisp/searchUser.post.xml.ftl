<main>
<status>
<#list status as s>
${s}~
<#if (s_has_next)></#if>
</#list>
</status>
<errorMessage>${errorMessage}</errorMessage>
</main>