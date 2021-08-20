<#import "template.ftl" as layout>
<@layout.registrationLayout bodyClass="template-body login-body" displayInfo=social.displayInfo displayWide=(realm.password && social.providers??); section>
    <#if section = "header">
        <div class="login-logo">${msg("doLogIn")}</div>
    <#elseif section = "form">
    <div id="kc-form" <#if realm.password && social.providers??>class="${properties.kcContentWrapperClass!}"</#if>>
    <div id="kc-form-wrapper" <#if realm.password && social.providers??>class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}"<#else>style="max-width: 380px; margin: auto auto; padding-left: 10px;"</#if>>
        <#if realm.password>
            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!}">
                    <label for="username" class="${properties.kcLabelClass!}"><#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else>${msg("email")}</#if></label>
                    <#if usernameEditDisabled??>
                        <input tabindex="1" id="username" class="${properties.kcInputClass!}<#if message?has_content> has-${message.type}</#if>" name="username" value="${(rememberEmail!'')}" placeholder="<#if !realm.loginWithEmailAllowed>${msg('userIdInput')}<#elseif !realm.registrationEmailAsUsername>${msg('userIdInput')}<#else>${msg('email')}</#if>" type="email" onkeyup="removeError(this, event)" disabled />
                    <#else>
                        <input tabindex="1" id="username" class="${properties.kcInputClass!}<#if message?has_content> has-${message.type}</#if>" name="username" value="${(rememberEmail!'')}" placeholder="<#if !realm.loginWithEmailAllowed>${msg('userIdInput')}<#elseif !realm.registrationEmailAsUsername>${msg('userIdInput')}<#else>${msg('email')}</#if>"  type="email" onkeyup="removeError(this, event)" autofocus autocomplete="off" />
                    </#if>
                </div>

                <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                    <div id="kc-form-options">
                        <#if realm.rememberMe && !usernameEditDisabled??>
                            <div class="checkbox remember-me">
                                <#if login.rememberMe??>
                                    <input tabindex="3" id="rememberMe" name="rememberMe" type="checkbox" checked>
                                <#else>
                                    <input tabindex="3" id="rememberMe" name="rememberMe" type="checkbox">
                                </#if>
                                <label for="rememberMe">${msg("rememberMe")}</label>
                            </div>
                            <div class="checkbox remember-email">
                                <#if login.rememberMe??>
                                    <input tabindex="3" id="rememberEmail" name="rememberEmail" type="checkbox" checked>
                                <#else>
                                    <input tabindex="3" id="rememberEmail" name="rememberEmail" type="checkbox">
                                </#if>
                                <label for="rememberEmail">${msg("rememberEmail")}</label>
                            </div>
                        </#if>
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormGroupClass!}">
                <input tabindex="4" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg("doLogIn")}"/>
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
    <#elseif section = "info" >
        <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
            <div id="kc-registration">
                <span>${msg("noAccount")} <a tabindex="6" href="${url.registrationUrl}">${msg("doRegister")}</a></span>
            </div>
        </#if>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/node_modules/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/login.js"></script>
    <script type="text/javascript">
        if ($(window).width() < 500 ) {
            $('input:checkbox[id="rememberMe"]').attr("checked", true);
            $('input:checkbox[id="rememberEmail"]').attr("checked", false);
        }
        else {
            $('input:checkbox[id="rememberMe"]').attr("checked", false);
            $('input:checkbox[id="rememberEmail"]').attr("checked", true);
        }
    </script>

</@layout.registrationLayout>
