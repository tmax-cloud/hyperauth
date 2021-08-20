<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
         <div class="header-icon withdrawal-cancel">
            ${msg("MSG_RESETPASSWORD__1")}
        </div>
    <#elseif section = "form">
         <div id="change-password-step2">
            <form id="change-password-step2-form" action="${url.loginAction}" method="post">
                <div id="kc-info" class="${properties.kcSignUpClass!}">
                    <div id="instruction" class="${properties.kcInfoAreaWrapperClass!}">
                        ${msg("MSG_RESETPASSWORD__2")}
                    </div>
                </div>
                <div>
                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="password" class="${properties.kcLabelClass!}">${msg("MSG_RESETPASSWORD__3")}</label>
                        </div>
                        <div id="password_wrapper" class="${properties.kcInputWrapperClass!} marginBottom">
                            <input type="password" id="password" name="password" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autocomplete="new-password" placeholder="${msg("MSG_RESETPASSWORD__4")}" onblur="passwordChange('password')" onkeyup="setButtonDisabled()"/>
                            <div class="error-container">
                                <div class="error_message" id="error_none_password" style="display: none">
                                    ${msg("MSG_ERROR_PASSWORD_1")}
                                </div>
                                <div class="error_message" id="error_length_password" style="display: none">
                                    ${msg("MSG_ERROR_PASSWORD_2")}
                                </div>
                                <div class="error_message" id="error_wrong_password" style="display: none">
                                    ${msg("MSG_ERROR_PASSWORD_3")}
                                </div>
                                <#if message?has_content && (message.type != 'warning')>
                                    <div class="error_message" id="error_exist_password_confirm" style="display: block">
                                        ${kcSanitize(message.summary)}
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </div>
                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="confirmPassword" class="${properties.kcLabelClass!}">${msg("MSG_RESETPASSWORD__7")}</label>
                        </div>
                        <div id="confirm_password_wrapper" class="${properties.kcInputWrapperClass!} marginBottom">
                            <input type="password" id="confirmPassword" name="confirmPassword" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autocomplete="new-password" placeholder="${msg("MSG_RESETPASSWORD__5")}" onblur="passwordChange('confirmPassword')" onkeyup="setButtonDisabled()"/>
                            <div class="error-container">
                                <div class="error_message" id="error_none_password_confirm" style="display: none">
                                    ${msg("MSG_ERROR_CONFIRMPASSWORD_1")}
                                </div>
                                <div class="error_message" id="error_wrong_password_confirm" style="display: none">
                                    ${msg("MSG_ERROR_CONFIRMPASSWORD_2")}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                 <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" style="margin-bottom: 30px;" type="submit" value="${msg("MSG_RESETPASSWORD__6")}" id="save_password" disabled={true} />
                </div>
            </form>
        </div>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/js/change-password.js"></script>
</@layout.registrationLayout>