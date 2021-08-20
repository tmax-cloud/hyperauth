<#import "template.ftl" as layout>
<@layout.mainLayout active='agreement' bodyClass='user'; section>
    <#if properties.styles_template?has_content>
        <#list properties.styles_template_agreement?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <@layout.contentHeader required=false; section>
        ${msg("agreement")}
    </@layout.contentHeader>
    </br>
    ${msg("agreementHtmlBody")}
    </br>

    <script type="text/javascript" src="${url.resourcesPath}/js/axios.min.js"></script>
    <script type="text/javascript" src="${url.resourcesPath}/js/agreement.js"></script>

    <hr>    
    <div id="agreement" class="agreementBox">

    <@layout.formButtonGroup>
    <#assign agreementUrl = url.accountUrl?replace("^(.*)(/account/?)(\\?(.*))?$", "$1/console/agreement?$4", 'r') />
    <form id="agreementUrl" action="${agreementUrl}" class="form-horizontal" method="post" enctype="multipart/form-data">

        <div class="rowBox">
            <div class="col1"><div class="tmax-logo"></div></div>
            <div class="col2">                
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_16")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="account_privacy_duty_terms-arrow" onclick="clickArrow('account_privacy_duty_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="account_privacy_duty_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_11")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="account_terms-arrow" onclick="clickArrow('account_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="account_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_12")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="service_terms-arrow" onclick="clickArrow('service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_13")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="privacy_terms-arrow" onclick="clickArrow('privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="privacy_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_17")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="third_privacy_terms-arrow" onclick="clickArrow('third_privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="third_privacy_terms" disabled></div>
                <div class="agree">
                    <div>
                        <#if account.attributes.agreeMailOpt?has_content>
                        <input type="checkbox" name="agreeMailOpt" id="check_common" onclick="activateSaveButton();" <#if account.attributes.agreeMailOpt == "true">checked="true"</#if>>                        
                        </#if>                        
                        <label for="check_common">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>                
            </div>            
        </div>
        <script>getTerms("oneAccount", "${locale.currentLanguageTag}");</script>
        
        <#if account.attributes.agree_ischecked_hyperspace?has_content && account.attributes.agree_ischecked_hyperspace = 'true'>
        <hr id="wapl-agree-hr" class="">
   
        <div id="wapl-agree" class="rowBox">
            <div class="col1"><div class="wapl-logo"></div></div>
            <div class="col2">                
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_18")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="wapl_service_terms-arrow" onclick="clickArrow('wapl_service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="wapl_service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_19")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="wapl_privacy_terms-arrow" onclick="clickArrow('wapl_privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="wapl_privacy_terms" disabled></div>                
                <div class="agree">
                    <div>
                        <#if account.attributes.agreePerhyperspaceOpt?has_content>
                        <input type="checkbox" name="agreePerhyperspaceOpt" id="check_privacy_wapl" onclick="activateSaveButton();" <#if account.attributes.agreePerhyperspaceOpt == "true">checked="true"</#if>>                        
                        </#if>                        
                        <label for="check_privacy_wapl">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_19")}<span>${msg("optional")}</span></label>
                    </div>
                    <div class="arrow" id="wapl_privacy_optional_terms-arrow" onclick="clickArrow('wapl_privacy_optional_terms-arrow')"></div>                   
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="wapl_privacy_optional_terms" disabled></div>
                <div class="agree">
                    <div>
                        <#if account.attributes.agreeAdvhyperspaceOpt?has_content>
                        <input type="checkbox" name="agreeAdvhyperspaceOpt" id="check_wapl" onclick="activateSaveButton();" <#if account.attributes.agreeAdvhyperspaceOpt == "true">checked="true"</#if>>                        
                        </#if>
                        <label for="check_wapl">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>
            </div>             
        </div>
        <script>getTerms("wapl", "${locale.currentLanguageTag}");</script>
        </#if>

        <#if account.attributes.agree_ischecked_portal?has_content && account.attributes.agree_ischecked_portal = 'true'>
        <hr id="portal-agree-hr" class="">

        <div id="portal-agree" class="rowBox">
            <div class="col1"><div class="portal-logo"></div></div>
            <div class="col2">        
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_18")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="portal_service_terms-arrow" onclick="clickArrow('portal_service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="portal_service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_19")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="portal_privacy_terms-arrow" onclick="clickArrow('portal_privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="portal_privacy_terms" disabled></div>                
                <div class="agree">
                    <div>
                        <#if account.attributes.agreeAdvPortalOpt?has_content>
                        <input type="checkbox" name="agreeAdvPortalOpt" id="check_portal" onclick="activateSaveButton();" <#if account.attributes.agreeAdvPortalOpt == "true">checked="true"</#if>>                        
                        </#if>                        
                        <label for="check_portal">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>
            </div>            
        </div>
        <script>getTerms("portal", "${locale.currentLanguageTag}");</script>
        </#if>

        <#if account.attributes.agree_ischecked_hypermeeting?has_content && account.attributes.agree_ischecked_hypermeeting = 'true'>
        <hr id="hypermeeting-agree-hr" class="">

        <div id="hypermeeting-agree" class="rowBox">            
            <div class="col1"><div class="hypermeeting-logo"></div></div>
            <div class="col2">                
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_18")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="hypermeeting_service_terms-arrow" onclick="clickArrow('hypermeeting_service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="hypermeeting_service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_19")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="hypermeeting_privacy_terms-arrow" onclick="clickArrow('hypermeeting_privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="hypermeeting_privacy_terms" disabled></div>                
                <div class="agree">
                    <div>
                        <#if account.attributes.agreeAdvHyperMeetingOpt?has_content>
                        <input type="checkbox" name="agreeAdvHyperMeetingOpt" id="check_hypermeeting" onclick="activateSaveButton();" <#if account.attributes.agreeAdvHyperMeetingOpt == "true">checked="true"</#if>>                        
                        </#if>                        
                        <label for="check_hypermeeting">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>
            </div>            
        </div>
        <script>getTerms("hypermeeting", "${locale.currentLanguageTag}");</script>
        </#if>

        <#if account.attributes.agree_ischecked_waplclass?has_content && account.attributes.agree_ischecked_waplclass = 'true'>
        <hr id="waplclass-agree-hr" class="">
        
        <div id="waplclass-agree" class="rowBox">                   
            <div class="col1"><div class="waplclass-logo"></div></div>
            <div class="col2">                
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_18")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="waplclass_service_terms-arrow" onclick="clickArrow('waplclass_service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplclass_service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_19")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="waplclass_privacy_terms-arrow" onclick="clickArrow('waplclass_privacy_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplclass_privacy_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_20")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="waplclass_teenager_terms-arrow" onclick="clickArrow('waplclass_teenager_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplclass_teenager_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_21")}</label>
                    </div>
                    <div class="arrow" id="waplclass_children_terms-arrow" onclick="clickArrow('waplclass_children_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplclass_children_terms" disabled></div>                
                <div class="agree">
                    <div>
                        <#if account.attributes.agreeAdvWaplclassOpt?has_content>
                        <input type="checkbox" name="agreeAdvWaplclassOpt" id="check_waplclass" onclick="activateSaveButton();" <#if account.attributes.agreeAdvWaplclassOpt == "true">checked="true"</#if>>                        
                        </#if>                        
                        <label for="check_waplclass">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>
            </div>            
        </div>
        <script>getTerms("waplclass", "${locale.currentLanguageTag}");</script>
        </#if>
        
        <#if account.attributes.agree_ischecked_waplmath?has_content && account.attributes.agree_ischecked_waplmath = 'true'>
        <hr id="waplmath-agree-hr" class="">

        <div id="waplmath-agree" class="rowBox">        
            <div class="col1"><div class="waplmath-logo"></div></div>
            <div class="col2">                
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_18")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="waplmath_service_terms-arrow" onclick="clickArrow('waplmath_service_terms-arrow')"></div>
                </div>
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplmath_service_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_19")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="waplmath_privacy_terms-arrow" onclick="clickArrow('waplmath_privacy_terms-arrow')"></div>
                </div>                    
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplmath_privacy_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_20")}<span>${msg("essential")}</span></label>
                    </div>
                    <div class="arrow" id="waplmath_teenager_terms-arrow" onclick="clickArrow('waplmath_teenager_terms-arrow')"></div>
                </div>                    
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplmath_teenager_terms" disabled></div>
                <div class="agree">
                    <div>
                        <label>${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_21")}</label>
                    </div>
                    <div class="arrow" id="waplmath_children_terms-arrow" onclick="clickArrow('waplmath_children_terms-arrow')"></div>
                </div>                    
                <div class="term ${locale.currentLanguageTag}" style="display:none;" id="waplmath_children_terms" disabled></div>                
                <div class="agree">
                    <div>
                        <#if account.attributes.agreeAdvWaplmathOpt?has_content>
                        <input type="checkbox" name="agreeAdvWaplmathOpt" id="check_waplmath" onclick="activateSaveButton();" <#if account.attributes.agreeAdvWaplmathOpt == "true">checked="true"</#if>>                        
                        </#if>                        
                        <label for="check_waplmath">${msg("MSG_CREATEACCOUNT_SERVICEAGREEMENT_14")}<span>${msg("optional")}</span></label>
                    </div>
                </div>
            </div>            
        </div>
        <script>getTerms("waplmath", "${locale.currentLanguageTag}");</script>
        </#if>

        <hr>

        <input type="hidden" name="stateChecker" value="${stateChecker}">
        <div class="form-group buttons">
            <div id="buttons"> 
                <button type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="submitAction" value="Cancel" onclick="openCancelModal(); return false;">${msg("doCancel")}</button>                                                               
                <button id="agreement-submit-button" type="submit" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" onclick="submitAgreement(); return false;" disabled>${msg("doSave")}</button>
            </div>
        </div>
    </form>
    </@layout.formButtonGroup>
    </div>
    
    <@layout.cancelModal>
        <div id="button-ok" class="button modal_button_right" onclick="cancelChangeAgreement()">
            ${msg("doOK")}
        </div>
    </@layout.cancelModal>

</@layout.mainLayout>
