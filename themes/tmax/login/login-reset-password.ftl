<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_template_login_reset_password?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if section = "form">
        <div id="forgot_password" class="login_reset_password">
            <div class="header-icon login_reset_password">
                ${msg("MSG_FINDPASSWORD__1")}
            </div>

            <div id="kc-info" class="${properties.kcSignUpClass!}">
                <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                    ${msg("MSG_FINDPASSWORD__2__1")}
                </div>
                <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                    ${msg("MSG_FINDPASSWORD__2__2")}
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="email" class="${properties.kcLabelClass!}">${msg("MSG_FINDPASSWORD__3")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!} marginBottom">
                    <input type="text" id="email" name="email" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus placeholder="${msg("MSG_FINDPASSWORD__4")}" onkeyup="idChange('send_email')" onchange="idChange('send_email')" onpaste="idChange('send_email')" />
                    <div class="error_message" id="error_wrong_email" style="display: none">
                        ${msg("MSG_ERROR_FINDPASSWORD_1")}
                    </div>
                    <div class="error_message" id="error_none_email" style="display: none">
                        ${msg("MSG_ERROR_FINDPASSWORD_2")}
                    </div>
                    <div class="error_message" id="error_register_by_sns" style="display: none">
                        ${msg("MSG_ERROR_FINDPASSWORD_3")}
                    </div>
                    <div class="error_message" id="error_empty_email" style="display: none">
                        ${msg("MSG_ERROR_FINDPASSWORD_4")}
                    </div>
                </div>

            </div>
            <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" id="send_email" onclick="clickSendEmailButton('email')" type="button" disabled={true} value="${msg("MSG_FINDPASSWORD__5")}"/>
            </div>
            <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                <div id="register">
                    <a href="${url.registrationUrl}">
                        ${msg("MSG_FINDPASSWORD__6")}
                    </a>
                </div>
            </#if>
        </div>

        <div id="verify_password" class="login_reset_password" style="display: none">
            <div class="header-icon login_reset_password">
                ${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_1")}
            </div>

            <div id="kc-info" class="${properties.kcSignUpClass!}">
                <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                    ${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_2")}
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="verification_input" class="${properties.kcLabelClass!}">${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_0")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!} marginBottom">
                    <input type="text" id="verification_input" name="verification_input" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus onkeyup="verificationChange('verification_confirm')" onchange="verificationChange('verification_confirm')" onpaste="verificationChange('verification_confirm')" placeholder="${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_3")}" />
                    <div class="error_message" id="error_wrong_code" style="display: none">
                        ${msg("MSG_ERROR_VERIFICATIONCODE_1")}
                    </div>
                </div>
            </div>
            <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" id="verification_confirm" type="button" value="${msg("MSG_FINDPASSWORD_POPUPVERIFYEMAILREQUEST_3")}" disabled={true} onclick="clickVerficiationConfirmButton()" />
            </div>

            <div class="description">
                ${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_4")} <span id="resend-button" style="color: #185692" onclick="clickResendEmailButton('email')">${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_5")}</span></a>
            </div>
            <div class="description">
                ${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_6")}
                <span style="color: #316AAD" onclick="location.reload(true)">${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_7")}</span>
            </div>

            <div class="modal hidden">
                <div class="md_overlay"></div>
                <div class="md_content">
                    <div class="md_content__header">
                        <span class="md_content__header__title">
                            ${msg("MSG_FINDPASSWORD_POPUPVERIFYEMAILREQUEST_1")}
                        </span>
                        <span class="md_content__header__close" onclick="closeModal()">
                        </span>
                    </div>
                    <hr>
                    <div class="md_content__text">
                        ${msg("MSG_FINDPASSWORD_POPUPVERIFYEMAILREQUEST_2_1")}
                    </div>
                    <div class="md_content__text">
                        ${msg("MSG_FINDPASSWORD_POPUPVERIFYEMAILREQUEST_2_2")}
                    </div>
                    <div class="button" onclick="closeModal()">
                        ${msg("MSG_FINDPASSWORD_POPUPVERIFYEMAILREQUEST_3")}
                    </div>
                </div>
            </div>

        </div>

        <div id="update_password" class="login_reset_password" style="display: none">
            <div class="header-icon login_reset_password">
                ${msg("MSG_RESETPASSWORD__1")}
            </div>

            <div id="kc-info" class="${properties.kcSignUpClass!}">
                <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                    ${msg("MSG_RESETPASSWORD__2")}
                </div>
            </div>

            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="password" class="${properties.kcLabelClass!}">${msg("MSG_RESETPASSWORD__3")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!} marginBottom">
                    <input type="password" id="password" name="password" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus placeholder="${msg("MSG_RESETPASSWORD__4")}" onkeyup="passwordChange()" onfocusout="passwordValidation()" />
                    <div class="error_message" id="error_none_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_1")}
                    </div>
                    <div class="error_message" id="error_length_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_2")}
                    </div>
                    <div class="error_message" id="error_wrong_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_3")}
                    </div>
                    <div class="error_message" id="error_sameAsBefore_password" style="display: none">
                        ${msg("MSG_ERROR_PASSWORD_4")}
                    </div>
                </div>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="password_confirm" class="${properties.kcLabelClass!}">${msg("MSG_RESETPASSWORD__7")}</label>
                </div>
                <div class="${properties.kcInputWrapperClass!} marginBottom">
                    <input type="password" id="password_confirm" name="password_confirm" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus placeholder="${msg("MSG_RESETPASSWORD__5")}" onkeyup="passwordChange()" onfocusout="passwordConfirmValidation()" />
                    <div class="error_message" id="error_none_password_confirm" style="display: none">
                        ${msg("MSG_ERROR_CONFIRMPASSWORD_1")}
                    </div>
                    <div class="error_message" id="error_wrong_password_confirm" style="display: none">
                        ${msg("MSG_ERROR_CONFIRMPASSWORD_2")}
                    </div>
                </div>
            </div>
            <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="button" value="${msg("MSG_RESETPASSWORD__6")}" id="save_password" onclick="clickPasswordSaveButton()" disabled={true} />
            </div>
        </div>

        <div id="reset_password_complete" class="login_reset_password" style="display: none">
            <div class="header-icon login_reset_password">
                ${msg("MSG_RESETPASSWORD_SUCCEED_1")}
            </div>

            <div class="image">

            </div>

            <div id="kc-info" class="${properties.kcSignUpClass!}">
                <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                    ${msg("MSG_RESETPASSWORD_SUCCEED_2_1")}
                </div>
                <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                    ${msg("MSG_RESETPASSWORD_SUCCEED_2_2")}
                </div>
            </div>
            <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                <a href="${url.loginRestartFlowUrl}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="button" value="${msg("MSG_RESETPASSWORD_SUCCEED_3")}" />
                </a>
            </div>
        </div>

        <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
        <script type="text/javascript" src="${url.resourcesPath}/js/login-reset-password.js"></script>
        <#if properties.scripts_reset_password_cnu?has_content>
            <#list properties.scripts_reset_password_cnu?split(' ') as script>
                <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
            </#list>
        </#if>

    </#if>
</@layout.registrationLayout>
