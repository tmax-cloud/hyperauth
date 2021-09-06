<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_outer_2nd_factor_wait?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if section = "header">
        <div class="header-icon ip-blocked">
            ${msg("MSG_OUTER_2ND_FACTOR_1")}
        </div>
    <#elseif section = "form">
        <div id="opt-code">
            <body onload="startTimer('${(outerAuthExpAt!'')}', '${(outerAuthWaitTtl!'')}')">
                <form id="otp-code-form" action="${url.loginAction}" method="post">
                    <div class="${properties.kcFormGroupClass!}">
                        <p id="instruction" >
                            ${msg("MSG_OUTER_2ND_FACTOR_2")?no_esc}
                        </p>
                    </div>
                    <div class="${properties.kcFormGroupClass!} marginTop">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <a id="outer2ndFactorLink" href="${outerUrl}?user_name=${userName}&realm_name=${realmName}&tab_id=${tabId}&additional_param=${additionalParameter}" target = '_blank'  onclick="enableConfirmButton();">${msg("MSG_OUTER_2ND_FACTOR_3")}</a>
                            <div id="timer-input-container" class="timer-input-container"></div>
                        </div>
                        </br>
                        </br>
                        </br>
                    </div>
                    <div class="${properties.kcFormGroupClass!}">
                        <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                            <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" id="confirm-button" type="submit" value ="${msg("OUTER_2ND_FACTOR_CONFIRM")}" disabled/>
                        </div>
                        <div id="kc-info-message" class="${properties.kcLabelWrapperClass!} infoText">
                            <p>${msg("MSG_OUTER_2ND_FACTOR_4")}  <a id="resend-link" href="#" onclick="clickResendButton(document.location.href); return false;">${msg("MSG_OUTER_2ND_FACTOR_5")}</a></p>
                            <#if client?? && client.baseUrl?has_content>
                                <p>${msg("MSG_OUTER_2ND_FACTOR_6")}  <a href="${client.baseUrl}" style="color: #185692;">${msg("MSG_OUTER_2ND_FACTOR_7")}</a></p>
                            </#if>
                        </div>
                        <div class="modal hidden">
                            <div class="md_overlay"></div>
                            <div class="md_content">
                                <div class="md_content__header">
                                    <span class="md_content__header__title">
                                        ${msg("MSG_OUTER_2ND_FACTOR_POPUP_1")}
                                    </span>
                                    <span class="md_content__header__close" onclick="closeModal()">
                                    </span>
                                </div>
                                <hr>
                                <div class="md_content__text">
                                    ${msg("MSG_OUTER_2ND_FACTOR_POPUP_2")}
                                </div>
                                <div class="button" onclick="closeModal()">
                                    ${msg("MSG_OUTER_2ND_FACTOR_POPUP_3")}
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
                                    ${msg("outer2ndFactorTimeExpiredMessage")?no_esc}
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
    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/outer-2nd-factor-wait.js"></script>
</@layout.registrationLayout>