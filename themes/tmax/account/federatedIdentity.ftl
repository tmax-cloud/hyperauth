<#import "template.ftl" as layout>
<@layout.mainLayout active='social' bodyClass='social'; section>

    <@layout.contentHeader ; section>
        ${msg("federatedIdentitiesHtmlTitle")}
    </@layout.contentHeader>

     <div id = 'page-descript'>${msg("federatedIdentitiesHtmlSubTitle")}</div>
        <hr id='hr-top'>
    <div id="federated-identities" class="form-horizontal">

    <#list federatedIdentity.identities as identity>
        <@layout.formGroup key="${identity.providerId!}" labelText="${identity.displayName!}">
            <#--<input disabled="true" class="form-control" value="${identity.userName!}">-->
            <#if identity.connected>
                <#if federatedIdentity.removeLinkPossible>
                <div id = "sns-username-wrapper">
                        <div id ='sns-username'>${identity.userName!}</div>
              </div>
                    <form id="SNS-form" action="${url.socialUrl}" method="post" class="form-inline">
                    
                    <#--  <input id ='sns-username'  disabled="true" class="form-control" value="${identity.userName!}">  -->
                        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
                        <input type="hidden" id="action" name="action" value="remove">
                        <input type="hidden" id="providerId" name="providerId" value="${identity.providerId!}">
                       
                        
                        <button style = "width:128px;min-width:auto;"id="remove-link-${identity.providerId!}" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!}" onclick="openCheckModal(); return false;">${msg("doSNSRemove")}</button>
       
                    </form>
                </#if>
            <#else>
                <div style ="visibility:hidden">이름</div> 
                <form id="SNS-form" action="${url.socialUrl}" method="post" class="form-inline">
                    <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
                    <input type="hidden" id="action" name="action" value="add">
                    <input type="hidden" id="providerId" name="providerId" value="${identity.providerId!}">
                    <button  style = "width:128px;min-width:auto;" id="add-link-${identity.providerId!}" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!}">${msg("doSNSAdd")}</button>
                </form>
            </#if>
        </@layout.formGroup>
    </#list>
    </div>
    
    <div id="checkModal" class="modal hidden">
        <div class="md_overlay"></div>
        <div class="md_content">
            <div class="md_content__header">
                <span class="md_content__header__title">
                    ${msg("SNSModalTitle")}
                </span>
                <span class="md_content__header__close" onclick="closeCheckModal(); return false;"></span>
            </div>
            <hr>
            <div class="md_content__text">
                ${msg("SNSModalBody")}                
            </div>
            <div class="md_content__button">
                <div id="button-cancel" class="button modal_button_left" onclick="closeCheckModal(); return false;">
                    ${msg("doCancel")}
                </div>
                
                <div id="button-ok" class="button modal_button_right" onclick="submitUnSNS()">
                    ${msg("doUnSNS")}
                </div>
            </div>
        </div>
    </div>


<script type="text/javascript" src="${url.resourcesPath}/js/federatedIdentity.js"></script>
</@layout.mainLayout>
