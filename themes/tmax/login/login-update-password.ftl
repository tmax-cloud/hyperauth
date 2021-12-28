<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_template_login_reset_password?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <#if section = "form">
        <div class="header-icon login_reset_password">
            ${msg("MSG_RESETPASSWORD__1")}
        </div>

        <div id="kc-info" class="${properties.kcSignUpClass!}">
            <div id="kc-info-wrapper" class="password-update-description" style = "font-size: 16px;color: #333333;margin-bottom: 50px;">
                ${msg("MSG_RESETPASSWORD__2")}
            </div>
        </div>

        <form id="kc-passwd-update-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <input type="text" id="username" name="username" value="${username}" autocomplete="username" readonly="readonly" style="display:none;"/>
            <input type="password" id="current-password" name="password" autocomplete="current-password" style="display:none;"/>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="password" class="${properties.kcLabelClass!}">${msg("MSG_RESETPASSWORD__3")}</label>
                </div>

                <div class="${properties.kcInputWrapperClass!} marginBottom">
                    <input type="password" id="password" name="password-new" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus placeholder="${msg("MSG_RESETPASSWORD__4")}" onkeyup="passwordChange()" onfocusout="passwordValidation()" />
                    <div class="error_message" id="error_none_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_1")}
                    </div>
                    <div class="error_message" id="error_length_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_2")}
                    </div>
                    <div class="error_message" id="error_wrong_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_3")}
                    </div>
                    <#if message?has_content>
                        <#if message.type = 'error' && message.summary = 'samePasswordMessage'>
                            <div class="error_message" id="error_sameAsBefore_password" style="display: block">
                                ${msg("MSG_ERROR_PASSWORD_4")}
                            </div>
                        </#if>
                    </#if>
                </div>

                <#--  <div class="${properties.kcInputWrapperClass!}">
                    <input type="password" id="password-new" name="password-new" class="${properties.kcInputClass!}" autofocus autocomplete="new-password" />
                </div>  -->
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="password-confirm" class="${properties.kcLabelClass!}">${msg("passwordConfirm")}</label>
                </div>

                <div class="${properties.kcInputWrapperClass!} marginBottom">
                    <input type="password" id="password_confirm" name="password-confirm" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus placeholder="${msg("MSG_RESETPASSWORD__5")}" onkeyup="passwordChange()" onfocusout="passwordConfirmValidation()" />
                    <div class="error_message" id="error_none_password_confirm" style="display: none">
                        ${msg("MSG_ERROR_CONFIRMPASSWORD_1")}
                    </div>
                    <div class="error_message" id="error_wrong_password_confirm" style="display: none">
                        ${msg("MSG_ERROR_CONFIRMPASSWORD_2")}
                    </div>
                </div>


                <#--  <div class="${properties.kcInputWrapperClass!}">
                    <input type="password" id="password-confirm" name="password-confirm" class="${properties.kcInputClass!}" autocomplete="new-password" />
                </div>  -->
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <#if isAppInitiatedAction??>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("MSG_RESETPASSWORD__6")}" />
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" type="submit" name="cancel-aia" value="true" />${msg("doCancel")}</button>
                    <#else>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("MSG_RESETPASSWORD__6")}" disabled = {true} id = "save_password" onclick="this.disabled='disabled'; this.form.submit();" style ="margin-top:90px"/>
                    </#if>
                </div>
            </div>
        </form>
        <script type="text/javascript" src="${url.resourcesPath}/js/login-reset-password.js?${properties.version}"></script>

    </#if>
</@layout.registrationLayout>
