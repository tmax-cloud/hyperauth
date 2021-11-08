<#macro mainLayout active bodyClass>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <title>${msg("accountManagementTitle")}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico">
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.styles?has_content>
        <#list properties.styles_template?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script type="text/javascript" src="${url.resourcesPath}/${script}?${properties.version}"></script>
        </#list>
    </#if>
</head>
<body class="account-console user ${bodyClass}">
    <header class="navbar navbar-default navbar-pf navbar-main header">
        <nav class="navbar" role="navigation">
            <div class="navbar-header">
                <div class="container">
                    <h1 class="navbar-title">Tmax</h1>
                    <#--  <div class="navbar-bg-left"></div>  -->
                    <!--<div class="navbar-bg-right"></div>-->
                </div>
            </div>
            <div class="navbar-collapse navbar-collapse-1">
                <div class="container">
                    <ul class="nav navbar-nav navbar-utility">
                         <#if referrer?has_content && referrer.url?has_content && referrer.name == '${' + "client_security-admin-console" + '}'>
                            <li>
                                <a href="${referrer.url}" id="referrer"><div class="icon ic-admin-console"></div>${msg("backTo",referrer.name)}</a>
                            </li>
                         </#if>
                         <#if realm.internationalizationEnabled>
                            <li>
                                <div class="kc-dropdown" id="kc-locale-dropdown">
                                    <a href="#" id="kc-current-locale-link"><div class="icon ic-language"></div><div id="language-text">${locale.current}</div><div id="language-arrow"></div></a>
                                    <ul>
                                        <#list locale.supported as l>
                                            <li class="kc-dropdown-item">
                                                <a href="${l.url}">${l.label}</a>
                                            </li>
                                        </#list>
                                    </ul>
                                </div>
                            </li>
                         </#if>
                         <li>
                            <a href="${url.logoutUrl}"><!--<div class="icon ic-sign-out"></div>-->${msg("doSignOut")}</a>
                         </li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>

    <div class="container">
        <div class="bs-sidebar">
            <ul>
                <li class="<#if active=='account'>active</#if>"><a href="${(url.accountUrl!'')}"><p id="account_icon"></p><p class="title">${msg("account")}</p></a></li>
                <!--<#if features.passwordUpdateSupported><li class="<#if active=='password'>active</#if>"><a href="${url.passwordUrl}"><p id="password_icon"></p><p class="title">${msg("password")}</p></a></li></#if>-->
                <li class="<#if active=='password'>active</#if>"><a href="${(url.passwordUrl!'')}"><p id="password_icon"></p><p class="title">${msg("passwordChange")}</p></a></li>
                <li class="<#if active=='additionalAuth'>active</#if>"><a href="${(url.additionalAuthUrl!'')}"><p id="addtionalAuth_icon"></p><p class="title">${msg("additionalAuth")}</p></a></li>
                <!--<li class="<#if active=='totp'>active</#if>"><a href="${(url.totpUrl!'')}"><p id="totp_icon"></p><p class="title">${msg("authenticator")}</p></a></li>-->
                <!--<#if features.identityFederation><li class="<#if active=='social'>active</#if>"><a href="${url.socialUrl}"><p id="social_icon"></p><p class="title">${msg("federatedIdentity")}</p></a></li></#if>-->
                <li class="<#if active=='social'>active</#if>"><a href="${(url.socialUrl!'')}"><p id="social_icon"></p><p class="title">${msg("federatedIdentity")}</p></a></li>
                <li id="agreement_tab" class="<#if active=='agreement'>active</#if>"><a href="${(url.agreementUrl!'')}"><p id="aggrement_icon"></p><p class="title">${msg("agreement")}</p></a></li>
                <li class="<#if active=='sessions'>active</#if>"><a href="${url.sessionsUrl}"><p id="sessions_icon"></p><p class="title">${msg("sessionsManage")}</p></a></li>
<#--                <li class="<#if active=='applications'>active</#if>"><a href="${url.applicationsUrl}"><p class="title">${msg("applications")}</p></a></li>-->
<#--                <#if features.log><li class="<#if active=='log'>active</#if>"><a href="${url.logUrl}"><p class="title">${msg("log")}</p></a></li></#if>-->
<#--                <#if realm.userManagedAccessAllowed && features.authorization><li class="<#if active=='authorization'>active</#if>"><a href="${url.resourceUrl}"><p class="title">${msg("myResources")}</p></a></li></#if>-->
            </ul>
        </div>
        <div class = "content-area-wrapper">
        <div class="content-area">
            <!--<#if message?has_content>
                <div class="alert alert-${message.type}">
                    <#if message.type=='success' ><span class="pficon pficon-ok"></span></#if>
                    <#if message.type=='error' ><span class="pficon pficon-error-circle-o"></span></#if>
                    <span class="kc-feedback-text">${kcSanitize(message.summary)?no_esc}</span>
                </div>
            </#if>-->
            <script type="text/javascript" src="${url.resourcesPath}/js/template.js?${properties.version}"></script>
            <#if message?has_content>                
                <div id="alertModal" class="modal">
                    <div class="md_overlay"></div>
                    <div class="md_content">
                        <div class="md_content__header">
                            <span class="md_content__header__title">
                                ${msg("doSave")}
                            </span>
                            <span class="md_content__header__close" onclick="closeAlertModal()"></span>
                        </div>
                        <hr>
                        <div class="md_content__text">
                            ${kcSanitize(message.summary)?no_esc}
                        </div>
                        <div class="md_content__button">                        
                            <div id="button-ok" class="button modal_button_right" onclick="closeAlertModal()">
                                ${msg("doOK")}
                            </div>
                        </div>
                    </div>
                </div>            
            </#if>

            <#nested "content">
        </div>
        </div>
    </div>
    <#if properties.scripts_template_hyperauth?has_content>
        <#list properties.scripts_template_hyperauth?split(' ') as script>
            <script type="text/javascript" src="${url.resourcesPath}/${script}?${properties.version}"></script>
        </#list>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/js/template.js?${properties.version}"></script>
</body>
</html>
</#macro>

<#macro contentHeader required=false allRequired=false>
    <div class="content-header">
        <div class="title">
            <h2>
                <#nested "headerText">
            </h2>
        </div>
        <#if !!required>
        <div class="subtitle">
            <div class="subtitleText"><span class="ic-required"></span> ${msg("requiredFields")}</div>
        </div>
        </#if>
        <#if !!allRequired>
        <div class="subtitle">
            <div class="subtitleText">${msg("allFieldsRequired")}</div>
        </div>
        </#if>
    </div>
</#macro>

<#macro formGroup key msgKey=key labelText="" required=false formGroupClass="">
    <div class="form-group ${formGroupClass}">
        <div class="label">
            <label for="${key}" class="control-label">
                <#if labelText?length == 0>
                    ${msg("${msgKey}")}
                <#else>
                    ${labelText}
                </#if>
            </label>
            <#if !!required>
            <span class="ic-required"></span>
            </#if>
        </div>

        <div class="control">
            <#nested "inputControl">
        </div>
    </div>
</#macro>

<#macro formButtonGroup>
    <div class="form-group buttons">
        <#nested "submitButton">
    </div>
</#macro>


<#macro cancelModal>
    <script type="text/javascript" src="${url.resourcesPath}/js/template.js?${properties.version}"></script>
    <div id="cancelModal" class="modal hidden">
        <div class="md_overlay"></div>
        <div class="md_content">
            <div class="md_content__header">
                <span class="md_content__header__title">
                    ${msg("cancelModalTitle")}
                </span>
                <span class="md_content__header__close" onclick="closeCancelModal(); return false;"></span>
            </div>
            <hr>
            <div class="md_content__text">
                ${msg("cancelModalMessage")}
                <br>
                ${msg("cancelModalMessage2")}
            </div>
            <div class="md_content__button">
                <div id="button-cancel" class="button modal_button_left" onclick="closeCancelModal(); return false;">
                    ${msg("doCancel")}
                </div>
                <#nested "OKButton">
                <!--<div id="button-ok" class="button modal_button_right" onclick="cancelChangeName()">
                    ${msg("doOK")}
                </div>-->
            </div>
        </div>
    </div>    
</#macro>