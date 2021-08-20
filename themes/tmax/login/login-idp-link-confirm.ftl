<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        <#--  ${msg("confirmLinkIdpTitle")}  -->
    <#elseif section = "form">
        <div id="login-idp-link-confirm">
            <form id="kc-idp-link-form" action="${url.loginAction}" method="post" >
                <div class="${properties.kcFormGroupClass!}">
                    <div class="link-with-kakao-icon"></div>
                </div>
                <div class="${properties.kcFormGroupClass!}">
                    <div id="instruction-container">
                        <span id="email-address">
                            
                        </span>
                        <span id="instruction" >
                            ${msg("MSG_LINKACCOUNT_MEMBER_1")?no_esc}
                            </br></br>
                            ${msg("MSG_LINKACCOUNT_MEMBER_2")?no_esc}
                        </span>
                    </div>
                </div>
                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!} padding-horizontal">
                        <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" name="submitAction" id="linkAccount" value="linkAccount" onclick="setButtonDisabledAndSubmit(); return false;">${msg("MSG_LINKACCOUNT_MEMBER_3")?no_esc}</button>
                    </div>
                </div>
            </form>
        </div>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/node_modules/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript">
        if (!!document.getElementById("email-address")) {
            document.getElementById(
                "email-address"
            ).innerText = window.sessionStorage.getItem("idp-user-email");
        }

        function setButtonDisabledAndSubmit() {
            document.getElementById("linkAccount").disabled = true;
            const input = $("<input>", { type: "hidden", id: "linkAccount", name: "submitAction", value: "linkAccount" }); 
            $('#kc-idp-link-form').append($(input));
            document.getElementById("kc-idp-link-form").submit();
        }
    </script>
</@layout.registrationLayout>
