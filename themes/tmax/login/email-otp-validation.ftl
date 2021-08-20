<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_email_otp_validation?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if section = "header">
         <div class="header-icon ip-blocked">
            ${msg("MSG_LOGIN_OTPCODE_1")}
        </div>
    <#elseif section = "form">
       <div id="opt-code">
            <body onload="startTimer('${(emailOtpCodeExpAt!'')}', '${(emailOtpCodeTtl!'')}')">
                <form id="otp-code-form" action="${url.loginAction}" method="post">
                    <div class="${properties.kcFormGroupClass!}">
                        <p id="instruction" >
                        ${msg("MSG_LOGIN_OTPCODE_2")?no_esc}
                        </p>
                    </div>
                    <div class="${properties.kcFormGroupClass!} marginTop">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="otpCode" class="${properties.kcLabelClass!}">${msg("MSG_LOGIN_OTPCODE_3")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!} marginBottom" style="min-height: 100px;">
                            <div id="timer-input-container" class="timer-input-container">
                                <input type="text" id="otpCode" name="otpCode" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" autofocus onkeyup="removeError(this)" onchange="removeError(this)" onpaste="removeError(this)"/>
                            </div>
                            <div>
                                <#if message?has_content && (message.type != 'warning')>
                                    <div id="error-section" class="alert alert-error" style="margin-top: 5px;">
                                        <span class="kc-feedback-text">${kcSanitize(message.summary)?no_esc}</span>
                                    </div>
                                <#else>
                                    <div id="error-section" class="empty-error" style="margin-top: 5px;"></div>
                                </#if>
                            </div>
                        </div>
                    </div>
                    <div class="${properties.kcFormGroupClass!}">
                        <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                            <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" id="login-otp" type="submit" disabled />
                        </div>
                        <div id="kc-info-message" class="${properties.kcLabelWrapperClass!} infoText">
                            <#--  현재 페이지 새로고침할 때 메일 재전송 되고있어서 임시방안으로 새로고침을 통한 재전송으로 구현해놓음  -->
                            <p>${msg("MSG_LOGIN_OTPCODE_5")}  <a id="resend-link" href="#" onclick="clickResendEmailButton(document.location.href); return false;">${msg("MSG_LOGIN_OTPCODE_6")}</a></p>
                            <#if client?? && client.baseUrl?has_content>
                                <p>${msg("MSG_LOGIN_OTPCODE_7")}  <a href="${client.baseUrl}" style="color: #185692;">${msg("MSG_LOGIN_OTPCODE_8")}</a></p>
                            </#if>
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
                        <div class="time_expired_modal hidden">
                            <div class="md_overlay"></div>
                            <div class="md_content">
                                <div class="md_content__header">
                                    <span class="md_content__header__title">
                                        ${msg("tempOtpCodeTimeExpiredTitle")}
                                    </span>
                                    <span class="md_content__header__close" onclick="closeTimeExpiredModal()">
                                    </span>
                                </div>
                                <hr>
                                <div class="md_content__text">
                                    ${msg("tempOtpCodeTimeExpiredMessage")?no_esc}
                                </div>
                                <div class="button" onclick="closeTimeExpiredModal()">
                                    ${msg("MSG_FINDPASSWORD_POPUPVERIFYEMAILREQUEST_3")}
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </body>
        </div>
    </#if>
    <script type="text/javascript">
        function removeError(input) {
            input.classList.remove("has-error");
            setTimeout(() => {
                const optInput = document.getElementById("otpCode");
                const otpButton = document.getElementById("login-otp");
                if (optInput.value !== "") {
                otpButton.disabled = false;
                } else {
                otpButton.disabled = true;
                }
            }, 300);
        }
    </script>
    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/email-otp-validation.js"></script>
</@layout.registrationLayout>