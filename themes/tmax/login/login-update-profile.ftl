<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_identity_provider?split(' ') as style>
            <link href="${url.resourcesPath}/${style}?${properties.version}" rel="stylesheet" />
        </#list>
    </#if>
    <#if section = "header">
        <#--  ${msg("loginProfileTitle")}  -->
    <#elseif section = "form">
        <div id="identity-step1" style="display: block;">
            <div id="identity-email">
                <form id="kc-identity-provider-form" class="${properties.kcFormClass!}" action="${url.loginAction}" onsubmit="return false;" method="post">
                    <#--  <#if user.editUsernameAllowed>
                        <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('username',properties.kcFormGroupErrorClass!)}">
                            <div class="${properties.kcLabelWrapperClass!}">
                                <label for="username" class="${properties.kcLabelClass!}">${msg("username")}</label>
                            </div>
                            <div class="${properties.kcInputWrapperClass!}">
                                <input type="text" id="username" name="username" value="${(user.username!'')}" class="${properties.kcInputClass!}"/>
                            </div>
                        </div>
                    </#if>  -->
                    <div class="${properties.kcFormGroupClass!}">
                        <div class="link-with-${providerId}-icon"></div>
                        <p id="instruction-title" >
                            ${msg("MSG_LINKACCOUNT_MESSAGE_1")?no_esc}
                        </p>
                    </div>
                    <div class="${properties.kcFormGroupClass!} marginTop">
                        <div class="${properties.kcInputWrapperClass!} marginBottom">
                            <label for="email" class="${properties.kcLabelClass!}">${msg("VendorAccount")}</label>
                            <input type="text" id="email" name="email" value="${(user.email!'')}" class="${properties.kcInputClass!}" autofocus onkeyup="validateEmail(this, event)" placeholder="${msg('email')}" onchange="validateEmail(this, event)" onpaste="validateEmail(this, event)"/>
                            <div class="${properties.kcInputWrapperClass!} error_message" id="error_email_format" style="display: none">
                                ${msg("MSG_ERROR_FINDPASSWORD_1")}
                            </div>
                            <div>
                                <#if message?has_content && (message.type != 'warning')>
                                    <div class="alert alert-${message.type}">
                                        <span class="kc-feedback-text">${kcSanitize(message.summary)?no_esc}</span>
                                    </div>
                                <#else>
                                    <div class="empty-error"></div>
                                </#if>
                            </div>
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('firstName',properties.kcFormGroupErrorClass!)}" style="display: none">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="firstName" class="${properties.kcLabelClass!}">${msg("firstName")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="hidden" id="firstName" name="firstName" value="${(user.firstName!'')}" class="${properties.kcInputClass!}" />
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('lastName',properties.kcFormGroupErrorClass!)}" style="display: none">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="lastName" class="${properties.kcLabelClass!}">${msg("lastName")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="hidden" id="lastName" name="lastName" value="${(user.lastName!'')}" class="${properties.kcInputClass!}" />
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('username',properties.kcFormGroupErrorClass!)}" style="display: none">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="username" class="${properties.kcLabelClass!}">${msg("username")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="username" name="username" value="${(user.username!'')}" class="${properties.kcInputClass!}"/>
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <#--  <#if isAppInitiatedAction??>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}" />
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" type="submit" name="cancel-aia" value="true" />${msg("doCancel")}</button>
                        <#else>  -->
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" id="next-button" type="button" onclick="checkEmailExist(); updateUsernameForm();" value="Next">${msg("doNext")}</button>
                        <#--  </#if>  -->
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div id="identity-step1-newAccount" style="display: none;">
            <div class="${properties.kcFormGroupClass!}">
                <div class="link-with-${providerId}-icon"></div>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <div id="instruction-container">
                    <span id="email-address">
                        
                    </span>
                    <span id="instruction" >
                        ${msg("MSG_LINKACCOUNT_NONMEMBER_1")?no_esc}
                    </span>
                    <div id = 'instruction2'>
                        ${msg("MSG_LINKACCOUNT_NONMEMBER_2")?no_esc}
                    </div>
                    
                    <p id="instruction3">
                        ${msg("MSG_LINKACCOUNT_NONMEMBER_3")?no_esc}
                    </p>
                </div>
            </div>
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!} padding-horizontal">
                    <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" id="next-button-new-account" type="button" onclick="checkNextButtonOfNewAccountPage()">${msg("doNext")}</button>
                </div>
            </div>
        </div>
        <div id="identity-step2" style="display: none;">
            <div class="header-icon register">
                ${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_1")}
            </div>
            <div>
                <div>
                    <input type="checkbox" id="check_all" onclick="clickAgreeInput('check_all')">
                    <label for="check_all">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_9")}</label>
                    <span class="arrow" onclick="clickArrow()"></span>
                </div>
                <div class="register-instruction">
                    ${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_10")}
                </div>
                <hr>
                <div class="${properties.kcFormGroupClass!}">
                     <div>
                        <input type="checkbox" name="agree" id="check_1" onclick="clickAgreeInput('check_1')">
                        <label for="check_1">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_15")}</label>
                    </div>
                     <div class="register-instruction-small">
                        ${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_20")?no_esc} (<a href="mailto:${msg('MSG_TEXT_SUPPORTMAIL_1')}">${msg("MSG_TEXT_SUPPORTMAIL_1")}</a>)
                    </div>
                </div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_2" onclick="clickAgreeInput('check_2')">
                        <label for="check_2">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_16")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="account_privacy_duty_terms-arrow" onclick="clickArrow('account_privacy_duty_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="account_privacy_duty_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_3" onclick="clickAgreeInput('check_3')">
                        <label for="check_3">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_11")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="account_terms-arrow" onclick="clickArrow('account_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="account_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_4" onclick="clickAgreeInput('check_4')">
                        <label for="check_4">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_12")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="service_terms-arrow" onclick="clickArrow('service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_5" onclick="clickAgreeInput('check_5')">
                        <label for="check_5">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_13")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="privacy_terms-arrow" onclick="clickArrow('privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="privacy_terms" disabled></div>
                 <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_6" onclick="clickAgreeInput('check_6')">
                        <label for="check_6">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_17")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="third_privacy_terms-arrow" onclick="clickArrow('third_privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="third_privacy_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_7" onclick="clickAgreeInput('check_7')">
                        <label for="check_7">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>

                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <input id="agree_button" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="button" value="${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_8")}" onclick="clickAgreeBottomButton()" disabled={true} />
                    </div>
                </div>
            </div>
        </div>
        <div id="identity-step3" style="display: none;">
            <div class="header-icon register">
                ${msg("MSG_CREATEACCOUNT_USERINFOINPUT_1")}
            </div>
            <form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post" onsubmit="validateInputFields(); return false;">
                <div class="form-group-wrapper">

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('email',properties.kcFormGroupErrorClass!)} required">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="email-for-new-account" class="${properties.kcLabelClass!}">${msg("email")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" style="background: #CCCCCC;" id="email-for-new-account" class="${properties.kcInputClass!}" name="email-for-new-account" placeholder="${msg("MSG_FINDPASSWORD__4")}" disabled/>
                        </div>
                    </div>
                    <#--  <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password',properties.kcFormGroupErrorClass!)} required">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="password" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_3")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="password" style="background: #CCCCCC;" id="password" class="${properties.kcInputClass!} ${properties.kcShortBottomInputClass!}" name="password" autocomplete="new-password" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_3")}" disabled/>
                        </div>
                    </div>
                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password-confirm',properties.kcFormGroupErrorClass!)} required">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="password-confirm" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_4")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="password" style="background: #CCCCCC;" id="password-confirm" class="${properties.kcInputClass!}" name="password-confirm" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_4")}" disabled/>
                        </div>
                    </div>  -->

                    <div class="${properties.kcFormGroupClass!} required">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="user.attributes.user_name" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_5")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <#if emailAsUserName>
                                <input type="text" style="background: #CCCCCC;" id="user.attributes.user_name" class="${properties.kcInputClass!}" name="user.attributes.user_name"
                                       placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_6")}" disabled/>
                            <#else>
                                <#if editUserNameAllowed>
                                    <input type="text" id="user.attributes.user_name" class="${properties.kcInputClass!}" name="user.attributes.user_name"
                                           placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_6")}" onkeyup="validateUserName(); updateHiddenInputValue()" onblur="validateUserName()"/>
                                <#else>
                                    <input type="text" style="background: #CCCCCC;" id="user.attributes.user_name" class="${properties.kcInputClass!}" name="user.attributes.user_name"
                                           placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_6")}" disabled/>
                                </#if>
                            </#if>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="hidden" id="input-username" name="input-username" value="${(user.username!'')}" class="${properties.kcInputClass!}"/>
                        </div>
                        <div class="${properties.kcInputWrapperClass!} error_message" id="error_username_empty" style="display: none">
                            ${msg("MSG_ERROR_USERNAME_1")}
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="input-first-name" class="${properties.kcLabelClass!}">${msg("firstName")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="input-first-name" name="input.first.name" class="${properties.kcInputClass!}" />
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="input-last-name" class="${properties.kcLabelClass!}">${msg("lastName")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="input-last-name" name="input.last.name" class="${properties.kcInputClass!}" />
                        </div>
                    </div>
                </div> 
                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <button style = "margin-top:200px"class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="button" id="sendVerificationEmailForNewAccountButton" onclick="sendVerificationEmailForNewAccount()" disabled>
                                ${msg("SignUp")}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/identity-provider.js?${properties.version}"></script>

    <#--to use ftl parameters in script blocks-->
    <#if editUserNameAllowed>
        <div class="${properties.kcFormGroupClass!}" style="display: none">
            <input type="hidden" id="editUserNameAllowed" name="editUserNameAllowed" value="true"/>
        </div>
    <#else>
        <div class="${properties.kcFormGroupClass!}" style="display: none">
            <input type="hidden" id="editUserNameAllowed" name="editUserNameAllowed" value="false"/>
        </div>
    </#if>
    <#if emailAsUserName>
        <div class="${properties.kcFormGroupClass!}" style="display: none">
            <input type="hidden" id="emailAsUserName" name="emailAsUserName" value="true"/>
        </div>
    <#else>
        <div class="${properties.kcFormGroupClass!}" style="display: none">
            <input type="hidden" id="emailAsUserName" name="emailAsUserName" value="false"/>
        </div>
    </#if>

    <script type="text/javascript">

        function updateHiddenInputValue() {
            var otherInputValue = document.getElementById('user.attributes.user_name').value;
            document.getElementById('input-username').value = otherInputValue;
        }

        var editUserNameAllowed = document.getElementById('editUserNameAllowed').value;
        var emailAsUserName = document.getElementById('emailAsUserName').value;

        function updateUsernameForm(){
            if(emailAsUserName == "true"){
                var emailForNewAccount = document.getElementById("email-for-new-account").value;
                document.getElementById('user.attributes.user_name').value = emailForNewAccount;
                document.getElementById('input-username').value = emailForNewAccount;
                document.getElementById(
                    "sendVerificationEmailForNewAccountButton"
                ).disabled = false;

            }else{
                document.getElementById('user.attributes.user_name').value = ${(user.username!'')}
                document.getElementById('input-username').value = ${(user.username!'')};
                if(editUserNameAllowed == "false"){
                    document.getElementById(
                        "sendVerificationEmailForNewAccountButton"
                    ).disabled = false;
                }
            }
        }
    </script>

    <#if properties.scripts_identity_provider_hyperauth?has_content>
        <#list properties.scripts_identity_provider_hyperauth?split(' ') as script>
            <script src="${url.resourcesPath}/${script}?${properties.version}" type="text/javascript"></script>
        </#list>
    </#if>
    <script type="text/javascript">
        window.sessionStorage.setItem("language", "${(locale.currentLanguageTag!'ko')}");
    </script>
</@layout.registrationLayout>
