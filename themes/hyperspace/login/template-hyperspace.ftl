<#macro registrationLayout bodyClass="template-body" displayInfo=false displayMessage=true displayRequiredFields=false displayWide=false showAnotherWayIfPresent=true backToLogin=false>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="${properties.kcHtmlClass!}">

<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
        </#list>
    </#if>
    <title>${msg("loginTitle",(realm.displayName!''))}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.svg?v=240718" />
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}?${properties.version}" type="text/javascript"></script>
        </#list>
    </#if>
    <#if properties.styles_template?has_content>
        <#list properties.styles_template?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <#if scripts??>
        <#list scripts as script>
            <script src="${script}?${properties.version}" type="text/javascript"></script>
        </#list>
    </#if>
</head>

<body class="${properties.kcBodyClass!}${bodyClass}">
  <div class="${properties.kcLoginClass!}">
    <div id="kc-header" class="${properties.kcHeaderClass!}">
       <#--  <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
            <div id="kc-locale">
                <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
                    <div class="kc-dropdown" id="kc-locale-dropdown">
                        <a href="#" id="kc-current-locale-link">${locale.current}</a>
                        <ul>
                            <#list locale.supported as l>
                                <li class="kc-dropdown-item"><a href="${l.url}">${l.label}</a></li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </div>
        </#if>  -->
    </div>
    <div class="${properties.kcFormCardClass!} <#if displayWide>${properties.kcFormCardAccountClass!}</#if>">
      <header class="${properties.kcFormHeaderClass!}">
        <#if backToLogin>
            <div id="kc-form-options" class="${properties.kcFormOptionsClass!} back-to-login">
                <div class="${properties.kcFormOptionsWrapperClass!}">
                    <span><a href="${url.loginUrl}"><span class="icon"></span>${kcSanitize(msg("backToLogin"))?no_esc}</a></span>
                </div>
            </div>
        </#if>
      </header>
      <div id="kc-content">
        <div id="kc-content-wrapper">
        <#if !(auth?has_content && auth.showUsername() && !auth.showResetCredentials())>
            <#if displayRequiredFields>
                <div class="${properties.kcContentWrapperClass!}">
                    <div class="${properties.kcLabelWrapperClass!} subtitle">
                        <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
                    </div>
                    <div class="col-md-10">
                        <h1 id="kc-page-title"><#nested "header"></h1>
                    </div>
                </div>
            <#else>
                <h1 id="kc-page-title"><#nested "header"></h1>
            </#if>
        <#else>
            <#if displayRequiredFields>
                <div class="${properties.kcContentWrapperClass!}">
                    <div class="${properties.kcLabelWrapperClass!} subtitle">
                        <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
                    </div>
                    <div class="col-md-10">
                        <#nested "show-username">
                        <div class="${properties.kcFormGroupClass!}">
                            <div id="kc-username">
                                <label id="kc-attempted-username">${auth.attemptedUsername}</label>
                                <a id="reset-login" href="${url.loginRestartFlowUrl}">
                                    <div class="kc-login-tooltip">
                                        <i class="${properties.kcResetFlowIcon!}"></i>
                                        <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            <#else>
                <#nested "show-username">
                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-username">
                        <label id="kc-attempted-username">${auth.attemptedUsername}</label>
                        <a id="reset-login" href="${url.loginRestartFlowUrl}">
                            <div class="kc-login-tooltip">
                                <i class="${properties.kcResetFlowIcon!}"></i>
                                <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                            </div>
                        </a>
                    </div>
                </div>
            </#if>
        </#if>
          <#-- App-initiated actions should not see warning messages about the need to complete the action -->
          <#-- during login.                                                                               -->
          <#--
          <#if displayMessage && message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
              <div class="alert alert-${message.type}">
                  <#if message.type = 'success'><span class="${properties.kcFeedbackSuccessIcon!}"></span></#if>
                  <#if message.type = 'warning'><span class="${properties.kcFeedbackWarningIcon!}"></span></#if>
                  <#if message.type = 'error'><span class="${properties.kcFeedbackErrorIcon!}"></span></#if>
                  <#if message.type = 'info'><span class="${properties.kcFeedbackInfoIcon!}"></span></#if>
                  <span class="kc-feedback-text">${kcSanitize(message.summary)?no_esc}</span>
              </div>
          </#if>
          -->

          <#nested "form">

          <#if auth?has_content && auth.showTryAnotherWayLink() && showAnotherWayIfPresent>
          <form id="kc-select-try-another-way-form" action="${url.loginAction}" method="post" <#if displayWide>class="${properties.kcContentWrapperClass!}"</#if>>
              <div <#if displayWide>class="${properties.kcFormSocialAccountContentClass!} ${properties.kcFormSocialAccountClass!}"</#if>>
                  <div class="${properties.kcFormGroupClass!}">
                    <input type="hidden" name="tryAnotherWay" value="on" />
                    <a href="#" id="try-another-way" onclick="document.forms['kc-select-try-another-way-form'].submit();return false;">${msg("doTryAnotherWay")}</a>
                  </div>
              </div>
          </form>
          </#if>

          <#if displayInfo>
              <div id="kc-info" class="${properties.kcSignUpClass!}">
                  <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                      <#nested "info">
                  </div>
              </div>
          </#if>
        </div>
      </div>

    </div>
    <footer class="${properties.kcFooterClass!}">
        
        <div class="copyright-wrapper">
            <div class="footer-tabs-lang-wrapper">
                <#if client.baseUrl?has_content>
                    <div class="footer-tab-wrapper">
                        <a id="terms-conditions-tab"  href="${client.baseUrl}/term-and-conditions" target="_blank">${msg("CM_TERMS_OF_USE")}</a>
                        <div class="kc-border">|</div>
                        <a id="privacy-policy-tab" href="${client.baseUrl}/privacy-policy" target="_blank">${msg("CM_PRIVACY_POLICY")}</a>
                        <div class="kc-border">|</div>
                        <a id="service-tab"  href="${client.baseUrl}/?force=true">${msg("CM_SERVICE_INTRO")}</a>
                    </div>
                </#if>
                <div class="locale-wrapper">
                    <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
                        <div id="kc-locale">
                            <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
                                <div class="kc-dropdown" id="kc-locale-dropdown">
                                    <a href="#" id="kc-current-locale-link">${locale.current}</a>
                                    <ul>
                                        <#list locale.supported as l>
                                            <li class="kc-dropdown-item"><a href="${l.url}">${l.label}</a></li>
                                        </#list>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </#if>
                </div>
            </div>
            <div class="footer-logo-copyright-wrapper">
                <div class="footer-logo"></div>
                <span class="footer-copyright">${msg("CM_COPYRIGHT")}</span>
            </div>
        </div>
    </footer>
  </div>
</body>
</html>
</#macro>
