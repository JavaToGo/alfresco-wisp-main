<main>
<#if versions?exists>
<#list versions as v>
<version>
<nodeRef>${v.nodeRef}</nodeRef>
<name>${v.name}</name>
<label>${v.label}</label>
<creator>${v.creator}</creator>
</version><#if (v_has_next)></#if>
</#list>
</#if>
<errorMessage>${errorMessage}</errorMessage>
</main>