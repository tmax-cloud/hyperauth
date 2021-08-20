<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
         <div class="header-icon email-sent">
            ${msg("MSG_PAGE_SESSION_1")}
        </div>
    <#elseif section = "form">
        <#--  <p id="instruction1" class="instruction">
            ${msg("pageExpiredMsg1")} <a id="loginRestartLink" href="${url.loginRestartFlowUrl}">${msg("doClickHere")}</a> .<br/>
            ${msg("pageExpiredMsg2")} <a id="loginContinueLink" href="${url.loginAction}">${msg("doClickHere")}</a> .
        </p>  -->
        <div class="timeout-img"></div>
        <div id="email-sent">
            <div class="${properties.kcFormGroupClass!}">
                <p id="welcome-instruction">
                ${msg("MSG_PAGE_SESSION_2")?no_esc}
                </p>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" style="margin-top: 95px; font-size: 18px;" onclick="location.href='${url.loginRestartFlowUrl}'">${msg("doOk")}</button>
            </div>
        </div>
    </#if>
</@layout.registrationLayout>
