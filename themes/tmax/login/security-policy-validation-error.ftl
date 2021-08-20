<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
         <div class="header-icon ip-blocked">
            ${msg("blockedTitle")}
        </div>
    <#elseif section = "form">
        <div id="ip-blocked">
            <div class="${properties.kcFormGroupClass!}">
                <p id="instruction" >
                ${msg("blockedMessage1")}
                <br>
                ${msg("blockedMessage2")}
                <br>
                ${msg("blockedMessage3")}
                </p>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <div id="info" >
                <#--  <div class="call-icon"></div>  -->
                <div class="info-message">${msg("blockedMessagePhoneNumber")}</div>
                <br>
                <#--  <div class="email-icon"></div>  -->
                <div class="info-message">${msg("blockedMessageEmailAddress")}</div>
                </div>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" style="margin-top: 95px; font-size: 18px;" <#if client.baseUrl?? && client.baseUrl!="">onclick="location.href='${client.baseUrl}'"<#else>onclick="location.href=document.location.origin"</#if>>${msg("sessionExpiredMsg3")}</button>
            </div>
        </div>
        <#if properties.scripts_security_policy_hyperauth?has_content>
            <#list properties.scripts_security_policy_hyperauth?split(' ') as script>
                <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
            </#list>
        </#if>
    </#if>
</@layout.registrationLayout>
