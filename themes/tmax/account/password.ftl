<#import "template.ftl" as layout>
<@layout.mainLayout active='password' bodyClass='password'; section>

    <@layout.contentHeader ; section>
        ${msg("changePasswordHtmlTitle")}
    </@layout.contentHeader>
    </br>
    ${msg("changePasswordHtmlBody")}
    </br>
    <hr>
    </br>
    <form id="password-form" action="${url.passwordUrl}" class="form-horizontal" method="post">
        <input type="text" id="username" name="username" value="${(account.username!'')}" autocomplete="username" readonly="readonly" style="display:none;">

        <#if password.passwordSet>
            <@layout.formGroup key="password">
                <input type="password" class="form-control" id="password" name="password" autofocus autocomplete="current-password" onblur="validatePassword(); return false;">
                <div id="eye-password" class="eye" onclick="clickEye(this)"></div>
            </@layout.formGroup>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_old_password_empty" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_EMPTY")}
            </div>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_wrong" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_WRONG")}
            </div>

            <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">

            <@layout.formGroup key="password-new" msgKey="passwordNew">
                <input type="password" class="form-control" id="password-new" name="password-new" autocomplete="new-password" onblur="validatePasswordNew(); return false;">
                <div id="eye-password-new" class="eye" onclick="clickEye(this)"></div>
            </@layout.formGroup>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_empty" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_EMPTY")}
            </div>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_length" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_LENGTH")}
            </div>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_type" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_TYPE")}
            </div>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_unchange" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_UNCHANGE")}
            </div>
            <@layout.formGroup key="password-confirm" msgKey="passwordConfirm">
                <input type="password" class="form-control" id="password-confirm" name="password-confirm" autocomplete="new-password" onblur="validatePasswordConfirm(); return false;">
                <div id="eye-password-confirm" class="eye" onclick="clickEye(this)"></div>
            </@layout.formGroup>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_confirm_empty" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_EMPTY2")}
            </div>
            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_confirm_unmatch" style="display: none">
                ${msg("MSG_ERROR_PASSWORD_UNMATCHING")}
            </div>

            <hr>
            <@layout.formButtonGroup>
                <div id="buttons">
                    <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel" onclick="openCancelModal(); return false;">${msg("doCancel")}</button>
                    <button type="submit" id="password-save-button" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save" disabled=true onclick="submitPassword(); return false;" >${msg("doSave")}</button>
                    <!--<button type="submit" id="test-button" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save" onclick="submitPassword(); return false;" >submitTest</button> -->
                    <!--<button type="submit" id="password-save-button" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Save" onclick="openCancelModal(); return false;" >test</button>-->
                </div>
            </@layout.formButtonGroup>

            <@layout.cancelModal>
                <div id="button-ok" class="button modal_button_right" onclick="cancelChangePassword()">
                    ${msg("doOK")}
                </div>
            </@layout.cancelModal>
        <#else>
            ${msg("changePasswordRegisteredBySns")}
        </#if>
    </form>
    <script type="text/javascript" src="${url.resourcesPath}/js/password.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
</@layout.mainLayout>
