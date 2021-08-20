<#import "template.ftl" as layout>
<@layout.registrationLayout bodyClass="template-body register-body"; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_template_register?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if section = "back-button">
      <div class="arrow-left" onclick="clickBackButtonOfRegister()"></div>
    <#elseif section = "form">
        <div id="step1" style="display: block;">
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
                <div class="term" style="display:none;" id="account_privacy_duty_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_3" onclick="clickAgreeInput('check_3')">
                        <label for="check_3">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_11")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="account_terms-arrow" onclick="clickArrow('account_terms-arrow')"></div>
                </div>
                <div class="term" style="display:none;" id="account_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_4" onclick="clickAgreeInput('check_4')">
                        <label for="check_4">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_12")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="service_terms-arrow" onclick="clickArrow('service_terms-arrow')"></div>
                </div>
                <div class="term" style="display:none;" id="service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_5" onclick="clickAgreeInput('check_5')">
                        <label for="check_5">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_13")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="privacy_terms-arrow" onclick="clickArrow('privacy_terms-arrow')"></div>
                </div>
                <div class="term" style="display:none;" id="privacy_terms" disabled></div>
                 <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_6" onclick="clickAgreeInput('check_6')">
                        <label for="check_6">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_17")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="third_privacy_terms-arrow" onclick="clickArrow('third_privacy_terms-arrow')"></div>
                </div>
                <div class="term" style="display:none;" id="third_privacy_terms" disabled></div>
                <div class="agree">
                    <div>
                        <input type="checkbox" name="agree" id="check_7" onclick="clickAgreeInput('check_7')">
                        <label for="check_7">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>

                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <input id="agree_button" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="button" value="${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_8")}" onclick="clickAgreeButton()" disabled={true} />
                    </div>
                </div>
            </div>
        </div>
        <div id="step2" style="display: none">
            <div class="header-icon register">
                ${msg("MSG_CREATEACCOUNT_USERINFOINPUT_1")}
            </div>
            <form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.registrationAction}" method="post" onsubmit="validateInputFields(); return false;">
                <div class="form-group-wrapper">

                    <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('email',properties.kcFormGroupErrorClass!)} required">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="email" class="${properties.kcLabelClass!}">${msg("MSG_FINDPASSWORD__3")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="email" class="${properties.kcInputClass!}" name="email" value="${(register.formData.email!'')}" autocomplete="email" placeholder="${msg("MSG_FINDPASSWORD__4")}" onkeyup="removeEmailValidationErrors()" onblur="validateEmail()"/>
                        </div>
                        <div class="${properties.kcInputWrapperClass!} error_message" id="error_email_format" style="display: none">
                            ${msg("MSG_ERROR_FINDPASSWORD_1")}
                        </div>
                        <div class="${properties.kcInputWrapperClass!} error_message" id="error_email_exist" style="display: none">
                            ${msg("createAccountExistEmailMessage")}
                        </div>
                        <#--  <div class="${properties.kcLabelWrapperClass!}">
                            <label for="email" class="${properties.kcLabelClass!}">${msg("MSG_FINDPASSWORD__3")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!} has-button">
                            <input type="text" id="email" class="${properties.kcInputClass!} small" name="email" value="${(register.formData.email!'')}" autocomplete="email"
                            placeholder="${msg("MSG_FINDPASSWORD__4")}" onkeyup="emailChange('email')"
                             />
                            <div class="${properties.kcFormGroupClass!}">
                                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">

                                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!} ${properties.kcFormSendButtonsClass!}" type="button" id="verificate_email" onclick="clickVerificationButton('verificate_email')" value="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_2")}" disabled={true} />

                                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!} ${properties.kcFormSendButtonsClass!}" type="button" id="resend_email" onclick="clickVerificationButton('resend_email')" value="${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_5")}" style="display: none" />

                                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!} ${properties.kcFormSendButtonsClass!}" type="button" id="verification_complete" value="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_9")}" style="display: none" disabled={true} />

                                </div>
                            </div>
                        </div>
                        <div class="error_message" id="error_email_not_done" style="display: none">
                            ${msg("MSG_ERROR_EMAIL_3")}
                        </div>
                        <div class="error_message" id="error_email_format" style="display: none">
                            ${msg("MSG_ERROR_FINDPASSWORD_1")}
                        </div>
                        <div class="error_message" id="error_email_exist" style="display: none">
                            ${msg("createAccountExistEmailMessage")}
                        </div>
                        <div class="error_message" id="error_email_send_failed" style="display: none">
                            ${msg("createAccountEmailSendFailedMessage")}
                        </div>
                        <div id="hidden_verification" style="display:none">
                            <div class="${properties.kcLabelWrapperClass!} description">
                                <div>
                                    ${msg("MSG_CREATEACCOUNT_USERINFOINPUT_8_1")}
                                </div>
                                <div>
                                    ${msg("MSG_CREATEACCOUNT_USERINFOINPUT_8_2")}
                                </div>
                            </div>
                            <div class="${properties.kcInputWrapperClass!} has-button">
                                <input type="text" id="verification" class="${properties.kcInputClass!} small" name="verification"
                                placeholder="${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_3")}" onkeyup="verificationChange('verification')"
                                />
                                <div class="${properties.kcFormGroupClass!}">
                                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!} ${properties.kcFormSendButtonsClass!}" type="button" id="verification_check_email" onclick="clickVerificationCheckButton()" value="${msg("MSG_FINDPASSWORD_CONFIRMVERIFICATIONCODE_1")}" disabled={true}/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="error_message" id="error_email_wrong_code" style="display: none">
                            ${msg("MSG_ERROR_VERIFICATIONCODE_1")}
                        </div>
                        <div class="error_message" id="error_email_time_expired" style="display: none">
                            ${msg("createAccountEmailTimeExpiredMessage")}
                        </div>  -->
                    </div>
                    
                    <#if passwordRequired??>
                        <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password',properties.kcFormGroupErrorClass!)} required">
                            <div class="${properties.kcLabelWrapperClass!}">
                                <label for="password" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_3")}</label>
                            </div>
                            <div class="${properties.kcInputWrapperClass!}">
                                <input type="password" id="password" class="${properties.kcInputClass!} ${properties.kcShortBottomInputClass!}" name="password" autocomplete="new-password" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_3")}" onkeyup="removePwdValidationErrors()" onblur="validatePassword()"/>
                                <div id="eye-password" class="eye" onclick="clickEye(this)">
                                </div>
                            </div>
                            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_empty" style="display: none">
                                ${msg("MSG_ERROR_PASSWORD_1")}
                            </div>
                            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_length" style="display: none">
                                ${msg("MSG_ERROR_PASSWORD_2")}
                            </div>
                            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_type" style="display: none">
                                ${msg("MSG_ERROR_PASSWORD_3")}
                            </div>
                        </div>

                        <div class="${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password-confirm',properties.kcFormGroupErrorClass!)} required">
                            <div class="${properties.kcLabelWrapperClass!}">
                                <label for="password-confirm" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_4")}</label>
                            </div>
                            <div class="${properties.kcInputWrapperClass!}">
                                <input type="password" id="password-confirm" class="${properties.kcInputClass!}" name="password-confirm" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_4")}" onkeyup="removePwdConfirmValidationErrors()" onblur="validatePasswordConfirm()" />
                                <div id="eye-password-confirm" class="eye" onclick="clickEye(this)">
                                </div>
                            </div>
                            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_confirm_empty" style="display: none">
                                ${msg("MSG_ERROR_CONFIRMPASSWORD_1")}
                            </div>
                            <div class="${properties.kcInputWrapperClass!} error_message" id="error_password_confirm_match" style="display: none">
                                ${msg("MSG_ERROR_CONFIRMPASSWORD_2")}
                            </div>
                        </div>
                    </#if>

                    <div class="${properties.kcFormGroupClass!} required">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="user.attributes.user_name" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_5")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.user_name" class="${properties.kcInputClass!}" name="user.attributes.user_name" value="${(register.formData['user.attributes.user_name']!'')}"
                            placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_6")}" onkeyup="removeUsernameValidationErrors()" onblur="validateUserName()"/>
                        </div>
                        <div class="${properties.kcInputWrapperClass!} error_message" id="error_username_empty" style="display: none">
                            ${msg("MSG_ERROR_USERNAME_1")}
                        </div>
                    </div>

                    <#--  <hr>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_10")}</label>
                        </div>
                        <div class="${properties.kcLabelWrapperClass!}">
                            <input type="radio" id="not_selected" name="user.attributes.gender" value="unselected" checked>
                            <label for="not_selected">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_11")}</label>
                            <input type="radio" id="male" name="user.attributes.gender" value="male">
                            <label for="male">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_12")}</label>
                            <input type="radio" id="female" name="user.attributes.gender" value="female">
                            <label for="female">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_13")}</label>
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="address" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_14")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.address" class="${properties.kcInputClass!} ${properties.kcShortBottomInputClass!}" name="user.attributes.address" value="${(register.formData['user.attributes.address']!'')}" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_15")}"
                            />
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.addressDetail" class="${properties.kcInputClass!}" name="user.attributes.addressDetail" value="${(register.formData['user.attributes.addressDetail']!'')}" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_16")}"
                            />
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="user.attributes.companyName" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_17")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.companyName" class="${properties.kcInputClass!}" name="user.attributes.companyName" value="${(register.formData['user.attributes.companyName']!'')}"  />
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="user.attributes.department" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_18")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.department" class="${properties.kcInputClass!}" name="user.attributes.department" value="${(register.formData['user.attributes.department']!'')}"  />
                        </div>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <div class="${properties.kcLabelWrapperClass!}">
                            <label for="user.attributes.phone" class="${properties.kcLabelClass!}">${msg("MSG_CREATEACCOUNT_USERINFOINPUT_19")}</label>
                        </div>
                        <div class="${properties.kcInputWrapperClass!}">
                            <input type="text" id="user.attributes.phone" class="${properties.kcInputClass!}" name="user.attributes.phone" value="${(register.formData['user.attributes.phone']!'')}" placeholder="${msg("MSG_CREATEACCOUNT_USERINFOINPUT_20")}"  />
                        </div>
                    </div>

                    <#if recaptchaRequired??>
                    <div class="form-group">
                        <div class="${properties.kcInputWrapperClass!}">
                            <div class="g-recaptcha" data-size="compact" data-sitekey="${recaptchaSiteKey}"></div>
                        </div>
                    </div>
                    </#if>
                    -->
                </div> 

                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" id="register_button" value="${msg("sendVerificationEmail")}" />
                    </div>
                </div>
            </form>
        </div>
        <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
        <script type="text/javascript" src="${url.resourcesPath}/js/register.js"></script>
        <#if properties.scripts_register_hyperauth?has_content>
            <#list properties.scripts_register_hyperauth?split(' ') as script>
                <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
            </#list>
        </#if>
        <#if !(url.selectedTheme == 'hyperauth' || url.selectedTheme == 'CNU')>
            <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
                <script>getTerms("${(locale.currentLanguageTag!'ko')}");</script>
            <#else>
                <script>getTerms("ko");</script>
            </#if>
        </#if>
    </#if>
</@layout.registrationLayout>
