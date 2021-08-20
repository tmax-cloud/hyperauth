<#import "template.ftl" as layout>
<@layout.registrationLayout bodyClass="template-body login-body login-password-body" displayInfo=false displayWide=true; section>
    <#if section = "header">
        <#if isBrokerLogin?has_content && isBrokerLogin = "true">
            <div class="${properties.kcFormGroupClass!}">
                <div class="link-with-kakao-icon"></div>
            </div>
        <#else>
            <div class="login-logo">${msg("doLogIn")}</div>
        </#if>
    <#elseif section = "form">
    <div id="kc-form">
      <div id="kc-form-wrapper" style="max-width: 380px; margin: auto auto;">
            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!}">
                    <label for="email_fixed" class="${properties.kcLabelClass!}">${msg("VendorAccount")}</label>
                    <input tabindex="1" id="email_fixed" class="${properties.kcInputClass!}" name="email_fixed" value="${(email!'')}" type="text" disabled="disabled" />
                </div>

                <div class="${properties.kcFormGroupClass!}">
                    <label for="password" class="${properties.kcLabelClass!}">${msg("password")}</label>
                    <input tabindex="2" id="password" class="${properties.kcInputClass!}" name="password" type="password" autocomplete="off" />
                </div>
                <div class="error-section">
                    <#if message?has_content && (message.type != 'warning')>
                        <div class="alert alert-${message.type}">
                            <span id="result-message-section" class="kc-feedback-text">${kcSanitize(message.summary)}</span>
                        </div>
                    </#if>
                </div>
                <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                    <div id="kc-form-options">
                        </div>
                        <div id="kc-forgot-pw" class="${properties.kcFormOptionsWrapperClass!}">
                            <#if realm.resetPasswordAllowed>
                                <span><a tabindex="5" href="${url.loginResetCredentialsUrl}">${msg("doForgotPassword")}</a></span>
                            </#if>
                        </div>
                  </div>

                  <div id="kc-form-buttons" class="${properties.kcFormGroupClass!}">
                    <input tabindex="4" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg("doLogIn")}"/>
                  </div>
            </form>
        </div>
      </div>
    </#if>
</@layout.registrationLayout>
