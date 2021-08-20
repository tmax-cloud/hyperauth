<#import "template-hyperspace-cnu.ftl" as layout>
<@layout.registrationLayout bodyClass="login-body login-body-hyperspace" displayInfo=social.displayInfo displayWide=(realm.password && social.providers??); section>
    <#if section = "header">
        <div class="login-logo-wrapper">
            <div class="login-logo-wapl">
                <#--  <div class="login-logo-image"></div>  -->
                <img class="cnu-symbol" src="${url.resourcesPath}/img/cnu_symbol.jpg"/>
                <img class="cnu-logo" src="${url.resourcesPath}/img/cnu_logo.jpg"/>
            </div>
        </div>
    <#elseif section = "form">
        <div id="kc-form" <#if realm.password && social.providers??>class="${properties.kcContentWrapperClass!}"</#if>>
            <div id="kc-form-wrapper" <#if realm.password && social.providers??>class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}"</#if>>
        <#--  <div id="kc-form">
            <div id="kc-form-wrapper">  -->
                <#if realm.password>
                    <form id="kc-form-login" onsubmit="return false;" action="${url.loginAction}" method="post">
                        <div class="${properties.kcFormGroupClass!}" id="idWrapper">
                            <#if usernameEditDisabled??>
                                <input tabindex="1" id="username" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" name="username" value="${(rememberEmail!'')}" placeholder="<#if !realm.loginWithEmailAllowed>${msg('username')}<#elseif !realm.registrationEmailAsUsername>${msg('usernameOrEmail')}<#else>${msg('email')}</#if>" type="text" onkeyup="validInput(this, event)" disabled />
                            <#else>
                                <input tabindex="1" id="username" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" name="username" value="${(rememberEmail!'')}" placeholder="<#if !realm.loginWithEmailAllowed>${msg('username')}<#elseif !realm.registrationEmailAsUsername>${msg('usernameOrEmail')}<#else>${msg('email')}</#if>"  type="text" onkeyup="validInput(this, event)"autofocus autocomplete="off" />
                            </#if>
                        </div>
                        <div class="${properties.kcFormGroupClass!}" id="passwordWrapper">
                            <input tabindex="2" id="password" class="${properties.kcInputClass!}<#if message?has_content> ${message.type}</#if>" name="password" placeholder="${msg('password')}" type="password" autocomplete="off" onkeyup="validInput(this, event)"/>
                             <#--  <div class="hyperspace-password hide-password"></div>  -->
                        </div>
                        <div>
                            <#if message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                                <div class="alert alert-${message.type}">
                                    <span id="result-message-section" class="kc-feedback-text"><script>removeDuplicatedMessage('${kcSanitize(message.summary)}');</script></span>
                                </div>
                            <#else>
                                <div class="empty-error"></div>
                            </#if>
                        </div>
                        <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                            <div id="kc-form-options">
                                <#if realm.rememberMe && !usernameEditDisabled??>
                                    <div class="checkbox remember-me">
                                        <input tabindex="3" id="rememberMe" name="rememberMe" type="checkbox">
                                        <label for="rememberMe">${msg("rememberMe")}</label>
                                    </div>
                                    <div class="checkbox remember-email">
                                        <input tabindex="3" id="rememberEmail" name="rememberEmail" type="checkbox" checked>
                                        <label for="rememberEmail">${msg("rememberEmail")}</label>
                                    </div>
                                </#if>
                            </div>
                        </div>
                        <div id="kc-form-buttons" class="${properties.kcFormGroupClass!}">
                            <input type="hidden" id="id-hidden-input" name="credentialId" <#if auth.selectedCredential?has_content>value="${auth.selectedCredential}"</#if>/>
                            <input tabindex="4" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="button" value="${msg("doLogIn")}" onclick="submitClick()" disabled/>
                        </div>
                    </form>
                </#if>
            </div>
            <#if realm.password && social.providers??>
                <div id="kc-social-providers" class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}">
                    <ul class="${properties.kcFormSocialAccountListClass!} <#if social.providers?size gt 4>${properties.kcFormSocialAccountDoubleListClass!}</#if>">
                        <#list social.providers as p>
                            <li class="${properties.kcFormSocialAccountListLinkClass!}"><a href="${p.loginUrl}" id="zocial-${p.alias}" class="zocial ${p.providerId}"> <span>${p.displayName} ${msg("doLogIn")}</span></a></li>
                        </#list>
                    </ul>
                </div>
            </#if>
        </div>

        <div class="wapl-option-wrapper">
            <#if realm.resetPasswordAllowed>
                <div  id="kc-forgot-pw" class="${properties.kcFormOptionsWrapperClass!}">
                    <span><a tabindex="5" href="${url.loginResetCredentialsUrl}">${msg("doForgotPassword")}</a></span>
                </div>         
            </#if>
           <#if realm.resetPasswordAllowed && realm.registrationAllowed>
                <div class="kc-border">|</div>
            </#if>
            <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                <div id="kc-registration"> 
                    <span><a tabindex="6" href="${url.registrationUrl}">${msg("doRegister")}</a></span>
                </div>
            </#if> 
        </div>
        <div class="kc-info-text-wrapper">
            <span class="kc-info-text">${msg("CM_UNSUPPORTED_BROWSER_02")}</span>
        </div>    
    </#if>
 <script type="text/javascript" src="${url.resourcesPath}/node_modules/jquery/dist/jquery.min.js"></script>
 <script type="text/javascript" src="${url.resourcesPath}/js/loginLogic.js"></script>
 <script type="text/javascript">
    if ($(window).width() < 500 ) {
        $('input:checkbox[id="rememberMe"]').attr("checked", true);
    }
    else {
        $('input:checkbox[id="rememberMe"]').attr("checked", false);
    }
 </script>
</@layout.registrationLayout>
