<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
   <#if section = "header">
         <div class="header-icon withdrawal-cancel">
            ${msg("withdrawalCancelTitle")}
        </div>
    <#elseif section = "form">
         <div id="withdrawal-cancel">
            <form id="withdrawal-cancel-form" onsubmit="return false;" action="${url.loginAction}" method="post">
                <div class="${properties.kcFormGroupClass!}">
                <div id = "script-wraper">
                    <div id="deletionDate">
                        ${msg("withdrawalDeletionDate")}${(deletionDate!'')}
                    </div>
                    <p id="instruction" >
                        ${msg("withdrawalMessage1")?no_esc}<span style='color: #000000;font-weight:500;'> ${msg("withdrawalMessage2")}</span>
                        ${msg("withdrawalMessage3")?no_esc}
                    </p>
                </div>
                </div>
                <div class="${properties.kcFormGroupClass!}">
                <div id="withdrawal-cancel-button-wraper">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!} padding-horizontal">
                        <button class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!}" id="withdrawal_cancel_button" onclick="openModal()">${msg("withdrawalCancel")}</button>
                    </div>
                    </div>
                </div>
                <div class="modal hidden">
                    <div class="md_overlay"></div>
                    <div class="md_content">
                        <div class="md_content__header">
                            <span class="md_content__header__title">
                                ${msg("withdrawalCancelModalTitle")}
                            </span>
                            <span class="md_content__header__close" onclick="closeModal()">
                            </span>
                        </div>
                        <hr>
                        <div class="md_content__text">
                            ${msg("withdrawalCancelModalMessage")}
                        </div>
                        <div class="md_content__button">
                            <div id="button-cancel" class="button modal_button_left" onclick="closeModal()">
                                ${msg("doCancel")}
                            </div>
                            <div id="button-ok" class="button modal_button_right" onclick="cancelWithdrawal()">
                                ${msg("doOk")}
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </#if>
    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/withdrawal-cancel.js?${properties.version}"></script>
</@layout.registrationLayout>