package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.otp.EmailOTPAuthenticator;
import com.tmax.hyperauth.caller.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class Outer2ndFactorProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public Outer2ndFactorProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }

    Status status = null;
	String out = null;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get( @QueryParam("user_name") String userName, @QueryParam("tab_id") String tabId,
                         @QueryParam("additional_param") String addiParam, @QueryParam("secret_key") String secretKey ) {
        log.info("***** GET /outer2ndFactor");

        //Check If User with the TabID is Waiting
        log.info("userName : " + userName);
        log.info("tabId : " + tabId);
        log.info("addiParam : " + addiParam);

        try {
            if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(tabId) || StringUtil.isEmpty(addiParam) ){
                log.error("Parameter missing");
                status = Status.BAD_REQUEST;
                out = "Outer 2nd Factor Authenticate Failed, Parameter missing";
                return Util.setCors(status, out);
            }
            RealmModel realm = session.getContext().getRealm();
            log.info("realm ID : " + realm.getId());
            UserModel user = session.users().getUserByUsername(userName, realm);

            String expTimeString = session.userCredentialManager().getStoredCredentialsByType(realm, user,
                    AuthenticatorConstants.USR_CRED_OUTER_WAIT_EXP_TIME).get(0).getCredentialData();
            if (Long.parseLong(expTimeString) < new Date().getTime()) {
                log.error("Outer 2nd Factor Authenticator Expired");
                status = Status.BAD_REQUEST;
                out = "Outer 2nd Factor Authenticate Failed, Expired";
                return Util.setCors(status, out);
            }

            if (!realm.getAuthenticatorConfigByAlias(AuthenticatorConstants.CONF_PRP_OUTER_2ND_FACTOR_ALIAS)
                    .getConfig().get(AuthenticatorConstants.CONF_PRP_SECRET_KEY).equalsIgnoreCase(secretKey)){
                log.error("Secret Key Not Matched");
                status = Status.UNAUTHORIZED;
                out = "Outer 2nd Factor Authenticate Failed, Secret Key Not Matched";
                return Util.setCors(status, out);
            } else {
                log.info("Secret Key Matched");
            }

            // TabID Check
            if ( session.userCredentialManager().getStoredCredentialsByType(realm, user, AuthenticatorConstants.USR_CRED_OUTER_TAB_ID) != null
                    && session.userCredentialManager().getStoredCredentialsByType(realm, user,
                    AuthenticatorConstants.USR_CRED_OUTER_TAB_ID).get(0).getCredentialData().equalsIgnoreCase(tabId)){
                log.info("Tab ID Matched");

                // Additional Param Check
                String addiParamKey = realm.getAuthenticatorConfigByAlias(AuthenticatorConstants.CONF_PRP_OUTER_2ND_FACTOR_ALIAS)
                        .getConfig().get(AuthenticatorConstants.CONF_PRP_ADDI_PARAM);
                if ( user.getAttribute(addiParamKey).get(0).equalsIgnoreCase(addiParam)){
                    log.info("Additional Parameter Matched");
                    CredentialModel credentials = new CredentialModel();
                    credentials.setId(UUID.randomUUID().toString());
                    credentials.setCreatedDate(new Date().getTime());
                    credentials.setType(AuthenticatorConstants.USR_CRED_OUTER_STATUS);
                    credentials.setCredentialData("on");

                    // Delete Previous Credentials if Exists
                    List< CredentialModel >  storedCredentials = session.userCredentialManager()
                            .getStoredCredentialsByType(realm, user, AuthenticatorConstants.USR_CRED_OUTER_STATUS);
                    removeCredentials(session, user, storedCredentials);

                    // Create New Credentials
                    session.userCredentialManager().createCredential(realm, user, credentials);

                    log.info(addiParamKey + " matched");
                    out = "Outer 2nd Factor Authenticate Status Update Success, User [ " + userName + " ]";
                    status = Status.OK;
                    return Util.setCors(status, out);

                } else{
                    log.error(addiParamKey + " not matched");
                    status = Status.BAD_REQUEST;
                    out = "Outer 2nd Factor Authenticate Failed," +  addiParamKey + " not matched";
                    return Util.setCors(status, out);
                }
            } else{
                log.error("tabId not matched");
                status = Status.BAD_REQUEST;
                out = "Outer 2nd Factor Authenticate Failed, TabID not matched";
                return Util.setCors(status, out);
            }

        }catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "Outer 2nd Factor Authenticate Failed";
            return Util.setCors(status, out);
        }
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        log.info("***** OPTIONS /outer2ndFactor");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private void removeCredentials(KeycloakSession session, UserModel user, List< CredentialModel > storedCredentials) {
        if ( storedCredentials != null && storedCredentials.size() > 0) {
            for ( CredentialModel storedCredential : storedCredentials) {
                session.userCredentialManager().removeStoredCredential(session.getContext().getRealm(), user , storedCredential.getId());
            }
        }
    }
}