package com.tmax.hyperauth.rest;

import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class VersionProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public VersionProvider(KeycloakSession session) {
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
    public Response get() {
        log.info("***** GET /version");
        try {
            out = "unKnown";
            if (System.getenv("HYPERAUTH_VERSION") != null)  out = System.getenv("HYPERAUTH_VERSION");
            status = Status.OK;
            return Util.setCors(status, out);

        }catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "Get Hyperauth Version failed";
            return Util.setCors(status, out);
        }
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        log.info("***** OPTIONS /version");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }
}