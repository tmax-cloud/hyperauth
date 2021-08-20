<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        <div class="header-icon email-sent">
            ${msg("MSG_CREATEACCOUNT_CREATEACCOUNTCOMPLETE_1")}
        </div>
    <#elseif section = "form">
       <div id="email-sent">
            <div class="email-confirm-img"></div>
            <div class="${properties.kcFormGroupClass!}">
                <p id="email-sent-instruction" style="font-weight: 400;">
                    <#--  <span id="user-email-text"></span>${msg("emailLinkIdp6", "KaKao")?no_esc}  -->
                    <span id="user-email-text"></span>${msg("emailLinkIdp6", idpAlias)?no_esc}
                </p>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <#--  <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" <#if client.baseUrl?? && client.baseUrl!="">onclick="goToLoginHome('${client.baseUrl}')"<#else>onclick="goToLoginHome(document.location.origin)"</#if>>확인</button>  -->
                <p id="idp-email-sent-instruction-small">
                    ${msg("MSG_TEXT__2")} <a id="resend-link" href="#" onclick="clickResendEmailButton('${url.loginAction}'); return false;">${msg("createAccountEmailVerifyInstruction3")}</a>
                </p>
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
        </div>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/login-idp-link-email.js"></script>
</@layout.registrationLayout>