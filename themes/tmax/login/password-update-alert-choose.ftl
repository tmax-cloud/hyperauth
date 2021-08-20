<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
         <div class="header-icon email-sent">
            ${msg("passwordUpdateTitle")}
        </div>
    <#elseif section = "form">
         <div id="change-password">
             <div class="${properties.kcFormGroupClass!}">
                <div class="change-password-img"></div>
                <p id="instruction">
                     ${msg("changePasswordMessage1")?no_esc}
                </p>
            </div>
            <form id="change-password-form" onsubmit="return false;" action="${url.loginAction}" method="post">
                 <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormHalfButtonsClass!} padding-horizontal">
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} btn-white-background" id="withdrawal_cancel_button" onclick="changeLater()">${msg("changePasswordLater")}</button>
                    </div>
                    <div id="kc-form-buttons" class="${properties.kcFormHalfButtonsClass!} padding-horizontal">
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" id="go_to_home_button" onclick="changePassword()">${msg("changePassword")}</button>
                    </div>
                </div>
            </form>
        </div>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/node_modules/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript">
        function changeLater() {
            const input = $("<input>", { type: "hidden", id: "passwordUpdateSkip", name: "passwordUpdateSkip", value: "t" }); $('#change-password-form').append($(input));
            document.getElementById("change-password-form").submit();
        }
        function changePassword() {
            const input = $("<input>", { type: "hidden", id: "passwordUpdateSkip", name: "passwordUpdateSkip", value: "f" }); $('#change-password-form').append($(input));
            document.getElementById("change-password-form").submit();
        }
    </script>
</@layout.registrationLayout>