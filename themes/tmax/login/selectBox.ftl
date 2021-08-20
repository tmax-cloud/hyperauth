<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        <div class="header-icon email-sent">
            로그인 수단 선택
        </div>
    <#elseif section = "form">
        <div id="secret-question">
            <form id="secret-question-form" action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!} marginTop">
                    <div class="${properties.kcLabelWrapperClass!}">
                        <label for="secret_answer" class="${properties.kcLabelClass!}">OTP : o , password : p</label>
                    </div>
                    <div class="${properties.kcInputWrapperClass!} marginBottom">
                        <input type="text" id="selection" name="selection" class="${properties.kcInputClass!}<#if message?has_content> has-${message.type}</#if>" autofocus onkeyup="removeError(this)"/>
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
                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" id="login-question" type="submit" />
                    </div>
                    <div id="kc-info-message" class="${properties.kcLabelWrapperClass!} infoText">
                        <#if client?? && client.baseUrl?has_content>
                            <p><a href="${client.baseUrl}" style="color: #042a54;">${msg("backToLogin")}</a></p>
                        </#if>
                    </div>
                </div>
            </form>
        </div>
    </#if>
    <script type="text/javascript">
        function removeError(input) {
            input.classList.remove("has-error");
        }
    </script>
</@layout.registrationLayout>