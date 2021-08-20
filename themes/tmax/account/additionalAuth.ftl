<#import "template.ftl" as layout>
<@layout.mainLayout active='additionalAuth' bodyClass='user'; section>
    <@layout.contentHeader required=false; section>
        ${msg("additionalAuthHtmlTitle")}
    </@layout.contentHeader>

<body>
</br>
${msg("additionalAuthHtmlBody")}
</br>
<hr>
<div class="illustBox">
    <div></div>
</div>
</br>
<#assign emailOtpAuthUrl = url.additionalAuthUrl?replace("^(.*)(/account/additionalAuth/?)(\\?(.*))?$", "$1/account/additionalAuth/emailOtp?$4", 'r') />
    <form id="additionalAuthUrl" action="${emailOtpAuthUrl}" class="form-horizontal" method="post">
        <p class="rightMargin">${msg("emailOtpOption")}</p>
        <input type="hidden" name="stateChecker" value="${stateChecker}"/>
        <label class="switch">
            <#if account.attributes.otpEnable?has_content && account.attributes.otpEnable = 'true'>
                <input type="checkbox" name="otpEnable" value="true" checked>
            <#else>
                <input type="checkbox" name="otpEnable" value="true">
            </#if>
            <span class="slider round"></span>
        </label>

        <p id="off">OFF</p>
        <p id="on" style="display:none;">ON</p>

        </br>
        <hr>
        </br>
        <@layout.formButtonGroup>
        <div id="additionalAuth-form-group" class="form-group">
            <div id="buttons">
                <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel" onclick="openCancelModal(); return false;">${msg("doCancel")}</button>                                                
                <button id="additionalAuth-save-button" type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save" disabled>${msg("doSave")}</button>
            </div>
        </div>
        </@layout.formButtonGroup>
    </form>


<#--    간편로그인 Section  -->
<#assign simpleLoginUrl = url.additionalAuthUrl?replace("^(.*)(/account/additionalAuth/?)(\\?(.*))?$", "$1/account/additionalAuth/simpleLogin?$4", 'r') />
    <form id="simpleLoginUrl" action="${simpleLoginUrl}" class="form-horizontal hidden" method="post">
        <p class="rightMargin">${msg("simpleLoginOption")}</p>
        <input type="hidden" name="stateChecker" value="${stateChecker}"/>
        <label class="switch">
            <#if additionalAuth.simpleLoginEnable>
                <input type="checkbox" name="simpleLogin" value="true" checked>
            <#else>
                <input type="checkbox" name="simpleLogin" value="true">
            </#if>
            <span class="slider round"></span>
        </label>
        <!--
        <p id="off">OFF</p>
        <p id="on" style="display:none;">ON</p>
        -->
        </br>
        <hr>
        </br>
        <@layout.formButtonGroup>
            <div class="form-group">
                <div id="buttons">                    
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel">${msg("doCancel")}</button>
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save">${msg("doSave")}</button>
                </div>
            </div>
        </@layout.formButtonGroup>
    </form>

    <@layout.cancelModal>
        <div id="button-ok" class="button modal_button_right" onclick="cancelChangeAdditionalAuth()">
            ${msg("doOK")}
        </div>
    </@layout.cancelModal>

    <script type="text/javascript" src="${url.resourcesPath}/js/additionalAuth.js"></script>
</body>

</@layout.mainLayout>
