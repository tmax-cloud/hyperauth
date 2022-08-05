<#--  <#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=false; section >
    <link href="${url.resourcesPath}/css/error.css" rel="stylesheet" />

    <#if section = "header">
        ${msg("errorTitle")}
    <#elseif section = "form">
        <div id="kc-error-message">
            <p class="instruction">${message.summary?no_esc}</p>
            <#if client?? && client.baseUrl?has_content>
                <p><a id="backToApplication" href="${client.baseUrl}">${kcSanitize(msg("backToApplication"))?no_esc}</a></p>
            </#if>
        </div>
        <div class="illust">asdfasdf</div>
    </#if>
</@layout.registrationLayout>  -->


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <#if properties.styles_template?has_content>
        <#list properties.styles_error?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">
</head>

<body>

<div class='error-box'>
    <div class ='error-text-box' >
        <p class ="error-title">${msg("errorTitle")}</P>
        <p class = 'error-message'>${message.summary?no_esc}</p>
         <#if client?? && url.loginRestartFlowUrl?has_content>
        <div class = "backToApplication" onclick="location.href ='${url.loginRestartFlowUrl}'">${kcSanitize(msg("backToApplication"))?no_esc}</div>
         </#if>
    </div>
    <div class="illust"></div>
 </div>
</body>

</html>  
